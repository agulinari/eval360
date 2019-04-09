package com.gire.eval360.projects.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gire.eval360.projects.service.remote.dto.templates.TemplateDto;


@Service
public class TemplateServiceRemoteImpl implements TemplateServiceRemote {

	@Autowired
	@Qualifier("templatesClient")
	private RestTemplate restTemplate;
	
	@Override
	public TemplateDto getTemplateByName(String template) {
		
		ResponseEntity<TemplateDto> call = this.restTemplate.getForEntity("/search/title?title="+template, TemplateDto.class);
		return call.getBody();
		
	}

	@Override
	public TemplateDto getTemplateById(Long idTemplate) {
		
		ResponseEntity<TemplateDto> call = this.restTemplate.getForEntity("/"+idTemplate, TemplateDto.class);		
		return call.getBody();
	}
}
