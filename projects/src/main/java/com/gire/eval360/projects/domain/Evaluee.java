package com.gire.eval360.projects.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Evaluee extends ProjectMember{

	
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER,orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "evaluee_id")
	private List<EvalueeFeedbackProvider> feedbackProviders = new ArrayList<>();

}
