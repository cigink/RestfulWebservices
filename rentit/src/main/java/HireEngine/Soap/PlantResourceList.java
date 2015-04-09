package HireEngine.Soap;

import java.util.List; 

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import HireEngine.Soap.PlantResource;
import lombok.Data;

/**
 * @author Vinod Rockson
 *
 */
@Data
@XmlRootElement(name = "plants")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantResourceList {
	@XmlElement(name = "plant")
	private List<PlantResource> resources;

	public PlantResourceList() {
	}

	public PlantResourceList(List<PlantResource> plants) {
		this.resources = plants;
	}
}
