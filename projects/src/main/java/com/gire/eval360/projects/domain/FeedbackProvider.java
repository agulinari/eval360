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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString(exclude = {"evaluees"}) 
@Data
@EqualsAndHashCode(of= {}, callSuper = true)
public class FeedbackProvider extends ProjectMember{
	
	
	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="feedback_provider_id")
	@JsonIgnore
	private List<EvalueeFeedbackProvider> evaluees = new ArrayList<>();
	
}
