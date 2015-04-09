package HireEngine.integration.rest;


import java.net.URI;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import HireEngine.Services.BuildService;
import HireEngine.Assembler.POAssembler;
import HireEngine.Assembler.PlantAssembler;
import HireEngine.Exception.PONotFoundException;
import HireEngine.Exception.PlantNotFoundException;
import HireEngine.Models.InStatus;
import HireEngine.Models.Plant;
import HireEngine.Models.PurchaseOrder;
import HireEngine.Models.Status;
import HireEngine.Resources.POResource;
import HireEngine.Resources.PlantResource;
import HireEngine.repositiories.PORespository;
import HireEngine.repositiories.PlantRepository;

@RestController
@RequestMapping(value="/rest/pos")
public class POController {

	
	@Autowired
	PORespository poRepo;

	@Autowired
	PlantRepository plantRepo;
	
	@Autowired
    BuildService builditProxy;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public POResource getPO(@PathVariable("id") Long id) throws Exception {
		
		PurchaseOrder fpo = poRepo.findOne(id);
		if (fpo == null) {
			throw new PONotFoundException(id);
		}
		POAssembler assemb = new POAssembler();
		POResource res = assemb.toResource(fpo);
		return res;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<POResource> getAll() {
		List<PurchaseOrder> pos = poRepo.findAll();
		POAssembler assemb = new POAssembler();
		List<POResource> resources = assemb.toResource(pos);
		return resources;
	}

	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
	public ResponseEntity<POResource> updatePO(@PathVariable("id") Long id) 
			throws PONotFoundException, PlantNotFoundException, Exception  {
		PurchaseOrder po = poRepo.findOne(id);
		po.setStatus(Status.ACCEPTED);
		po.setInstatus(InStatus.PENDING_CREATE);
//		poRepo.delete(id);
		PurchaseOrder newpo = poRepo.saveAndFlush(po);
		POAssembler assemb = new POAssembler();
		POResource newRes = assemb.toResource(newpo);
//		po.setStatus(Status.PENDING_CONFIRMATION);
//		poRepo.saveAndFlush(po);
//		System.out.println("in update po controller");
//		System.out.println(po.getId());
//		System.out.println(id);
//		
		builditProxy.updatePO(newRes);
		
		return new ResponseEntity<POResource>(newRes, HttpStatus.OK);
	}

	@RequestMapping(value="/create", method = RequestMethod.POST)
	public ResponseEntity<POResource> createPO(@RequestBody POResource res)
			throws PONotFoundException, PlantNotFoundException, Exception  {
//		System.out.println("I am here");
		PurchaseOrder po = new PurchaseOrder();
		
		if (res.getPlant() == null) {
			throw new PlantNotFoundException(null);
		}
		else{		
			
			PlantResource resource = res.getPlant();
			Plant plant = new Plant();
			plant.setId(resource.getIdRes());
			plant.setName(resource.getName());
			plant.setDescription(resource.getDescription());
			plant.setPrice(resource.getPrice());
			po.setPlant(plant);
		}
		
//		if (plantRepo.isPlantAvailable(po.getPlant().getName(),
//				po.getStartDate(), po.getEndDate()).isEmpty()) {
//			// Throw exception if plant NOT available
//			throw new PlantNotFoundException(po.getPlant().getId());
//		}
		
		po.setPhrId(res.getPhrId());
		po.setStartDate(res.getStartDate());
		po.setEndDate(res.getEndDate());
		po.setStatus(Status.PENDING_CONFIRMATION);
		po.setInstatus(InStatus.PENDING_CREATE);
		// Compute the total cost of the hiring
		po.calcCost(po.getPlant());
		
		PurchaseOrder newpo = poRepo.save(po);
//		System.out.println(newpo);
		// Return CREATED status after PO is successfully created
		POAssembler assemb = new POAssembler();
		POResource newRes = assemb.toResource(newpo);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(new URI(newRes.getId().getHref()));	    
    	return new ResponseEntity<>(newRes, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value="/remit", method = RequestMethod.POST)
	public ResponseEntity<POResource> remit(@RequestBody POResource res)
			throws PONotFoundException, PlantNotFoundException, Exception  {
//		System.out.println("I am here");
//		System.out.println("in po controller");
//		System.out.println(res.getIdPo());
		PurchaseOrder po = poRepo.findOne(res.getIdPo());
		po.setInstatus(InStatus.REMITTANCE_ADVICE);
		PurchaseOrder newpo = poRepo.saveAndFlush(po);
		POAssembler assemb = new POAssembler();
		POResource newRes = assemb.toResource(newpo);	
		return new ResponseEntity<>(newRes, HttpStatus.OK);
	}
	
	@RequestMapping(value="/update", method = RequestMethod.POST)
	public ResponseEntity<POResource> updatePOS(@RequestBody POResource res)
			throws PONotFoundException, PlantNotFoundException, Exception  {
//		System.out.println("I am here");
//		System.out.println("in po controller");
//		System.out.println(res.getIdPo());
		PurchaseOrder po = poRepo.findOne(res.getIdPo());
		if (res.getPlant() == null) {
			throw new PlantNotFoundException(null);
		}
		else{		
			
			PlantResource resource = res.getPlant();
			Plant plant = new Plant();
			plant.setId(resource.getIdRes());
			plant.setName(resource.getName());
			plant.setDescription(resource.getDescription());
			plant.setPrice(resource.getPrice());
			po.setPlant(plant);
		}
		
//		if (plantRepo.isPlantAvailable(po.getPlant().getName(),
//				po.getStartDate(), po.getEndDate()).isEmpty()) {
//			// Throw exception if plant NOT available
//			throw new PlantNotFoundException(po.getPlant().getId());
//		}
		
		po.setPhrId(res.getPhrId());
		po.setStartDate(res.getStartDate());
		po.setEndDate(res.getEndDate());		
		po.setStatus(Status.PENDING_CONFIRMATION);
		po.setInstatus(InStatus.PENDING_CREATE);
		po.setCost(res.getCost());
		PurchaseOrder newpo = poRepo.saveAndFlush(po);
		POAssembler assemb = new POAssembler();
		POResource newRes = assemb.toResource(newpo);	
		return new ResponseEntity<>(newRes, HttpStatus.OK);
	}
	
	@RequestMapping(value="/inreject", method = RequestMethod.POST)
	public ResponseEntity<POResource> rejectinvoice(@RequestBody POResource res)
			throws PONotFoundException, PlantNotFoundException, Exception  {
//		System.out.println("I am here");
//		System.out.println("in po controller");
		System.out.println(res.getIdPo());
		PurchaseOrder po = poRepo.findOne(res.getIdPo());
		po.setInstatus(InStatus.REJECTED);
		PurchaseOrder newpo = poRepo.saveAndFlush(po);
		POAssembler assemb = new POAssembler();
		POResource newRes = assemb.toResource(newpo);	
		return new ResponseEntity<>(newRes, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/accept", method = RequestMethod.DELETE)
	public ResponseEntity<POResource> rejectPO(@PathVariable("id") Long id) throws Exception {
		PurchaseOrder po = poRepo.findOne(id);
		po.setStatus(Status.REJECTED);
		po.setInstatus(InStatus.NOT_SENT);
//		poRepo.delete(id);
		PurchaseOrder newpo = poRepo.saveAndFlush(po);
		POAssembler assemb = new POAssembler();
		POResource newRes = assemb.toResource(newpo);	
		
		builditProxy.rejectPO(newRes);
		return new ResponseEntity<>(newRes, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/accept", method = RequestMethod.POST)
	public ResponseEntity<POResource> acceptPO(@PathVariable("id") Long id) throws Exception {
		PurchaseOrder po = poRepo.findOne(id);
		po.setStatus(Status.ACCEPTED);
		po.setInstatus(InStatus.SENT);
//		poRepo.delete(id);
		PurchaseOrder newpo = poRepo.saveAndFlush(po);
		POAssembler assemb = new POAssembler();
		POResource newRes = assemb.toResource(newpo);
//		System.out.println("in accpet po controller");
//		System.out.println(po.getId());
//		System.out.println(id);
//		
		builditProxy.acceptPO(newRes);
		
		return new ResponseEntity<POResource>(newRes, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/close", method = RequestMethod.POST)
	public ResponseEntity<POResource> closePO(@PathVariable("id") Long id) {
		PurchaseOrder po = poRepo.findOne(id);
		po.setStatus(Status.CLOSED);
		po.setInstatus(InStatus.CLOSED);
		poRepo.saveAndFlush(po);		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{po.id}/updates/{up.id}/accept", method = RequestMethod.DELETE)
	public ResponseEntity<POResource> rejectPOUpdate(@RequestBody Long id) {
		PurchaseOrder po = poRepo.findOne(id);
		POResource pos = new POResource();
		po.setStatus(Status.REJECTED);
		poRepo.delete(id);
		poRepo.saveAndFlush(po);
		return new ResponseEntity<POResource>(pos, HttpStatus.OK);
	}

	@RequestMapping(value = "/{po.id}/updates/{up.id}/accept", method = RequestMethod.POST)
	public ResponseEntity<POResource> acceptPOUpdate(@RequestBody Long id) {
		PurchaseOrder po = poRepo.findOne(id);
		POResource pos = new POResource();
		po.setStatus(Status.ACCEPTED);
		poRepo.delete(id);
		poRepo.saveAndFlush(po);
		return new ResponseEntity<POResource>(pos, HttpStatus.ACCEPTED);
	}

//	@RequestMapping(value = "/{po.id}/updates", method = RequestMethod.PUT)
//	public ResponseEntity<POResource>  updatePO(@RequestBody Long id) {
//		return null;
//
//	}


	@RequestMapping(value = "/{po.id}/updates", method = RequestMethod.POST)
	public ResponseEntity<POResource> requestPOUpdate(
			@RequestBody POResource res, @PathVariable("id") Long id) {
		PurchaseOrder po = new PurchaseOrder();
		po.setStartDate(res.getStartDate());
		po.setEndDate(res.getEndDate());
		PlantResource plantRes = res.getPlant();
		Plant plant = new Plant();
		plant.setName(plantRes.getName());
		plant.setDescription(plantRes.getDescription());
		plant.setPrice(plantRes.getPrice());
		po.setPlant(plant);
		po.setCustomer(res.getCustomer());
		po.calcCost(plant);
		poRepo.delete(id);
		po.setStatus(Status.PENDING_CONFIRMATION);
		poRepo.saveAndFlush(po);
		new ResponseEntity<POResource>(res, HttpStatus.OK);
		return null;
	}

	@ExceptionHandler(PONotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handPONotFoundException(PONotFoundException ex) {
			}

}

