package com.ewha.pumpkin.domain;

import java.util.List;

public class Payments {
	private List<Payment> payments;

	public Payments(List<Payment> payments) {
		this.payments = payments;
	}

	public void addPayment(Payment payment) {
		this.payments.add(payment);
		final Customer customer = payment.getCustomer();
		try {
			customer.addAlarm(new Alarm("결제 완료"));
		} catch (RuntimeException e) {
			customer.addAlarm(new Alarm("결제 실패"));
		}
	}
}
