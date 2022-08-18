# 마이크로서비스 개발을 위한 Domain Driven Design

![image](https://user-images.githubusercontent.com/52458039/185303488-15911b22-1008-476f-a20b-fee9d3b028ef.png)


- 과거형/수동형 을 사용한다.

![image](https://user-images.githubusercontent.com/52458039/185303569-b1f3ed2a-6a46-4354-8736-023b910f78e4.png)


- 이벤트의 source가 된다.

![image](https://user-images.githubusercontent.com/52458039/185303607-b07339ef-a119-4336-abad-89276a17f617.png)

- 특정 Command가 Entity나 Aggregate에 영향을 줘서 Object가 Event를 갖게 된다.
- Aggregate 끼리는 객체 참조하지 않고 id로 참조한다. (tightly coupled 가 아닌, loosely coupled 되어 있다.)

![image](https://user-images.githubusercontent.com/52458039/185303883-6edbac25-6d26-45a5-82c1-fd03cb50c40b.png)

1. 아이템을 장바구니어 넣으라는 명령을 내린다.
2. 명령은 아이템이라는 Aggregate에 영향을 미침.
3. 이 아이템은 명령에 의해 카트에 들어가게 됨. 
    - Aggregate / Event는 항상 같이 일어난다.
    - Aggregate의 state 변화가 있으면 Event가 발생한다.
    - Event가 발생하면 Aggregate의 state 변화가 생긴다.)

(Command → Aggregate → Event 의 Flow를 가진다.)

![image](https://user-images.githubusercontent.com/52458039/185303935-445e5966-de2d-4087-9bf2-6e56aa168d22.png)

이 이벤트는 Time Series로 시계열 로 표현한다. (오른쪽으로 갈 수록 나중 일이 된다.)

![image](https://user-images.githubusercontent.com/52458039/185303969-361e5211-612b-44ff-bff4-3c2431057f47.png)

Command와 Event도 항상 짝으로 이루어 진다. (경우에 따라 Command가 명확해서 생략하는 경우도 있음)

![image](https://user-images.githubusercontent.com/52458039/185304028-e7b8558f-d8b2-4930-88e2-5f6c58b9446f.png)

이렇게 Command-Aggregate-Event가 하나의 묶음으로 진행된다.

## Domain Driven Design (설계 과정)

1. Event Storming (Command - Aggregate - Event 단위 정의)

![image](https://user-images.githubusercontent.com/52458039/185304068-93f4f9dd-d5ad-4db4-88b5-d66c8c89dc5a.png)

2. Grouping (Aggregate 그룹핑)

![image](https://user-images.githubusercontent.com/52458039/182768412-9b891769-7ee8-4c9f-9ff0-9abc14478501.png)

3. Boris diagram (서비스간 통신)

![image](https://user-images.githubusercontent.com/52458039/185304273-8c6d4139-cc7a-4948-8bad-8745269772a4.png)

4. SNAP-E (디테일한 스펙)

![image](https://user-images.githubusercontent.com/52458039/185304314-daf97de0-7fda-422c-9365-fcf87a2a04cb.png)

**Grouping** 에 고려해야할 점

1. 데이터 ownership
2. 서비스 배포주기를 독립적으로 가져갈 조건들
3. 독립적인 scalability

보통 Grouping을하면 BoundedContext로 하나 이상의 aggregate를 가지도록 구분할 수 있다. (보통 2개의 aggregate가 하나의 BoundedContext에 있다면, 이 aggregate는 서로의 의존관계가 높다는 의미)

**Boris 다이어그램** : aggregate간의 통신 (각 서비스간의 상관관계, 서비스간의 요청방식을 동기/비동기로 할지 판단)

**SNAP-E 예시**

- API : ex) /orders, /buy, /sell
- Data : 주문
- User Story : 매도 요청, 매수 요청, 주식 검색(회사이름)
- Risk : 외부 마켓 시스템 장애에 대한 고려
- UI

Q&A

- sticky note → jira, miro 를 통해 자산화 중요 (디자인 단계, 개발 테크니컬 기록 → 다른 프로젝트에 참고 가능)
- 디자인 단계(4주) → 개발 단계 (8~12주) : 총 3 ~ 4개월 정도 걸림
