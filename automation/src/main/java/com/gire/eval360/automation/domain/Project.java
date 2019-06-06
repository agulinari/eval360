package com.gire.eval360.automation.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Project {
	 private Long id;
	 private Long idTemplate;
	 private String name;
	 private String description;
	 private @Singular List<Admin> admins;
	 private @Singular List<Evaluee> evaluees;
}
