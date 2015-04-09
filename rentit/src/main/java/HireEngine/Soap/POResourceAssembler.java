package HireEngine.Soap;

import java.util.ArrayList;
import java.util.List;

import HireEngine.Models.PurchaseOrder;
import HireEngine.Soap.POResource;

public class POResourceAssembler {

	public static POResource toResource(PurchaseOrder po) {
		POResource poResource = new POResource();
		poResource.setIdPo(po.getId());
		poResource.setCost(po.getCost());
		poResource.setStartDate(po.getStartDate());
		poResource.setEndDate(po.getEndDate());
		poResource.setPlantResource(PlantResourceAssembler.toResources(po.getPlant()));
		return poResource;
	}

	public List<POResource> toResource(List<PurchaseOrder> pos) {
		List<POResource> poress = new ArrayList<>();

		for (PurchaseOrder po : pos) {
			poress.add(toResource(po));
		}
		return poress;
	}


}
