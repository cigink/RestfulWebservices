package buildit.Assembler;

import java.util.ArrayList;
import java.util.List;

import buildit.Resource.PlantResource;
import buildit.models.Plant;

public class PlantResourceAssembler {
	public PlantResource toResources(Plant plant) {
		PlantResource res = new PlantResource();

		res.setName(plant.getName());
		res.setDescription(plant.getDescription());
		res.setPrice(plant.getPrice());
		return res;
	}

	public List<PlantResource> toResource(List<Plant> plants) {
		List<PlantResource> pRess = new ArrayList<>();

		for (Plant plant : plants) {
			pRess.add(toResources(plant));
		}
		return pRess;
	}
}
