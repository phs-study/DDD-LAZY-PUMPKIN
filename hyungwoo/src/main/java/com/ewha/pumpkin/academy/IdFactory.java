package com.ewha.pumpkin.academy;

public class IdFactory {
    private static Long directorId = 0L;
    private static Long parentId = 0L;
    private static Long studentId = 0L;
    private static Long teacherId = 0L;

    public static Long generateDirectorId() {
        return directorId++;
    }

    public static Long generateParentId() {
        return parentId++;
    }

    public static Long generateStudentId() {
        return studentId++;
    }

    public static Long generateTeacherId() {
        return teacherId++;
    }
}
