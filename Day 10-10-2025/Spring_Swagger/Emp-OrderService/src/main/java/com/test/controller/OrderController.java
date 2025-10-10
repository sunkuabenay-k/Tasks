package com.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.Order;

@RestController
@RequestMapping("/order")
public class OrderController {
	@GetMapping("/{id}")
	public Order getOrderById(@PathVariable("id") int id) {
		return new Order(id,"Laptop",20000.00);
	}
}
