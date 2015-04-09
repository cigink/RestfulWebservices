/**
 * 
 */
package buildit.integration;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;

/**
 * @author Vinod Rockson
 *
 */
public class SirenToHalTransformer {
	
	public String transformCollection(String input) throws IOException {
		Resource spec = new ClassPathResource("specCollection.json", this.getClass());
        List<Object> objs = JsonUtils.jsonToList(spec.getInputStream());
        Chainr chainr = Chainr.fromSpec(objs);
        String result = JsonUtils.toJsonString(chainr.transform(JsonUtils.jsonToMap(input)));
		return result;
	}
	public String transformSingleInstance(String input) throws IOException {
		Resource spec = new ClassPathResource("specSingleInstance.json", this.getClass());
        List<Object> objs = JsonUtils.jsonToList(spec.getInputStream());
        Chainr chainr = Chainr.fromSpec(objs);
        String result = JsonUtils.toJsonString(chainr.transform(JsonUtils.jsonToMap(input)));
		return result;
	}
}

