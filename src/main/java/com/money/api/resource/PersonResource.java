package com.money.api.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.money.api.event.ResourceCreatedEvent;
import com.money.api.model.Person;
import com.money.api.repository.PersonRepository;
import com.money.api.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonResource {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired 
	private PersonService personService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public Page<Person> search(@RequestParam(required = false, defaultValue = "%") String nome, Pageable pageable) {
		return personRepository.findByNomeContaining(nome, pageable);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Person> findById(@PathVariable Long codigo) {
		Person person = personRepository.findOne(codigo);
		return person != null ? ResponseEntity.ok().body(person) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	private ResponseEntity<Person> create(@Valid @RequestBody Person person, HttpServletResponse response) {
		
		Person savedPerson = personRepository.save(person);		
		publisher.publishEvent(new ResourceCreatedEvent(this, response, savedPerson.getCodigo()));		
		return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);		
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	private void delete(@PathVariable Long codigo) {
		
		personRepository.delete(codigo);
	}
	
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Person> update(@PathVariable Long codigo, @Valid @RequestBody Person person) {
		
		Person savedPerson = personService.update(codigo, person);		
		return ResponseEntity.ok(savedPerson);
	}
	
	@PutMapping("/{codigo}/active")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateActive(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		personService.updateActive(codigo, ativo);
	}
}
