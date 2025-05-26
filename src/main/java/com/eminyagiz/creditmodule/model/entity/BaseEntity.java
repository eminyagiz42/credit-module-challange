package com.eminyagiz.creditmodule.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
    @CreatedBy
    protected String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Date createdDate = Date.from(Instant.now());

    @LastModifiedBy
    protected String lastModifiedBy;

    @LastModifiedDate
    protected Date lastModifiedDate;
}
