package buildit.service;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonProcessingException;

import buildit.Resource.POResource;
import buildit.Resource.PlantHireRequestResource;
import buildit.Resource.PlantResource;
import buildit.models.Plant;
import buildit.models.PlantHireRequest;
import buildit.models.PoStatus;
import buildit.models.Status;
import buildit.repositories.PHRRepository;
import buildit.repositories.PlantRepository;

@Service
public class PlantHireRequestManager {
    
	@Autowired
    PlantRepository plantRepository;
	
	@Autowired
    PHRRepository phrRepository;	
	
    @Autowired
    RentalService rentitProxy;

	public PlantHireRequest createPurchaseOrder(PlantHireRequestResource phrs, Long id) throws PlantNotAvailableException, RestClientException, JsonProcessingException {
		PlantHireRequest phr = phrRepository.findOne(id);

//		PlantResource plantRS = new PlantResource();
//		plantRS.add(new Link(phr.getPlantRef()));
//		System.out.println("Inside Manager");
//		System.out.println(phrs.getIdRes());
//		System.out.println(id);
		POResource po = rentitProxy.createPurchaseOrder(id, phrs.getPlant(), phr.getStartDate(), phr.getEndDate(), phr.getPrice(), phr.getStatus(), phr.getPoId());

//		phr.setPlantRef(po.getId().getHref());
//		phr.setPrice(po.getCost());
		phrRepository.save(phr);
		return phr;
	}
	
	public PlantHireRequest updatePurchaseOrder(PlantHireRequestResource phrs, Long id) throws PlantNotAvailableException, RestClientException, JsonProcessingException {
		PlantHireRequest phr = phrRepository.findOne(id);

//		PlantResource plantRS = new PlantResource();
//		plantRS.add(new Link(phr.getPlantRef()));
//		System.out.println("Inside Manager");
//		System.out.println(phrs.getIdRes());
//		System.out.println(id);
		POResource po = rentitProxy.updatePurchaseOrder(id, phrs.getPlant(), phr.getStartDate(), phr.getEndDate(), phr.getPrice(), phr.getStatus(), phr.getPoId());

//		phr.setPlantRef(po.getId().getHref());
//		phr.setPrice(po.getCost());
		phrRepository.save(phr);
		return phr;
	}

	public PlantHireRequest sendPurchaseOrder(PlantHireRequestResource phrs, Long id) throws PlantNotAvailableException, RestClientException, JsonProcessingException {
		PlantHireRequest phr = phrRepository.findOne(id);

//		PlantResource plantRS = new PlantResource();
//		plantRS.add(new Link(phr.getPlantRef()));
//		System.out.println("Inside Manager");
//		System.out.println(phrs.getIdRes());
//		System.out.println(id);
//		System.out.println("in manager");
//		System.out.println(phr.getPoId());
		
		POResource po = rentitProxy.sendRemittance(id, phrs.getPlant(), phr.getStartDate(), phr.getEndDate(), phr.getPrice(), phr.getStatus(), phr.getPoId());

//		phr.setPlantRef(po.getId().getHref());
//		phr.setPrice(po.getCost());
		phrRepository.save(phr);
		return phr;
	}
	
	public PlantHireRequest sendRejectPurchaseOrder(PlantHireRequestResource phrs, Long id) throws PlantNotAvailableException, RestClientException, JsonProcessingException {
		PlantHireRequest phr = phrRepository.findOne(id);

//		PlantResource plantRS = new PlantResource();
//		plantRS.add(new Link(phr.getPlantRef()));
//		System.out.println("Inside Manager");
//		System.out.println(phrs.getIdRes());
//		System.out.println(id);
//		System.out.println("in manager");
//		System.out.println(phr.getPoId());
		
		POResource po = rentitProxy.sendRejectMsg(id, phrs.getPlant(), phr.getStartDate(), phr.getEndDate(), phr.getPrice(), phr.getStatus(), phr.getPoId());

//		phr.setPlantRef(po.getId().getHref());
//		phr.setPrice(po.getCost());
		phrRepository.save(phr);
		return phr;
	}
	
	public PlantHireRequest createPlantHireRequest(PlantHireRequestResource phrs) throws NullPointerException, RestClientException, PlantNotAvailableException  {

	  Plant plant = new Plant();	
	  plant.setId(phrs.getPlant().getIdres());
	  plant.setName(phrs.getPlant().getName());
	  plant.setDescription(phrs.getPlant().getDescription());
	  plant.setPrice(phrs.getPlant().getPrice());
	  plantRepository.save(plant);	  

	  PlantHireRequest phr = new PlantHireRequest();
	  phr.setPlant(plant);
	  phr.setStartDate(phrs.getStartDate());
	  phr.setEndDate(phrs.getEndDate());
	  phr.calcCost(plant);
	  phr.setStatus(Status.PENDING_CONFIRMATION);
	  phr.setPostatus(PoStatus.PENDING_CREATE);
//	  phr.setPlantRef(phrs.getPlantResource().getLink("self").getHref());  
	  phrRepository.save(phr);
	  return phr;
	}
	
	
	public PlantHireRequest updatePlantHireRequest(Long id, PlantHireRequestResource phrs, PoStatus s){
		
		Plant plant = new Plant();	
//		plant.setId(phrs.getPlantResource().getIdres());
		plant.setName(phrs.getPlant().getName());
		plant.setDescription(phrs.getPlant().getDescription());
		plant.setPrice(phrs.getPlant().getPrice());		
		plantRepository.save(plant);	
		
		PlantHireRequest phr = phrRepository.getOne(id);
		phr.setPlant(plant);		
		phr.setStartDate(phrs.getStartDate());
		phr.setEndDate(phrs.getEndDate());
		phr.calcCost(plant);
		phr.setStatus(Status.PENDING_CONFIRMATION);
		phr.setPostatus(s);
//		phr.setPlantRef(phrs.getPlant().getLink("self").getHref());		
		phrRepository.save(phr);
		return phr;
	}
	
	public PlantHireRequest approvePlantHireRequest(Long id) {
		PlantHireRequest phr = phrRepository.findOne(id);
		phr.setStatus(Status.ACCEPTED);
		
		phrRepository.save(phr);
		return phr;
	}
	
	public PlantHireRequest rejectPlantHireRequest(Long id) {
		PlantHireRequest phr = phrRepository.findOne(id);
		phr.setStatus(Status.REJECTED);
		
		phrRepository.save(phr);
		return phr;
	}

	public PlantHireRequest closePlantHireRequest(Long id){
		PlantHireRequest phr = phrRepository.findOne(id);
		phr.setStatus(Status.CLOSED);
		
		phrRepository.save(phr);
		return phr;
	}

	public PlantHireRequest getPlantHireRequest(Long id) {
		return phrRepository.findOne(id);
	}

	public List<PlantHireRequest> getAllPlantHireRequests() {
		return phrRepository.findAll();
	}
}
