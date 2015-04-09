package HireEngine.Soap;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import HireEngine.Soap.PlantResource;
import lombok.Data;

@Data
@XmlRootElement(name = "purchase_order")
public class POResource {
	
	Long idPo;
	String Name;
	@Temporal(TemporalType.DATE)
	Date startDate;

	@Temporal(TemporalType.DATE)
	Date endDate;
	Float Cost;
	PlantResource plantResource;
	CustomerResource customerResource;

}
