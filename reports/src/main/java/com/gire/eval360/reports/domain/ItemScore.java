package com.gire.eval360.reports.domain;

import java.math.BigDecimal;

import com.gire.eval360.reports.domain.Item.ItemBuilder;

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
public class ItemScore {
	
	private BigDecimal currentPerformance;
	private BigDecimal desiredPerformance;

}
