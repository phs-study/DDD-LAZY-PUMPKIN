package com.ewha.pumpkin.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class Alarm {
	private String id;
	private String message;

	public Alarm(String message) {
		this.id = NanoIdUtils.randomNanoId();
		this.message = message;
	}
}
