package com.gire.eval360.users.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Authority extends AuditedEntity{
	
	 @Column(name = "NAME", length = 50)
	 @NotNull
	 @Enumerated(EnumType.STRING)
	 private AuthorityName name;

	 @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
	 private List<User> users;

}
