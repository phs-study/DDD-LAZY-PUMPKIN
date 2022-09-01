package com.ewha.pumpkin.service;

public class BaseService {
	public void throwIf(boolean condition, RuntimeException exception) {
		if (condition) {
			throw exception;
		}
	}

	public void throwIfNot(boolean condition, RuntimeException exception) {
		throwIf(!condition, exception);
	}

	public void throwIfNull(Object object, RuntimeException exception) {
		if (object == null) {
			throw exception;
		}
	}
}
