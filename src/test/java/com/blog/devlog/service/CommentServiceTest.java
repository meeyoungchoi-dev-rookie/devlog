package com.blog.devlog.service;

import com.blog.devlog.domain.Article;
import com.blog.devlog.domain.Comment;
import com.blog.devlog.dto.request.CommentRequest;
import com.blog.devlog.repository.ArticleRepository;
import com.blog.devlog.repository.CommentRepository;
import com.zaxxer.hikari.HikariDataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.List;

import static com.blog.devlog.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest {

    public static final Integer BOARD_NO = 1;
    public static final Integer COMMENT_NO = 1;
    public static final Integer COMMENT_NO2 = 2;
    public static final Integer COMMENT_NO3 = 3;
    public static final Integer COMMENT_NO4 = 4;
    public static final Integer COMMENT_NO5 = 5;
    public static final Integer COMMENT_NO6 = 6;

    public static final Integer COMMENT_NO7 = 7;
    public static final Integer COMMENT_NO8 = 8;
    public static final Integer COMMENT_NO9 = 9;
    public static final Integer COMMENT_NO10 = 10;


    private ArticleService articleService;
    private ArticleRepository articleRepository;
    private CommentService commentService;
    private CommentRepository commentRepository;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        articleRepository = new ArticleRepository(dataSource);
        commentRepository = new CommentRepository(dataSource);
        articleService = new ArticleService(transactionManager ,articleRepository);
        commentService = new CommentService(transactionManager , commentRepository);
    }

    @AfterEach
    void after() throws SQLException {
        commentRepository.deleteCommentsRelatedWithArticle(BOARD_NO);
        articleRepository.delete(BOARD_NO);
    }

    @Test
    @DisplayName("댓글 등록 서비스 테스트")
    void createComment() throws SQLException {

        Article article = new Article( BOARD_NO ,
                                       "1번글_제목" ,
                                        "1번글 내용" ,
                                        "userA"
        );

        Article articleEntity = articleRepository.save(article);

        CommentRequest commentRequest = new CommentRequest( COMMENT_NO ,
                                                            article.getBoardNo() ,
                                                            null ,
                                                            null ,
                                                            1,
                                                            0 ,
                                                            "#1번 댓글" ,
                                                            1 ,
                                                            "userA");


        commentRequest.makeEntity(commentRequest);

        Comment insertResult = commentService.createComment(commentRequest);

        assertThat(insertResult.getCommentContent()).isEqualTo(commentRequest.getCommentContent());
        assertThat(insertResult.getCommentBoardNo()).isEqualTo(commentRequest.getCommentBoardNo());
        assertThat(insertResult.getCommentParentNo()).isEqualTo(commentRequest.getCommentParentNo());
        assertThat(insertResult.getCommentGroupNo()).isEqualTo(commentRequest.getCommentGroupNo());
    }


    @Test
    @DisplayName("댓글 목록 조회 서비스 테스트")
    void getCommentList() throws SQLException {


        // given
        // 1. 게시글 추가
        Article article = new Article( BOARD_NO ,
                "1번게시글 제목1" ,
                "1번게시글 내용1" ,
                "userA");

        articleService.insert(article);



        // 2. 댓글 추가
        CommentRequest commentOne = new CommentRequest(  COMMENT_NO ,
                                                         article.getBoardNo() ,
                                                         null ,
                                                         null ,
                                                         1 ,
                                                         0 ,
                                                        "#1번 댓글" ,
                                                         1 ,
                                                           "userB");



        Comment commentOneInsertResult = commentService.createComment(commentOne);
        System.out.println("첫번째 댓글 등록 결과: " + commentOneInsertResult.toString());

      
        CommentRequest commentTwo = new CommentRequest( COMMENT_NO2 ,
                                                        article.getBoardNo() ,
                                                        null ,
                                                        null ,
                                                        2 ,
                                                        0 ,
                                                        "#2번 댓글" ,
                                                        2 ,
                                                        "userC");

        Comment commentTwoInsertResult = commentService.createComment(commentTwo);
        System.out.println("두번째 댓글 등록 결과: " + commentTwoInsertResult.toString());


        CommentRequest commentThree = new CommentRequest( COMMENT_NO3 ,
                article.getBoardNo() ,
                null ,
                null ,
                3 ,
                0 ,
                "#3번 댓글" ,
                3 ,
                "userC");

        Comment commentThreeInsertResult = commentService.createComment(commentThree);
        System.out.println("두번째 댓글 등록 결과: " + commentTwoInsertResult.toString());

        List<Comment> comments = commentService.getCommentList(article.getBoardNo());

        assertThat(comments.size()).isEqualTo(3);
    }
    
    @Test
    @DisplayName("댓글 수정 서비스 테스트")
    void updateTest() throws SQLException {
        // given
        // 1. 게시글 추가
        Article article = new Article( BOARD_NO ,
                "1번게시글 제목1" ,
                "1번게시글 내용1" ,
                "userA");

        articleService.insert(article);



        // 2. 댓글 추가
        CommentRequest commentOne = new CommentRequest(  COMMENT_NO ,
                article.getBoardNo() ,
                null ,
                null ,
                1 ,
                0 ,
                "#1번 댓글" ,
                1 ,
                "userB");



        Comment commentOneInsertResult = commentService.createComment(commentOne);
        System.out.println("첫번째 댓글 등록 결과: " + commentOneInsertResult.toString());


        CommentRequest commentTwo = new CommentRequest( COMMENT_NO2 ,
                article.getBoardNo() ,
                null ,
                null ,
                2 ,
                0 ,
                "#2번 댓글" ,
                2 ,
                "userC");

        Comment commentTwoInsertResult = commentService.createComment(commentTwo);
        System.out.println("두번째 댓글 등록 결과: " + commentTwoInsertResult.toString());


        CommentRequest commentThree = new CommentRequest( COMMENT_NO3 ,
                article.getBoardNo() ,
                null ,
                null ,
                3 ,
                0 ,
                "#3번 댓글" ,
                3 ,
                "userC");

        Comment commentThreeInsertResult = commentService.createComment(commentThree);
        System.out.println("두번째 댓글 등록 결과: " + commentTwoInsertResult.toString());


        // 2번 댓글 수정
        Comment findCommentTwoResult = commentService.findOneComment(commentTwoInsertResult.getCommentNo() , commentTwoInsertResult.getCommentBoardNo());
        System.out.println("findCommentTwoResult : " + findCommentTwoResult.toString());


        CommentRequest updateComment = new CommentRequest( findCommentTwoResult.getCommentNo() ,
                findCommentTwoResult.getCommentBoardNo() ,
                findCommentTwoResult.getCommentCreatedAt() ,
                findCommentTwoResult.getCommentCreatedAt() ,
                findCommentTwoResult.getCommentGroupNo() ,
                findCommentTwoResult.getCommentIdx() ,
                "#2번 댓글 내용 수정이당" ,
                findCommentTwoResult.getCommentParentNo() ,
                findCommentTwoResult.getUserId());

        Integer updateCountResult = commentService.updateComment(updateComment);
        assertThat(updateCountResult).isEqualTo(1);

        assertThat(updateComment.getCommentNo()).isEqualTo(findCommentTwoResult.getCommentNo());
        assertThat(updateComment.getCommentIdx()).isEqualTo(findCommentTwoResult.getCommentIdx());
        assertThat(updateComment.getCommentGroupNo()).isEqualTo(findCommentTwoResult.getCommentGroupNo());
        assertThat(updateComment.getCommentParentNo()).isEqualTo(findCommentTwoResult.getCommentParentNo());
    }


    @Test
    @DisplayName("댓글 삭제 서비스 테스트 - 계층구조 고려하여 [삭제된 댓글입니다] 로 표시되도록 update")
    void deleteStatusUpdate() throws SQLException {
        // given
        // 1. 게시글 추가
        Article article = new Article( BOARD_NO ,
                "1번게시글 제목1" ,
                "1번게시글 내용1" ,
                "userA");

        articleService.insert(article);



        // 2. 댓글 추가
        CommentRequest commentOne = new CommentRequest(  COMMENT_NO ,
                article.getBoardNo() ,
                null ,
                null ,
                1 ,
                0 ,
                "#1번 댓글" ,
                1 ,
                "userB");



        Comment commentOneInsertResult = commentService.createComment(commentOne);
        System.out.println("첫번째 댓글 등록 결과: " + commentOneInsertResult.toString());


        CommentRequest commentTwo = new CommentRequest( COMMENT_NO2 ,
                article.getBoardNo() ,
                null ,
                null ,
                2 ,
                0 ,
                "#2번 댓글" ,
                2 ,
                "userC");

        Comment commentTwoInsertResult = commentService.createComment(commentTwo);
        System.out.println("두번째 댓글 등록 결과: " + commentTwoInsertResult.toString());


        CommentRequest commentThree = new CommentRequest( COMMENT_NO3 ,
                article.getBoardNo() ,
                null ,
                null ,
                3 ,
                0 ,
                "#3번 댓글" ,
                3 ,
                "userC");

        Comment commentThreeInsertResult = commentService.createComment(commentThree);
        System.out.println("두번째 댓글 등록 결과: " + commentTwoInsertResult.toString());


        // 2번 댓글 삭제
        Comment findCommentTwoResult = commentService.findOneComment(commentTwoInsertResult.getCommentNo() , commentTwoInsertResult.getCommentBoardNo());
        System.out.println("findCommentTwoResult : " + findCommentTwoResult.toString());


        CommentRequest updateDeleteComment = new CommentRequest( findCommentTwoResult.getCommentNo() ,
                findCommentTwoResult.getCommentBoardNo() ,
                findCommentTwoResult.getCommentCreatedAt() ,
                findCommentTwoResult.getCommentCreatedAt() ,
                findCommentTwoResult.getCommentGroupNo() ,
                findCommentTwoResult.getCommentIdx() ,
                "삭제된 댓글입니다" ,
                findCommentTwoResult.getCommentParentNo() ,
                findCommentTwoResult.getUserId());

        Integer deleteCommentCount = commentService.deleteComment(updateDeleteComment.getCommentNo());
        assertThat(deleteCommentCount).isEqualTo(1);

        assertThat(updateDeleteComment.getCommentNo()).isEqualTo(findCommentTwoResult.getCommentNo());
        assertThat(updateDeleteComment.getCommentIdx()).isEqualTo(findCommentTwoResult.getCommentIdx());
        assertThat(updateDeleteComment.getCommentGroupNo()).isEqualTo(findCommentTwoResult.getCommentGroupNo());
        assertThat(updateDeleteComment.getCommentParentNo()).isEqualTo(findCommentTwoResult.getCommentParentNo());
    }
}