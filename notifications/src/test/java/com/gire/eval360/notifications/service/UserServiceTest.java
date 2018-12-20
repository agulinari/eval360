package com.gire.eval360.notifications.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gire.eval360.notifications.service.remote.UserServiceRemote;
import com.gire.eval360.notifications.service.remote.domain.UserResponse;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserServiceRemote userServiceRemote;
	
	@Test
	public void getUserByIdTest() {
		Mono<UserResponse> response = userServiceRemote.getUserById(Long.valueOf(1));
		Mono<String> mailResponse = response.flatMap(user-> {String mail = user.getMail();
		return Mono.just(mail);});
		
		assertTrue(mailResponse.block().contains("@"));
	}

}
