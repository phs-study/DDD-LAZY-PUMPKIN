package com.ewha.pumpkin.academy;

public class Director {
    private String name;

    public void addStudent(Student student) {
        Databaes.addStudent(student);
    }
}
