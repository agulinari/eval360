package com.gire.eval360.projects.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@JsonIgnoreProperties({"createdDate","modifiedDate","modifiedBy","createdBy","hibernateLazyInitializer" , "handler"})
public abstract class AuditedEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column
    @CreatedBy
    private String createdBy;

    @Column
    @LastModifiedBy
    private String modifiedBy;

}