package com.gire.eval360.notifications.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTest {

	@Autowired
	EmailService emailService;
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testSendEmail() {
		
		emailService.sendSimpleMessage("sivori.daniel@gmail.com", "prueba", "prueba");
		
	}

}
