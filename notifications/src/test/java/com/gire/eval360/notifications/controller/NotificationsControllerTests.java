package com.gire.eval360.notifications.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.thymeleaf.ITemplateEngine;

import com.gire.eval360.notifications.config.ServicesProperties;
import com.gire.eval360.notifications.dto.NotificationProvidersRequest;
import com.gire.eval360.notifications.dto.UserProvider;
import com.gire.eval360.notifications.service.EmailService;

@RunWith(SpringRunner.class)
public class NotificationsControllerTests {

    private WebTestClient client;

    @MockBean
    private ITemplateEngine templateMail;
    
    @MockBean
    private EmailService emailService;
    
    @MockBean
    private ServicesProperties servicesProperties;
    
    @Before
    public void setUp() {
        client = WebTestClient.bindToController(
                new NotificationsController(templateMail, emailService, servicesProperties))
                .build();
    }
    
    @Test
    public void testNotificateToProviders() throws UnsupportedEncodingException, MessagingException {
  
    	UserProvider provider = UserProvider.builder().username("alinari").mail("agustinlinari@gmail.com").build();
    	NotificationProvidersRequest request = NotificationProvidersRequest.builder().idEvalueeFP(Long.valueOf(1)).idProject(Long.valueOf(1)).provider(provider).build();
    	
    	String renderedHtmlContext = "<html>Main enviado</html>";
    	BDDMockito.when(templateMail.process(BDDMockito.eq("NotificationMail"), BDDMockito.any())).thenReturn(renderedHtmlContext);
    	
    	UserProviderNotification result = UserProviderNotification.builder().status(0).resultSend("OK - Provider Notificado").user(provider).build();
    	NotificationProvidersResponse response = NotificationProvidersResponse.builder().notificationResult(result).build();
    	
        client.post().uri("/notifications/providers")
                 .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(request))
                 .exchange().expectStatus().isOk().expectBody(NotificationProvidersResponse.class).isEqualTo(response);
		
		BDDMockito.verify(emailService, BDDMockito.times(1)).sendSimpleMessage("agustinlinari@gmail.com", "Recordatorio de Feedback pendiente", renderedHtmlContext);

    }
    
    @Test
    public void testNotificationToProvidersMessagingException() throws UnsupportedEncodingException, MessagingException {
    	UserProvider provider = UserProvider.builder().username("alinari").mail("agustinlinari@gmail.com").build();
    	NotificationProvidersRequest request = NotificationProvidersRequest.builder().idEvalueeFP(Long.valueOf(1)).idProject(Long.valueOf(1)).provider(provider).build();
    	
    	String renderedHtmlContext = "<html>Main enviado</html>";
    	BDDMockito.when(templateMail.process(BDDMockito.eq("NotificationMail"), BDDMockito.any())).thenReturn(renderedHtmlContext);
    	
    	UserProviderNotification result = UserProviderNotification.builder().status(1).resultSend("NOK - null").user(provider).build();
    	NotificationProvidersResponse response = NotificationProvidersResponse.builder().notificationResult(result).build();
    	
    	BDDMockito.doThrow(MessagingException.class).when(emailService).sendSimpleMessage("agustinlinari@gmail.com", "Recordatorio de Feedback pendiente", renderedHtmlContext);
    	
        client.post().uri("/notifications/providers")
                 .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(request))
                 .exchange().expectStatus().isOk().expectBody(NotificationProvidersResponse.class).isEqualTo(response);
		    	
    }
}
