package com.gire.eval360.projects.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TeamMember extends AuditedEntity{


	@ManyToMany(mappedBy = "teamMembers")
    private List<Project> projects;

}