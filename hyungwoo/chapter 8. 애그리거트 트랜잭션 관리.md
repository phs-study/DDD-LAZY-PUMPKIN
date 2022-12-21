# 8.1 애그리거트와 트랜잭션
- 한 애그리거트를 두 사용자가 동시에 변경할 때 트랜잭션이 필요하다.
- 두 스레드가 동시에 쓰기 작업을 진행할 경우 일관성이 깨지는 문제가 발생한다.
- 일관성이 깨지지 않도록 하기 위해 다음 두 가지 중 하나를 해야 한다.
  - a 스레드가 정보를 조회하고 쓰기 작업을 진행할 동안 b 스레드의 쓰기 작업을 막는다.
  - a 스레드가 정보를 조회하고 쓰기 작업을 진행하기 전, b 스레드가 쓰기작업을 진행한다면, a 스레드는 정보를 다시 조회 후 쓰기작업을 진행한다.

이 방법을 적용하기 위해서 DBMS 가 지원하는 트랜잭션과 함께 애그리거트를 위한 추가적인 트랜잭션 처리 기법이 필요하다.
- 선점 잠금
- 비선점 잠금

# 8.2 선점 잠금 (Pessimistic Lock)
- 애그리거트를 먼저 조회한 스레드가 애그리거트 사용이 끝날 때까지 다른 스레드가 해당 애그리거트를 수정하지 못하게 막는 방식이다.
1. 스레드 a가 애그리거트를 선점 잠금 방식으로 조회한다.
2. 스레드 b가 애그리거트를 조회하면 스레드 a 에서 애그리거트에 대한 잠금이 해제할 때까지 블로킹된다.
3. 스레드 a 에서 쓰기 작업을 수행후 트랜잭션을 커밋하면서 잠금을 해제한다.
4. 대기하고 있던 스레드 b가 애그리거트에 접근한다.
5. 스레드 a 에서 애그리거트의 변경 내역이 스레드 b에서 반영된 애그리거트를 조회하게 된다.

- 선점 잠금은 보통 DBMS 가 제공하는 **행단위 잠금**을 사용해서 구현한다.
  - 오라클과 같은 대부분의 DBMS 는 for update 와 같은 쿼리를 사용해서 특정 레코드에 한 커넥션 접근할 수 있는 잠금 장치를 제공한다.

- JPA 에서는 find() 메서드에 LockModeType 을 인자로 받아서 선점 잠금 방식을 적용한다.
```java
Order order = entityManager.find(Order.class, orderNo, LockModeType.PESSIMISTIC_WRITE);
```

- 하이버네이트에서 PESSIMISTIC_WRITE 를 잠금 모드로 사용하면 `for update` 쿼리로 선점 잠금을 구현한다.
- @Lock 애너테이션으로 잠금 모드를 지정할 수 있다.
```java
public interface MemberRepository extends Repository<Member, MemberId> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Member m where m.id = :id")
    Optional<Member> findByIdForUpdate(@Param("id") MemberId memberId);
}
```

## 8.2.1 선점 잠금과 교착 상태(DeadLock)

- 선점 잠금 방식을 사용할 때 잠금 순서에 따른 교착 상태가 발생하지 않도록 주의해야 한다.

1. 스레드 1 : A 애그리거트에 대한 선점 잠금 구함
2. 스레드 2 : B 애그리거트에 대한 선점 잠금 구함
3. 스레드 1 : B 애그리거트에 대한 선점 잠금 시도
4. 스레드 2 : A 애그리거트에 대한 선점 잠금 시도

- 스레드 2가 B 애그리거트에 대한 선점 잠금을 구했기 때문에 스레드 1은 영원히 B 애그리거트에 대한 선점 잠금을 구할 수 없다. -> `교착상태`
- 이 문제를 해결하기 위해 잠금을 구할 때 최대 대기 시간을 지정해야 한다.
- JPA 에서는 `힌트` 를 사용한다.
```java
Map<String, Object> hints = new HashMap<>();
hints.put("javax.persistence.lock.timeout", 2000);
Order order = entityManager.find(Order.class, orderNo, LockModeType.PESSIMISTIC_WRITE, hints);
```

- 지정한 시간 이내에 잠금을 구하지 못하면 예외가 발생한다.
- 이 힌트는 DBMS 에 따라 힌트가 적용되지 않을 수 있으므로 DBMS 가 관련 기능을 지원하는지 확인 해야 한다.
- @QueryHints 애너테이션을 사용해서 쿼리 힌트를 지정할 수 있다.
```java
public interface MemberRepository extends Repository<Member, MemberId> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({
            @QueryHint(name = "javax.persistence.lock.timeout", value = "2000")
    })
    @Query("select m from Member m where m.id = :id")
    Optional<Member> findByIdForUpdate(@Param("id") MemberId memberId);
}
```

> DBMS 에 따라 교착 상태에 빠진 커넥션 처리방식이 다르다. ex) 쿼리별로 대기시간 지정 or 커넥션 단위로만 대기 시간 지정 -> 따라서 DBMS 에 대해 JPA 가 어떤 식으로 대기 시간을 처리하는지 반드시 확인해야 한다.

# 8.3 비선점 잠금 (Optimistic Lock)
- 동시에 접근하는 것을 막는 대신 변경한 데이터를 실제 DBMS 에 반영하는 시점에 변경 가능 여부를 확인하는 방식이다.
- 애그리거트에 버전으로 사용할 숫자 타입 프로퍼티를 추가해야 비선점 잠금을 구현할 수 있다.
- 애그리거트를 수정할 때마다 버전으로 사용할 프로퍼티 값이 1 씩 증가한다.
```sql
UPDATE apptable SET version = version + 1, colx = ?, coly = ?
WHERE aggid = ? and version = 현재버전
```

- update 시 버전이 일치해야한 데이터를 수정할 수 있다.
- 응용 서비스는 버전에 대해 알 필요가 없다.
- 비선점 잠금이 걸린 상태에서 트랜잭션 충돌이 발생하면 `OptimisticLockingFailureException` 이 발생한다.
- 트랜잭션 충돌 해소를 위해 데이터의 버전을 함께 줘서 (form 데이터 형식에 추가), 서버로의 요청을 보낼 때 버전을 함께 줘서 버전이 같은 경우에만 수정기능을 수행하도록 로직을 구성할 수 있다.
- 이를 통해 트랜잭션 충돌이 발생한 시점을 명확하게 구분할 수 있다.
  - VersionConflictException -> 누군가 애그리거트를 수정함
  - OptimisticLockingFailureException -> 누군가와 거의 동시에 애그리거트를 수정함

## 8.3.1 강제 버전 증가
- JPA 에서는 애그리거트 루트가 아닌 다른 엔티티의 값이 변경되면 루트 엔티티의 버전 값은 갱신하지 않는다.
- 애그리거트 관점에서는 위 특징이 문제가 된다. (논리적으로 함께 버전이 업데이트 되야 한다.)
- 따라서 루트 엔티티 값이 바뀌지 않았더라도 애그리거트 구성요소 중 일부 값이 바뀌면 루트 애그리거트의 버전도 증가해야 비선점 잠금이 올바르게 동작한다.
- JPA 에서는 강제로 버전 값을 증가시키는 잠금 모드를 지원한다.

```java
@Repository
public class JpaOrderRepository extends OrderRepository {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Ovveride
    public Order findByIdOptimisticLockMode(OrderNo id) {
        return entityManager.find(
                Order.class, id, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        );
    }
}
```
- Spring data JPA 에서는 @Lock 애너테이션을 이용해서 지정하면 된다.

# 8.4 오프라인 선점 잠금
- 엄격하게 데이터 충돌을 막고 싶다면 누군가 수정 화면을 보고 있을 때 수정 화면 자체를 실행하지 못하도록 해야 한다.
- 이는 선점 잠금 방식 or 비선점 잠금 방식으로 구현할 수 없다.
- 이때 오프라인 선점 잠금 방식을 사용한다.
  - 여러 트랜잭션에 걸쳐 동시 변경을 막는다.
  - 첫 번째 트랜잭션을 시작할 때 오프라인 잠금을 선점하고, 마지막 트랜잭션에서 잠금을 해제한다. 잠금을 해제하기 전까지 다른 사용자는  잠금을 구할 수 없다.
  - 만약 첫번째 트랜잭션이 종료되기 전에 프로그램이 종료되면 잠금을 해제하지 않으므로 다른 사용자는 영원히 잠금을 구할 수 없는 상황이 발생한다.
  - 따라서 오프라인 선점 방식은 잠금 유효 시간을 가져야 한다.

## 8.4.1 오프라인 선점 잠금을 위한 LockManager 인터페이스와 관련 클래스
- 오프라인 선점 잠금은 크게 4가지 기능이 필요하다.
  - 잠금 선점 시도
  - 잠금 확인
  - 잠금 해제
  - 잠금 유효시간 연장

- 서비스 코드로 반드시 다음 유효성 검사를 실행 해야 한다.
  - 잠금 유효 시간이 지났으면 이미 다른 사용자가 잠금을 선점한다.
  - 잠금을 선점하지 않은 사용자가 기능을 실행했다면 기능 실행을 막아야 한다.


## 8.4.2 DB 를 이용한 LockManager 구현
패스