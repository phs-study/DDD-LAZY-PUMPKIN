package com.ewha.pumpkin.domain;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.websocket.AuthenticationException;

public class Academy {
	private String id;
	private List<Teacher> teachers;
	private Students students;
	private Payments payments;
	private List<Customer> customers;

	public void addStudent(Student student) {
		this.students.addStudent(student);
	}

	public Optional<Student> getStudent(String id) {
		return students.findById(id);
	}

	public List<Student> getStudents() {
		return students.findAll();
	}

	public void addTeacher(Teacher teacher) throws AuthenticationException {
		teacher.addTeacher(teacher);
	}

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public Optional<Teacher> getTeacher(String id) {
		return teachers.stream()
			.filter(teacher -> teacher.getId().equals(id))
			.findAny();
	}

	public void addPayment(Payment payment) {
		this.payments.addPayment(payment);
	}

	public Optional<Customer> getCustomer(String id) {
		return this.customers.stream()
			.filter(customer -> customer.getId().equals(id))
			.findAny();
	}
}
