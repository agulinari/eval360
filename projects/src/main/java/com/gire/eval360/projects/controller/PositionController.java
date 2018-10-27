package com.gire.eval360.projects.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.projects.domain.Position;
import com.gire.eval360.projects.repository.PositionRepository;

@RestController
@RequestMapping("/positions")
public class PositionController {

	private final PositionRepository positionRepository;
	
	@Autowired
	public PositionController(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}
	
	@GetMapping
	public ResponseEntity<Collection<Position>> getAll(){
		return ResponseEntity.ok(positionRepository.findAll());
	}
	
}