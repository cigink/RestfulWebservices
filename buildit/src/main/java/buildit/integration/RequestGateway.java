/**
 * 
 */
package buildit.integration;

import java.util.List;

import buildit.Resource.PlantResource;

/**
 * @author Vinod Rockson
 *
 */
public interface RequestGateway {
	List<PlantResource> getAllPlants();
	PlantResource getPlant(Long id);
}
