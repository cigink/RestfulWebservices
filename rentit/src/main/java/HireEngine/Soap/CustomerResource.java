package HireEngine.Soap;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name="customer")
public class CustomerResource {

	Long id;

	String name;
	String vatNumber;
}
