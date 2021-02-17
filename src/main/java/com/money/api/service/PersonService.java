package com.money.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.money.api.model.Person;
import com.money.api.repository.PersonRepository;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	public Person update(Long codigo, Person person) {
		
		Person savedPerson = findById(codigo);
			
		BeanUtils.copyProperties(person, savedPerson, "codigo");
		return personRepository.save(savedPerson);
	}	

	public void updateActive(Long codigo, Boolean ativo) {
		
		Person savedPerson = findById(codigo);
		
		savedPerson.setAtivo(ativo);
		personRepository.save(savedPerson);		
	}
	
	public Person findById(Long codigo) {
		Person savedPerson = personRepository.findOne(codigo);
		if (savedPerson == null)
			throw new EmptyResultDataAccessException(1);
		return savedPerson;
	}
}
