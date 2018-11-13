package com.gire.eval360.templates.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	@NonNull
	private String title;
	
	@Column
	@NonNull
	private String description;
	
	@Column
	@NonNull
	@Enumerated(EnumType.STRING)
	private ItemType itemType;
	
	@Column
	@NonNull
	private Integer position;
	
	@ManyToOne
	@JoinColumn(name = "section_id")
	@JsonIgnore
	private Section section;
	
}
