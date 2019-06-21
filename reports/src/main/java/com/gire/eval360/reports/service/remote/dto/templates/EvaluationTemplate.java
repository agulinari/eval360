package com.gire.eval360.reports.service.remote.dto.templates;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class EvaluationTemplate {

	private Long id;
	private String title;
	private  String idUser;
	private List<Section> sections;
	
}
