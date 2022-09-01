package com.ewha.pumpkin.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {
	@CreatedDate
	@Column(nullable = false, updatable = false)
	LocalDateTime createdDateTime;

	@LastModifiedDate
	@Column(nullable = false)
	@Setter
	LocalDateTime lastModifiedDateTime;
}
