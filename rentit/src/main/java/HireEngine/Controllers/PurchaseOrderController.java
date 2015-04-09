package HireEngine.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/phrs")
public class PurchaseOrderController {
	@RequestMapping(value="/form")
	public String getForm() {
		return "phrs/query_plant_catalog";
	}

}
