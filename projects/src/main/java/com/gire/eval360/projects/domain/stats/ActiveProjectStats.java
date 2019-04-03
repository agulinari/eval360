package com.gire.eval360.projects.domain.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class ActiveProjectStats {
	
	private String projectName;
	private Integer evaluees;
	private Integer evaluationsByManagers;
	private Integer evaluationsByPeers;
	private Integer evaluationsBySubordiantes;
	
}
