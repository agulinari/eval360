package com.gire.eval360.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class Address extends AuditedEntity {

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String postalCode;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;
}
