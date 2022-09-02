package com.ewha.pumpkin.academy;

import lombok.Getter;

import java.util.List;

@Getter
public class Director {
    private Long id;
    private String name;

    public Director(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addStudentAndParent(Student student, Parent parent) {
        DataBase.addStudent(student);
        DataBase.addParent(parent);
        student.setParent(parent);
        parent.setStudent(student);
    }

    public void addStudentAndSetParent(Student student, Parent parent) {
        DataBase.addStudent(student);
        student.setParent(parent);
        parent.setStudent(student);
    }

    public void addTeacher(Teacher teacher) {
        DataBase.addTeacher(teacher);
    }

    public List<Student> showStudents() {
        return DataBase.findStudentList();
    }

    public List<Teacher> showTeachers() {
        return DataBase.findTeacherList();
    }

    public Student showStudent(Long studentId) {
        return DataBase.findStudentById(studentId);
    }

    public Teacher showTeacher(Long teacherId) {
        return DataBase.findTeacherById(teacherId);
    }
}
