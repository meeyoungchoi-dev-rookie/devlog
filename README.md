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
+ 게시글에 달린 댓글 전체 조회 테스트
+ 댓글을 계층형으로 달수 있도록 그룹 , 깊이 , 부모댓글번호 컬럼을 댓글 테이블에 추가
+ 댓글 리파지터리 CRUD 설계
+ 댓글 리파지터리 CRUD 테스트 코드 작성
+ 계층형 댓글 insert 테스트 코드 작성
+ 계층형으로 댓글 조회 테스트 코드 작성
+ 계층형 댓글 수정 테스트 코드 작성
+ 계층형 댓글 삭제 테스트 코드 작성
+ 계층형 댓글 CRUD 비즈니스 로직 기능 설계
+ 계층형 댓글 CRUD 비즈니스 로직 테스트 코드 작성 및 테스트 진행
+ 계층형 댓글 비즈니스 로직에 트랜잭션 적용
+ 댓글 컨트롤러 REST API 설계
+ POST MAN 사용하여 댓글 컨트롤러 REST API 테스트
+ MockMvc 객체 사용하여 댓글 컨트롤러 기능 테스트


### 계층형 댓글 테이블 설계 
1. 필요한 데이터
+ 댓글번호 , 부모번호 , 깊이 , 그룹번호
+ 부모번호는 댓글에 답글이 달릴때 답글이 어떤 댓글에대한 것인지 파악하기 위해 사용
+ 깊이는 계층형 댓글에서 나중에 달린 댓글일수록 화면에 보여질때 깊이가 달라진다
+ 그룹번호는 가장 상위 댓글의 PK를 사용했다
+ 왜?
+ 게시글에 달린 모든 댓글을 조회할때 같은 그룹에 속한 모든 댓글을 한번에 편리하게 조회해오기 위해 사용했다

2. 계층형으로 댓글을 정렬하기 위한 과정
+ 댓글이 속한 그룹으로 오름차순 정렬한다
+ 댓글의 깊이로 오름차순 정렬한다
+ 댓글 번호로 내림차순 정렬한다

### 계층형 댓글 삭제에 대한 고민
- 계층형 댓글을 삭제시 하위 계층 댓글 구조에 영향을 줄수 있다고 판단
- 따라서 삭제버튼을 클릭한 댓글의 내용을 "삭제된 댓글입니다" 로 수정
- 추후 클라이언트단에서 보여줄떄 해당 No의 댓글이 표시될 계층 자리에 "삭제된 댓글입니다"로 표시되도록 하기 위함

### 코드 리펙토링 
1. 테스트 코드 리펙토링
+ 게시글 CRUD 테스트시 boardNo가 PK여서 테스트를 돌릴때 마다 PK를 변경한후 테스트를 진행해야 했다
+ 해결과정
+ boardNo 필드를 static으로 선언하고 임의의 값을 설정해 뒀다
+ crud 테스트를 진행할 때 boardNo 필드를 사용하여 테스트를 한다
+ 테스트가 종료되면 @AfterEach 어노테이션으로 DB 테이블에 저장된 임의의 boardNo row를 삭제시켜 줬다
+ 이를 통해 테스트가 종료될때 마다 PK row가 삭제되기 때문에 boardNo를 변경하지 않고도 반복적으로 테스트를 진행할 수 있게 되었다


2. 컨트롤러에서 JSON 객체를 자바 DTO로 받는 객체를 ArticleRequest 클래스로 통일
+ 이전 코드에서는 insert용 객체 따로 , update 용 객체 따로 분리되어 있었다
+ 이를 하나의 DTO로 처리하도록 ArticleRequest 객체로 통일했다


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

----------------------------------------------------------------------------
4. DB 서버에 접속하기 위한 USERNAME 과 PASSWORD 정보가 설정되어 있지 않아 애플리케이션 서버에서 DB 서버에 로그인할 수 없다는 에러가 발생했다
- 해결과정
+ application.yaml에 DB username과 password 정보를 설정해 줬다


--------------------------------------------------------------------------
5. 오라클 DB 서버에서 게시글 테이블의 row를 delete 한후 커밋을 하지 않아 애플리케이션 서버가 DB 서버에 쿼리를 날리지못하는 에러가 발했다
- DB 서버에서 트랜잭션이 완료되어야 애플리케이션 서버에서 커넥션을 연결하여 사용할 수 있다
- 그런데 DB 서벅에서 커밋을 해주지 않아 커넥션이 커넥션 풀에 반환되지 않아 애플리켕션 서버가 DB 서버에 명령을 줄 수 없었다
- 해결과정
+ DB 서버에서 작업한 내용을 커밋한 후 애플리케이션 서버를 재시작 시켰다

--------------------------------------------------------------------------
6. 댓글 테이블이 게시글 번호를 FK로 갖고 있는데 댓글을 삭제하지 않고 게시글을 먼저 삭제하려고 했기 때문에 FK가 있어 삭제할수 없다는 에러가 발생했다
+ ORA-02292: integrity constraint (ADMIN.COMMENT_BOARD_NO_FK) violated - child record found
+ FK인 댓글을 먼저 삭제한후 게시글을 삭제해 줌으로써 에러를 해결했다

7. update 쿼리시 where 전에 ,(콤마)가 붙어 있어 에러가 발생했다
+ 오라클 쿼리 오류(ORA-01747: invalid user.table.column, table.column, or column specification)
+ where 구문 시작전에 붙은 ,(콤마)를 삭제해 줬다

### SQL 관련 발생했던 예외
####오라클 sql ORA-00904 : 부적합한 식별자 invalid identifier
+ insert와 select 쿼리를 날릴때 발생했다
+ comments 테이블에 존재하지 않는 컬럼을 insert 및 조회하려고 해서 발생했다
#### 인덱스에서누락된IN또는OUT매개변수:
+ update 쿼리를 날릴때 발생했다
+ 쿼리파라미터의 개수가 부족해서 ? 개수와 맞지 않았다
#### java.sql.SQLException: 부적합한 열 이름
+ 테이블에 존재하지 않는 컬럼명을 입력했기 때문에 발생했더
+ 테이블의 컬럼명과 jdbc sql문의 컬럼명을 맞춰줬다
#### Error code - ORA-00001 : 유일성 제약조건에 위배됩니다 (unique constraint violated)
+ 테이블에 데이터 insert시 PK 컬럼은 고유한 값을 가져야 하는데 PK 컬럼이 중복되서 에러가 발생했다
#### 오라클 sql ORA-00933 : SQL COMMAND NOT PROPERLY ENDED
+ sql 구문이 올바르게 종료되지 않아서 발생했다
+ ; (세미콜론을 뺴먹었다)

#### ORA-02292: integrity constraint (ADMIN.COMMENT_BOARD_NO_FK) violated - child record found
+ 댓글 테이블이 게시글 번호를 FK로 갖고 있는데 댓글을 삭제하지 않고 게시글을 먼저 삭제하려고 했기 때문에 FK가 있어 삭제할수 없다는 에러가 발생했다
+ FK에 해당하는 테이블을 row를 먼저 삭제하고 FK를 참조하는 부모 테이블의 row를 삭제해 줬다

#### 오라클 쿼리 오류(ORA-01747: invalid user.table.column, table.column, or column specification)
+ 테이블의 컬럼명 또는 컬럼명의 이름에 이상이 있을경우 발생하는 에러

### SQL 테이블에 컬럼추가하는 방법
+ alter table 테이블명 add (컬럼명 타입 null 여부);
