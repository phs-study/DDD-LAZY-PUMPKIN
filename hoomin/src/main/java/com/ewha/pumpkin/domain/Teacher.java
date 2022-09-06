package com.ewha.pumpkin.domain;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.websocket.AuthenticationException;

import com.ewha.pumpkin.domain.enums.TeacherType;

public class Teacher {
	private String id;
	private Academy academy;
	private TeacherType teacherType;

	public Teacher(Academy academy, TeacherType teacherType) {
		this.academy = academy;
		this.teacherType = teacherType;
	}

	public String getId() {
		return id;
	}

	public void addStudent(String customerId) throws AuthenticationException {
		if (this.teacherType != TeacherType.HEAD) {
			throw new AuthenticationException("원장 선생님만 학생을 등록 할 수 있습니다.");
		}
		final Customer customer = this.academy.getCustomer(customerId).orElseThrow();
		final Student student = new Student(this.academy, customer);
		academy.addStudent(student);
	}

	public List<Student> getStudents() {
		return this.academy.getStudents();
	}

	public Optional<Student> getStudent(String id) {
		return this.academy.getStudent(id);
	}

	public void addTeacher(Teacher teacher) throws AuthenticationException {
		if (this.teacherType != TeacherType.HEAD) {
			throw new AuthenticationException("원장 선생님만 강사를 등록 할 수 있습니다.");
		}
		this.academy.addTeacher(teacher);
	}

	public List<Teacher> getTeachers() {
		return this.academy.getTeachers();
	}

	public Optional<Teacher> getTeacher(String id) {
		return this.academy.getTeacher(id);
	}
}
