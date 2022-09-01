package com.ewha.pumpkin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.pumpkin.domain.Student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService extends BaseService {

	public void checkIn() {
		Student student = new Student();
		student.checkIn();
	}

	public void checkOut() {
		Student student = new Student();
		student.checkOut();
	}
}
