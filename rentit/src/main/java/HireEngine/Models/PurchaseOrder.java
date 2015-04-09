package HireEngine.Models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import HireEngine.Models.Plant;
import lombok.Data;

@Entity
@Data
public class PurchaseOrder {

	@Id
	@GeneratedValue
	Long id;
	
	Long phrId;

	Float cost;

	@OneToOne
	Customer customer;

	@OneToOne(cascade = CascadeType.ALL)
	Plant plant;

	@Temporal(TemporalType.DATE)
	Date startDate;

	@Temporal(TemporalType.DATE)
	Date endDate;	
	
	Status status;
	
	InStatus instatus;

	@SuppressWarnings("deprecation")
	public void calcCost(Plant p) {
		cost = ((endDate.getDate() - startDate.getDate())+1)
				 * p.getPrice();
	}

}
