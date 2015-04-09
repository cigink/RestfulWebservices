package HireEngine.integration.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import HireEngine.Application;
import HireEngine.SimpleDbConfig;
import HireEngine.Models.Plant;
import HireEngine.Resources.PlantResource;
import HireEngine.repositiories.PlantRepository;

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
@ActiveProfiles("test")
public class PlantRestControllerTests {
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private PlantRepository plantRepo;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@DatabaseSetup("dataset.xml")
	public void testGetAllPlants() throws Exception {
		mockMvc.perform(get("/rest/plants")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(14)));
	}

	@Test
	@DatabaseSetup("dataset.xml")
	public void testGetAnExistingPlant() throws Exception {
		Plant plant = plantRepo.findOne(10001L);

		mockMvc.perform(get("/rest/plants/{id}", 10001L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(plant.getName())))
				.andExpect(
						jsonPath("$.description", is(plant.getDescription())))
				.andExpect(
						jsonPath("$.price", is(plant.getPrice().doubleValue())));
	}

	@Test
	@DatabaseSetup("dataset.xml")
	public void testGetANonExistingPlant() throws Exception {
		mockMvc.perform(get("/rest/plants/{id}", 10015L)).andExpect(
				status().isNotFound());
	}

	@Test
	@DatabaseSetup("EmptyPlantCatalog.xml")
	public void testAddPlant() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		PlantResource plant = new PlantResource();
		plant.setName("Bicycle");
		plant.setDescription("Nice and shiny");
		plant.setPrice(100.0f);

		long count = plantRepo.count();

		mockMvc.perform(
				post("/rest/plants").contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(plant))).andExpect(
				status().isCreated());

		assertThat("Table size", plantRepo.count(), equalTo(count + 1));
	}
}