package com.gire.eval360.projects.domain.history;

import java.time.LocalDate;
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
@Builder
@EqualsAndHashCode(callSuper = false)
public class EvaluationInstance {

	private Long idProject;
	private String projectName;
	private @Singular List<Long> feedbackProvidersIds;
	private LocalDate startDate;
	private LocalDate endDate;

}
