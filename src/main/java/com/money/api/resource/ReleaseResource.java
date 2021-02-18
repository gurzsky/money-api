package com.money.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.money.api.event.ResourceCreatedEvent;
import com.money.api.model.Release;
import com.money.api.repository.ReleaseRepository;

@RestController
@RequestMapping("/release")
public class ReleaseResource {
	
	@Autowired
	private ReleaseRepository releaseRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Release> find() {
		return releaseRepository.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<?> findById(@PathVariable Long codigo) {
		
		Release release = releaseRepository.findOne(codigo);
		
		if (release != null)
				return ResponseEntity.ok().body(release);
		
		return ResponseEntity.noContent().build();
		
	}
	
	@PostMapping
	public ResponseEntity<Release> save(@Valid @RequestBody Release release, HttpServletResponse response) {
		
		Release savedRelease = releaseRepository.save(release);
		publisher.publishEvent(new ResourceCreatedEvent(this, response, savedRelease.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(savedRelease);
	}

}
