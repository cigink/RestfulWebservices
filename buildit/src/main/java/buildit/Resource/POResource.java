package buildit.Resource;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import buildit.models.Customer;
import buildit.models.Status;
import buildit.util.ResourceSupport;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@XmlRootElement(name = "purchase_order")
public class POResource extends ResourceSupport {

	Long idPo;
	Long phrId;
	String Name;
	@Temporal(TemporalType.DATE)
	Date startDate;

	@Temporal(TemporalType.DATE)
	Date endDate;
//	Status status;
	Float cost;
	PlantResource plant;
	Customer customer;

}
