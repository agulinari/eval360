package com.gire.eval360.projects.service.remote;

import com.gire.eval360.projects.service.remote.dto.templates.TemplateDto;

public interface TemplateServiceRemote {

	TemplateDto getTemplateByName(String template);
	
	TemplateDto getTemplateById(Long id);

}
