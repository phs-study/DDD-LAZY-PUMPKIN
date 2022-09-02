package com.ewha.pumpkin.academy.dto;

import lombok.Getter;

@Getter
public class RegisterStudentDto {
    // not null
    private String studentName;
    // nullable
    private String motherName;
    // nullable
    private String fatherName;
    // nullable
    private String fatherPhoneNumber;
    // nullable
    private String motherPhoneNumber;
}
