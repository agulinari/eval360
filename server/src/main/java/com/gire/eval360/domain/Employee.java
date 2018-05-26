package com.gire.eval360.domain;

import java.sql.Date;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Employee extends AuditedEntity{

	@Column
	@NonNull
	private String name;
	
	@Column
	@NonNull
	private  String lastname;

	@Column
	@NonNull
	private Date birthDate;
	
	@Column
	@NonNull
	private Date startDate;
	
	@Column
	@NonNull
	private String photo;
	
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private ContactInfo contactInfo;
	
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private Address address;
    
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private User user;

    @ManyToOne
	@JoinColumn(name = "area_id")
	private Area area;
	
    @ManyToOne
	@JoinColumn(name = "position_id")
	private Position position;
    
	@ManyToOne
	@JoinColumn(name = "boss_id")
	private Employee boss;
	
	@OneToMany(mappedBy = "boss", cascade = CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JsonIgnore
	private Collection<Employee> subordiantes;
}
