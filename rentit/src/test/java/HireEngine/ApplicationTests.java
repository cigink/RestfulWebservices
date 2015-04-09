package HireEngine;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import HireEngine.repositiories.PlantRepository;

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
public class ApplicationTests {
	@Autowired
	PlantRepository plantRepo;

	@Test
	@DatabaseSetup("dataset.xml")
	public void countAllPlants() {
		assertEquals(14, plantRepo.count());
	}

	@Test
	@DatabaseSetup("dataset.xml")
	public void countExcavators() {
		assertEquals(6, plantRepo.findByNameLike("Excavator").size());
	}

	@Test
	@DatabaseSetup("dataset.xml")
	public void countLikeNameAndPriceRange() {
		assertEquals(4, plantRepo.finderMethod("Excavator", 200.00f, 400.00f)
				.size());
	}
}