package com.gire.eval360.notifications.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
      
	@Autowired
    private JavaMailSender emailSender;
     
    public void sendSimpleMessage(String to, String subject, String html) throws MessagingException, UnsupportedEncodingException {
           	    	
    	MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,StandardCharsets.UTF_8.name());
        helper.setText(html,true);
        helper.addInline("logo",new ClassPathResource("static/imgs/personas.png"));
        helper.addInline("css", new ClassPathResource("static/css/style.css"));
        message.setFrom(new InternetAddress("no-reply@eval360.com", "NoReply"));
        message.setReplyTo(InternetAddress.parse("no-reply@eval360.com", false));
                
        helper.setTo(to); 
        helper.setSubject(subject); 
        emailSender.send(message);
    }
    
}
