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
        System.out.println("세번째 댓글 등록 결과: " + commentTwoInsertResult.toString());


        // 2번 댓글에 대한 답글 등록
        CommentRequest replyCommentTwo_4 = new CommentRequest( COMMENT_NO4 ,
                article.getBoardNo() ,
                null ,
                null ,
                commentTwoInsertResult.getCommentGroupNo() ,
                commentTwoInsertResult.getCommentIdx() ,
                "#2_1 2번 댓글에 대한 답글" ,
                commentTwoInsertResult.getCommentParentNo() ,
                "userD");

        Comment commentFourInsertResult = commentService.createComment(replyCommentTwo_4);
        System.out.println("4번째 #2_1 2번 댓글에 대한 답글: " + commentFourInsertResult.toString());

        // 1번 댓글에 대한 답글
        CommentRequest replyCommentOne_5 = new CommentRequest( COMMENT_NO5 ,
                article.getBoardNo() ,
                null ,
                null ,
                commentOneInsertResult.getCommentGroupNo() ,
                commentOneInsertResult.getCommentIdx() ,
                "#1_1 1번 댓글에 대한 답글" ,
                commentOneInsertResult.getCommentParentNo() ,
                "userE");

        Comment commentFivthInsertResult = commentService.createComment(replyCommentOne_5);
        System.out.println("5번째 #1_1 1번 댓글에 대한 답글: " + commentFivthInsertResult.toString());


        // #2_1번 댓글에 대한 답글
        CommentRequest replyCommnetTwo_2 = new CommentRequest( COMMENT_NO6 ,
                article.getBoardNo() ,
                null ,
                null ,
                commentTwoInsertResult.getCommentGroupNo() ,
                commentTwoInsertResult.getCommentIdx() ,
                "#2_2 : #2_1번 댓글에 대한 답글" ,
                commentTwoInsertResult.getCommentParentNo() ,
                "userF");


        Comment commentReplyTwo_2Result = commentService.createComment(replyCommnetTwo_2);
        System.out.println("6번째 #2_2 : #2_1번 댓글에 대한 답글 : " + commentReplyTwo_2Result.toString());


        // #7번 댓글
        CommentRequest commentSeventh = new CommentRequest( COMMENT_NO7 ,
                BOARD_NO ,
                null ,
                null ,
                COMMENT_NO7 ,
                0 ,
                "#7 : 7번째 댓글" ,
                COMMENT_NO7 ,
                "userG");
        Comment commentSeventthInsertResult = commentService.createComment(commentSeventh);
        System.out.println("7번째 #7 댓글: " + commentSeventthInsertResult.toString());


        // #8번 댓글
        CommentRequest commentEight = new CommentRequest( COMMENT_NO8 ,
                BOARD_NO ,
                null ,
                null ,
                COMMENT_NO8 ,
                0 ,
                "#8 : 8번째 댓글" ,
                COMMENT_NO8 ,
                "userH");

        Comment commentEightInsertResult = commentService.createComment(commentEight);
        System.out.println("8번째 #8 댓글: " + commentEightInsertResult.toString());




        // #9번 댓글 => #2_3 : #2_2(6번댓글)에 대한 답글
        CommentRequest commentReplyNine = new CommentRequest( COMMENT_NO9 ,
                BOARD_NO ,
                null ,
                null ,
                commentReplyTwo_2Result.getCommentGroupNo() ,
                commentReplyTwo_2Result.getCommentIdx() ,
                "#10 : #2_3 (#2_2 6번 댓글에 대한 답글)" ,
                commentReplyTwo_2Result.getCommentParentNo() ,
                "userI"
        );

        Comment commentReplyNineInsertResult = commentService.createComment(commentReplyNine);

        // #10번 댓글 -> #7 (7번 댓글)에 대한 답글
        CommentRequest replyCommentSeventh_1 = new CommentRequest(  COMMENT_NO10 ,
                BOARD_NO ,
                null ,
                null ,
                commentSeventthInsertResult.getCommentGroupNo() ,
                commentSeventthInsertResult.getCommentIdx() ,
                "#10 : #7-1 (#7 7번 댓글에 대한 답글" ,
                commentSeventthInsertResult.getCommentParentNo() ,
                "userJ"
        );

        Comment replyCommentSeventhResult = commentService.createComment(replyCommentSeventh_1);
        List<Comment> comments = commentService.getCommentList(article.getBoardNo());

        // then
        assertThat(comments.size()).isEqualTo(10);


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
        System.out.println("세번째 댓글 등록 결과: " + commentTwoInsertResult.toString());


        // 2번 댓글에 대한 답글 등록
        CommentRequest replyCommentTwo_4 = new CommentRequest( COMMENT_NO4 ,
                                                               article.getBoardNo() ,
                                                               null ,
                                                                null ,
                                                               commentTwoInsertResult.getCommentGroupNo() ,
                                                                commentTwoInsertResult.getCommentIdx() ,
                                                                "#2_1 2번 댓글에 대한 답글" ,
                                                                commentTwoInsertResult.getCommentParentNo() ,
                                                                "userD");

        Comment commentFourInsertResult = commentService.createComment(replyCommentTwo_4);
        System.out.println("4번째 #2_1 2번 댓글에 대한 답글: " + commentFourInsertResult.toString());

        // 1번 댓글에 대한 답글
        CommentRequest replyCommentOne_5 = new CommentRequest( COMMENT_NO5 ,
                                                                article.getBoardNo() ,
                                                                null ,
                                                                null ,
                                                                commentOneInsertResult.getCommentGroupNo() ,
                                                                commentOneInsertResult.getCommentIdx() ,
                                                                "#1_1 1번 댓글에 대한 답글" ,
                                                                commentOneInsertResult.getCommentParentNo() ,
                                                                "userE");

        Comment commentFivthInsertResult = commentService.createComment(replyCommentOne_5);
        System.out.println("5번째 #1_1 1번 댓글에 대한 답글: " + commentFivthInsertResult.toString());


        // #2_1번 댓글에 대한 답글
        CommentRequest replyCommnetTwo_2 = new CommentRequest( COMMENT_NO6 ,
                                                                article.getBoardNo() ,
                                                                null ,
                                                                null ,
                                                                commentTwoInsertResult.getCommentGroupNo() ,
                                                                commentTwoInsertResult.getCommentIdx() ,
                                                                "#2_2 : #2_1번 댓글에 대한 답글" ,
                                                                commentTwoInsertResult.getCommentParentNo() ,
                                                                "userF");


        Comment commentReplyTwo_2Result = commentService.createComment(replyCommnetTwo_2);
        System.out.println("6번째 #2_2 : #2_1번 댓글에 대한 답글 : " + commentReplyTwo_2Result.toString());


        // #7번 댓글
        CommentRequest commentSeventh = new CommentRequest( COMMENT_NO7 ,
                                                            BOARD_NO ,
                                                            null ,
                                                            null ,
                                                            COMMENT_NO7 ,
                                                            0 ,
                                                            "#7 : 7번째 댓글" ,
                                                            COMMENT_NO7 ,
                                                            "userG");
         Comment commentSeventthInsertResult = commentService.createComment(commentSeventh);
        System.out.println("7번째 #7 댓글: " + commentSeventthInsertResult.toString());


        // #8번 댓글
        CommentRequest commentEight = new CommentRequest( COMMENT_NO8 ,
                                                          BOARD_NO ,
                                                          null ,
                                                          null ,
                                                          COMMENT_NO8 ,
                                                          0 ,
                                                          "#8 : 8번째 댓글" ,
                                                            COMMENT_NO8 ,
                                                            "userH");

        Comment commentEightInsertResult = commentService.createComment(commentEight);
        System.out.println("8번째 #8 댓글: " + commentEightInsertResult.toString());




        // #9번 댓글 => #2_3 : #2_2(6번댓글)에 대한 답글
        CommentRequest commentReplyNine = new CommentRequest( COMMENT_NO9 ,
                                                              BOARD_NO ,
                                                              null ,
                                                              null ,
                                                              commentReplyTwo_2Result.getCommentGroupNo() ,
                                                              commentReplyTwo_2Result.getCommentIdx() ,
                                                              "#10 : #2_3 (#2_2 6번 댓글에 대한 답글)" ,
                                                                commentReplyTwo_2Result.getCommentParentNo() ,
                                                              "userI"
        );

        commentService.createComment(commentReplyNine);

        // #10번 댓글 -> #7 (7번 댓글)에 대한 답글
        CommentRequest replyCommentSeventh_1 = new CommentRequest(  COMMENT_NO10 ,
                                                                    BOARD_NO ,
                                                                    null ,
                                                                    null ,
                                                                    commentSeventthInsertResult.getCommentGroupNo() ,
                                                                    commentSeventthInsertResult.getCommentIdx() ,
                                                                    "#10 : #7-1 (#7 7번 댓글에 대한 답글" ,
                                                                    commentSeventthInsertResult.getCommentParentNo() ,
                                                                    "userJ"
        );

        commentService.createComment(replyCommentSeventh_1);

        List<Comment> comments = commentService.getCommentList(article.getBoardNo());

        assertThat(comments.size()).isEqualTo(10);
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

        // 2번 댓글에 대한 답글 등록
        CommentRequest replyCommentTwo_4 = new CommentRequest( COMMENT_NO4 ,
                article.getBoardNo() ,
                null ,
                null ,
                commentTwoInsertResult.getCommentGroupNo() ,
                commentTwoInsertResult.getCommentIdx()  + 1,
                "#2_1 2번 댓글에 대한 답글" ,
                commentTwoInsertResult.getCommentParentNo() ,
                "userD");

        Comment commentFourInsertResult = commentService.createComment(replyCommentTwo_4);
        System.out.println("4번째 #2_1 2번 댓글에 대한 답글: " + commentFourInsertResult.toString());

        // 1번 댓글에 대한 답글
        CommentRequest replyCommentOne_5 = new CommentRequest( COMMENT_NO5 ,
                article.getBoardNo() ,
                null ,
                null ,
                commentOneInsertResult.getCommentGroupNo() ,
                commentOneInsertResult.getCommentIdx() ,
                "#1_1 1번 댓글에 대한 답글" ,
                commentOneInsertResult.getCommentParentNo() ,
                "userE");

        Comment commentFivthInsertResult = commentService.createComment(replyCommentOne_5);
        System.out.println("5번째 #1_1 1번 댓글에 대한 답글: " + commentFivthInsertResult.toString());


        // #2_1번 댓글에 대한 답글
        CommentRequest replyCommnetTwo_2 = new CommentRequest( COMMENT_NO6 ,
                article.getBoardNo() ,
                null ,
                null ,
                commentFourInsertResult.getCommentGroupNo() ,
                commentFourInsertResult.getCommentIdx() + 1,
                "#2_2 : #2_1번 댓글에 대한 답글" ,
                commentFourInsertResult.getCommentParentNo() ,
                "userF");


        Comment commentReplyTwo_2Result = commentService.createComment(replyCommnetTwo_2);
        System.out.println("6번째 #2_2 : #2_1번 댓글에 대한 답글 : " + commentReplyTwo_2Result.toString());


        // #7번 댓글
        CommentRequest commentSeventh = new CommentRequest( COMMENT_NO7 ,
                BOARD_NO ,
                null ,
                null ,
                COMMENT_NO7 ,
                0 ,
                "#7 : 7번째 댓글" ,
                COMMENT_NO7 ,
                "userG");
        Comment commentSeventthInsertResult = commentService.createComment(commentSeventh);
        System.out.println("7번째 #7 댓글: " + commentSeventthInsertResult.toString());


        // #8번 댓글
        CommentRequest commentEight = new CommentRequest( COMMENT_NO8 ,
                BOARD_NO ,
                null ,
                null ,
                COMMENT_NO8 ,
                0 ,
                "#8 : 8번째 댓글" ,
                COMMENT_NO8 ,
                "userH");

        Comment commentEightInsertResult = commentService.createComment(commentEight);
        System.out.println("8번째 #8 댓글: " + commentEightInsertResult.toString());




        // #9번 댓글 => #2_3 : #2_2(6번댓글)에 대한 답글
        CommentRequest commentReplyNine = new CommentRequest( COMMENT_NO9 ,
                BOARD_NO ,
                null ,
                null ,
                commentReplyTwo_2Result.getCommentGroupNo() ,
                commentReplyTwo_2Result.getCommentIdx() + 1 ,
                "#10 : #2_3 (#2_2 6번 댓글에 대한 답글)" ,
                commentReplyTwo_2Result.getCommentParentNo() ,
                "userI"
        );

        commentService.createComment(commentReplyNine);

        // #10번 댓글 -> #7 (7번 댓글)에 대한 답글
        CommentRequest replyCommentSeventh_1 = new CommentRequest(  COMMENT_NO10 ,
                BOARD_NO ,
                null ,
                null ,
                commentSeventthInsertResult.getCommentGroupNo() ,
                commentSeventthInsertResult.getCommentIdx() ,
                "#10 : #7-1 (#7 7번 댓글에 대한 답글" ,
                commentSeventthInsertResult.getCommentParentNo() ,
                "userJ"
        );

        commentService.createComment(replyCommentSeventh_1);

        // #2_2 6번글 수정
        Comment findCommentTwoResult_2 = commentService.findOneComment(replyCommnetTwo_2.getCommentNo() , replyCommnetTwo_2.getCommentBoardNo());
        System.out.println("findCommentTwoResult_2 : " + findCommentTwoResult_2.toString());


        CommentRequest updateComment = new CommentRequest( findCommentTwoResult_2.getCommentNo() ,
                findCommentTwoResult_2.getCommentBoardNo() ,
                findCommentTwoResult_2.getCommentCreatedAt() ,
                findCommentTwoResult_2.getCommentCreatedAt() ,
                findCommentTwoResult_2.getCommentGroupNo() ,
                findCommentTwoResult_2.getCommentIdx() ,
                "#2_2 6번 댓글 내용 수정이당" ,
                findCommentTwoResult_2.getCommentParentNo() ,
                findCommentTwoResult_2.getUserId());

        Integer updateCountResult = commentService.updateComment(updateComment);
        assertThat(updateCountResult).isEqualTo(1);

        assertThat(updateComment.getCommentNo()).isEqualTo(findCommentTwoResult_2.getCommentNo());
        assertThat(updateComment.getCommentIdx()).isEqualTo(findCommentTwoResult_2.getCommentIdx());
        assertThat(updateComment.getCommentGroupNo()).isEqualTo(findCommentTwoResult_2.getCommentGroupNo());
        assertThat(updateComment.getCommentParentNo()).isEqualTo(findCommentTwoResult_2.getCommentParentNo());
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