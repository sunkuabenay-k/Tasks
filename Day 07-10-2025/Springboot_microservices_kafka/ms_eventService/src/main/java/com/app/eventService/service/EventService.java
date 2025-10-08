package com.app.eventService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.eventService.entity.Event;
import com.app.eventService.repository.EventRepository;

public class EventService {
	@Autowired
	private EventRepository repository;
	
	public Event saveEvent(Event event) {
		return repository.save(event);
	}
	
	public List<Event> fetchEvents(){
		return repository.findAll();
	}
	
	public Event fetchEventbyId(Long id) {
		return repository.getReferenceById(id);
	}
	
}
