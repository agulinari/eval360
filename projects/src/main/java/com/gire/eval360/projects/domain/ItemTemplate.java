package com.gire.eval360.projects.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class ItemTemplate extends AuditedEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String description;
	
	@Column
	@NonNull
	private Integer position;
	
	@ManyToOne
	@JoinColumn(name = "BOSS_ID")
	@JsonIgnore
	private Section section;
	
	@Column
	@NonNull
	private Integer weight;

}
