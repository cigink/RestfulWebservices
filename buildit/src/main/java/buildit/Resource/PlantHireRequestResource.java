package buildit.Resource;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import buildit.models.Customer;
import buildit.models.PoStatus;
import buildit.models.Status;
import buildit.models.WorksEngineer;
import buildit.util.ResourceSupport;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
//@XmlRootElement(name = "plant_hire_request")
public class PlantHireRequestResource extends ResourceSupport {

	Long idRes;
	Long poId;
	PlantResource plant;
	Customer siteEngineer;
	WorksEngineer worksEngineer;
	Float price;
	@Temporal(TemporalType.DATE)
	Date startDate;

	@Temporal(TemporalType.DATE)
	Date endDate;
	Status status;
	PoStatus postatus;
	
}
