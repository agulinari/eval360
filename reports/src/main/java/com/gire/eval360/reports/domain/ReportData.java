package com.gire.eval360.reports.domain;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

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
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;
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
