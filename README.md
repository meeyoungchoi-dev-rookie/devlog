# 블로그 프로젝트 

## version 01 환경설정
+ spring boot
+ oracle
+ jdbc
+ thymeleaf
+ bootstrap

## version 01 목표
+ 게시글 CRUD
+ 댓글 CRUD
+ 테스트 코드 작성

## version 01 작업 진행 내용
+ jdbc 사용하여 오라클 데이터베이스 연결 설정 및 테스트
+ 게시글 테이블 생성
+ 게시글 CRUD 작업 진행을 위한 리파지터리 생성
+ 리파지터리 CRUD 테스트 코드 작성
+ 게시글 서비스 CRUD 테스크 코드 작성
+ 게시글 수정시 엔티티에 update 메서드를 만들어 조회해온 객체의 데이터를 변경하는 방식으로 메서드 설계

### 발생했던 에러
1. JDBC를 사용하여 테이블에 insert하기 위한 쿼리 생성시 board_no 시퀀스를 넣어주는 방법을 찾지 못했다
```roomsql
  String sql = "insert into boards(board_no, title, content , user_id, board_created_at, board_updated_at) VALUES(board_seq.NEXTVAL ,? ,? ,? ,? ,?)";

```

2. 게시글 저장시 날짜를 같이 저장하는데 타입이 sql.date 타입을 처리하는 중 타입이 일치하지 않는다는 에러가 발생했다
- 원인
- util.date 타입을 sql.date 타입으로 변환하지 않고 그대로 insert 하려고 했기 때문에 에러가 발생했다
- 해결과정
  1. Date 객체를 생성하여 현재 날짜와 시간을 가져온다
  2. getTime 메서드를 통해 Date를 밀리세컨드로 변환하여 숫자 데이터로 변환한다
  3. sql.Date 타입으로 변환해 준다
```java
Date insertDate = new Date();
Long timeInMilliSeconds = insertDate.getTime();
java.sql.Date date = new java.sql.Date(timeInMilliSeconds);
```


3. 삭제 처리가 잘 되었는지 테스트하기위해 예외가 발생하는지 확인하는 방법
- 원인
- 삭제 처리가 완료된후 id를 사용하여 다시 데이터를 조회하면 예외가 발생한다
- 예외가 발생하는지 확인하기 위한 테스트 코드를 작성하던 중 예외가 생성되지 않았다는 에러가 발생했다
```java
java.lang.AssertionError: Expecting code to raise a throwable.
```

- 해결과정
1. 조회하는 메서드에서 조회된 결과값이 없을 경우 NoSuchElementException 예외를 던지도록 코드 수정
2. 삭제가 잘되었는지 검증하기 위해 assertThatThrownBy 메서드를 사용하여 삭제된 id를 사용하여 조회했을때 발생한 예외 타입이 NoSuchElementException 타입과 일치하는지 검증하는 테스트 코드 작성
