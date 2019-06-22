package com.gire.eval360.reports.service.dto.statistics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder=true)
@ToString(includeFieldNames=true)
public class StatisticsSp {
	
	private String nameProject;
    private @Singular List<StatisticsSpSection> statisticsSpSections;
	private @Singular List<StatisticsSpEvaluee> statisticsSpEvaluees;
	
}
