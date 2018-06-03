package com.gire.eval360.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.domain.Position;
import com.gire.eval360.repository.PositionRepository;

@RestController
@RequestMapping("/positions")
public class PositionController {

	private final PositionRepository positionRepository;
	
	@Autowired
	public PositionController(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}
	
	@GetMapping
	@CrossOrigin(origins = "http://localhost:4200")
	public ResponseEntity<Collection<Position>> getAll(){
		return ResponseEntity.ok(positionRepository.findAll());
	}
	
}