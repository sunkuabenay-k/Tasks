package com.app.eventService.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eventRegistration")
public class EventRegistration {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long registration_id;
	String name;
	String email_id;
	@Column(name = "event_id")
	Long event_id;
	
	
}
