package com.ewha.pumpkin.academy;

import lombok.Getter;

import java.util.List;

@Getter
public class Parent {
    private Long id;
    private String fatherName;
    private String motherName;
    private String fatherPhoneNumber;
    private String motherPhoneNumber;

    // one to many
    private List<PayHistory> payHistories;
    // one to many
    private List<Student> students;

    public static Parent create(String fatherName, String motherName, String fatherPhoneNumber, String motherPhoneNumber) {
        Parent parent = new Parent();
        parent.id = IdFactory.generateParentId();
        parent.fatherName = fatherName;
        parent.motherName = motherName;
        parent.fatherPhoneNumber = fatherPhoneNumber;
        parent.motherPhoneNumber = motherPhoneNumber;
        return parent;
    }

    public boolean matchFatherPhoneNumber(String fatherPhoneNumber) {
        return this.fatherPhoneNumber.equals(fatherPhoneNumber);
    }

    public boolean matchMotherPhoneNumber(String motherPhoneNumber) {
        return this.motherPhoneNumber.equals(motherPhoneNumber);
    }

    public void setStudent(Student student) {
        this.students.add(student);
    }
}
