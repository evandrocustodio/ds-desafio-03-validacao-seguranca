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

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;

@Service
public class CityService {
	@Autowired
	private CityRepository cityRepository;

	@Transactional(readOnly = true)
	public List<CityDTO> findAll() {
		return cityRepository.findAll(Sort.by("name")).stream().map(c -> new CityDTO(c)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public Page<CityDTO> findAllPaged(Pageable pageable) {
		Page<City> cities = cityRepository.findAll(pageable);
		return cities.map(c -> new CityDTO(c));	
	}

	@Transactional(readOnly = true)
	public CityDTO findById(Long id) {
		Optional<City> object = cityRepository.findById(id);
		City entity = object.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
		return new CityDTO(entity);
	}

	@Transactional
	public CityDTO insert(CityDTO dto) {
		City entity = new City();
		entity.setName(dto.getName());
		entity = cityRepository.save(entity);
		return new CityDTO(entity);
	}

	@Transactional
	public CityDTO update(Long id, CityDTO dto) {
		City entity;
		try {
			entity = cityRepository.getOne(id);
			entity.setName(dto.getName());
			entity = cityRepository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);
		}
		return new CityDTO(entity);
	}

	public void delete(Long id) {
		try {
			cityRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found: " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}
	}
}
