package HireEngine.Resources;

import java.util.Date; 

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import HireEngine.Models.Customer;
import HireEngine.Models.InStatus;
import HireEngine.Models.Status;
import HireEngine.SupportClass.ResourceSupport;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "purchase_order")
public class POResource extends ResourceSupport {

	Long idPo;
	Long phrId;
	String Name;
	@Temporal(TemporalType.DATE)
	Date startDate;

	@Temporal(TemporalType.DATE)
	Date endDate;	
	Status status;	
	InStatus instatus;
	Float cost;
	PlantResource plant;
	Customer customer;
}


