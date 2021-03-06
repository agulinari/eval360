package com.gire.eval360.projects.domain.dto;

import java.util.List;

import com.gire.eval360.projects.domain.Status;

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
public class ReviewerStatus {

	private Long id;
	private Long idUser;
	private String username;
	private String avatar;
	private Status status;
	private Integer pendingReports;
	private Integer completedReports;
	private @Singular List<EvalueeDetail> evaluees;
	
}
