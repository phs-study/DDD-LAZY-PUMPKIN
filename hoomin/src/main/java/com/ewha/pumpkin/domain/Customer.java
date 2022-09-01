package com.ewha.pumpkin.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

/**
 * 고객은 여러 학원에 가입될 수 있으나
 * 각각의 학원에는 별개의 고객으로 존재한다
 *
 * @author : lhm0805
 * @date : 2022. 09. 02. 오전 12:37:24
 */

public class Customer {
	private String id;
	private Alarms alarms;
	private Academy academy;
	private Students students;

	public Customer(Academy academy) {
		this.id = NanoIdUtils.randomNanoId();
		this.academy = academy;
	}

	public String getId() {
		return id;
	}

	public void addAlarm(Alarm alarm) {
		this.alarms.addAlarm(alarm);
	}

	public void pay(Money money) {
		final Payment payment = new Payment(money, this);
		academy.addPayment(payment);
	}
}
