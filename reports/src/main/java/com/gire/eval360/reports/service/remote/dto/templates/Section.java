package com.gire.eval360.reports.service.remote.dto.templates;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Section {
	
	private Long id;
	private String name;
	private String description;
	private SectionType sectionType;
	private Integer position;
	private List<ItemTemplate> items;
	
}
