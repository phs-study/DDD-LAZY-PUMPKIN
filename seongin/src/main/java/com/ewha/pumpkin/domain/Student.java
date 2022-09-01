package com.ewha.pumpkin.domain;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;

@Entity
@Getter
public class Student extends BaseEntity {
	@Id
	@Column(name = "student_sn")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sn;

	private String name;
	private String contactNumber;
	private LocalDateTime lastCheckInDate;
	private LocalDateTime lastCheckOutDate;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Parent> parentList = new ArrayList<>();

	public static Student create(String name, String contactNumber) {
		Student student = new Student();
		student.name = name;
		student.contactNumber = contactNumber;
		return student;
	}

	public void updateParentList(List<Parent> parentList) {
		parentList.forEach(this::addParent);
	}

	private void addParent(Parent parent) {
		parent.setStudent(this);
		parentList.add(parent);
	}

	public void checkIn() {
		validateCheckIn();
		lastCheckInDate = LocalDateTime.now();
	}

	public void checkOut() {
		validateCheckOut();
		lastCheckOutDate = LocalDateTime.now();
	}

	private void validateCheckIn() {
		if(lastCheckInDate != null) {
			throw new RuntimeException("이미 체크인 하셨습니다.");
		}
	}

	private void validateCheckOut() {
		if(lastCheckInDate == null) {
			throw new RuntimeException("체크인 기록이 없습니다.");
		}
		if(lastCheckOutDate != null) {
			throw new RuntimeException("이미 체크아웃 하셨습니다.");
		}

		final LocalDateTime now = LocalDateTime.now();
		final LocalDateTime strOfDate = now.with(ChronoField.NANO_OF_DAY, LocalTime.MIN.toNanoOfDay());
		final LocalDateTime endOfDate = now.with(ChronoField.NANO_OF_DAY, LocalTime.MAX.toNanoOfDay());

		if(lastCheckInDate.compareTo(strOfDate) < 0) {
			throw new RuntimeException("이전 체크인 기록이 남아있습니다.");
		}

		if(lastCheckOutDate.compareTo(endOfDate) > 0) {
			throw new RuntimeException("체크아웃할 수 없는 시간입니다.");
		}
	}
}
