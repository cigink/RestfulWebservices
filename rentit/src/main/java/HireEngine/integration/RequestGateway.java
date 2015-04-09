/**
 * 
 */
package HireEngine.integration;

import java.util.List;

import HireEngine.Resources.POResource;

/**
 * @author Vinod Rockson
 *
 */
public interface RequestGateway {
	List<POResource> getAllPOs();
	POResource getPo(Long id);
}
