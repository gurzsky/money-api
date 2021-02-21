package com.money.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.api.model.Person;
import com.money.api.model.Release;
import com.money.api.repository.PersonRepository;
import com.money.api.repository.ReleaseRepository;
import com.money.api.service.exception.InactiveOrNonexistentPersonException;

@Service
public class ReleaseService {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private ReleaseRepository releaseRepository; 

	public Release saveRelease(Release release) {
		
		Person person = personRepository.findOne(release.getPessoa().getCodigo());
		
		if (person == null || person.isInactive()) {
			throw new InactiveOrNonexistentPersonException();
		}
		
		return releaseRepository.save(release);
	}
}
