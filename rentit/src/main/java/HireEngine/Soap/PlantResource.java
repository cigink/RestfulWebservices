package HireEngine.Soap;

/**
 * @author Vinod Rockson
 *
 */
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name="plant")
public class PlantResource {
	Long idRes;
	String name;
	String desc;
	Float price;
}

