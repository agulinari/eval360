package com.gire.eval360.notifications.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import com.gire.eval360.notifications.config.ServicesProperties;
import com.gire.eval360.notifications.dto.NotificationProvidersRequest;
import com.gire.eval360.notifications.dto.UserProvider;
import com.gire.eval360.notifications.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationsController {
	
	private final ITemplateEngine  templateMail;
	private final EmailService emailService;
	private final ServicesProperties servicesProperties;
	
	@Autowired
	public NotificationsController(final ITemplateEngine  templateMail,final EmailService emailService, final ServicesProperties servicesProperties) {
		this.templateMail = templateMail;
		this.emailService = emailService;
		this.servicesProperties = servicesProperties;
	}

	@PostMapping("/providers")
	public NotificationProvidersResponse notificateToProviders(@RequestBody NotificationProvidersRequest request) throws Exception {
				
		List<UserProviderNotification> usersProvidersNotifyStatus=request.getProviders().stream().map(p -> {
	    	String to = p.getMail();
			String subject = "Recordatorio de Feedback pendiente";
    		String url = servicesProperties.getNotificationsUrl();
    		String link = url+"/main/project-tasks/"+request.getIdProject()+"/evaluation/"+request.getIdEvalueeFP();
    		String message = "Se recuerda que ud tiene un feedback pendiente. Por favor ingrese al siguiente link para completarlo.";
    		Context context = new Context();
            context.setVariable("data", p);
            context.setVariable("message", message);
            context.setVariable("linkRef", link);
            String renderedHtmlContent = templateMail.process("NotificationMail", context);
            
    		try {
    			emailService.sendSimpleMessage(to,subject,renderedHtmlContent);
    		}catch (IOException e) {
    			log.error(e.getMessage());
    			return new UserProviderNotification(new UserProvider(p.getUsername(),p.getMail()),"NOK"+ " - "+e.getMessage(),1);
			} catch(Exception e) {
				System.out.println("Error: "+e);
			}
    		return new UserProviderNotification(new UserProvider(p.getUsername(),p.getMail()),"OK - Provider Notificado",0);
	    }).collect(Collectors.toList());
		
		return new NotificationProvidersResponse(usersProvidersNotifyStatus);
	}

}
