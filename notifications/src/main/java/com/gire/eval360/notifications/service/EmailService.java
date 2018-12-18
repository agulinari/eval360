package com.gire.eval360.notifications.service;

public interface EmailService {
	
	public void sendSimpleMessage(String to, String subject, String text);

}
