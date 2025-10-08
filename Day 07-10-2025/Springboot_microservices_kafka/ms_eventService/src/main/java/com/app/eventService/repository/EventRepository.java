package com.app.eventService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.eventService.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
	
	

}
