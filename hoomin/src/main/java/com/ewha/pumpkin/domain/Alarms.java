package com.ewha.pumpkin.domain;

import java.util.List;

public class Alarms {
	private List<Alarm> alarms;

	public void clear() {
		alarms.clear();
	}

	public void addAlarm(Alarm alarm) {
		this.alarms.add(alarm);
		// 외부 api 요청
	}
}
