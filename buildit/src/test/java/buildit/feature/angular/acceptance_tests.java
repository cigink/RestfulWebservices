/**
 * 
 */
package buildit.feature.angular;

import java.sql.Statement;

import junit.framework.Assert;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * @author Vinod Rockson
 *
 */
public class acceptance_tests {
	
	IDatabaseTester db = null;
	WebDriver user = null;

	@Before
	public void beforeScenario() throws ClassNotFoundException {
	    if (db == null) 
	       db = new JdbcDatabaseTester("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/planttest", "postgres", "vinodpresql");

	    System.setProperty("webdriver.chrome.driver", "C:/chromedriver");
	    user = new ChromeDriver();
	}

	@After
	public void afterScenario() throws Exception {
	    Statement st = db.getConnection().getConnection().createStatement();
	    st.executeUpdate("delete from plant_hire_request");
	    st.executeUpdate("delete from plant");
	    st.close();
	}	
	//update PHR
	@Given("^PHR in \"(.*?)\" status$")
	public void phr_in_status(String arg1) throws Throwable {
		Statement st = db.getConnection().getConnection().createStatement();
		if(arg1=="REJECTED")
    	st.executeUpdate("delete from plant_hire_request");
	}

	@SuppressWarnings("deprecation")
	@Given("^Site Engineer is viewing \"(.*?)\"$")
	public void site_Engineer_is_viewing(String arg1) throws Throwable {
		user.get("http://localhost:8080/#/phr");
		Assert.assertEquals(user.findElement(By.name("Plant Name")), arg1);
	}

	@When("^Site Engineer clicks the \"(.*?)\" button$")
	public void site_Engineer_clicks_the_button(String arg1) throws Throwable {
		user.findElement(By.id(arg1)).click();
	}

	@Then("^Site Engineer should be able change PHR$")
	public void site_Engineer_should_be_able_change_PHR() throws Throwable {
		user.get("http://localhost:8080/#/phr/update");
		user.findElement(By.name("Plant Name")).sendKeys("Dumper");
		user.findElement(By.name("Search Plant")).click();
		user.findElement(By.id("action")).click();
	}

	@When("^the Plants is not listed$")
	public void the_Plants_is_not_listed() throws Throwable {
		user.get("http://localhost:8080/#/phr");
	}

	@Then("^Site Engineer should be able cancel the PHR$")
	public void site_Engineer_should_be_able_cancel_the_PHR() throws Throwable {
		user.get("http://localhost:8080/#/phr");
		user.findElement(By.name("cancelPHR")).click();
	}
	
	//extend PHR(missing function)
	@Then("^Site Engineer should be able update PHR$")
	public void site_Engineer_should_be_able_update_PHR() throws Throwable {
		user.get("http://localhost:8080/#/phr/update");
		user.findElement(By.name("Plant Name")).sendKeys("Truck");
		user.findElement(By.name("Search Plant")).click();
		user.findElement(By.id("action")).click();
	}
	
	//cancelPHR(missing functions)
	@Then("^Site Engineer should be able to see \"(.*?)\" PHR status$")
	public void site_Engineer_should_be_able_to_see_PHR_status(String arg1) throws Throwable {
		user.get("http://localhost:8080/#/phr");
		user.findElement(By.name("cancelPHR")).click();
		user.findElement(By.name("PHR Status")).sendKeys("CANCELLED");
	}

	@Given("^PO in \"(.*?)\" status$")
	public void po_in_status(String arg1) throws Throwable {
		Statement st = db.getConnection().getConnection().createStatement();
		if(arg1=="REJECTED")
			st.executeUpdate("delete from plant_hire_request");
		else
			st.executeUpdate("delete from plant_hire_request");
			
	}

}
