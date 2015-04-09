package HireEngine.Services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import HireEngine.Exception.PlantNotFoundException;
import HireEngine.Resources.POResource;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class BuildService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${buildit.host:}")
	String host;

//	@Value("${buildit.port:}")
//	String port;
	
	public POResource acceptPO(POResource pos) 
			throws RestClientException,PlantNotFoundException, JsonProcessingException {

	   POResource newPos = new POResource();
	   newPos.setPhrId(pos.getPhrId());
	   newPos.setIdPo(pos.getIdPo());
	   newPos.setStartDate(pos.getStartDate());
	   newPos.setEndDate(pos.getEndDate());
	   newPos.setCost(pos.getCost());
	   newPos.setPlant(pos.getPlant());
//	   System.out.println("Inside Buildit service");
//	   System.out.println(pos.getIdPo());

		ResponseEntity<POResource> PO = restTemplate.postForEntity(
				host+"/rest/phr/iaccept", newPos, POResource.class);
		return PO.getBody();
 }
	public POResource rejectPO(POResource pos) 
			throws RestClientException,PlantNotFoundException, JsonProcessingException {

	   POResource newPos = new POResource();
	   newPos.setPhrId(pos.getPhrId());
	   newPos.setIdPo(pos.getIdPo());
	   newPos.setStartDate(pos.getStartDate());
	   newPos.setEndDate(pos.getEndDate());
	   newPos.setCost(pos.getCost());
	   newPos.setPlant(pos.getPlant());
//	   System.out.println("Inside Buildit service");
//	   System.out.println(pos.getIdPo());

		ResponseEntity<POResource> PO = restTemplate.postForEntity(
				host+"/rest/phr/rejectpo", newPos, POResource.class);
		return PO.getBody();
 }
	
	public POResource updatePO(POResource pos) 
			throws RestClientException,PlantNotFoundException, JsonProcessingException {

	   POResource newPos = new POResource();
	   newPos.setPhrId(pos.getPhrId());
	   newPos.setIdPo(pos.getIdPo());
	   newPos.setStartDate(pos.getStartDate());
	   newPos.setEndDate(pos.getEndDate());
	   newPos.setCost(pos.getCost());
	   newPos.setPlant(pos.getPlant());
//	   System.out.println("Inside Buildit service");
//	   System.out.println(pos.getIdPo());

		ResponseEntity<POResource> PO = restTemplate.postForEntity(
				host+"/rest/phr/updatepo", newPos, POResource.class);
		return PO.getBody();
 }
	

}
