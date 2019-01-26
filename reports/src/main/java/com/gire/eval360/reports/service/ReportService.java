package com.gire.eval360.reports.service;

import com.gire.eval360.reports.domain.ReportData;

import reactor.core.publisher.Mono;

public interface ReportService {
	
	Mono<ReportData> generateReport(Long idProject, Long idEvaluee, Long idTemplate);

}
