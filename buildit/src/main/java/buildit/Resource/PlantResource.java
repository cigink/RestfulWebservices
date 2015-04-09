package buildit.Resource;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import buildit.util.ResourceSupport;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@XmlRootElement(name = "plant")
public class PlantResource extends ResourceSupport {

	Long idres;
	String name;
	String description;
	Float price;

}
