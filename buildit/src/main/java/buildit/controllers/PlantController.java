/**
 * 
 */
package buildit.controllers;

/**
 * @author Vinod Rockson
 *
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import buildit.Resource.PlantResource;
import buildit.service.RentalService;

@RestController
@RequestMapping("/rest/plants")
public class PlantController {
	@Autowired
    RentalService rentitProxy;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public List<PlantResource> getAllPlants(){
		return rentitProxy.findAll();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/query")
	@ResponseStatus(HttpStatus.OK)
	public List<PlantResource> listPlantCatalog(@RequestParam("name") String name,
			@RequestParam("startDate") String date1, @RequestParam("endDate") String date2) throws Exception{
		
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		    Date Date1 = df.parse(date1);
		    Date Date2 = df.parse(date2);
		    
		    return rentitProxy.findAvailablePlants(name, Date1, Date2);
			 }
	}

