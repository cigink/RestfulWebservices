package HireEngine.integration.rest;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


import HireEngine.Assembler.PlantAssembler;
import HireEngine.Exception.PlantNotFoundException;
import HireEngine.Models.Plant;
import HireEngine.Resources.PlantResource;
import HireEngine.repositiories.PlantRepository;

@RestController
@RequestMapping(value = "/rest/plants")
public class HireController {

	@Autowired
	PlantRepository repo;

	@RequestMapping(method = RequestMethod.GET, value = "")
	@ResponseStatus(HttpStatus.OK)
	public List<PlantResource> getAll() {
		List<Plant> plants = repo.findAll();
		PlantAssembler assembler = new PlantAssembler();
		List<PlantResource> res = assembler.toResource(plants);
		return res;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/query")
	@ResponseStatus(HttpStatus.OK)
	public List<PlantResource> getavailableplants(
			@RequestParam("name") String name,
			@RequestParam("startDate") String date1,
			@RequestParam("endDate") String date2) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date Date1 = df.parse(date1);
		Date Date2 = df.parse(date2);

		List<Plant> plants = repo.isPlantAvailable(name, Date1, Date2);
		PlantAssembler assembler = new PlantAssembler();
		List<PlantResource> res = assembler.toResource(plants);
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<PlantResource> getPlant(@PathVariable("id") Long id) throws Exception {
		Plant plant = repo.findOne(id);
		if (plant == null) {
			throw new PlantNotFoundException(id);
		}
		PlantAssembler assembler = new PlantAssembler();
		ResponseEntity<PlantResource> resp = new ResponseEntity<PlantResource>(
				assembler.toResource(plant), HttpStatus.OK);
		return resp;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<PlantResource> createPlant(@RequestBody PlantResource pres)
			throws Exception, PlantNotFoundException {
		if (pres == null) {
			return null;
		}
		Plant plant = new Plant();
		plant.setName(pres.getName());
		plant.setDescription(pres.getDescription());
		plant.setPrice(pres.getPrice());

		Plant createdPlant = repo.saveAndFlush(plant);
		PlantAssembler assembler = new PlantAssembler();
		PlantResource res = assembler.toResource(createdPlant);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(new URI(res.getId().getHref()));	    
    	return new ResponseEntity<>(res, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/update", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<PlantResource> updatePlant(@PathVariable("id") Long id, @RequestBody PlantResource plant)
			throws Exception, PlantNotFoundException {
		ResponseEntity<PlantResource> response;
		if (plant == null) {
			response = new ResponseEntity<PlantResource>(
					HttpStatus.NOT_FOUND);
			return response;
		}
		
		Plant p = repo.getOne(id);
		p.setName(plant.getName());
		p.setDescription(plant.getDescription());
		p.setPrice(plant.getPrice());
		repo.save(p);
		PlantAssembler assembler = new PlantAssembler();
		PlantResource res = assembler.toResource(p);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(new URI(res.getId().getHref()));	    
    	return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
	public ResponseEntity<PlantResource> deletePlant(
			@PathVariable("id") Long id) {
		Plant p = repo.findOne(id);
		repo.delete(p);
//		PlantAssembler assembler = new PlantAssembler();
//		PlantResource newRes = assembler.toResource(p);
//		ResponseEntity<PlantResource> resp = new ResponseEntity<PlantResource>(
//				newRes, HttpStatus.OK);
		return null;
	}

	// @RequestMapping("/plants")
	// public String list(Model model) {
	// model.addAttribute("plants", repo.findAll());
	// return "plants/list";
	// }
	//
	// @RequestMapping(value = "/plants/form")
	// public String form(Model model) {
	// model.addAttribute("plant", new Plant());
	// return "plants/create";
	// }
	//
	// @RequestMapping(value = "/plants", method = RequestMethod.POST)
	// public String create(Plant plant) {
	// repo.saveAndFlush(plant);
	// return "redirect:/plants";
	// }
	//
	// @RequestMapping("/plants/{id}")
	// public String show(Model model, @PathVariable Long id) {
	// model.addAttribute(id);
	// return "plants/{id}";
	// }
	//
	// @RequestMapping("/plants/{id}/form")
	// public String edit(Model model, @PathVariable Long id) {
	// model.addAttribute(repo.findOne(id));
	// return "plants/edit";
	//
	// }
	//
	// @RequestMapping(value = "/plants/{id}", method = RequestMethod.PUT)
	// public String update(Plant plant, @PathVariable Long id) {
	// repo.saveAndFlush(plant);
	// return "redirect:/plants";

	@ExceptionHandler(PlantNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handPlantNotFoundException(PlantNotFoundException ex) {
	}

}
