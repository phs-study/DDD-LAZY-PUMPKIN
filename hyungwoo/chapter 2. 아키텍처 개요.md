# 2.1 네 개의 영역
아키텍처 설계의 전형적인 네가지 영역
- 표현 : 사용자의 요청을 해석하여 응용서비스에 전달함. 응용 서비스의 실행 결과를 사용자가 이해할 수 있는 형식으로 변환함.
- 응용 : 사용자에게 제공해야할 기능을 구현한다. (ex : 주문 등록, 주문 취소, 상품 상세 조회)
  - **응용 서비스는 로직을 직접 수행하기보다는 도메인 모델에 로직 수행을 위임한다.** 
- 도메인 : 도메인의 핵심 규칙을 로직으로 구현한다.
- 인프라스트럭처 : 구현 기술을 다룬다. (ex : RDBMS 연동, 메시징 큐 메시지 전송 or 수신, SMTP 를 이용한 메일 발송 기능 구현, REST API 호출 처리 -> 실제 구현을 담당)



# 2.2 계층 구조 아키텍처
![image](https://user-images.githubusercontent.com/52458039/188440612-2ee380e9-8ac6-46e1-ba9e-4a7898756729.png)

**계층 구조의 특성상 상위 계층에서 하위 계층으로의 의존만 존재하고 하위 계층은 상위 계층에 의존하지 않는다.**

계층 구조를 엄격하게 적용한다면 상위 계층은 바로 아래의 계층에만 의존을 가져가야 하지만, 구현의 편리함을 위해 계층 구조를 유연하게 적용하기도 한다.
ex : `응용 계층`은 `도메인 계층`에만 의존해야 하지만, 외부 시스템과의 연동을 위해 더 아래 계층인 `인프라스트럭처 계층`에 의존하기도 한다.

![image](https://user-images.githubusercontent.com/52458039/188584442-49f75b17-6b77-461e-b47a-898edb87b8b2.png)
계층 구조에 따르면 `도메인`과 `응용 계층`은 룰 엔진과 DB 연동을 위해 위와 같은 `인프라스트럭처 모듈`에 의존하게 된다.

> 룰 엔진? 

예시 : 도메인 가격 계산 규칙 
`DroolsRuleEngine`
```java
public class DroolsRuleEngine {
    private KieContainer kContainer;
    
    public DroolsRuleEngine() {
        KieServices ks = KieServices.Factory.get();
        kContainer = ks.getKieClasspathContainer();
    }
    
    public void evaluate(String sessionName, List<?> facts) {
        KieSession kSession = kContainer.newKieSession(sessionName);
        try {
            facts.forEach(x -> kSession.insert(k));
            kSession.fireAllRules();
        } finally {
            kSession.dispose();
        }
    }
}
```
위 코드는 인프라스트럭처 계층에서 구현하는 룰 엔진 로직이다.

```java
public class CalculateDiscountService {    
    private DroolsRuleEngine ruleEngine;
    
    public CalculateDiscountService() {
        ruleEngine = new DroolsRuleEngine();    
    }        
    
    public Money calculateDiscount(List<OrderLine> orderLines, String customerId) {
        Customer customer = findCustomer(customerId);
        MutalbeMoney money = new MutableMoney(0);
        List<?> facts = Arrays.asList(customer, money);
        facts.addAll(orderLines);
        ruleEngine.evaluate("discountCalculation", facts);
        return money.toImmutableMoney();    
    }
}
```
응용 영역인 CalculateDiscountService (서비스 영역) 에서는 인프라 스트럭처 영역의 DroolsRuleEngine 을 사용한다.
위 코드에는 문제점이 2가지 있다.

- CalculateDiscountService 만 테스트 하기 어렵다. (Rule Engine 을 의존하고 있기 때문)
- 구현 방식을 변경하기 어렵다. (Rule Engine 을 의존하고 있기 때문)

이 두 문제는 DIP 로 해결할 수 있다.

# 2.3 DIP (Dependency Inversion Principle)
![image](https://user-images.githubusercontent.com/52458039/188590072-ca678366-bf76-4f08-958b-ea9eebfe9117.png)

우리는 가격 할인 계산을 위해 `특정 고객 정보` 와 `특정 룰 엔진` 이 필요하다.

가격 할인 계산을 하는 응용 영역 (CalculateDiscountService)는 **고수준 모듈**이다.
반대로 JPA 와 룰 엔진을 이용하는 모듈은 **저수준 모듈**이다.

고수준 모듈을 구현하기 위해 여러 하위 기능이 필요한데, 이 하위 기능을 저수준 모듈로 볼 수 있다.

우리는 2.2 장에서 고수준 모듈이 저수준 모듈을 의존할 때 2가지 문제를 직면했다.

DIP 를 이용하여 의존 관계를 역전 시켜서 **저수준 모듈이 고수준 모듈에 의존하도록** 바꾸면 이 문제를 해결할 수 있다.

-> 인터페이스를 이용한다.

Drools 는 구현체 이므로 이를 추상화한 인터페이스를 정의한다.
```java
public interface RuleDiscounter {
    Money applyRules(Customer customer, List<OrderLine> orderLines);
}
```

이제 CalculateDiscountService 가 RuleDiscounter 를 이용하도록 바꾼다.
```java
public class CalculateDiscountService {    
    private RuleDiscounter ruleDiscounter;
    
    public CalculateDiscountService(RuleCounter ruleCounter) {
      this.ruleDiscounter = ruleCounter;    
    }        
    
    public Money calculateDiscount(List<OrderLine> orderLines, String customerId) {
        Customer customer = findCustomer(customerId);
        return ruleDiscounter.applyRules(customer, orderLines);    
    }
}
```
이렇게 인터페이스를 통해 고수준 모듈인 CalculateDiscountService 는, DroolsRuleEngine 을 몰라도 생성자를 통해 주입 받아서 처리할 수 있다.

추상화한 RuleDiscounter 상속하여 우리가 사용하고자 하는 Drools 로 구현하면 된다.
```java
public class DroolsRuleDiscounter implements RuleDiscounter {
    private KieContainer kContainer;
    
    public DroolsRuleEngine() {
        KieServices ks = KieServices.Factory.get();
        kContainer = ks.getKieClasspathContainer();
    }
    
    @Override
    public void applyRules(Customer customer, List<OrderLine> orderLines) {
        KieSession kSession = kContainer.newKieSession("discountSession");
        try {
            // money
            facts.forEach(x -> kSession.insert(k));
            kSession.fireAllRules();
        } finally {
            kSession.dispose();
        }
    }
}
```
![image](https://user-images.githubusercontent.com/52458039/188597083-8fe0b499-036a-4b15-b8a7-d603bece156a.png)
이렇게 DIP 를 적용하면서 더이상 CalculateDiscountService 는 Drools 에 의존하지 않는다.
(고수준 -> 저수준 의존에서 저수준 -> 고수준 의존으로 바뀌게 된다.)

## 2.3.1 DIP 주의사항
DIP 를 잘못생각하면 단순히 인터페이스와 구현 클래스를 분리하는 정도로 받아들일 수 있다.
-> 저수준 모듈의 관점에서 인터페이스를 추출하게 되면, 결국에는 고수준 모듈이 저수준 모듈을 의존할 수 밖에 없다.

DIP 를 적용할 때, 하위 기능을 추상화한 인터페이스는 **고수준 모듈 관점에서 도출해야 한다.**

## 2.3.2 DIP와 아키텍처
![image](https://user-images.githubusercontent.com/52458039/188603091-a38c62c4-4cac-44cf-9942-a057c82b8038.png)
DIP 를 적용하면 인프라스트럭처 영역이 응용 영역과 도메인 영역에 의존하는 구조가 된다.

> DIP 를 항상 적용할 필요는 없다. (일부는 의존시키는게 효과적이다. & 추상화 대상이 잘 떠오르지 않을 수 있음.) 무조건 DIP 를 적용하려고 시도하지 말고 DIP 의 이점을 얻는 수준에서 적용 범위를 검토하자.


# 2.4 도메인 영역의 주요 구성요소
1. 엔티티 (Entity)
   - 고유 식별자를 가진다.
   - 자신의 라이프 사이클을 갖는다.
2. 밸류(Value)
   - 고유 식별자를 갖지 않는다.
   - 개념적인 하나의 값을 표현한다.
   - 엔티티의 속성, 혹은 다른 밸류의 속성으로 사용된다.
3. 애그리거트(Aggregate)
   - 연관된 엔티티와 밸류 객체를 개념적으로 하나로 묶은 것이다.
   - ex : Order, OrderLine, Orderer -> `주문 애그리거트` 로 묶을 수 있다.
4. 리포지토리(Repository)
   - 도메인 모델의 영속성을 처리한다.
   - ex : DBMS 테이블의 엔티티 객체를 로딩 or 저장
5. 도메인 서비스(Domain Service)
   - 특정 엔티티에 속하지 않은 도메인 로직을 제공한다.
   - ex : 할인 금액 계산을 위해 `상품`, `쿠폰`, `회원 등급`, `구매 등급` 과같이 다양한 조건을 이용해서 구현할 경우, 여러 엔티티와 밸류가 연관된다. 이때 도메인 서비스에서 로직을 구현한다.

## 2.4.1 엔티티와 밸류
** DB 모델과 도메인 모델의 엔티티의 차이
1. 도메인 모델 엔티티는 데이터와 함꼐 도메인 기능을 함께 제공한다.
-> 단순히 데이터를 담고 있는 데이터 구조라기보다 데이터와 함께 기능을 제공하는 객체이다.

이렇게 기능을 엔티티가 담당하기 때문에 기능을 캡슐화하여 데이터의 임의 변경을 막을 수 있다.

2. 도메인 모델 엔티티는 두 개 이상의 데이터가 개념적으로 하나인 경우 밸류 타입을 이용해서 표현할 수 있다.

RDBMS 로는 밸류를 제대로 표현하기 힘들다. (ex : 주문 테이블에 주문자를 어떻게 표현?)
반면, 도메인 모델의 밸류는 개념적으로 하나의 묶음을 의미하기에 명확하게 이해할 수 있다.

> 1장에서 본것과 같이, 밸류는 불변으로 구현할 것을 권장함. 엔티티의 밸류 타입 데이터가 변경된다면, 밸류 객체 자체를 완전히 교체하는 것을 의미한다.

## 2.4.2 애그리거트
도메인이 커질수록 개발할 도메인 모델도 커지고, 많은 엔티티와 밸류가 출현하게 된다.
점점 도메인 모델이 복잡해지게 된다.

도메인 모델이 복잡해지면 개발자가 전체 구조가 아닌 한개 엔티티와 밸류에만 집중하게되는 상황이 발생한다.
-> 도메인 모델을 개별 객체뿐만 아니라 상위 수준에서 모델을 볼 수 있어야 전체 모델의 관계와 개별 모델을 이해하는데 도움이 된다.

도메인 모델에서 전체 구조를 이해하는데 도움이 되는 것이 바로 `애그리거트` 이다.
`애그리거트` : 관련 객체를 하나로 묶은 군집이다. (ex : 주문, 배송지 정보, 주문자, 주문 목록, 총 결제 금액 -> 주문은 `상위 개념`, 나머지는 `하위 개념` 으로 볼 수 있다.)

애그리거트는 군집에 속한 객체를 관리하는 루트 엔티티를 갖는다.
루트 엔티티는 애그리거트에 속해있는 엔티티와 밸류 객체를 이용해서 애그리거트가 구현해야할 기능을 제공한다.

애그리거트를 사용하기 위해, 애그리거트 루트가 제공하는 기능을 실행하고, 애그리거트 루트를 통해 간접적으로 애그리거트 내의 다른 엔티티나 밸류 객체에 접근한다.
-> 애그리거트의 내부 구현을 숨기고, 애그리거트 단위로 구현을 캡슐화할 수 있다.

경우에 따라 애그리거트 루트를 통하지 않고는 다른 엔티티나 밸류 객체를 변경하지 못할 수 있다.

>애그리거트는 구현할때 고려해야할 점이 많다. 구성에 따른 구현의 복잡도가 증가할 수도 있고, 트랜잭션 범위가 달라지기도하고, 선택한 구현 기술에 따라 애그리거트 구현에 제약이 생기기도 함.

## 2.4.3 리포지터리
도메인 객체를 지속적으로 사용하기 위해 로컬파일과 같은 물리 저장소에 도메인 객체를 보관해야 한다. 이를 위한 도메인 모델이 `리포지터리` 이다.
리포지터리는 **애그리거트 단위**로 도메인 객체를 저장하고 조회하는 기능을 정의한다.

# 2.5 요청 처리 흐름
[생략]

# 2.6 인프라스트럭처 개요
인프라 스트럭처는 표현, 응용, 도메인 영역을 지원한다.

DIP 에서 언급한 것 처럼 도메인 영역과 응용 영역에서 인프라스트럭처의 기능을 직접 사용하는 것보다 이 두 영역에 정의한 인터페이스를 인프라스트럭처 영역에서 구현하는 것이 시스템을 더 유연하고 테스트하기 쉽게 만들어 준다.

하지만 무조건 인프라스트럭처에 대한 의존을 없앨 필요는 없다.
대표적인 사례가 다음과 같다.
- 스프링이 제공하는 @Transactional 애노테이션 사용
- JPA 가 제공하는 @Entity, @Table 애노테이션 사용

이를 통해 도메인, 응용 계층은 인프라스트럭처 계층에 일부 의존하지만, 트랜잭션 혹은 XML 매핑 설정을 손쉽게 처리할 수 있다.
따라서 DIP 의 장점을 해치지 않는 범위에서 응용 영역과 도메인 영역에서 구현 기술에 대한 의존을 가져가는 것은 나쁘지 않다.

# 2.7 모듈 구성
우리가 지금까지 봐왔던 계층구조 (4 영역) 아키텍처는 별도 `패키지`에 위치한다.
패키지 구성 규칙에 정답은 없지만 주로 아키텍쳐 각 영역별로 패키지를 구성한다.
만약 도메인이 크다면, 하위 도메인 별로 모듈을 나누고 각 하위 도메인마다 별도 패키지를 구성한다.
각 도메인 모듈에서 도메인에 속한 애그리거트를 기준으로 다시 패키지를 구성한다. (하위 도메인을 하위 패키지로 구성한 모듈로 구성할 수 있다.)

도메인이 복잡하다면 도메인 모델과 도메인 서비스를 별도 패키지에 위치시켜도 된다.
응용 서비스도 도메인 별로 패키지를 구분할 수도 있다.

> 모듈 구조를 어디까지 세분화 해야 할지에 대한 규칙은 없지만, 한 패키지에 너무 많은 타입이 몰리지만 않으면 된다. (최소 10~15개 정도만 유지하도록 한다.)



