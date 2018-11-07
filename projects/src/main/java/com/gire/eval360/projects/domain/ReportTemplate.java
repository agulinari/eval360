package com.gire.eval360.projects.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	@NotBlank
	private String title;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "USER_CREATOR_REPORT_TEMPLATE_id")
//	private User creatorReportTemplate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PROJECT_ID") 
	private Project project;
}
