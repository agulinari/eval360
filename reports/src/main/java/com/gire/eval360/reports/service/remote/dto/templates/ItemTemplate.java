package com.gire.eval360.reports.service.remote.dto.templates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemTemplate {
	
	private Long id;
	private String title;
	private String description;
	private ItemType itemType;
	private Integer position;
	
}
