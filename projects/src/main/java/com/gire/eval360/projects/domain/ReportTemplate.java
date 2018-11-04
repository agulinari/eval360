package com.gire.eval360.projects.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ReportTemplate extends AuditedEntity{

	@Column
	@NotBlank
	private String title;
	
	@OneToOne(mappedBy = "reportTemplate", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Employee creator;
}
