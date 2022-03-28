# spring-mvc-1  
스프링 MVC 1 - 상품 등록 예제를 통한 스프링 MVC 정리                 
강의 : [스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술, 김영한 강사님](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1)            

## Project Setting              
* Spring boot version : 2.6.5                   
* Dependencies
  - Spring Web
  - Thymeleaf
  - Lombok                    
                    
## README 정리 방식                 
실습 내용 중 중요하다 생각하는 부분 / 새롭게 알게 된 부분에 대한 내용을 기록       
* 이론과 관련한 부분은 pdf 기반으로 공부. 해당 repository에는 실습 관련 개인 공부용 정리             
* 해당 이론과 관련한 실습 코드는 커밋 링크          
          
------------------                   

### [welcome page 추가](https://github.com/HunSeongPark/spring-mvc-1/commit/5fad6399a7c54812d8fc49fc41e4108cafdead28)                
- `/resources/static/index.html` 경로에 해당하는 index.html의 경우 기본 페이지로 지정된다.        

### [ThymeLeaf](https://github.com/HunSeongPark/spring-mvc-1/commit/0309074295abc3f70e6f7352f6348af1129c2488)
- 사용 선언
  - `<html xmlns:th="http://www.thymeleaf.org">`
- HTML 속성 변경
  - ex) href 변경
  - `th:href="@{/css/bootstrap.min.css}"`
  - 기본 순수 HTML 속성인 `href=A`를 `th:href=B`로 변경
  - HTML을 그대로 거칠 때는 기본 href 속성 사용, Thymeleaf 템플릿을 거칠 때는 th:href 값으로 대체되며 동적인 변경 가능
  - 그래서 Thymeleaf는 순수 HTML을 웹 브라우저에서 열어도 확인 가능하며, 서버를 통해 Thymeleaf 템플릿을 거쳐 동적으로도 확인 할 수 있는 *Natural Templates*라고 한다.
- URL 링크 표현식
  - `@{link}`와 같은 문법을 가짐
  - ex) `th:href="@{/css/bootstrap.min.css}"`
  - Path Variable과 같은 형태로도 표현식을 쓸 수 있다.
  - ex) `th:href="@{/basic/items/{itemId}(itemId=${item.id})}"`
  - 쿼리 파라미터도 생성 할 수 있다.
  - ex) `th:href="@{/basic/items/{itemId}(itemId=${item.id}, query='test')}"`
    - 결과 : `basic/items/1?query=test`
- 리터럴 대체 
  - `|...|`와 같은 문법을 가짐
  - 문자와 표현식을 + 연산을 통해 사용할 필요없이 한번에 사용 가능
  - ex) `th:onclick="|location.href='@{/basic/items/add}'|"`
  - 위와 같이 문자 `location.href=`와 표현식 `@{/basic/items/add}`를 리터럴 대체를 통해 한번에 사용할 수 있다.
- 변수 표현식
  - `${item.price}`와 같은 문법을 가짐
  - model에 포함된 값 또는 Thymeleaf 변수로 선언한 값을 조회 할 수 있다.
  - 프로퍼티 접근법을 통해 `item.price`와 같이 사용할 수 있다.
- 반복 출력
  - `th:each="item : ${items}"`와 같은 문법을 가짐
  - items 컬렉션에 포함된 데이터 item을 반복문을 돌며 사용
  - ex) `<tr th:each="item : ${items}"`
  - 위와 같이 테이블에 items 컬렉션 내 item의 값을 반복 출력을 통해 보여줄 수 있다.

### [@RequiredArgsConstructor](https://github.com/HunSeongPark/spring-mvc-1/commit/0309074295abc3f70e6f7352f6348af1129c2488)
- Lombok에서 제공하는 어노테이션
- final 키워드가 붙은 멤버변수에 대한 Constructor 자동 생성
- Constructor가 하나만 있으면 Spring이 해당 생성자에 대해 @Autowired 어노테이션을 달아 의존관계 주입
```java
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;
    
    // *** @RequiredArgsConstructor에 의해 모두 생략됨 ***
    // @Autowired
    // public BasicItemController(ItemRepository itemRepository) {
    // this.itemRepository = itemRepository;
    // }
```                 

### [@ModelAttribute](https://github.com/HunSeongPark/spring-mvc-1/commit/b287fcff9c77a99c5b3db68d2a772496865b6994)
- Request Parameter를 객체에 담을 수 있는 방법
- 프로퍼티 접근법을 통해 Request Parameter의 값들을 객체의 프로퍼티에 자동으로 세팅한다.
```java
public String addItemV2(@ModelAttribute Item item) {
  ...
}
```
- @ModelAttribute 어노테이션은 model에 해당 객체를 자동으로 넣어주는 기능을 수행한다.
- 이 때 이름은 `@ModelAttribute("name")`과 같이 지정하여 꺼낼 수 있으며, 생략 시 해당 객체의 변수명으로 지정된다.(`@ModelAttribute Item item`일 경우 "item")
- 심지어는 @ModelAttribute도 생략 가능하다. wow
```java
@PostMapping("/add")
    public String addItemV3(Item item) {
        itemRepository.save(item);
//        model.addAttribute("item", item); // 생략 가능

        return "basic/item";
    }
```
