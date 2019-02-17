package com.gire.eval360.evaluations.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of={"id"})
public class Item {
	
	private Long id;
	private String type;
	private String value;
	private String value1;

}
