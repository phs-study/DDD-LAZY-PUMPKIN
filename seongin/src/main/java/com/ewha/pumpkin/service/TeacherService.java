package com.ewha.pumpkin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ewha.pumpkin.domain.Parent;
import com.ewha.pumpkin.domain.Student;
import com.ewha.pumpkin.domain.Teacher;
import com.ewha.pumpkin.domain.enums.TeacherRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService extends BaseService {

	void addStudent() {
		final List<Parent> parentList = createParent();
		String name = "홍학생";
		String contactNumber = "01012345678";
		Student student = Student.create(name, contactNumber);
		student.updateParentList(parentList);
	}

	public List<Student> getStudentList() {
		Student student = new Student();
		return List.of(student);
	}
	private List<Parent> createParent() {
		String name = "홍엄마";
		String contactNumber = "01012345678";
		return List.of(Parent.create(name, contactNumber));
	}

	void addTeacher() {
		Teacher teacher = new Teacher();
		throwIfNot(isDirector(teacher), new RuntimeException("강사 등록 권한이 없습니다."));
	}

	public List<Teacher> getTeacherList() {
		Teacher teacher = new Teacher();
		throwIfNot(isDirector(teacher), new RuntimeException("강사 목록 조회 권한이 없습니다."));
		return List.of(teacher);
	}

	boolean isDirector(Teacher teacher) {
		return teacher.getTeacherRole() == TeacherRole.DIRECTOR;
	}
}
