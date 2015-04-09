package HireEngine.Assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import HireEngine.Models.PurchaseOrder;
import HireEngine.Models.Status;
import HireEngine.Resources.POResource;
import HireEngine.SupportClass.ExtendedLink;
import HireEngine.integration.rest.POController;

public class POAssembler extends
		ResourceAssemblerSupport<PurchaseOrder, POResource> {

	public POAssembler() {
		super(POController.class, POResource.class);
	}

	@Override
	public POResource toResource(PurchaseOrder po) {
		POResource poResource = createResourceWithId(po.getId(), po);
		poResource.setIdPo(po.getId());
//		System.out.println("Inside PO Assembler");
//		System.out.println(po.getId());
//		System.out.println(poResource.getIdPo());
		poResource.setCost(po.getCost());
		poResource.setEndDate(po.getEndDate());
		PlantAssembler assem = new PlantAssembler();
		poResource.setPlant(assem.toResource(po.getPlant()));
		poResource.setStartDate(po.getStartDate());
		Status s = po.getStatus();
		poResource.setStatus(s);
		poResource.setPhrId(po.getPhrId());
		poResource.setInstatus(po.getInstatus());
//		System.out.println(s);
//		System.out.println(po);
		
		switch (s) { 
		case PENDING_CONFIRMATION:
//			System.out.println(po.getId());
			try {
				poResource.add(new ExtendedLink(linkTo(
						methodOn(POController.class).rejectPO(po.getId()))
						.toString(), "rejectPO", "DELETE"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				poResource.add(new ExtendedLink(linkTo(
						methodOn(POController.class).acceptPO(po.getId()))
						.toString(), "acceptPO", "POST"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			case ACCEPTED:
			poResource.add(new ExtendedLink(linkTo(
					methodOn(POController.class).closePO(po.getId()))
					.toString(), "closePO", "POST"));
			poResource.setStatus(Status.PENDING_CONFIRMATION);
//			poResource
//					.add(new ExtendedLink(linkTo(
//							methodOn(POController.class).requestPOUpdate(
//									poResource, po.getId())).toString(),
//							"requestPOUpdate", "POST"));
			break;
			case REJECTED:
			try {
				poResource.add(new ExtendedLink(linkTo(
						methodOn(POController.class).updatePO(po.getId()))
						.toString(), "updatePO", "POST"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			poResource.add(new ExtendedLink(linkTo(
					methodOn(POController.class).closePO(po.getId()))
					.toString(), "closePO", "POST"));
			break;
//		case PENDING_UPDATE:
//			poResource.add(new ExtendedLink(linkTo(
//					methodOn(POController.class).rejectPOUpdate(po.getId()))
//					.toString(), "rejectPOUpdate", "DELETE"));
//			poResource.add(new ExtendedLink(linkTo(
//					methodOn(POController.class).acceptPOUpdate(po.getId()))
//					.toString(), "acceptPOUpdate", "POST"));
//			break;

		case CLOSED:
			break;
		default:
			break;
		}
//		System.out.println(poResource);
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
