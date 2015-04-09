package buildit.controllers;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import buildit.Resource.POResource;
import buildit.Assembler.PlantHireRequestAssembler;
import buildit.Resource.PlantHireRequestResource;
import buildit.models.Plant;
import buildit.models.PlantHireRequest;
import buildit.models.PoStatus;
import buildit.models.Status;
import buildit.repositories.PHRRepository;
import buildit.repositories.PlantRepository;
import buildit.service.PlantHireRequestManager;
import buildit.service.RentalService;
import buildit2.exception.PHRNotFoundException;

@RestController
@RequestMapping("rest/phr")
public class PHRController {
	PlantHireRequestResource phres;
	POResource pores;

	@Autowired
	PHRRepository phrRepo;
	
	@Autowired
	PlantRepository pRepo;
	
	@Autowired
	PlantHireRequestManager phrManager;
	
	@Autowired
    RentalService rentitProxy;
	
	PlantHireRequestAssembler phrAssembler = new PlantHireRequestAssembler();

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<PlantHireRequestResource> getPHR(
			@PathVariable("id") Long id) throws PHRNotFoundException {
		PlantHireRequest phr = phrRepo.findOne(id);
		if (phr == null) {
			throw new PHRNotFoundException(null);
		}
		PlantHireRequestAssembler assembler = new PlantHireRequestAssembler();
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				assembler.toResource(phr), HttpStatus.OK);
		return resp;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<PlantHireRequestResource> getAll() {
		List<PlantHireRequest> pos = phrRepo.findAll();
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		List<PlantHireRequestResource> resources = assemb.toResource(pos);
		return resources;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> createPO(
			@RequestBody PlantHireRequestResource res)
			throws PHRNotFoundException {

		ResponseEntity<PlantHireRequestResource> resp;
		if (res == null) {
			throw new PHRNotFoundException(null);
		}
		PlantHireRequest phr = new PlantHireRequest();
		phr.setStartDate(res.getStartDate());
		phr.setEndDate(res.getEndDate());
		if (res.getPlant() != null) {

			// phr.setId(res.getIdRes());
			phr.setSiteEngineer(res.getSiteEngineer());
			phr.setWorksEngineer(res.getWorksEngineer());
			/*
			 * Plant plant = new Plant(); //
			 * plant.setId(res.getPlantResource().getIdres());
			 * plant.setName(res.getPlantResource().getName());
			 * plant.setDesc(res.getPlantResource().getDescription());
			 * plant.setPrice(res.getPlantResource().getPrice()); Plant
			 * savedPlant = plantRepo.saveAndFlush(plant);
			 */

			phr.setPlantRef(res.getPlant().getId().toString());

			phr.setPrice(res.getPrice());
		}

		// if (plantRepo.isPlantAvailable(phr.getPlant().getName(),
		// phr.getStartDate(), phr.getEndDate()).isEmpty()) {
		// // Throw exception if plant NOT available
		// throw new PHRNotFoundException(phr.getPlant().getId());
		// }
		// Compute the total cost of the hiring
		// po.calcCost(po.getPlant());
		phr.setStatus(Status.PENDING_CONFIRMATION);
		PlantHireRequest new_phr = phrRepo.saveAndFlush(phr);
		// Return CREATED status after PO is successfully created
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assemb.toResource(new_phr);
		resp = new ResponseEntity<PlantHireRequestResource>(newRes,
				HttpStatus.CREATED);
//		System.out.println(newRes.getIdRes());
		return resp;
	}

	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> createPlantHireRequest(@RequestBody PlantHireRequestResource phr) throws Exception {
//		PlantHireRequest _phr = new PlantHireRequest();
//		_phr.setStartDate(phr.getStartDate());
//		_phr.setEndDate(phr.getEndDate());
//		if (phr.getPlantResource() != null) {
//			_phr.setPlantRef(phr.getPlantResource().getId().getHref());
//			_phr.setPlantName(phr.getPlantResource().getName());
//		}
		
		PlantHireRequest newPHR = phrManager.createPlantHireRequest(phr);
		PlantHireRequestResource res = phrAssembler.toResource(newPHR);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(new URI(res.getId().getHref()));	    
    	return new ResponseEntity<>(res, headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/accept", method = RequestMethod.DELETE)
	public ResponseEntity<PlantHireRequestResource> rejectPhr(
			@PathVariable("id") Long id) {
		PlantHireRequest phr = phrRepo.findOne(id);
		phr.setStatus(Status.REJECTED);
		phr.setPostatus(PoStatus.PENDING_CREATE);
//		phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		PlantHireRequestAssembler assembler = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assembler.toResource(phr);
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				newRes, HttpStatus.OK);
		return resp;
	}

	@RequestMapping(value = "/{id}/accept", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> acceptPhr(
			@PathVariable("id") Long id) throws Exception{
		PlantHireRequest phr = phrRepo.findOne(id);
		phr.setStatus(Status.ACCEPTED);
		phr.setPostatus(PoStatus.PENDING_CONFIRMATION);
		// phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assemb.toResource(phr);
		phres=newRes;

		phrManager.createPurchaseOrder(newRes,phr.getId());
		
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				newRes, HttpStatus.OK);

		return resp;
	}
	
	@RequestMapping(value = "/{id}/accepts", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> acceptPhrforRejectPo(
			@PathVariable("id") Long id) throws Exception{
		PlantHireRequest phr = phrRepo.findOne(id);
		phr.setStatus(Status.ACCEPTED);
		phr.setPostatus(PoStatus.PENDING_CONFIRMATION);
		// phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assemb.toResource(phr);

		phrManager.updatePurchaseOrder(newRes,phr.getId());
		
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				newRes, HttpStatus.OK);

		return resp;
	}

	@RequestMapping(value = "/{id}/close", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> closePHR(@PathVariable("id") Long id) {
		PlantHireRequest phr = phrRepo.findOne(id);
		phr.setStatus(Status.CLOSED);
		phr.setPostatus(PoStatus.CLOSED);
//		phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(HttpStatus.OK);
		return resp;
	}
	
	@RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> cancelPHR(@PathVariable("id") Long id) {
		PlantHireRequest phr = phrRepo.findOne(id);
		phr.setStatus(Status.CANCELLED);
		phr.setPostatus(PoStatus.CANCELLED);
//		phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(HttpStatus.OK);
		return resp;
	}

	@RequestMapping(value = "/iaccept", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> acceptInvoice(
			@RequestBody POResource res ) throws Exception{
//		System.out.println("I am Here");
//		System.out.println(res.getPhrId());
		PlantHireRequest phr = phrRepo.findOne(res.getPhrId());
		phr.setPostatus(PoStatus.ACCEPTED);
		phr.setPoId(res.getIdPo());
		// phrRepo.delete(id);
		pores=res;
		phrRepo.saveAndFlush(phr);
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assemb.toResource(phr);
		
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				newRes, HttpStatus.OK);

		return resp;
	}
	
	@RequestMapping(value = "/rejectpo", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> rejectPO(
			@RequestBody POResource res ) throws Exception{
//		System.out.println("I am Here");
//		System.out.println(res.getPhrId());
		PlantHireRequest phr = phrRepo.findOne(res.getPhrId());
		phr.setStatus(Status.ACCEPTED);
		phr.setPostatus(PoStatus.REJECTED);
		phr.setPoId(res.getIdPo());
		// phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assemb.toResource(phr);
		
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				newRes, HttpStatus.OK);

		return resp;
	}
	
	@RequestMapping(value = "/updatepo", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> updatePO(
			@RequestBody POResource res ) throws Exception{
//		System.out.println("I am Here");
//		System.out.println(res.getPhrId());
		PlantHireRequest phr = phrRepo.findOne(res.getPhrId());
		phr.setStatus(Status.ACCEPTED);
		phr.setPostatus(PoStatus.PENDING_UPDATE);
		phr.setPoId(res.getIdPo());
		// phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assemb.toResource(phr);
		
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				newRes, HttpStatus.OK);

		return resp;
	}
	
	@RequestMapping(value = "/{id}/iapprove", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> approveInvoice(
			@PathVariable("id") Long id) throws Exception{
//		System.out.println("I am Here");
//		System.out.println(res.getPhrId());
		PlantHireRequest phr = phrRepo.findOne(id);
		phr.setPostatus(PoStatus.CLOSED);
		// phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assemb.toResource(phr);
//		System.out.println(phr);

		phrManager.sendPurchaseOrder(newRes,phr.getId());
		
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				newRes, HttpStatus.OK);

		return resp;
	}
	
	@RequestMapping(value = "/{id}/ireject", method = RequestMethod.DELETE)
	public ResponseEntity<PlantHireRequestResource> rejectInvoice(
			@PathVariable("id") Long id) throws Exception{
//		System.out.println("I am Here");
//		System.out.println(res.getPhrId());
		PlantHireRequest phr = phrRepo.findOne(id);
		phr.setPostatus(PoStatus.CLOSED);
		// phrRepo.delete(id);
		phrRepo.saveAndFlush(phr);
		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
		PlantHireRequestResource newRes = assemb.toResource(phr);
		
		phrManager.sendRejectPurchaseOrder(newRes,phr.getId());
	
		ResponseEntity<PlantHireRequestResource> resp = new ResponseEntity<PlantHireRequestResource>(
				newRes, HttpStatus.OK);

		return resp;
	}
	
	@RequestMapping(value = "/{id}/updates", method = RequestMethod.PUT)
	public ResponseEntity<PlantHireRequestResource> updatePhrforRejectPo(@RequestBody PlantHireRequestResource phr,
			@PathVariable("id") Long id) throws Exception, PHRNotFoundException {
		
		ResponseEntity<PlantHireRequestResource> response;
		if (phr == null) {
			response = new ResponseEntity<PlantHireRequestResource>(
					HttpStatus.NOT_FOUND);
			return response;
		}
		PlantHireRequest newPHR = phrManager.updatePlantHireRequest(id, phr, PoStatus.PENDING_UPDATE);
		PlantHireRequestResource res = phrAssembler.toResource(newPHR);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(new URI(res.getId().getHref()));	    
    	return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/extendupdate", method = RequestMethod.PUT)
	public ResponseEntity<PlantHireRequestResource> updatePhrforextend(@RequestBody PlantHireRequestResource phr,
			@PathVariable("id") Long id) throws Exception, PHRNotFoundException {
		
		ResponseEntity<PlantHireRequestResource> response;
		if (phr == null) {
			response = new ResponseEntity<PlantHireRequestResource>(
					HttpStatus.NOT_FOUND);
			return response;
		}
		PlantHireRequest newPHR = phrManager.updatePlantHireRequest(id, phr, PoStatus.PENDING_UPDATES);
		PlantHireRequestResource res = phrAssembler.toResource(newPHR);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(new URI(res.getId().getHref()));	    
    	return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/update", method = RequestMethod.PUT)
	public ResponseEntity<PlantHireRequestResource> updatePhr(@RequestBody PlantHireRequestResource phr,
			@PathVariable("id") Long id) throws Exception, PHRNotFoundException {
		
		ResponseEntity<PlantHireRequestResource> response;
		if (phr == null) {
			response = new ResponseEntity<PlantHireRequestResource>(
					HttpStatus.NOT_FOUND);
			return response;
		}


//		if (phrRepo.findOne(res.getId()) == null) {
//			throw new PHRNotFoundException(res.getId());
//		}

//		PlantHireRequest phr = new PlantHireRequest();
//		phr.setStartDate(res.getStartDate());
//		phr.setEndDate(res.getEndDate());
//		if (res.getPlant() != null) {
//
//			phr.setId(res.getIdRes());
//			phr.setSiteEngineer(res.getSiteEngineer());
//			phr.setWorksEngineer(res.getWorksEngineer());
//			Plant plant = new Plant();	
//			plant.setId(phrs.getPlantResource().getIdres());
//			plant.setName(res.getPlant().getName());
//			plant.setDescription(res.getPlant().getDescription());
//			plant.setPrice(res.getPlant().getPrice());
//			pRepo.save(plant);
//			phr.setPlant(plant);
//			phr.setPrice(res.getPrice());
//		}
//
//		// po.calcCost(po.getPlant());
//		phr.setStatus(Status.PENDING_CONFIRMATION);
//		PlantHireRequest new_phr = phrRepo.saveAndFlush(phr);
//		// Return CREATED status after PO is successfully created
//		PlantHireRequestAssembler assemb = new PlantHireRequestAssembler();
//		PlantHireRequestResource newRes = assemb.toResource(new_phr);
//		response = new ResponseEntity<PlantHireRequestResource>(newRes,
//				HttpStatus.CREATED);
//		return response;
		PlantHireRequest newPHR = phrManager.updatePlantHireRequest(id, phr, PoStatus.PENDING_CREATE);
		PlantHireRequestResource res = phrAssembler.toResource(newPHR);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(new URI(res.getId().getHref()));	    
    	return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{phr.id}/updates/{up.id}/accept", method = RequestMethod.DELETE)
	public ResponseEntity<PlantHireRequestResource> rejectPhrUpdate(
			@RequestBody Long id) {
		PlantHireRequest phr = new PlantHireRequest();
		phr.setStatus(Status.REJECTED);
		return null;
	}
	
	@RequestMapping(value = "/{po.id}/updates/{up.id}/accept", method = RequestMethod.POST)
	public ResponseEntity<PlantHireRequestResource> acceptPhrUpdate(
			@RequestBody Long id) {
		PlantHireRequest phr = new PlantHireRequest();
		phr.setStatus(Status.PENDING_CONFIRMATION);
		return null;
	}
	
//	@RequestMapping(value = "/{po.id}/updates/{up.id}/accept", method = RequestMethod.POST)
//	public ResponseEntity<PlantHireRequestResource> requestPhrUpdate(
//			@RequestBody Long id) {
//		PlantHireRequest phr = new PlantHireRequest();
//		phr.setStatus(Status.PENDING_CONFIRMATION);
//		return null;
//	}
	
	public String invoicecheck()
	{
		if(phres.getStartDate()==pores.getStartDate() && phres.getPlant().getId() == pores.getPlant().getId() && phres.getPrice() == pores.getCost())
		{
			return "Invoice is correct";
		}
		else{
			return "Invoice is incoreect";
		}
	}
	
	@ExceptionHandler(PHRNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void handPONotFoundException(PHRNotFoundException ex) {
	}

}