package com.gire.eval360.notifications.service;

import java.io.IOException;
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

import com.sendgrid.Attachments;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import aj.org.objectweb.asm.Type;
import net.bytebuddy.description.type.TypeDefinition;

@Component
public class EmailServiceImpl implements EmailService {
      
	//@Autowired
    //private JavaMailSender emailSender;
     
//    public void sendSimpleMessage(String to, String subject, String html) throws MessagingException, UnsupportedEncodingException {
//       
//    	MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,StandardCharsets.UTF_8.name());
//        helper.setText(html,true);
//        helper.addInline("logo",new ClassPathResource("static/imgs/personas.png"));
//        helper.addInline("css", new ClassPathResource("static/css/style.css"));
//        message.setFrom(new InternetAddress("no-reply@eval360.com", "NoReply"));
//        message.setReplyTo(InternetAddress.parse("no-reply@eval360.com", false));
//                
//        helper.setTo(to); 
//        helper.setSubject(subject); 
//        emailSender.send(message);
//    }
    
	public void sendSimpleMessage(String to, String subject, String html) throws IOException {
		String from = "no-reply@eval360.com";
		Email efrom = new Email(from);
		Email eto = new Email(to);
		Content content = new Content("text/html", html);
		Mail mail = new Mail(efrom, subject, eto, content);
				
//		Attachments logo = new Attachments();
//		logo.setContentId("logo");
//		logo.setDisposition("inline");
//		logo.setFilename("static/imgs/personas.png");
//		
//		Attachments css = new Attachments();
//		css.setContentId("css");
//		css.setDisposition("inline");
//		css.setFilename("static/css/style.css");
//		
//		mail.addAttachments(logo);
//		mail.addAttachments(css);
	
		SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}
}
