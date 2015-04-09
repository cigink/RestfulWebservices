package HireEngine.Models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Entity
@Data
@XmlRootElement
public class Customer {

	@Id
	@GeneratedValue
	Long id;

	String name;
	String vatNumber;

}
