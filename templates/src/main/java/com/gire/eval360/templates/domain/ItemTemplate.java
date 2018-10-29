package com.gire.eval360.templates.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ItemTemplate extends AuditedEntity{
	
	@Column
	private String title;
	
	@Column
	private String description;
	
	@Column
	private ItemType type;
	
	@Column
	@NonNull
	private Integer position;
	
	@ManyToOne
	@JoinColumn(name = "section_id")
	@JsonIgnore
	private Section section;
	
}
