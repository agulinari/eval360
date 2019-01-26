package com.gire.eval360.reports.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class ReportData {

	String username;
	Integer managers;
	Integer peers;
	Integer directReports;
	List<String> strengths;
	List<String> weaknesses;
	List<AreaToImprove> areasToImprove;
	List<Comment> comments;
	List<Section> detailedResults;
	
	
}
