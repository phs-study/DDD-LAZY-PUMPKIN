package com.ewha.pumpkin.domain;

import java.time.LocalDateTime;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class Attendance {
	private String id;
	private LocalDateTime localDateTime;

	public Attendance() {
		this.id = NanoIdUtils.randomNanoId();
		this.localDateTime = LocalDateTime.now();
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}
}
