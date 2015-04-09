package HireEngine.Soap;

import java.util.ArrayList; 
import java.util.List;

import HireEngine.Soap.PlantResource;
import HireEngine.Models.Plant;

/**
 * @author Vinod Rockson
 *
 */
public class PlantResourceAssembler {
	public static PlantResource toResources(Plant plant) {
		PlantResource resource = new PlantResource();

		resource.setName(plant.getName());
		resource.setDesc(plant.getDescription());
		resource.setPrice(plant.getPrice());
		return resource;
	}


	public static PlantResourceList toResource(List<Plant> plants) {
		 List<PlantResource> press = new ArrayList<PlantResource>();

		for (Plant plant : plants) {
			press.add(toResources(plant));
		}
		return new PlantResourceList(press);
	}
}

