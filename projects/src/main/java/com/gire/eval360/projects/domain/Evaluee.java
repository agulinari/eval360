package com.gire.eval360.projects.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalIdCache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@NaturalIdCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@EqualsAndHashCode(callSuper=false)
public class Evaluee extends ProjectMember{

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "feedbackProvider",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<EvalueeFeedbackProvider> feedbackProviders = new ArrayList<>();

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID")    
	private Project project;
}
