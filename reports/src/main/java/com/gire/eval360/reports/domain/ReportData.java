package com.gire.eval360.reports.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"id"})
@Builder
@Document(collection = "reports")
public class ReportData {
	
	@Id
	private String id;
	private Long idProject;
	private Long idEvaluee;
	private String username;
	private Integer managers;
	private Integer peers;
	private Integer directReports;
	private List<String> strengths;
	private List<String> weaknesses;
	private List<AreaToImprove> areasToImprove;
	private List<Comment> comments;
	private List<Section> detailedResults;
	
	
}
