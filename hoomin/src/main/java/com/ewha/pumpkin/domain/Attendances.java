package com.ewha.pumpkin.domain;

import java.util.ArrayList;
import java.util.List;

public class Attendances {
	private List<Attendance> attendances = new ArrayList<>();

	public void add(Attendance attendance) {
		if (!attendances.isEmpty()) {
			final Attendance lastAttendance = attendances.get(attendances.size() - 1);
			if (lastAttendance.getLocalDateTime().toLocalDate().equals(attendance.getLocalDateTime().toLocalDate())) {
				throw new IllegalStateException("이미 출석체크 했습니다.");
			}
		}
		attendances.add(attendance);
	}
}
