package com.gire.eval360.notifications.service;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.gire.eval360.notifications.service.remote.domain.UserResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTest {

	@Autowired
	EmailService emailService;
	@Autowired
	TemplateEngine templateMail;
	
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	@Ignore
	public void testSendEmail() {
		UserResponse user = new UserResponse("sivori.daniel","daniel_sivori@yahoo.com.ar", true);
		Context context = new Context();
        context.setVariable("data", user);
        context.setVariable("message", "Se informa que ud tiene un feedback pendiente. Ingrese al siguiente link para completarlo.");
        context.setVariable("linkRef", "www.google.com.ar");
        
        String renderedHtmlContent = templateMail.process("NotificationMail", context);
		try {
			emailService.sendSimpleMessage("sivori.daniel@gmail.com", "Eval 360",renderedHtmlContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		
	}

}
