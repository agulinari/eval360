package com.gire.eval360.notifications.service;

import java.io.IOException;

public interface EmailService {
	
	public void sendSimpleMessage(String to, String subject, String html) throws IOException ;

}
