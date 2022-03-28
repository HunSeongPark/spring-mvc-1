# spring-mvc-1  
스프링 MVC 1 - 상품 등록 예제를 통한 스프링 MVC 정리                 
강의 : [스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술, 김영한 강사님](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1)            

## Project Requirements
- Domain model  
  - 상품 ID `id`
  - 상품 이름 `itemName`
  - 가격 `price`
  - 수량 `quantity`               
                
- Features
  - 상품 목록 `/basic/items`
  - 상품 상세 `/basic/items/{itemId}`
  - 상품 등록 `/basic/items/add`
  - 상품 수정 `/basic/items/{itemId}/edit`

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
- Query Parameter
  - Query Parameter의 값은 다음과 같이 받아올 수 있다.
  - ex) `status=true` query를 받아오고자 할 때 => `${param.status}`       
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

### [PRG(Post-Redirect-Get) 와 RedirectAttributes](https://github.com/HunSeongPark/spring-mvc-1/commit/26ffd7a7c96bffc2c1c87e135ec1d1fa1ea30a65)
#### PRG(Post-Redirect-Get)
- 웹 브라우저의 새로고침은 마지막에 서버에 전송한 데이터를 다시 전송한다. (= 마지막 Request를 다시 수행)
- 만약 POST 메서드 수행 후 새로고침 시 중복된 POST 요청이 일어난다.
- 상품 등록을 예로 들었을 때, POST /add를 통해 상품을 등록하고 새로고침을 누를 때마다 POST /add가 반복 요청되어 상품이 중복으로 등록되는 문제가 발생한다.
- 이러한 문제를 해결하기 위해 POST(상품 등록) 이후 GET(상품 상세화면)으로 Redirect를 호출해주는 방법을 PRG(Post-Redirect-Get)이라고 한다.
- PRG를 적용하면 POST(상품 등록) 이후 GET(상품 상세화면)으로 Redirect 되어 새로고침을 누를 때마다 POST가 아닌 GET이 요청되어 POST의 중복 문제가 발생하지 않는다.
- 스프링은 `redirect:/...`를 통해 Redirect를 지원한다.
- POST(상품 등록) 이후 GET(상품 상세화면)으로 Redirect하는 방식은 아래와 같다.
```java
@PostMapping("/add")
    public String addItemV11(Item item) {
        Item savedItem = itemRepository.save(item);
        return "redirect:/basic/items/" + savedItem.getId();
    }
```
- 그러나 위와 같은 방식은 URL에 `+ savedItem.getId()`와 같이 변수를 더해서 사용하는 방식이므로, URL 인코딩이 되지 않아 위험한 방식이다.
- 이를 해결하기 위해서는 `RedirectAttributes`를 사용해야 한다.
#### RedirectAttributes
- RedirectAttributes를 사용하면 위에서 발생한 URL 인코딩의 문제를 해결하고 PathVariable, Query Parameter 등의 지원이 가능하다.
```java
@PostMapping("/add")
    public String addItemV4(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
    }
```
- 위 코드의 redirect 결과는 다음과 같다.
  - `/basic/items/3?status=true`                
