package com.ewha.pumpkin.academy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBase {
    private static Map<Long, Director> directors = new HashMap<>();
    private static Map<Long, Parent> parents = new HashMap<>();
    private static Map<Long, Student> students = new HashMap<>();
    private static Map<Long, Teacher> teachers = new HashMap<>();

    static {
        Long directorId = IdFactory.generateDirectorId();
        directors.put(directorId, new Director(directorId, "admin"));
    }

    public static void addParent(Parent parent) {
        parents.put(parent.getId(), parent);
    }

    public static void addStudent(Student student) {
        students.put(student.getId(), student);
    }

    public static void addTeacher(Teacher teacher) {
        teachers.put(teacher.getId(), teacher);
    }

    public static Director findAdminDirector() {
        return directors.get("admin");
    }

    public static List<Student> findStudentList() {
        return new ArrayList<>(students.values());
    }

    public static List<Teacher> findTeacherList() {
        return new ArrayList<>(teachers.values());
    }

    public static Student findStudentById(Long studentId) {
        return students.get(studentId);
    }

    public static Teacher findTeacherById(Long teacherId) {
        return teachers.get(teacherId);
    }

    public static boolean existParent(String fatherPhoneNumber, String motherPhoneNumber) {
        return parents.values().stream()
                .filter(parent -> parent.matchFatherPhoneNumber(fatherPhoneNumber))
                .filter(parent -> parent.matchMotherPhoneNumber(motherPhoneNumber))
                .findAny()
                .map(parent -> true)
                .orElse(false);
    }

    public static Parent findParentByPhoneNumber(String fatherPhoneNumber, String motherPhoneNumber) {
        return parents.values().stream()
                .filter(parent -> parent.matchFatherPhoneNumber(fatherPhoneNumber))
                .filter(parent -> parent.matchMotherPhoneNumber(motherPhoneNumber))
                .findAny()
                .orElse(null);
    }

    public static Parent findUserById(String parentsId) {
        return parents.get(parentsId);
    }

    public static Collection<Parent> findAll() {
        return parents.values();
    }
}
