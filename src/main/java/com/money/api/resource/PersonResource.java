package com.money.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.money.api.model.Person;
import com.money.api.repository.PersonRepository;

@RestController
@RequestMapping("/person")
public class PersonResource {
	
	@Autowired
	private PersonRepository personRepository;
	
	@GetMapping
	private List<Person> list() {
		
		return personRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	private ResponseEntity<?> findById(@PathVariable Long codigo) {
		
		Person person = personRepository.findOne(codigo);		
		if (person != null) 
			return ResponseEntity.ok().body(person);
				
		return ResponseEntity.noContent().build();
		
	}
	
	@PostMapping
	private ResponseEntity<Person> create(@Valid @RequestBody Person person, HttpServletResponse response) {
		
		Person savedPerson = personRepository.save(person);
		
		URI uri = ServletUriComponentsBuilder
			.fromCurrentRequestUri()
			.path("/{codigo}")
			.buildAndExpand(savedPerson.getCodigo())
			.toUri();
		
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(savedPerson);		
	}
}
