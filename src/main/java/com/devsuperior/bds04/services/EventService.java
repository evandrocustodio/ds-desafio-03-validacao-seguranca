package com.devsuperior.bds04.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;
import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;

@Service
public class EventService {
	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private CityRepository cityRepository;	

	@Transactional(readOnly = true)
	public List<EventDTO> findAll() {
		return eventRepository.findAll(Sort.by("name")).stream().map(e -> new EventDTO(e)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<EventDTO> findAllPaged(Pageable pageable) {
		Page<Event> cities = eventRepository.findAll(pageable);
		return cities.map(e -> new EventDTO(e));
	}

	@Transactional(readOnly = true)
	public EventDTO findById(Long id) {
		Optional<Event> object = eventRepository.findById(id);
		Event entity = object.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
		return new EventDTO(entity);
	}

	@Transactional
	public EventDTO insert(EventDTO dto) {
		Event entity = new Event();
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		Optional<City> e = cityRepository.findById(dto.getCityId());
		entity.setCity(e.get());
		entity = eventRepository.save(entity);
		return new EventDTO(entity);
	}

	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		Event entity;
		try {
			entity = eventRepository.getOne(id);
			entity.setName(dto.getName());
			entity.setDate(dto.getDate());
			entity.setUrl(dto.getUrl());
			Optional<City> c = cityRepository.findById(dto.getCityId());
			entity.setCity(c.get());
			entity = eventRepository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);
		}
		return new EventDTO(entity);
	}

	public void delete(Long id) {
		try {
			eventRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}
	}
}
