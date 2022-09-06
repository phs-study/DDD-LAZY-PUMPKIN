package com.ewha.pumpkin.academy;

import com.ewha.pumpkin.academy.dto.RegisterStudentDto;
import com.ewha.pumpkin.academy.dto.RegisterTeacherDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller("/academy")
public class AcademyController {
    // 학생 등록
    @PostMapping("/student")
    void createStudent(@RequestBody RegisterStudentDto registerStudentDto) {
        // todo : 권한 처리 필요
        String fatherPhoneNumber = registerStudentDto.getFatherPhoneNumber();
        String motherPhoneNumber = registerStudentDto.getMotherPhoneNumber();
        Director director = DataBase.findAdminDirector();

        Student registeredStudent = Student.create(registerStudentDto.getStudentName());
        // 해당 가정 최초 등록
        if (DataBase.existParent(fatherPhoneNumber, motherPhoneNumber)){
            director.addStudentAndParent(registeredStudent, Parent.create(registerStudentDto.getFatherName(), registerStudentDto.getMotherName(), fatherPhoneNumber, motherPhoneNumber));
            return;
        }
        // 이미 부모님이 등록되어있다면
        director.addStudentAndSetParent(
                Student.create(registerStudentDto.getStudentName()),
                DataBase.findParentByPhoneNumber(fatherPhoneNumber, motherPhoneNumber)
        );
    }

    // 학생 목록 조회
    @GetMapping("/student/list")
    List<Student> showStudents() {
        // todo : 권한 처리 필요
        Director director = DataBase.findAdminDirector();
        return director.showStudents();
    }

    // 학생 개별 조회
    @GetMapping("/student/{studentId}")
    Student showStudent(@PathVariable Long studentId) {
        // todo : 권한 처리 필요
        Director director = DataBase.findAdminDirector();
        return director.showStudent(studentId);
    }

    // 강사 등록
    @PostMapping("/teacher")
    void createStudent(@RequestBody RegisterTeacherDto registerTeacherDto) {
        // todo : 권한 처리 필요
        Director director = DataBase.findAdminDirector();
        director.addTeacher(Teacher.create(registerTeacherDto.getName(), registerTeacherDto.getSubject()));
    }

    // 강사 목록 조회
    @GetMapping("/teacher/list")
    List<Teacher> showTeachers() {
        // todo : 권한 처리 필요
        Director director = DataBase.findAdminDirector();
        return director.showTeachers();
    }

    // 강사 개별 조회
    @GetMapping("/teacher/{teacherId}")
    Teacher showTeacher(@PathVariable Long teacherId) {
        // todo : 권한 처리 필요
        Director director = DataBase.findAdminDirector();
        return director.showTeacher(teacherId);
    }

    @PostMapping("/pay")
    void pay() {
        // 결제 요청 (주문 객체 생성) parent(1) - Payment(N)
        // 외부 시스템 연결
        // (외부 api fail 시 결제 실패 알람 전송 후, Payment 의 status 실패 업데이트)
        // (외부 api success 시 결제 성공 알람 전송 후, Payment 의 status 성공 업데이트)

        // -> 기록이 필요하므로 테이블에 row 가 쌓여야 할 듯
    }

    @PostMapping("/check")
    void check() {
        // student 가 출석체크시 호출
        // 내부적으로 출석 체크 (Attendance 객체 생성 학생과 1 : 1, 쌓이지않는다.)
        // 외부 시스템을 통해 알람 발송 (스케줄러 이용 해서 특정시간마다 보낼수있도록한다. 알람에 대한모델은 없어도되지않을까?)
        // 만약 알람 발송 실패 시, call back or transaction 이 가능하도록 구현

        // -> 기록이 필요하므로 테이블에 row 가 쌓여야 할 듯 (대신 매일 1회씩만 row 가 쌓이도록)
    }
}
