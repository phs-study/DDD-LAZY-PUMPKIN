package com.ewha.pumpkin.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 일급 컬렉션 / 도메인인가? 값타입인가? 둘다 아닌가?
 *
 * JPA에서 연관관계는 일대다의 경우 명확하게 컬렉션을 사용해야 합니다. 단일 객체를 사용할 수 없습니다.
 * 대신 임베디드 타입(@Embedded) 으로 한번 감싸면 어느정도 원하는 목표를 달성하실 수 있을꺼에요.
 * 하지만 임베디드 타입으로 한번 감싸면 JPQL등을 작성할 때, 그리고 실제 개발할 때, 객체를 한번 더 거쳐야 하기 때문에
 * 오는 불편함들도 많습니다. 이런 점들을 고려하셔서 결정하시면 됩니다. (저는 이런 불편함 점들 때문에 실용적인 관점에서
 * JPA 연관관계에 일급 컬렉션은 잘 사용하지는 않습니다.)
 *
 * 현재 비즈니스 로직에서 일급 컬렉션을 사용하는게 더 좋으면 일급 컬렉션을 사용하면 되고, 단순히 리스트를 받아서 처리해도
 * 문제가 없으면 리스트로 처리해도 됩니다. 예를 들어서 비즈니스 로직이 매우 단순하고, 또 단순히 리스트를 돌리면서 출력하는
 * 로직만 있다면 일급 컬렉션을 만드는게 큰 의미가 없겠지요. 반대로 비즈니스 로직이 매우 복잡하고, 리스트의 내용을 가지고
 * 뭔가 계산하는 로직들이 많이 있다면 일급 컬렉션이 이 업무를 담당해주면 좋습니다. 추가로 비즈니스 로직이 파편화
 * 되지 않고, 일급 컬렉션이 관리를 해주니 재사용성도 높아지겠지요.
 * JPA를 사용해도 일급컬렉션을 사용할 수 있습니다. 예를 들어서 주문 내역을 List<Order> orders로 가지고 왔는데,
 * 리스트는 사실 비즈니스 로직을 제공할 수 있는 방법이 없지요. List가 제공하는 기능만 사용할 수 있으니까요.
 * 그래서 일급 컬렉션을 하나 만들어두고 그 안에 JPA에서 조회한 List<Order> orders를 넣어주면 일급 컬렉션을 통해서 많을 것을 제공할 수 있습니다.
 * 추가로 클린코드 책 6. 객체와 자료 구조를 읽어보시면, 말씀드린 트레이드 오프에 대한 몇가지 인사이트를 얻으실 수 있습니다.
 *
 * @author : lhm0805
 * @date : 2022. 09. 01. 오후 11:53:22
 */
public class Students {
	private List<Student> students;

	public Students(List<Student> students) {
		this.students = students;
	}

	public void addStudent(Student student) {
		this.students.add(student);
	}

	public List<Student> findAll() {
		return new ArrayList<>(students);
	}

	public Optional<Student> findById(String id) {
		return this.students.stream()
			.filter(student -> student.getId().equals(id))
			.findAny();
	}
}
