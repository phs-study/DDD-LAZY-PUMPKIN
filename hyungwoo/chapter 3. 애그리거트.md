# 3.1 애그리거트
상위 모델에 대한 이해 없이 개별 객체 수준에서 모델을 바라보면 상위 수준에서 관계를 파악하기 어려움.
-> 주요 도메인 요소간의 관계를 파악하기 어렵다는 것은 코드를 변경하고 확장하는 것이 어려워 진다는 의미다.
>세부 모델만 이해하면 코드를 수정하기 꺼려지기 때문에 코드 변경을 최대한 회피하며 요구사항과 타협하며 꼼수를 부리게됨. -> 장기적으로 코드를 더 수정하기 어렵게 만든다.

따라서 복잡한 도메인을 이해하고 관리하기 쉬운 단위로 만들려면 상위 수준에서 모델을 조망할 방법이 필요하다. 바로 `애그리거트` 이다.<br>
**애그리거트** : 관련된 객체를 하나의 군으로 묶어준다. -> 모델 간의 관계를 개별 모델 수준과 상위 수준에서 모두 이해할 수 있다.

![image](https://user-images.githubusercontent.com/52458039/189912116-e311c7fe-266f-42cf-a45f-cb62631366fc.png)

- 모델을 이해하는 데 도움을 준다.
- 일관성을 관리하는 기준도 된다.
    - 따라서 복잡한 도메인을 단순한 구조로 만들어 준다.
- 관련된 모델을 하나로 모았기 때문에 한 애그리거트에 속한 객체는 유사하거나 동일한 라이프 사이클을 갖는다.
    - 즉, 관련객체를 함께 생성해야 한다.
    - 도메인 규칙에 따라 일부 객체를 만들 필요가 없을 수 있지만, 대부분 함꼐 생성하고 함께 제거한다.
- 경계를 갖는다. (독립된 개체군이다.)
    - 자기 자신을 관리할 뿐 다른 애그리거트를 관리하지 않는다.
    - 경계 설정의 기본
        - 도메인 규칙 : 함꼐 생성되는 구성요소는 한 애그리거트에 속할 가능성이 높다.
        - 요구사항 : 사용자 요구사항에 따라 함꼐 변경되는 빈도가 높은 객체는 한 애그리거트에 속할 가능성이 높다.
    - 단순히 A 가 B를 갖는다고, A와 B가 같은 애그리거트라고 할 수 없다. (A와 B의 라이프사이클을 생각해야 한다.)

>도메인에 대한 경험이 생기고 도메인 규칙을 제대로 이해할 수록 애그리거트의 실제 크기는 줄어든다. 경험상, 다수의 애그리거트가 한 개의 엔티티 객체만 갖는 경우가 많았고, 두 개 이상의 엔티티로 구성되는 애그리거트는 드물었다고 함.

# 3.2 애그리거트 루트
도메인 규칙을 지키려면 애그리거트에 속한 모든 객체가 정상 상태를 가져야 한다.
이렇게 애그리거트에 속한 모든 객체를 일관된 상태로 유지하려면 애그리거트 전체를 관리할 주체가 필요하다.
**바로 애그리거트의 루트 엔티티이다. (애그리거트의 대표 엔티티이다.)**


## 3.2.1 도메인 규칙과 일관성
애그리거트 루트의 핵심 역할은 **애그리거트의 일관성이 깨지지 않도록 하는 것이다.**
- 애그리거트 루트는 애그리거트가 제공해야 할 도메인 기능을 구현한다.
    - 애그리거트 루트가 제공하는 메서드는 도메인 규칙에 따라 애그리거트에 속한 객체의 일관성이 깨지지 않도록 구현해야 한다.

애그리거트 루트를 통해서만 도메인 로직을 구현하게 만들려면 다음 두 가지 습관을 적용해야 한다.
- 단순히 필드를 변경하는 set 메서드를 공개(public) 범위로 만들지 않는다.
- 밸류 타입은 불변으로 구현한다.

## 3.2.2 애그리거트 루트의 기능 구현
애그리거트 루트는 애그리거트 내부의 다른 객체를 조합해서 기능을 완성한다.

만약 팀 표준 혹은 구현 기술로 인해 애그리거트 루트의 내부 객체를 불변으로 구현할 수 없다면, 변경 기능을 패키지나 protected 범위로 한정하여 외부에서 실행할 수 없도록 제한하는 방법도 있다.
보통 한 애그리거트에 속한 모델은 한 패키지에 속하기 때문에 외부에서 상태 변경 기능을 실행하는걸 방지할 수 있다.

## 3.2.3 트랜잭션 범위
트랜잭션 범위는 작을수록 좋다.
한 트랜잭션이 여러 테이블을 수정할 수록 잠금 대상이 많아지므로, 그만큼 동시에 처리할 수 있는 트랜잭션 개수가 줄어든다는 걸 의미한다.
따라서 이는 전체적인 성능(처리량)을 떨어뜨린다.

- 한 트랜잭션에서는 한 개의 애그리거트만 수정해야 한다.
    - 한 트랜잭션에 두 개 이상의 애그리거트를 수정하면 트랜잭션 충돌이 발생할 가능성이 높아진다.
    - 성능 저하가 발생하게 된다.
    - 한 트랜잭션에서 한 개의 애그리거트만 수정한다는 것은 애그리거트가 다른 애그리거트를 변경하지 않는다는 것을 의미한다.
    - 만약 한 트랜잭션에 두 개 이상의 애그리거트를 변경해야 한다면 응용 서비스에서 각 애그리거트의 상태를 변경한다. (한 애그리거트안에서 다른 애그리거트를 수정하지 말고!)

>도메인 이벤트를 사용하면 한 트랜잭션에서 한 개의 애그리거트를 수정하면서도 동기나 비동기로 다른 애그리거트의 상태를 변경하는 코드를 작성할 수도 있다.

아래의 경우 한 트랜잭션에서 두 개 이상의 애그리거트를 변경하는 것을 고려할 수 있다.
- 팀 표준 : 팀이나 조직의 표준에 따라 사용자 유스케이스와 관련된 응용 서비스의 기능을 한 트랜잭션으로 실행해야 하는 경우가 있을 경우
- 기술 제약 : 기술적으로 이벤트 방식을 도입할 수 없는 경우 한 트랜잭션에서 다수의 애그리거트를 수정해서 일관성을 처리해야 하는 경우
- UI 구현의 편리 : 운영자의 편리함을 위해 주문 목록 화면에서 여러 주문의 상태를 한 번에 변경하고 싶을 경우, 한 트랜잭션에서 여러 주문 애그리거트의 상태를 변경해야 한다.


# 3.3 리포지터리와 애그리거트
애그리거트는 개념상 완전한 한 개의 도메인 모델을 표현한다.
-> 객체의 영속성을 처리하는 리포지터리는 애그리거트의 단위로 존재한다.

ex : Order 와 OrderLine 각각에 대한 리포지터리를 만들지 않는다. 애그리거트 루트인 Order 를 위한 리포지터리만 존재한다.

애그리거트는 개념적으로 하나이므로 리포지터리는 애그리거트 전체를 저장소에 영속화 해야한다.
마찬가지로 애그리거트를 저장소에서 가져온 경우에도 온전한 애그리거트를 제공해야 한다.

# 3.4 ID 를 이용한 애그리거트 참조

애그리거트는 다른 애그리거트를 참조한다.
애그리거트 관리 주체는 `애그리거트 루트` 이므로 애그리거트가 다른 애그리거트를 참조한다는 것은 다른 애그리거트의 루트를 참조한다는 것과 같다.

```java
public class Order {
    private Orderer orderer;
    // ...
}

public class Orderer {
    private Member member; // 회원 애그리거트 루트인 Member 를 '필드' 로 참조한다.
    private String name;
    // ...
}

public class Member {
    // ...
}
```

필드로 다른 애그리거트를 직접 참조한다는 것은 개발자에게 구현의 편리함을 제공한다.
(JPA 는 `@ManyToOne`, `@OneToONe` 과 같은 애너테이션을 이용해서 연관 객체를 로딩하는 기능을 제공하고 있어서 필드를 이용한 다른 애그리거트를 쉽게 참조할 수 있다.)

하지만 필드를 이용한 애그리거트 참조는 다음 문제를 야기할 수 있다.
- 편한 탐색 오용
    - 한 애그리거트에서 다른 애그리거트의 상태를 변경하기 쉬워지므로 애그리거트 간의 의존 결합도가 높아져 결과적으로 애그리거트의 변경을 어렵게 만든다.
- 성능에 대한 고민
    - JPA 사용 시 애그리거트의 어떤 기능을 사용하느냐에 따라 지연 or 즉시 로딩을 적절히 사용할 줄 알아야 한다.
- 확장 어려움
    - 사용자가 늘고 트래픽이 늘면 더이상 단일 서버, 단일 DBMS 로 서비스를 제공하기 어렵다.
    - 하위 도메인 마다 서로 다른 DBMS 를 사용하거나, 다른 데이터 저장소(ex : nosql)를 사용하게 된다.
        - 더이상 JPA 같은 단일 기술만을 사용할 수 없게 된다.

이런 3 가지 문제를 완화하기 위해, ID 를 이용하여 다른 애그리거트를 참조하는 것이다.
(DB 테이블의 외래키로 참조하는 것과 비슷하다.)

```java
public class Order {
    private Orderer orderer;
    // ...
}

public class Orderer {
    private MemberId memberId; // 회원 애그리거트 루트인 Member 를 '필드' 로 참조한다.
    private String name;
    // ...
}

public class Member {
    private MemberId id;
    // ...
}
```
이를 통해 애그리거트의 경계를 명확히 하고 애그리거트 간 물리적인 연결을 제거해주기 때문에 모델의 복잡도를 낮춰준다. 또, 애그리거트 간의 의존을 제거하므로 응집도를 높여주는 효과도 있다.
구현 복잡도도 낮아진다. (다른 애그리거트 직접 참조가 없어서 JPA 의 경우, 지연 로딩 or 즉시 로딩에 대한 고민을 할 필요 없다.)

## 3.4.1 ID를 이용한 참조와 조회 성능
ID 를 이용하게 되면 JPA 에서 지연 로딩이 되는 꼴이기 때문에, N+1 문제가 발생하게 된다.

이를 해결하려면 객체 참조 & 즉시 로딩을 사용하면 된다. 하지만 이는 객체 참조로 다시 돌아가는 꼴이 된다.
```java
@Repository
public class JpaOrderViewDao implements OrderViewDao {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public List<OrderView> selectByOrderer(String ordererId) {
      String selectQuery =
              "select new com.myshop.order.application.dto.OrderView(o, m, p) " +
                      "from Order o join o.orderLines ol, Member m, Product p " +
                      "where o.orderer.memberId.id = :ordererId " +
                      "and o.orderer.memberId = m.id " +
                      "and index(ol) = 0 " +
                      "and ol.productId = p.id " +
                      "order bhy o.number.number desc";
      TypedQuery<OrderView> query = em.createQuery(selectQuery, OrderView.class);
      query.setParameter("ordererId", ordererId);
      return query.getResultList();
    }
}
```
따라서 ID 참조 방식을 사용하면서 N+1 문제를 해결하려면 `조회 전용 쿼리`를 사용하면 된다. (관련 테이블을 조인해 한번의 쿼리로 필요한 데이터를 로딩한다.)

** 애그리거트 마다 서로 다른 저장소를 사용하면, 한 번의 쿼리로 관련 애그리거트를 조회할 수 없다. 따라서 조회 성능을 높이기 위해 **캐시**를 적용하거나 **조회 전용 저장소**를 따로 구성한다.
>한 대의 DB 장비로 대응할 수 없는 수준의 트래픽이 발생하는 경우 캐시나 조회 전용 저장소는 필수로 선택해야 하는 기법이다.


# 3.5 애그리거트 간 집합 연관
애그리거트 간에 1 : N, M : N 연관이 있을 수 있다. (이 두 연관은 Collection 을 이용한 연관이다.)

```java
public class Category {
    private Set<Product> products; // 다른 애그리거트에 대한 1 : N 연관
}
```
1 : N 관계는 Set 과 같은 컬렉션을 이용해서 표현할 수 있다.
but, 개념적으로 1 : N 연관이 있더라도 성능 문제 때문에 애그리거트 간의 1 : N 연관을 실제 구현에 반영하지 않는다. (N 의 모든 데이터를 메모리에 퍼올리면 성능 저하 발생하기 때문)

M : N 의 경우에도 실제 양방향 관계이지만, 요구사항에 따라 실제 구현에는 단방향 M : N 연관만 봐야할 수도 있다.
RDBMS 관점에서 M : N 연관을 구현하려면 조인 테이블을 사용하면 된다.

>목록이나 상세 화면과 같은 조회 기능은 조회 전용 모델을 이용해서 구현하는 것이 좋다.


# 3.6 애그리거트를 팩토리로 사용하기
특정 상점이 상품을 등록하는 것을 생각해보자.
```java
public class RegisterProductService {
  public ProductId registerNewProduct(NewProductRequest req) {
    Store store = storeRepository.findById(req.getStoreId());
    checkNull(store);
    if (stoer.isBlocked()) {
        throw new StoreBlockedException();
    }
    ProductId id = productRepository.nextId();
    Product product = new Product(id, store.getId() /* ... 생략 */);
    productRepository.save(product);
    return id;
  }
}
```

Product 생성 가능 여부는 도메인 규칙인데, 응용 서비스에 노출되고 있다.

이를 감추기 위해 별도의 `도메인 서비스`나 `팩토리 클래스`를 만들수도 있지만 Store 애그리거트에서 만들 수도 있다.

```java
public class Store {
    public Product createProduct(ProductId newProductId /* ... 생략*/) {
      if (isBlocked()) {
        throw new StoreBlockedException();
      }
      return new Product(newProductId, getId() /* ... 생략 */);
    }
}
```

이렇게 Store 애그리거트에서 Product 애그리거트를 생성하는 팩토리 역할을 만들 수 있다. (팩토리 역할과 주요 도메인 로직을 구현하게 만든다.)

```java
public class RegisterProductService {
  public ProductId registerNewProduct(NewProductRequest req) {
    Store store = storeRepository.findById(req.getStoreId());
    checkNull(store);
    ProductId id = productRepository.nextId();
    Product product = store.createProduct(id/* ... 생략 */);
    productRepository.save(product);
    return id;
  }
}
```
이제 응용 서비스 로직은 더이상 도메인 규칙이 노출되지 않는다.
도메인의 응집도도 높아지게 된다.

만약 Store 애그리거트가 Product 애그리거트를 생성할 때, 많은 정보를 알아야 한다면 다른 팩토리에 위임하는 방법도 있다.
```java
public class Store {
    public Product createProduct(ProductId newProductId, ProductInfo pi) {
      if (isBlocked()) {
        throw new StoreBlockedException();
      }
      return ProductFactory.create(newProductId, getId(), pi);
    }
}
```
