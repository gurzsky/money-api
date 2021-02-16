package com.money.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.money.api.event.ResourceCreatedEvent;

@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent>{

	@Override
	public void onApplicationEvent(ResourceCreatedEvent event) {
		
		HttpServletResponse response = event.getResponse();
		Long codigo = event.getCodigo();
		
		addHeaderLocation(response, codigo);
	}

	private void addHeaderLocation(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{codigo}")
				.buildAndExpand(codigo)
				.toUri();
			
		response.setHeader("Location", uri.toASCIIString());
	}
}
