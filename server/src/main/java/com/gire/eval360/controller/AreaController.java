package com.gire.eval360.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gire.eval360.domain.Area;
import com.gire.eval360.repository.AreaRepository;

@RestController
@RequestMapping("/areas")
public class AreaController {

	private final AreaRepository areaRepository;
	
	@Autowired
	public AreaController(AreaRepository areaRepository) {
		this.areaRepository = areaRepository;
	}
	
	@GetMapping
	public ResponseEntity<Collection<Area>> getAll(){
		return ResponseEntity.ok(areaRepository.findAll());
	}
	
}
