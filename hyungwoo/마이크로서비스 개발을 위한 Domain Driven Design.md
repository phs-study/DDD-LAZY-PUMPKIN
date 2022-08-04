# 마이크로서비스 개발을 위한 Domain Driven Design

![Untitled.png (946×262)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/c7b51b47-4058-4745-92c4-9c5565947145/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050133Z&X-Amz-Expires=86400&X-Amz-Signature=cfac029e03d30468e598508f9597167941da35eafe7227b4544b39eef2e02e31&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

- 과거형/수동형 을 사용한다.

![Untitled.png (942×146)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/9e233003-0495-4570-bf8a-1e0671a7a548/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050206Z&X-Amz-Expires=86400&X-Amz-Signature=b6a6fb46f3ae64a5a3214f4e47bc3b83d1e910131e7117af6710ff0afff0f24f&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

- 이벤트의 source가 된다.

- ![Untitled.png (966×448)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/94382963-999a-428c-b9ec-3d0c3f9e0509/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050221Z&X-Amz-Expires=86400&X-Amz-Signature=20aa81df12b818b53c36c1339bbb21f111f00ecff0e7e51e716df3e6e7ec555c&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

- 특정 Command가 Entity나 Aggregate에 영향을 줘서 Object가 Event를 갖게 된다.
- Aggregate 끼리는 객체 참조하지 않고 id로 참조한다. (tightly coupled 가 아닌, loosely coupled 되어 있다.)

![Untitled.png (742×421)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/32ee2deb-1b85-48a2-bf20-6f7fb9b5ee57/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050235Z&X-Amz-Expires=86400&X-Amz-Signature=d06509bbdf527dd88b946e259aa8b9c8a4fea0239d5bc7bab1b1f96cc3529eae&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

1. 아이템을 장바구니어 넣으라는 명령을 내린다.
2. 명령은 아이템이라는 Aggregate에 영향을 미침.
3. 이 아이템은 명령에 의해 카트에 들어가게 됨. 
    - Aggregate / Event는 항상 같이 일어난다.
    - Aggregate의 state 변화가 있으면 Event가 발생한다.
    - Event가 발생하면 Aggregate의 state 변화가 생긴다.)

(Command → Aggregate → Event 의 Flow를 가진다.)

![Untitled.png (754×433)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/d7f04367-9322-4e91-93aa-7db4531cc155/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050245Z&X-Amz-Expires=86400&X-Amz-Signature=10fdb612cbe4ba155c36accbdf6a9a423cfaf92a9f00c1cc5a3647c3ae8266c3&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

이 이벤트는 Time Series로 시계열 로 표현한다. (오른쪽으로 갈 수록 나중 일이 된다.)

![Untitled.png (815×455)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/d2b84bef-8e07-4013-ba02-f63644669136/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050312Z&X-Amz-Expires=86400&X-Amz-Signature=d41b5cba15c1d9829ee9e09753f8d1ffda4f1b5770ed7684b51812d61e1b33b0&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

Command와 Event도 항상 짝으로 이루어 진다. (경우에 따라 Command가 명확해서 생략하는 경우도 있음)

![Untitled.png (724×514)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/4d96d477-bfae-4ad4-bd25-16b242c60179/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050329Z&X-Amz-Expires=86400&X-Amz-Signature=2fb951c3d149d370615f69faf384862f4370be7983bfaa89adefff62cc7a49f9&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

이렇게 Command-Aggregate-Event가 하나의 묶음으로 진행된다.

## Domain Driven Design (설계 과정)

1. Event Storming (Command - Aggregate - Event 단위 정의)

![Untitled.png (608×201)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/769afdb4-edb2-4986-b0a6-ca0d81cceb4e/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050438Z&X-Amz-Expires=86400&X-Amz-Signature=c37d7ee265cb4000e5b5d3d5d6481c5d876c4823034c2ea4267f3e5acba82ef3&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

2. Grouping (Aggregate 그룹핑)

   ![image-20220804140528055](/Users/wu2ee/Library/Application Support/typora-user-images/image-20220804140528055.png)

3. Boris diagram (서비스간 통신)

![Untitled.png (824×657)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/8e184085-e9ae-4cf6-8c10-901fba494d2f/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050920Z&X-Amz-Expires=86400&X-Amz-Signature=c06a5937a82ddd5839bed9aaf013f652efd1ad151e193088947eae2b7af9bf3f&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

4. SNAP-E (디테일한 스펙)

![Untitled.png (422×618)](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/d60b0797-bf23-4b7d-bcc9-d4cd4e81ac57/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220804%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220804T050929Z&X-Amz-Expires=86400&X-Amz-Signature=d2aa37d3c598c5921777174fc979c4f484ad624cfb419a880cbf0ef6e8a8d8d6&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

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
