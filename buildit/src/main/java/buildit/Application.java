package buildit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import buildit.service.CustomResponseErrorHandler;
import buildit.service.RentalService;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@ImportResource({"classpath:META-INF/integration/integration.xml","classpath:META-INF/integration/integration-mail.xml"})
public class Application {
	
	@Autowired
	private WebMvcProperties mvcProperties = new WebMvcProperties();
	
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate _restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		_restTemplate.setMessageConverters(messageConverters);
		_restTemplate.setErrorHandler(new CustomResponseErrorHandler());
		return _restTemplate;
	}

	@Bean
	public RentalService rentalService() {
		return new RentalService();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
