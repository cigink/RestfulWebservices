package HireEngine.integration.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import HireEngine.Application;
import HireEngine.SimpleDbConfig;
import HireEngine.Resources.POResource;
import HireEngine.Resources.PlantResource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class,
		SimpleDbConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
public class POControllerIntegrationTest {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	private ObjectMapper mapper = new ObjectMapper();

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@SuppressWarnings("deprecation")
	@Test
	@DatabaseSetup(value = "EmptyDatabase.xml")
	public void testCreatePurchaseOrder() throws Exception {
		PlantResource plant = new PlantResource();
		plant.setIdRes(10001L);
		plant.setName("Excavator");
		plant.setPrice(300f);
		plant.setDescription("New Plant");
		plant.add(new Link("http://localhost:8070/rest/plants/10001"));
		plant.setPrice(300f);

		POResource poResource = new POResource();
		poResource.setPlant(plant);
		poResource.setStartDate(new Date(2014, 10, 6));
		poResource.setEndDate(new Date(2014, 10, 10));

		MvcResult result = mockMvc
				.perform(
						post("/rest/pos").contentType(
								MediaType.APPLICATION_JSON).content(
								mapper.writeValueAsString(poResource)))
				.andExpect(status().isCreated()).andReturn();

		POResource poR = mapper.readValue(result.getResponse()
				.getContentAsString(), POResource.class);
		assertThat(poR.getLink("self"), is(notNullValue()));
		assertThat(poR.get_link("acceptPO"), is(notNullValue()));
		assertThat(poR.get_link("rejectPO"), is(notNullValue()));
		assertThat(poR.getLinks().size() + poR.get_links().size(), is(3));
	}
	
	@Test
	@DatabaseSetup(value = "dataset.xml")
	public void testAcceptPurchaseOrder() throws Exception {
		MvcResult result = mockMvc
				.perform(get("/rest/pos/{id}", 10001))
				.andExpect(status().isOk()).andReturn();
		
		POResource poResource = mapper.readValue(result.getResponse()
				.getContentAsString(), POResource.class);
		
		assertThat(poResource.get_link("acceptPO"), is(notNullValue()));

		Link approve = poResource.get_link("acceptPO");

		result = mockMvc.perform(post(approve.getHref()))
				.andExpect(status().isOk()).andReturn();

		poResource = mapper.readValue(
				result.getResponse().getContentAsString(), POResource.class);
		assertThat(poResource.getLink("self"), is(notNullValue()));
		assertThat(poResource.get_link("closePO"), is(notNullValue()));
		assertThat(
				poResource.getLinks().size() + poResource.get_links().size(),
				is(2));
	}

	@SuppressWarnings("deprecation")
	@Test
	@DatabaseSetup(value = "dataset.xml")
<<<<<<< HEAD
    public void testUpdatePurchaseOrder() throws Exception {
        MvcResult result = mockMvc
        		.perform(get("/rest/pos/{id}", 10003))
                .andExpect(status().isOk()).andReturn();
        
        POResource poResource = mapper.readValue(result.getResponse()
        		.getContentAsString(), POResource.class);
        assertThat(poResource.get_link("updatePO"), is(notNullValue()));

        PlantResource plant = new PlantResource();
        plant.setIdRes(10002L);
        plant.setPrice(800f);
        poResource.setPlant(plant);
		poResource.setStartDate(new Date(2014, 10, 8));
		poResource.setEndDate(new Date(2014, 10, 19));
        
        Link modify = poResource.get_link("updatePO");

        result = mockMvc.perform(post(modify.getHref())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(poResource)))
                .andExpect(status().isOk())
                .andReturn();

        poResource = mapper.readValue(result.getResponse().getContentAsString(), POResource.class);
        assertThat(poResource.getLink("self"), is(notNullValue()));
		assertThat(poResource.get_link("closePO"), is(notNullValue()));
		assertThat(
				poResource.getLinks().size() + poResource.get_links().size(),
				is(2));
    }
	
	@Test
	@DatabaseSetup(value = "dataset.xml")
    public void testRejectPurchaseOrder() throws Exception {
        MvcResult result = mockMvc
        		.perform(get("/rest/pos/{id}", 10001))
                .andExpect(status().isOk()).andReturn();
        POResource poResource = mapper.readValue(result.getResponse()
        		.getContentAsString(), POResource.class);
        assertThat(poResource.get_link("rejectPO"), is(notNullValue()));
        
        Link delete = poResource.get_link("rejectPO");
        
        result = mockMvc.perform(delete(delete.getHref())).
        		andExpect(status().isOk()).andReturn();

        poResource = mapper.readValue(result.getResponse().getContentAsString(), POResource.class);
        assertThat(poResource.getLink("self"), is(notNullValue()));
        assertThat(poResource.get_link("updatePO"), is(notNullValue()));
        assertThat(poResource.get_link("closePO"), is(notNullValue()));
        assertThat(
        		poResource.getLinks().size() + poResource.get_links().size(),
        		is(3));
    }
	
	@Test
	@DatabaseSetup(value = "dataset.xml")
    public void testClosePurchaseOrder() throws Exception {
		MvcResult result = mockMvc
				.perform(get("/rest/pos/{id}", 10005))
				.andExpect(status().isOk()).andReturn();
		
        POResource poResource = mapper.readValue(result.getResponse().getContentAsString(), POResource.class);
        
        assertThat(poResource.get_link("closePO"), is(notNullValue()));

        Link finish = poResource.get_link("closePO");

        result = mockMvc.perform(delete(finish.getHref()))
                .andExpect(status().isOk())
                .andReturn();

        poResource = mapper.readValue(result.getResponse().getContentAsString(), POResource.class);
        
        assertThat(poResource.getLink("self"), is(notNullValue()));
        assertThat(
        		poResource.getLinks().size(), is(1));
    }
}
=======
	public void testUpdatePurchaseOrder() throws Exception {
		MvcResult result = mockMvc.perform(get("/rest/pos/{id}", 10001L))
				.andExpect(status().isOk()).andReturn();
		POResource poResource = mapper.readValue(result.getResponse()
				.getContentAsString(), POResource.class);
		assertThat(poResource.get_link("updatePO"), is(notNullValue()));

		PlantResource plant = new PlantResource();
		plant.setIdRes(10002L);
		plant.setPrice(800f);
		poResource.setPlant(plant);
		poResource.setStartDate(new Date(2014, 10, 8));
		poResource.setEndDate(new Date(2014, 10, 19));

		Link modify = poResource.get_link("updatePO");

		result = mockMvc
				.perform(
						post(modify.getHref()).contentType(
								MediaType.APPLICATION_JSON).content(
								mapper.writeValueAsString(poResource)))
				.andExpect(status().isCreated()).andReturn();

		poResource = mapper.readValue(
				result.getResponse().getContentAsString(), POResource.class);
		assertThat(poResource.getLink("self"), is(notNullValue()));
		assertThat(poResource.get_link("acceptPO"), is(notNullValue()));
		assertThat(poResource.get_link("rejectPO"), is(notNullValue()));
		assertThat(
				poResource.getLinks().size() + poResource.get_links().size(),
				is(3));
	}

	@Test
	@DatabaseSetup(value = "DatabaseWithPendingPO.xml")
	public void testRejectPurchaseOrder() throws Exception {
		MvcResult result = mockMvc.perform(get("/rest/pos/{id}", 10002L))
				.andExpect(status().isOk()).andReturn();
		POResource poResource = mapper.readValue(result.getResponse()
				.getContentAsString(), POResource.class);
		assertThat(poResource.get_link("rejectPO"), is(notNullValue()));

		Link delete = poResource.get_link("rejectPO");

		result = mockMvc.perform(delete(delete.getHref()))
				.andExpect(status().isOk()).andReturn();

		poResource = mapper.readValue(
				result.getResponse().getContentAsString(), POResource.class);
		assertThat(poResource.getLink("self"), is(notNullValue()));
		assertThat(poResource.get_link("updatePO"), is(notNullValue()));
		assertThat(
				poResource.getLinks().size() + poResource.get_links().size(),
				is(2));
	}

	@Test
	@DatabaseSetup(value = "DatabaseWithPendingPO.xml")
	public void testClosePurchaseOrder() throws Exception {
		MvcResult result = mockMvc.perform(get("/rest/pos/{id}", 10002L))
				.andExpect(status().isOk()).andReturn();
		POResource poResource = mapper.readValue(result.getResponse()
				.getContentAsString(), POResource.class);
		assertThat(poResource.get_link("closePO"), is(notNullValue()));

		Link finish = poResource.get_link("closePO");
>>>>>>> 1b72259c019214836cd53ac20466530695e3ca19

		result = mockMvc.perform(delete(finish.getHref()))
				.andExpect(status().isOk()).andReturn();

		poResource = mapper.readValue(
				result.getResponse().getContentAsString(), POResource.class);
		assertThat(poResource.getLink("self"), is(notNullValue()));
		assertThat(
				poResource.getLinks().size() + poResource.get_links().size(),
				is(1));
	}
}
