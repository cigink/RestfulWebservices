/**

 * 
 */
package HireEngine.Soap;

import java.util.Date; 
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import HireEngine.repositiories.PlantRepository;
import HireEngine.repositiories.PORespository;
import HireEngine.Exception.PONotFoundException;
import HireEngine.Exception.PlantNotFoundException;
import HireEngine.Models.Plant;
import HireEngine.Models.PurchaseOrder;
import HireEngine.Models.Status;
import HireEngine.Soap.POResource;

/**
 * @author Vinod Rockson
 *
 */
@WebService
public class SOAP_Service {
	
	@Autowired
	PlantRepository plantRepos;
	
	@Autowired
	PORespository poRepo;
	
	
	@WebMethod
	public PlantResourceList getAllPlants() {
		List<Plant> plants = plantRepos.findAll();
		PlantResourceList plantResourceList = PlantResourceAssembler.toResource(plants);
		return plantResourceList;
	}
	
	@WebMethod
	public PlantResourceList findAllAvailablePlants(@WebParam(name="name") String name, 
			@WebParam(name="startDate") Date date1, @WebParam(name="endDate") Date date2) 
	{
		List<Plant> plant = plantRepos.isPlantAvailable(name, date1, date2);
		PlantResourceList resc = PlantResourceAssembler.toResource(plant);
		return resc;
	}
	
	@WebMethod
	public POResource CreatePurchaseOrder(@WebParam(name="id") Long id, @WebParam(name="startDate") Date date1,
			@WebParam(name="endDate") Date date2) throws PlantNotFoundException{

		PurchaseOrder po = new PurchaseOrder();	
		Plant p = plantRepos.findPlant(plantRepos.getOne(id).getName(), date1, date2);
		if (p == null){
			throw new PlantNotFoundException(id);
		}		
		
		po.setId(id);
	    po.setStartDate(date1);
	    po.setEndDate(date2);
	    po.calcCost(p);
	    po.setPlant(p);
	    po.setStatus(Status.PENDING_CONFIRMATION);
	    POResource res = POResourceAssembler.toResource(poRepo.saveAndFlush(po));
	    return res;
	}
	
	@WebMethod 
	public POResource UpdatePurchaseOrder(@WebParam(name="pos") POResource pos) throws PlantNotFoundException, 
			PONotFoundException{
		
		PurchaseOrder po = new PurchaseOrder();	
		po = poRepo.findOne(pos.getIdPo());
		if (po == null){
			throw new PONotFoundException(pos.getIdPo());
		}	
		
		if(pos.getPlantResource() != null){
		    Plant plant = plantRepos.findPlant(pos.getPlantResource().getName(),pos.getStartDate(),
		    		pos.getEndDate());
		    po.setPlant(plant);
		}
		else{
			throw new PlantNotFoundException(pos.getPlantResource().getIdRes());
		}
		
		if(!pos.getStartDate().equals("[string?]")){

	    	po.setStartDate(pos.getStartDate());
	    }
		
		if(!pos.getEndDate().equals("")){

	    	po.setEndDate(pos.getEndDate());
	    }
	    
		po.setId(po.getId());
		po.calcCost(po.getPlant());
		po.setStatus(Status.PENDING_CONFIRMATION);
	    POResource res = POResourceAssembler.toResource(poRepo.saveAndFlush(po));
	    return res;
	}
	
	@WebMethod
	public POResource CancelPurchaseOrder(@WebParam(name="id") Long id){
		PurchaseOrder po = poRepo.findOne(id);
		po.setStatus(Status.CLOSED);
	    POResource res = POResourceAssembler.toResource(poRepo.saveAndFlush(po));
	    return res;
	}
}
