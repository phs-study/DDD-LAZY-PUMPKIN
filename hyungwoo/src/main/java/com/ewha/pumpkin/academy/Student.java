package com.ewha.pumpkin.academy;

import lombok.Getter;

@Getter
public class Student {
    private Long id;
    private String name;

    // many to one
    private Parent parent;

    public static Student create(String name) {
        Student student = new Student();
        student.id = IdFactory.generateStudentId();
        student.name = name;
        return student;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
