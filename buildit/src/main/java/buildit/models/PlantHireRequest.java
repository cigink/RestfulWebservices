package buildit.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Entity
@Data
//@XmlRootElement
public class PlantHireRequest {
	@Id
	@GeneratedValue
	Long id;
	
	Long poId;

	@OneToOne
	Plant plant;
	
	String plantRef;
	String plantName;
	

	@OneToOne
	Customer siteEngineer;
	
	@OneToOne
	WorksEngineer worksEngineer;

	Float price;

	@Temporal(TemporalType.DATE)
	Date startDate;

	@Temporal(TemporalType.DATE)
	Date endDate;

	@Enumerated(EnumType.STRING)
	Status status;
	
	@Enumerated(EnumType.STRING)
	PoStatus postatus;
	

	@SuppressWarnings("deprecation")
	public void calcCost(Plant p) {
		price = ((endDate.getDate() - startDate.getDate())+1)
				 * p.getPrice();
	
	}
}
