package com.hexagon.studentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hexagon.studentservice.dto.School;

@FeignClient("SCHOOL-SERVICE")
public interface SchoolClient {
	
	 @GetMapping("/school/{id}")
	 
	 School method1(@PathVariable long id);
	 
	 
}
