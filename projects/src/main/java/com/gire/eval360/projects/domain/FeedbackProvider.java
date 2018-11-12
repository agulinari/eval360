package com.gire.eval360.projects.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class FeedbackProvider extends ProjectMember{

	private static final long serialVersionUID = 1L;
	
	private Long idEvaluation;
	
	private Long idUser;

	@ManyToMany(mappedBy = "feedbackProviders", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
    private List<Project> projects;
	
	@OneToMany(mappedBy = "evaluee",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<EvalueeFeedbackProvider> evaluees = new ArrayList<>();
		
}
