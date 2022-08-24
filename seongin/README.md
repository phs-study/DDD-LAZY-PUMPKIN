https://www.youtube.com/watch?v=F7EnW8dfetU 

# DDD, Boris and Snap-E
- DDD
  - 왜 DDD/MSA인가 ?
    - common language의 역할
      - communication cost minimize
      - 작은 변화 싸이클을 유도
      - 개발자/비개발자의 도메인 영역을 일치시킬 수 있음
        - 개발자는 비개발자에 비해 도메인 전문 지식이 부족하다.
        - 비개발자는 개발자에 비해 IT 관련 지식이 부족하다.
        - 서로의 언어를 고집하기 보다는 "도메인"에 대해서 이야기할 수 있다.
          - ex) 응시자는 응시중 다른 시험을 시작할 수 있습니까 ?
  - Event Storming
    - 정의
      - IT와 Business의 align
      - MSA design에서 domain과 subDomain을 구분할 수 있다.
    - 구성 요소
      - Domain Events
        - 알림 혹은 상태 전이
        - 과거형/수동형으로 표현
      - Command
        - 요청 혹은 트리거
        - 도메인 이벤트의 발생지(source)
      - Aggregate 
        - 한 개 혹은 그 이상의 entity로 구성
        - Aggregate끼리는 루즈 커플링(오브젝트 탐색이 불가능, id로 식별)
      - Command -> Aggregate -> Domain Event 동작 (Aggregate와 Domain Event 사이에서는 원자성을 보존되어야함.)
        1. Add Item to Card
        2. Item
        3. Item Added to Cart
- Boris
  - 설계 내용(Aggregate, Domain)을 기술적 구현으로 표현
  - 동기/비동신 통신, 외부 통신 등
- Snap-E
  - Aggregate의 상세 스펙
    - API, DATA, STORY, RISK, UI 등


# 1.1 도메인 이란 ?
> 소프트웨어로 해결하고자 하는 문제 영역

# 1.2 도메인 전문가와 개발자 간 지식 공유
> 개발자도 도메인 지식을 갖추면 올바른 제품을 개발할 확률이 높아진다.

# 1.3 도메인 모델
> 도메인을 개념적으로 표현한 것

# 1.4 도메인 모델 패턴
- 애플리케이션 아키텍처 구성
  1. 사용자 인터페이스 혹은 표현 (Presentation)
     - 사용자의 요청을 처리하고 사용자에게 정보를 보여준다. 여기서 사용자는 소프트웨어를 사용하는 사람뿐만 아니라 외부 시스템일 수도 있다. 
  2. 응용 (Application)
     - 사용자가 요청한 기능을 실행한다. 업무 로직을 직접 구현하지 않으며 도메인 계층을 조합해서 기능을 실행한다. 
  3. 도메인 (Domain)
     - 시스템이 제공할 도메인 규칙을 구현한다. 
  4. 인프라스트럭쳐(Infrastructure) 
     - 데이터베이스나 메시징 시스템과 같은 외부 시스템과의 연동을 처리한다.

# 1.5 도메인 모델 도출
> 핵심 구성요소, 규칙, 기능을 찾아 도메인 모델을 구성한다.