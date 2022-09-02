package com.ewha.pumpkin.academy;

import lombok.Getter;

@Getter
public class Teacher {
    private Long id;
    private String name;
    private String subject;

    public static Teacher create(String name, String subject) {
        Teacher teacher = new Teacher();
        teacher.id = IdFactory.generateTeacherId();
        teacher.name = name;
        teacher.subject = subject;
        return teacher;
    }
}
