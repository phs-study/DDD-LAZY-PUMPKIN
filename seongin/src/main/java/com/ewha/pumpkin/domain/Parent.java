package com.ewha.pumpkin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sun.istack.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class Parent extends BaseEntity {
	@Id
	@Column(name = "parent_sn")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sn;

	private String name;
	private String contactNumber;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_sn", foreignKey = @ForeignKey(name = "fk_parent__student_sn"))
	@Setter(AccessLevel.PROTECTED)
	private Student student;

	public static Parent create(String name, String contactNumber) {
		Parent parent = new Parent();
		parent.name = name;
		parent.contactNumber = contactNumber;
		return parent;
	}

}
