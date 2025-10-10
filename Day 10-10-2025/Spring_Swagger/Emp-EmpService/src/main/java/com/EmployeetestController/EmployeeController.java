package com.EmployeetestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	@Autowired
	private RestTemplate restTemplate;
	@GetMapping("/getorder/{id}")
	public Map<String,Object> getOrderdetails(@PathVariable int id){
		String url = "http://localhost:8081/order/"+id;
		Map<String,Object> response = restTemplate.getForObject(url, Map.class);
		response.put("message", "Order fetched Successfully");
		return response;
	}
	@GetMapping("/Info")
	public String getOrderInfo() {
		return "Order Completed";
	}
}
