package com.gire.eval360.projects.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class EvalueeReviewer {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(name="evaluee_id", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JsonIgnore
	private Evaluee evaluee;

	@JoinColumn(name="reviewer_id", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	private Reviewer reviewer;


	public EvalueeReviewer(Evaluee evaluee, Reviewer reviewer) {
		this.reviewer = reviewer;
		this.evaluee = evaluee;
	}
	
}
