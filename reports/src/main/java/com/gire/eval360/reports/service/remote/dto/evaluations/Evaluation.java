package com.gire.eval360.reports.service.remote.dto.evaluations;

import java.time.LocalDateTime;
import java.util.List;

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
public class Evaluation {
	
	private String id;
	private Long idProject;
	private Long idFeedbackProvider;
	private Long idEvaluee;
	private String username;
	private String relationship;
	private LocalDateTime date;
	private @Singular List<Section> sections;
	
}
