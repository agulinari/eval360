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
	
	public BigDecimal getDesiredImprovementByMe() {
		BigDecimal dif = this.getDesiredPerformanceByMe().subtract(this.getCurrentPerformanceByMe());
		
		if (dif.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		} else {
			return dif.multiply(BigDecimal.valueOf(20));
		}
	}

	public BigDecimal getDesiredImprovementByColleagues() {
		BigDecimal dif = this.getDesiredPerformanceByColleagues().subtract(this.getCurrentPerformanceByColleagues());
		
		if (dif.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		} else {
			return dif.multiply(BigDecimal.valueOf(20));
		}
	}
	
	public BigDecimal getDesiredImprovementByManagers() {
		BigDecimal dif = this.getDesiredPerformanceByManagers().subtract(this.getCurrentPerformanceByManagers());
		
		if (dif.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		} else {
			return dif.multiply(BigDecimal.valueOf(20));
		}
	}

	public BigDecimal getDesiredImprovementByPeers() {
		BigDecimal dif = this.getDesiredPerformanceByPeers().subtract(this.getCurrentPerformanceByPeers());
		
		if (dif.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		} else {
			return dif.multiply(BigDecimal.valueOf(20));
		}
	}
	
	public BigDecimal getDesiredImprovementByDirectReports() {
		BigDecimal dif = this.getDesiredPerformanceByDirectReports().subtract(this.getCurrentPerformanceByDirectReports());
		
		if (dif.compareTo(BigDecimal.ZERO) <= 0) {
			return BigDecimal.ZERO;
		} else {
			return dif.multiply(BigDecimal.valueOf(20));
		}
	}
	
}
