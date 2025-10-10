package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.EmployeetestController")
public class EmpEmpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpEmpServiceApplication.class, args);
	}
	
	@Bean
	public RestTemplate resttemplate() {
		return new RestTemplate();
	}

}
