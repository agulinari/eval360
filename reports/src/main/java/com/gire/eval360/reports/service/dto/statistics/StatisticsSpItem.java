package com.gire.eval360.reports.service.dto.statistics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder=true)
public class StatisticsSpItem {

	private Long id;
	private String title;
	private String description;
	private @Singular List<StatisticsSpPoint> points;
}
