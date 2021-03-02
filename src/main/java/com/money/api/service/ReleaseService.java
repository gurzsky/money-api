package com.money.api.service;

import org.springframework.beans.BeanUtils;
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

		checkPerson(release);
		return releaseRepository.save(release);
	}

	public Release update(Long codigo, Release release) {

		Release savedRelease = searchRelease(codigo);
		if (!savedRelease.getPessoa().equals(release.getPessoa())) {
			checkPerson(release);
		}

		BeanUtils.copyProperties(release, savedRelease, "codigo");
		return releaseRepository.save(savedRelease);
	}

	private void checkPerson(Release release) {
		Person person = null;
		if (release.getPessoa().getCodigo() != null)
			person = personRepository.findOne(release.getPessoa().getCodigo());

		if (person == null || person.isInactive())
			throw new InactiveOrNonexistentPersonException();
	}

	public Release searchRelease(Long codigo) {

		Release releaseSaved = releaseRepository.findOne(codigo);
		if (releaseSaved == null) {
			throw new IllegalArgumentException();
		}
		return releaseSaved;
	}
}
