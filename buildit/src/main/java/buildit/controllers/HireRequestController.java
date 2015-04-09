/**
 * 
 */
package buildit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Vinod Rockson
 *
 */
@Controller
@RequestMapping("/phr")
public class HireRequestController {
	@RequestMapping(value="/form")
	public String getForm() {
		return "phrs/query_plant_catalog";
	}
}

