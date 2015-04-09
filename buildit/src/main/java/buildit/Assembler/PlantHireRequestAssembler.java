package buildit.Assembler;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import buildit.Resource.PlantHireRequestResource;
import buildit.Resource.PlantResource;
import buildit.controllers.PHRController;
import buildit.models.Plant;
import buildit.models.PlantHireRequest;
import buildit.models.PoStatus;
import buildit.models.Status;
import buildit.util.ExtendedLink;

public class PlantHireRequestAssembler extends
		ResourceAssemblerSupport<PlantHireRequest, PlantHireRequestResource> {

	PlantResourceAssembler plantAssembler = new PlantResourceAssembler();

	public PlantHireRequestAssembler() {
		super(PHRController.class, PlantHireRequestResource.class);
	}

	@Override
	public PlantHireRequestResource toResource(PlantHireRequest phr) {
		PlantHireRequestResource phrResource = createResourceWithId(
				phr.getId(), phr);
//		phrResource.setIdRes(phr.getId());
		phrResource.setPrice(phr.getPrice());
		phrResource.setEndDate(phr.getEndDate());
		phrResource.setStartDate(phr.getStartDate());
		phrResource.setStatus(phr.getStatus());
		phrResource.setPostatus(phr.getPostatus());
		Plant p = phr.getPlant();
		PlantResource ps = plantAssembler.toResources(p);
		phrResource.setPlant(ps);
		
		if(phr.getStatus()==Status.PENDING_CONFIRMATION && phr.getPostatus()==PoStatus.PENDING_UPDATE)
		{
			switch(phr.getStatus()){
			case PENDING_CONFIRMATION:
				phrResource.add(new ExtendedLink(linkTo(
						methodOn(PHRController.class).rejectPhr(phr.getId()))
						.toString(), "rejectPHR", "DELETE"));
				try {
					phrResource.add(new ExtendedLink(linkTo(
							methodOn(PHRController.class).acceptPhrforRejectPo(phr.getId()))
							.toString(), "acceptPHR", "POST"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
		}
		}
		else if(phr.getStatus()==Status.PENDING_CONFIRMATION && phr.getPostatus()==PoStatus.PENDING_UPDATES)
		{
			switch(phr.getStatus()){
			case PENDING_CONFIRMATION:
				phrResource.add(new ExtendedLink(linkTo(
						methodOn(PHRController.class).rejectPhr(phr.getId()))
						.toString(), "rejectPHR_Update", "DELETE"));
				try {
					phrResource.add(new ExtendedLink(linkTo(
							methodOn(PHRController.class).acceptPhrforRejectPo(phr.getId()))
							.toString(), "acceptPHR_Update", "POST"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
		}
		}
		else if(phr.getStatus()==Status.ACCEPTED && phr.getPostatus()==PoStatus.PENDING_UPDATE)
		{
			switch(phr.getStatus()){
			case ACCEPTED:
			try {
				phrResource.add(new ExtendedLink(linkTo(
						methodOn(PHRController.class).updatePhrforRejectPo(null, phr.getId()))
						.toString(), "updatePHR", "PUT"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			phrResource.add(new ExtendedLink(linkTo(
					methodOn(PHRController.class).cancelPHR(phr.getId()))
					.toString(), "cancelPHR", "POST"));
			break;
		}
		}
		else if(phr.getPostatus()!=PoStatus.ACCEPTED && phr.getPostatus()!=PoStatus.CLOSED){
		switch (phr.getStatus()) {
		case PENDING_CONFIRMATION:
			phrResource.add(new ExtendedLink(linkTo(
					methodOn(PHRController.class).rejectPhr(phr.getId()))
					.toString(), "rejectPHR", "DELETE"));
			try {
				phrResource.add(new ExtendedLink(linkTo(
						methodOn(PHRController.class).acceptPhr(phr.getId()))
						.toString(), "acceptPHR", "POST"));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		// case ACCEPTED:
		// phrResource.add(new ExtendedLink(linkTo(
		// methodOn(PHRController.class).acceptPhr(phr.getId()))
		// .toString(), "acceptPOUpdate", "POST"));
		// break;
		case REJECTED:
			try {
				phrResource.add(new ExtendedLink(linkTo(
						methodOn(PHRController.class).updatePhr(null, phr.getId()))
						.toString(), "updatePHR", "PUT"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			phrResource.add(new ExtendedLink(linkTo(
					methodOn(PHRController.class).cancelPHR(phr.getId()))
					.toString(), "cancelPHR", "POST"));
			break;
		case CLOSED:
//			phrResource.add(new ExtendedLink(linkTo(
//					methodOn(PHRController.class).closePHR(phr.getId()))
//					.toString(), "closePHR", "PUT"));
			break;
		case ACCEPTED:
			try {
				phrResource.add(new ExtendedLink(linkTo(
						methodOn(PHRController.class).updatePhrforextend(null,phr.getId()))
						.toString(), "extendPHR", "PUT"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			phrResource.add(new ExtendedLink(linkTo(
					methodOn(PHRController.class).closePHR(phr.getId()))
					.toString(), "closePHR", "POST"));
			break;
		default:
			break;
		}
		}
		else{
			switch (phr.getPostatus()) {
//			case PENDING_CONFIRMATION:
//				phrResource.add(new ExtendedLink(linkTo(
//						methodOn(PHRController.class).rejectPhr(phr.getId()))
//						.toString(), "rejectPHR", "DELETE"));
//				try {
//					phrResource.add(new ExtendedLink(linkTo(
//							methodOn(PHRController.class).acceptPhr(phr.getId()))
//							.toString(), "acceptPHR", "POST"));
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				break;
//			// case ACCEPTED:
//			// phrResource.add(new ExtendedLink(linkTo(
//			// methodOn(PHRController.class).acceptPhr(phr.getId()))
//			// .toString(), "acceptPOUpdate", "POST"));
//			// break;
//			case REJECTED:
//				try {
//					phrResource.add(new ExtendedLink(linkTo(
//							methodOn(PHRController.class).updatePhr(null, phr.getId()))
//							.toString(), "updatePHR", "PUT"));
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				phrResource.add(new ExtendedLink(linkTo(
//						methodOn(PHRController.class).closePHR(phr.getId()))
//						.toString(), "cancelPHR", "POST"));
//				break;
			case CLOSED:

				break;
			case ACCEPTED:
//				System.out.println("Inside Accepted");
				try {
					phrResource.add(new ExtendedLink(linkTo(
							methodOn(PHRController.class).approveInvoice(phr.getId()))
							.toString(), "accept_Invoice", "POST"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					phrResource.add(new ExtendedLink(linkTo(
							methodOn(PHRController.class).rejectInvoice(phr.getId()))
							.toString(), "reject_Invoice", "DELETE"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
			
		}	

		return phrResource;
	}

	public List<PlantHireRequestResource> toResource(List<PlantHireRequest> pos) {
		List<PlantHireRequestResource> poress = new ArrayList<>();

		for (PlantHireRequest po : pos) {
			poress.add(toResource(po));
		}
		return poress;
	}
	
}
