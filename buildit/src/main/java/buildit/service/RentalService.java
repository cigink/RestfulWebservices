package buildit.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import buildit.Resource.POResource;
import buildit.Resource.PlantHireRequestResource;
import buildit.Resource.PlantResource;
import buildit.models.Status;
import buildit.repositories.PlantRepository;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class RentalService {
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	PlantRepository pRepo;
	
	@Value("${rentit.host:}")
	String host;

//	@Value("${rentit.port:}")
//	String port;

	public List<PlantResource> findAvailablePlants(String plantName,
			Date startDate, Date endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		PlantResource[] plants = restTemplate
				.getForObject(
						host+"/rest/plants/query?name={name}&startDate={start}&endDate={end}",
						PlantResource[].class, plantName,
						formatter.format(startDate), formatter.format(endDate));

		return Arrays.asList(plants);
	}
	
	public List<PlantResource> findPlant(Long id) {

		PlantResource[] plants =restTemplate
				.getForObject(
						host+"/rest/plants?id={id}",
						PlantResource[].class, id);

		return Arrays.asList(plants);
	}
	
	public List<PlantResource> findAll() {
		PlantResource[] plants = restTemplate.getForObject(
				host+"/rest/plants", 
				PlantResource[].class);
		return Arrays.asList(plants);
	}

	public POResource createPurchaseOrder(Long id, PlantResource plant, Date startDate,
			Date endDate, Float cost, Status status, Long poid) throws RestClientException,
			PlantNotAvailableException, JsonProcessingException {

		POResource po = new POResource();
		po.setPhrId(id);
		po.setPlant(plant);
		po.setStartDate(startDate);
		po.setEndDate(endDate);
		po.setCost(cost);
		po.setIdPo(poid);
//		System.out.println("PHR");
//		System.out.println(po.getPhrId());
//		po.setStatus(status);

		ResponseEntity<POResource> PO = restTemplate.postForEntity(
				host+"/rest/pos/create", po, POResource.class);
		return PO.getBody();
	}
	
	public POResource updatePurchaseOrder(Long id, PlantResource plant, Date startDate,
			Date endDate, Float cost, Status status, Long poid) throws RestClientException,
			PlantNotAvailableException, JsonProcessingException {

		POResource po = new POResource();
		po.setPhrId(id);
		po.setPlant(plant);
		po.setStartDate(startDate);
		po.setEndDate(endDate);
		po.setCost(cost);
		System.out.println(cost);
		po.setIdPo(poid);
//		System.out.println("PHR");
//		System.out.println(po.getPhrId());
//		po.setStatus(status);

		ResponseEntity<POResource> PO = restTemplate.postForEntity(
				host+"/rest/pos/update", po, POResource.class);
		return PO.getBody();
	}
	
//	public List<POResource> updatePO(Date startDate, Date endDate, Long id) 
//			throws PlantNotAvailableException, JsonProcessingException {
//		
//	   ResponseEntity<POResource> por = new ResponseEntity<POResource>(null);
//	   PlantResource plant = new PlantResource();
//	   POResource po = new POResource();
//	   po.setPlant(plant);
//	   po.setIdPo(id);
//	   po.setStartDate(new LocalDate(2014,10,6).toDate());
//	   po.setEndDate(new LocalDate(2014,10,10).toDate());
//	   po.add(new Link(host+"/rest/pos/"+po.getIdPo(), "self"));
//		
//	   por = restTemplate.postForEntity(host+"/rest/pos/"+po.getIdPo(), po, POResource.class);
//	  
//	   if (por.getStatusCode().equals(HttpStatus.CONFLICT))
//	          throw new PlantNotAvailableException();
//	  
//	   return Arrays.asList(por.getBody());
//	 }
	
	public POResource sendRemittance(Long id, PlantResource plant, Date startDate,
			Date endDate, Float cost, Status status, Long poid) throws RestClientException,
			PlantNotAvailableException, JsonProcessingException {

		POResource po = new POResource();
		po.setPhrId(id);
		po.setPlant(plant);
		po.setStartDate(startDate);
		po.setEndDate(endDate);
		po.setCost(cost);
		po.setIdPo(poid);
//		System.out.println("in rental");
//		System.out.println(poid);
//		System.out.println("PHR");
//		System.out.println(po.getPhrId());
//		po.setStatus(status);

		ResponseEntity<POResource> PO = restTemplate.postForEntity(
				host+"/rest/pos/remit", po, POResource.class);
		return PO.getBody();
	}

	public POResource sendRejectMsg(Long id, PlantResource plant, Date startDate,
			Date endDate, Float cost, Status status, Long poid) throws RestClientException,
			PlantNotAvailableException, JsonProcessingException {

		POResource po = new POResource();
		po.setPhrId(id);
		po.setPlant(plant);
		po.setStartDate(startDate);
		po.setEndDate(endDate);
		po.setCost(cost);
		po.setIdPo(poid);
//		System.out.println("in rental");
//		System.out.println(poid);
//		System.out.println("PHR");
//		System.out.println(po.getPhrId());
//		po.setStatus(status);

		ResponseEntity<POResource> PO = restTemplate.postForEntity(
				host+"/rest/pos/inreject", po, POResource.class);
		return PO.getBody();
	}
	public PlantHireRequestResource createPHR(PlantResource plant,
			Date startDate, Date endDate, Float cost)
			throws RestClientException, PlantNotAvailableException,
			JsonProcessingException {

		PlantHireRequestResource phr = new PlantHireRequestResource();
		phr.setPlant(plant);
		phr.setStartDate(startDate);
		phr.setEndDate(endDate);
		phr.setPrice(cost);

		PlantHireRequestResource PHR = restTemplate.postForObject(
				host+"/rest/phr", phr,
				PlantHireRequestResource.class);
		return PHR;
	}

	public void rejectPO(Date startDate, Date endDate, Long id)
		throws RestClientException, PlantNotAvailableException,
		JsonProcessingException {

		POResource phr = new POResource();
		phr.setStartDate(startDate);
		phr.setEndDate(endDate);
		phr.setIdPo(id);
		restTemplate.delete(host+"/rest/pos/"+phr.getIdPo()+"/accept");	
	}
		
		
	public PlantHireRequestResource approvePHR(Date startDate, Date endDate, Long id, Float cost) 
			throws RestClientException,PlantNotAvailableException, JsonProcessingException {

	   PlantHireRequestResource phr = new PlantHireRequestResource();
	   phr.setIdRes(id);
	   phr.setStartDate(startDate);
	   phr.setEndDate(endDate);
	   phr.setPrice(cost);

	   ResponseEntity<PlantHireRequestResource> PHR = restTemplate.postForEntity(
			   host+"/rest/phr/"+phr.getIdRes()+"/accept", new HttpEntity<String>(new HttpHeaders()).getBody(),
				PlantHireRequestResource.class);
	   return PHR.getBody();
 }

 }
