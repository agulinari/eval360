package com.gire.eval360.reports.domain;

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
public class Item {
	
	private String name;
	private String description;
	private BigDecimal currentPerformanceByMe;
	private BigDecimal desiredPerformanceByMe;
	private BigDecimal currentPerformanceByColleagues;
	private BigDecimal desiredPerformanceByColleagues;
	private BigDecimal currentPerformanceByManagers;
	private BigDecimal desiredPerformanceByManagers;
	private BigDecimal currentPerformanceByPeers;
	private BigDecimal desiredPerformanceByPeers;
	private BigDecimal currentPerformanceByDirectReports;
	private BigDecimal desiredPerformanceByDirectReports;

}
