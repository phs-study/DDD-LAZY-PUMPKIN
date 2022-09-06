package com.ewha.pumpkin.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class Student {
	private String id;
	private Academy academy;
	private Attendances attendances = new Attendances();
	private Customer customer;

	public Student(Academy academy, Customer customer) {
		this.id = NanoIdUtils.randomNanoId();
		this.academy = academy;
		this.customer = customer;
	}

	public String getId() {
		return id;
	}

	public void attendanceCheck() {
		this.attendances.add(new Attendance());
		attendances.add(new Attendance());
		customer.addAlarm(new Alarm("출석 완료"));
	}
}
