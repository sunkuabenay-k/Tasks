package com.app.eventService.service;

import com.app.eventService.entity.Event;
import com.app.eventService.kafka.EventProducer;
import com.app.eventService.repository.EventRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventProducer eventProducer;

    public EventService(EventRepository eventRepository, EventProducer eventProducer) {
        this.eventRepository = eventRepository;
        this.eventProducer = eventProducer;
    }

    public Event createEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        String notificationMessage = "New Event Created: " + savedEvent.getTitle();
        eventProducer.sendEventNotification(notificationMessage);
        return savedEvent;
    }

    // New method: Get all events
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // New method: Get event by id
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // New method: Update event by id
    public Event updateEvent(Long id, Event updatedEvent) {
        return eventRepository.findById(id).map(event -> {
            event.setTitle(updatedEvent.getTitle());
            event.setDescription(updatedEvent.getDescription());
            Event saved = eventRepository.save(event);
            // Notify update
            eventProducer.sendEventNotification("Event Updated: " + saved.getTitle());
            return saved;
        }).orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }

    // New method: Delete event by id
    public void deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            eventProducer.sendEventNotification("Event Deleted with ID: " + id);
        } else {
            throw new RuntimeException("Event not found with id " + id);
        }
    }
}
