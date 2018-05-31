package com.gire.eval360.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class ContactInfo extends AuditedEntity {

    @Column
    private String internalNumber;

    @Column
    private String telephoneNumber;

    @Email
    @NonNull
    @NotBlank
    @Column
    private String email;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

}