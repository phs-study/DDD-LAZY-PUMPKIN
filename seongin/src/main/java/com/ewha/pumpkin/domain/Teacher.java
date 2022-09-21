package com.ewha.pumpkin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ewha.pumpkin.domain.enums.TeacherRole;

import lombok.Getter;

@Entity
@Getter
public class Teacher extends BaseEntity {
	@Id
	@Column(name = "teacher_sn")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sn;

	@Enumerated(EnumType.STRING)
	private TeacherRole teacherRole;
}
