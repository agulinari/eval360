package com.gire.eval360.reports.domain;

import java.math.BigDecimal;
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
public class Section {

	private String name;
	private String description;
	private BigDecimal currentPerformanceByMe;
	private BigDecimal desiredPerformanceByMe;
	private BigDecimal currentPerformanceByColleagues;
	private BigDecimal desiredPerformanceByColleagues;
	private List<Item> items;
	private List<Comment> comments;
	
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
}
