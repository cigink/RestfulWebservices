/**
 * @author Cigin
 *
 */

package Integration.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import buildit.Application;
import buildit.SimpleDbConfig;
import buildit.Resource.POResource;
import buildit.Resource.PlantHireRequestResource;
import buildit.Resource.PlantResource;
import buildit.models.Status;
import buildit.service.CustomResponseErrorHandler;
import buildit.service.RentalService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, SimpleDbConfig.class})
@WebAppConfiguration
//@ActiveProfiles("test")
//***PURCHASE ORDER - SUCCESSFUL CASE****
public class PlantCatalogServiceTests_Success {
	
	@Value("${rentit.host:}")
	String host;

//	@Value("${rentit.port:}")
//	String port;
	
	@Configuration
	static class TestConfiguration {
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
	}

	@Autowired
	private RentalService rentItProxy;
	@Autowired
	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	@Before
	public void setup() {
		this.mockServer = MockRestServiceServer.createServer(this.restTemplate);
	}

	@Test//Query the Plants
	public void testFindAvailablePlants() throws Exception {
		Resource responseBody = new ClassPathResource("AvailablePlantsV1.json",
				this.getClass());
		ObjectMapper mapper = new ObjectMapper();
		List<PlantResource> list = mapper.readValue(
				responseBody.getFile(),
				mapper.getTypeFactory().constructCollectionType(List.class,
						PlantResource.class));

		mockServer
				.expect(requestTo(host+"/rest/plants/query?name=Excavator&startDate=2014-10-06&endDate=2014-10-10"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(
						withSuccess(responseBody, MediaType.APPLICATION_JSON));

		List<PlantResource> result = rentItProxy.findAvailablePlants(
				"Excavator", new LocalDate(2014, 10, 6).toDate(),
				new LocalDate(2014, 10, 10).toDate());

		mockServer.verify();
		assertEquals(result, list);
	}

	@Test//Choose any plant from the returned list of plants
	public void testGetaPlant() throws Exception {
		Resource responseBody = new ClassPathResource("AvailablePlantsV1.json",
				this.getClass());
		ObjectMapper mapper = new ObjectMapper();
		List<PlantResource> list = mapper.readValue(
				responseBody.getFile(),
				mapper.getTypeFactory().constructCollectionType(List.class,
						PlantResource.class));

		mockServer
				.expect(requestTo(host+"/rest/plants?id=10001"))
				.andExpect(method(HttpMethod.GET))
				.andRespond(
						withSuccess(responseBody, MediaType.APPLICATION_JSON));

		List<PlantResource> result = rentItProxy.findPlant(10001L);
		
		mockServer.verify();
		assertEquals(result, list);
	}
	
	@Test//Create PHR
	public void testCreatePHR() throws Exception {
		// Resource responseBody = new ClassPathResource("New.json",
		// this.getClass());
		ObjectMapper mapper = new ObjectMapper();
		PlantResource pRes = new PlantResource();
		pRes.setName("Excavator");
		pRes.setDescription("A plant");
		pRes.setPrice((float) 300.00);

		PlantHireRequestResource phr = new PlantHireRequestResource();
		phr.setPlant(pRes);
		phr.setStartDate(new LocalDate(2014, 10, 6).toDate());
		phr.setEndDate(new LocalDate(2014, 10, 10).toDate());

		mockServer
				.expect(requestTo(host+"/rest/phr"))
				.andExpect(method(HttpMethod.POST))
				.andRespond(
						withSuccess(mapper.writeValueAsString(phr),
								MediaType.APPLICATION_JSON));

		PlantHireRequestResource result = rentItProxy.createPHR(pRes,
				new LocalDate(2014, 10, 6).toDate(),
				new LocalDate(2014, 10, 10).toDate(), (float) 350.00);

		mockServer.verify();
		assertEquals(result, phr);
	}
	
   @Test//Approve PHR
   public void testApprovePHR() throws Exception {
		// Resource responseBody = new ClassPathResource("ApprovePHR.json",
		// this.getClass());
		ObjectMapper mapper = new ObjectMapper();
		
		PlantHireRequestResource res = new PlantHireRequestResource();
		PlantResource plantRes = new PlantResource();
		plantRes.setDescription("15 Tonne Large excavator");
		plantRes.setName("Excavator");
		plantRes.setIdres(10005L);
		res.setPlant(plantRes);
		res.setPrice(400f);
		res.setStartDate(new Date(114,10,06));
		res.setEndDate(new Date(114,10,10));
		res.setStatus(Status.ACCEPTED);
		
		System.out.println(mapper.writeValueAsString(res));
		
//		List<PlantHireRequestResource> pRes = mapper.readValue(
//				responseBody.getFile(),
//				mapper.getTypeFactory().constructCollectionType(List.class,
//						PlantHireRequestResource.class));
		
		mockServer
				.expect(requestTo(host+"/rest/phr/10005/accept"))
				.andExpect(method(HttpMethod.POST))
				.andRespond(
						withSuccess(mapper.writeValueAsString(res), MediaType.APPLICATION_JSON));
	    
		PlantHireRequestResource result = rentItProxy.approvePHR(
				new LocalDate(2014, 10, 6).toDate(),
				new LocalDate(2014, 10, 10).toDate(), 10005L, 400f);
	   
	    mockServer.verify();
	    assertEquals(result, res);	 
	
	}
	
	@Test//Check that the purchase order was successfully created
	public void testCreatePO() throws Exception {
		// Resource responseBody = new ClassPathResource("New.json",
		// this.getClass());
		ObjectMapper mapper = new ObjectMapper();
		PlantResource pRes = new PlantResource();
		pRes.setName("Excavator");
		pRes.setDescription("A plant");
		pRes.setPrice((float) 300.00);

		POResource po = new POResource();
		po.setName("Excavator");
		po.setPlant(pRes);
		po.setStartDate(new LocalDate(2014, 10, 6).toDate());
		po.setEndDate(new LocalDate(2014, 10, 10).toDate());

		mockServer
				.expect(requestTo(host+"/rest/pos/create"))
				.andExpect(method(HttpMethod.POST))
				.andRespond(
						withSuccess(mapper.writeValueAsString(po),
								MediaType.APPLICATION_JSON));

		POResource result = rentItProxy.createPurchaseOrder(1000L,pRes,
				new LocalDate(2014, 10, 6).toDate(),
				new LocalDate(2014, 10, 10).toDate(), (float) 350.00, Status.PENDING_CONFIRMATION, 5000L);

		mockServer.verify();
		assertEquals(result, po);
	}

	@Test//Update Purchase Order
	public void testUpdatePO() throws Exception {
//		Resource responseBody = new ClassPathResource("PurchaseOrderV1.json",
//				this.getClass());
		ObjectMapper mapper = new ObjectMapper();
		
		POResource res = new POResource();
		PlantResource plantRes = new PlantResource();
		plantRes.setDescription("15 Tonne Large excavator");
		plantRes.setName("Excavator");
		plantRes.setIdres(10005L);
		res.setPlant(plantRes);
		res.setCost(500f);
		res.setStartDate(new Date(114,10,07));
		res.setEndDate(new Date(114,10,11));
//		res.setStatus(Status.ACCEPTED);
		
//		List<POResource> pRes = mapper.readValue(
//				responseBody.getFile(),
//				mapper.getTypeFactory().constructCollectionType(List.class,
//						POResource.class));
		
		mockServer
				.expect(requestTo(host+"/rest/pos/update"))
				.andExpect(method(HttpMethod.POST))
				.andRespond(
						withSuccess(mapper.writeValueAsString(res), MediaType.APPLICATION_JSON));

	   
	    POResource result = rentItProxy.updatePurchaseOrder(10005L, plantRes, new LocalDate(2014, 10, 7).toDate(),new LocalDate(2014, 10, 11).toDate(), 500f, Status.ACCEPTED, 10005L);
	   
	    mockServer.verify();
	    assertEquals(result,res);	  
	}
	
 }