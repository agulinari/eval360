package com.gire.eval360.reports.service.dto.statistics;

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
public class StatisticsSpSection {

	private String name;
	private List<StatisticsSpItem> statisticSpItems;

}
