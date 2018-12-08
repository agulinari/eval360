package com.gire.eval360.evaluations.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"id"})
@Builder
@Document(collection = "evaluations")
public class Evaluation {
	
	@Id
	private String id;
	private Long idProject;
	private Long idFeedbackProvider;
	private Long idEvaluee;
	@CreatedDate
	private LocalDateTime date;
	private @Singular List<Section> sections;
	
}
