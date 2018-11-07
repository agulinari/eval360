package com.gire.eval360.projects.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
public class Item extends AuditedEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "ITEM_TEMPLATE_ID")
	private ItemTemplate itemTemplate;
	
	@Column
	private Integer score;
	
	@ManyToOne
	@JoinColumn(name = "EVALUATION_ID")
	@JsonIgnore
	private Evaluation evaluation;

}
