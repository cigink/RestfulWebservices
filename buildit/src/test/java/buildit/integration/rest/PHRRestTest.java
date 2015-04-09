/**
 *
 */
package buildit.integration.rest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import buildit.Application;
import buildit.SimpleDbConfig;
import buildit.Resource.PlantHireRequestResource;
import buildit.Resource.PlantResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class,
		SimpleDbConfig.class })
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
public class PHRRestTest {
	@Autowired
	private WebApplicationContext webac;

	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webac).build();
	}

	@SuppressWarnings("deprecation")
	@Test
	@DatabaseSetup(value = "EmptyDatabase.xml")
	public void testCreatePlantHireRequest() throws Exception {
		PlantResource plant = new PlantResource();
		plant.add(new Link("http://localhost:8080/rest/plants/10001"));
		plant.setPrice(300f);

		PlantHireRequestResource phr = new PlantHireRequestResource();
		phr.setPlant(plant);
		phr.setStartDate(new Date(2014, 10, 6));
		phr.setEndDate(new Date(2014, 10, 10));

		MvcResult result = mockMvc
				.perform(
						post("/rest/phr").contentType(
								MediaType.APPLICATION_JSON).content(
								mapper.writeValueAsString(phr)))
				.andExpect(status().isCreated())
				// Verify the cost associated with the PHR
				.andReturn();

		PlantHireRequestResource phrp = mapper.readValue(result.getResponse()
				.getContentAsString(), PlantHireRequestResource.class);
		assertThat(phrp.getLink("self"), is(notNullValue()));
		assertThat(phrp.getlink("acceptPHR"), is(notNullValue()));
		assertThat(phrp.getlink("rejectPHR"), is(notNullValue()));
		assertThat(phrp.getLinks().size() + phrp.getlinks().size(), is(3));
	}

	@Test
	@DatabaseSetup(value = "DatabaseWithPendingPHR.xml")
	public void testApprovePlantHireRequest() throws Exception {
		MvcResult result = mockMvc.perform(get("/rest/phr/{id}", 10001L))
				.andExpect(status().isOk()).andReturn();
		PlantHireRequestResource phrp = mapper.readValue(result.getResponse()
				.getContentAsString(), PlantHireRequestResource.class);

		System.out.println(phrp);
		assertThat(phrp.getlink("acceptPHR"), is(notNullValue()));

		Link approve = phrp.getlink("acceptPHR");

		result = mockMvc.perform(post(approve.getHref()))
				.andExpect(status().isOk()).andReturn();

		phrp = mapper.readValue(result.getResponse().getContentAsString(),
				PlantHireRequestResource.class);
		assertThat(phrp.getLink("self"), is(notNullValue()));
		assertThat(phrp.getlink("closePHR"), is(notNullValue()));
		assertThat(phrp.getlinks().size() + phrp.getLinks().size(), is(2));
	}

	@Test
	@DatabaseSetup(value = "DatabaseWithPendingPHR.xml")
	public void testRejectPhr() throws Exception {
		MvcResult result = mockMvc
				.perform(delete("/rest/phr/{id}/accept", 10001L))
				.andExpect(status().isOk()).andReturn();
		PlantHireRequestResource phrrp = mapper.readValue(result.getResponse()
				.getContentAsString(), PlantHireRequestResource.class);
		assertThat(phrrp.getlink("rejectPHR"), is(notNullValue()));

		Link reject = phrrp.getlink("rejectPHR");

		result = mockMvc.perform(delete(reject.getHref()))
				.andExpect(status().isOk()).andReturn();

		phrrp = mapper.readValue(result.getResponse().getContentAsString(),
				PlantHireRequestResource.class);
		assertThat(phrrp.getLink("self"), is(notNullValue()));
		// assertThat(phrrp.getLink("submitPurchaseOrder"), is(notNullValue()));
		assertThat(phrrp.getLinks().size() + phrrp.getlinks().size(), is(2));
	}
}