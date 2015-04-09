package HireEngine.Assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import HireEngine.Models.Plant;
import HireEngine.Resources.PlantResource;
import HireEngine.SupportClass.ExtendedLink;
import HireEngine.integration.rest.HireController;

public class PlantAssembler extends ResourceAssemblerSupport<Plant, PlantResource>{

	public PlantAssembler() {
		super(HireController.class, PlantResource.class);
	}

	public PlantResource toResource(Plant plant) {
		PlantResource res = createResourceWithId(
				plant.getId(), plant);

		res.setName(plant.getName());
		res.setDescription(plant.getDescription());
		res.setPrice(plant.getPrice());
		try {
			res.add(new ExtendedLink(linkTo(
					methodOn(HireController.class).updatePlant(plant.getId(),null))
					.toString(), "updatePlant", "PUT"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		
		res.add(new ExtendedLink(linkTo(
				methodOn(HireController.class).deletePlant(plant.getId()))
				.toString(), "deletePlant", "DELETE"));
		
		
		return res;
	}

	public List<PlantResource> toResource(List<Plant> plants) {
		List<PlantResource> ress = new ArrayList<>();

		for (Plant plant : plants) {
			ress.add(toResource(plant));
		}
		return ress;
	}
}
