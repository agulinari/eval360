package com.gire.eval360.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.users.repository.UserRepository;


@RestController
@RequestMapping("/users")
public class UserController {

	private  UserRepository userRepository;
	
	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	
//	@GetMapping("/findByName")
//	@CrossOrigin(origins = "http://localhost:4200")
//	public ResponseEntity<Collection<User>> findUserByName(@RequestParam String name){
//		return ResponseEntity.ok(userRepository.findUserByUsernameContainingIgnoreCase(name));
//	}
}
