package com.ewha.pumpkin.domain;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class Payment {
	private String id;
	private Money money;
	private Customer customer;

	public Payment(Money money, Customer customer) {
		this.id = NanoIdUtils.randomNanoId();
		this.money = money;
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}
}
