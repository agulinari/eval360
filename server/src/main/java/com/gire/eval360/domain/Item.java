package com.gire.eval360.domain;

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
	
	@ManyToOne
	@JoinColumn(name = "item_template_id")
	private ItemTemplate itemTemplate;
	
	@Column
	private Integer score;
	
	@ManyToOne
	@JoinColumn(name = "evaluation_id")
	@JsonIgnore
	private Evaluation evaluation;

}
