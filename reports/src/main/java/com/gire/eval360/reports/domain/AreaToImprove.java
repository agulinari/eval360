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
public class AreaToImprove {
	
	private String areaAssessed;
	private BigDecimal desiredImprovement;
	private Score ownView;
	private Score managersView;
	private Score peersView;
	private Score directReportsView;

}
