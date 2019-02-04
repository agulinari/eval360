package com.gire.eval360.notifications.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

public interface EmailService {
	
	public void sendSimpleMessage(String to, String subject, String html) throws MessagingException, UnsupportedEncodingException ;

}
