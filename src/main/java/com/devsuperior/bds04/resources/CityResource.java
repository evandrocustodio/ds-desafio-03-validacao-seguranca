package com.devsuperior.bds04.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.services.CityService;

@RestController
@RequestMapping(value = "/cities")
public class CityResource {
	@Autowired
	private CityService cityService;

	@GetMapping
	public ResponseEntity<List<CityDTO>> findAll() {

		List<CityDTO> cities = cityService.findAll();

		return ResponseEntity.ok().body(cities);
	
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CityDTO> findById(@PathVariable Long id) {
		CityDTO dto = cityService.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<CityDTO> insert(@Valid @RequestBody CityDTO dto) {
		CityDTO returnedDTO = cityService.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(returnedDTO.getId())
				.toUri();
		return ResponseEntity.created(uri).body(returnedDTO);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<CityDTO> update(@PathVariable Long id, @RequestBody CityDTO dto) {
		CityDTO city = cityService.update(id, dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(city);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		cityService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
