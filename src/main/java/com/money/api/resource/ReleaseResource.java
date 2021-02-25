package com.money.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.money.api.event.ResourceCreatedEvent;
import com.money.api.exceptionHandler.MoneyExceptionHandler.Error;
import com.money.api.model.Release;
import com.money.api.repository.ReleaseRepository;
import com.money.api.repository.filter.ReleaseFilter;
import com.money.api.service.ReleaseService;
import com.money.api.service.exception.InactiveOrNonexistentPersonException;

@RestController
@RequestMapping("/release")
public class ReleaseResource {
	
	@Autowired
	private ReleaseRepository releaseRepository;
	
	@Autowired
	private ReleaseService releaseService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")	
	public Page<Release> search(ReleaseFilter releaseFilter, Pageable pageable) {
		return releaseRepository.filter(releaseFilter, pageable);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<?> findById(@PathVariable Long codigo) {
		
		Release release = releaseRepository.findOne(codigo);
		
		if (release != null)
				return ResponseEntity.ok().body(release);
		
		return ResponseEntity.noContent().build();
		
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Release> save(@Valid @RequestBody Release release, HttpServletResponse response) {
		
		Release savedRelease = releaseService.saveRelease(release);
		publisher.publishEvent(new ResourceCreatedEvent(this, response, savedRelease.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(savedRelease);
	}
	
	@ExceptionHandler({InactiveOrNonexistentPersonException.class})
	public ResponseEntity<Object> handleInactiveOrNonexistentPersonException(InactiveOrNonexistentPersonException ex) {
		
		String userMessage = messageSource.getMessage("inactive-or-noneexistent.person", null, LocaleContextHolder.getLocale());
		String developerMessage = ex.toString();
		
		List<Error> errors = Arrays.asList(new Error(userMessage, developerMessage));
		
		return ResponseEntity.badRequest().body(errors);
		
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long codigo) {
		
		releaseRepository.delete(codigo);
	}
	
}
