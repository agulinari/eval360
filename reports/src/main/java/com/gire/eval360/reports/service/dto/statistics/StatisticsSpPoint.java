package com.gire.eval360.reports.service.dto.statistics;

import java.math.BigDecimal;

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
public class StatisticsSpPoint {

	private StatisticsSpEvaluee statisticSpEvaluee;
	private BigDecimal pointManagers;
	private BigDecimal pointPeers;
	private BigDecimal pointDirectReports;
	private BigDecimal point;

}
