package com.blog.devlog.repository;

import com.blog.devlog.domain.Article;
import com.blog.devlog.domain.Comment;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.blog.devlog.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;


class CommentRepositoryTest {

    public static final Integer BOARD_NO = 10;
    public static final Integer COMMENT_NO = 6;
    public static final Integer COMMENT_NO1 = 7;
    public static final Integer COMMENT_NO2 = 8;
    public static final Integer COMMENT_NO3 = 9;
    public static final Integer COMMENT_NO4 = 10;

    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        articleRepository = new ArticleRepository(dataSource);
        commentRepository = new CommentRepository(dataSource);
    }

    @AfterEach
    void after() throws SQLException {
        commentRepository.deleteCommentsRelatedWithArticle(BOARD_NO);
        articleRepository.delete(BOARD_NO);
    }

    @Test
    @DisplayName("게시글에 댓글 추가 테스트_댓글이 하나만 달리는 경우")
    void insertComment() throws SQLException {

        // given
        // 게시글 등록
        Article article = new Article(BOARD_NO, "10번글_제목", "10번글_내용", "userB");
        Article inserResult = articleRepository.save(article);
        System.out.println("게시글 등록결과: " + inserResult.toString());


        // 게시글에 댓글 등록
        Comment comment = new Comment(COMMENT_NO,
                BOARD_NO,
                "10번글_첫번째 댓글",
                "userA",
                null,
                null,
                0,
                0,
                COMMENT_NO);
        Comment insertResult = commentRepository.insert(comment);
        System.out.println("insert 결과: " + insertResult.toString());

        // when
        Comment commentResult = commentRepository.findOne(article.getBoardNo());

        // then
        assertThat(commentResult.getCommentNo()).isEqualTo(comment.getCommentNo());
        assertThat(commentResult.getCommentBoardNo()).isEqualTo(comment.getCommentBoardNo());
    }


    @Test
    @DisplayName("단일 댓글 수정 테스트")
    void update() throws SQLException {

        // given
        //1.게시글 추가
        //2. 댓글 추가
        Article article = new Article(BOARD_NO, "10번글_제목", "10번글_내용", "userB");
        Article articleInnsertResult = articleRepository.save(article);

        Comment comment = new Comment(COMMENT_NO,
                BOARD_NO,
                "10번글_첫번째 댓글",
                "userA",
                null,
                null,
                0,
                0,
                COMMENT_NO);
        Comment commentInsertResult = commentRepository.insert(comment);

        // when
        // 3. 댓글 조회
        Comment findResult = commentRepository.findOne(articleInnsertResult.getBoardNo());


        // 4. 댓글 수정
        Comment updateComment = new Comment(findResult.getCommentNo(),
                findResult.getCommentBoardNo(),
                "10번글_첫번쨰 댓글_수정1",
                findResult.getUserId(),
                findResult.getCommentCreatedAt(),
                findResult.getCommentUpdatedAt(),
                findResult.getCommentParentNo(),
                findResult.getCommentIdx(),
                findResult.getCommentGroupNo());
        System.out.println("댓글 수정: " + updateComment.toString());

        int updateOkCount = commentRepository.update(updateComment);
        System.out.println("updateOkCount = " + updateComment);

        // 5. 수정된 댓글 조회
        List<Comment> comments = commentRepository.findAll(updateComment.getCommentBoardNo());

        // then
        assertThat(comments.get(0).getCommentNo()).isEqualTo(findResult.getCommentNo());
        assertThat(comments.get(0).getCommentBoardNo()).isEqualTo(findResult.getCommentBoardNo());
        assertThat(updateOkCount).isEqualTo(1);
    }


    @Test
    @DisplayName("단일 댓글 삭제 테스트")
    void delete() throws SQLException {

        // given
        // 1. 게시글 추가


        Article article = new Article(BOARD_NO, "10번글_제목", "10번글_내용", "userB");
        Article articleInnsertResult = articleRepository.save(article);

        // 2. 댓글 추가
        Comment comment = new Comment(COMMENT_NO,
                BOARD_NO,
                "10번글_첫번째 댓글",
                "userA",
                null,
                null,
                0,
                0,
                COMMENT_NO);
        Comment commentInsertResult = commentRepository.insert(comment);

        // 3. 댓글 조회
        Comment findResult = commentRepository.findOne(articleInnsertResult.getBoardNo());

        // when
        // 4. 댓글 삭제
        commentRepository.delete(findResult.getCommentNo());

        // then
        //  5. 해당 댓글을 조회했을떄 예외가 발생하는지

       Assertions.assertThrows(NoSuchElementException.class,
               () -> {
                    commentRepository.findOne(findResult.getCommentNo());
               });
    }


    @Test
    @DisplayName("게시글에 달린 댓글 전체 조회")
    void getAllComments() throws SQLException {

        // given
        //1. 게시글 생성
        Article article = new Article(BOARD_NO, "10번글_제목", "10번글_내용", "userA");

        // 2. 게시글 저장
        articleRepository.save(article);

        // 3. 10번글에 대한 댓글 여러개 생성
        Comment comment1 = new Comment( COMMENT_NO,
                                        article.getBoardNo(),
                                        "10번글_첫번째 댓글",
                                        "userB",
                                        null,
                                        null,
                                        COMMENT_NO,
                                        0,
                                         COMMENT_NO);
        Comment comment2 = new Comment( COMMENT_NO1,
                                        article.getBoardNo(),
                                        "10번글_두번째_댓글",
                                        "userC",
                                        null,
                                        null,
                                        COMMENT_NO1,
                                        0,
                                        COMMENT_NO1);


        Comment comment3 = new Comment( COMMENT_NO2,
                                        article.getBoardNo(),
                                        "10번글_세번째_댓글",
                                        "userD",
                                        null,
                                        null,
                                        COMMENT_NO2,
                                        0,
                                        COMMENT_NO2);


        Comment comment4 = new Comment( COMMENT_NO3,
                article.getBoardNo(),
                "10번글_네번째_댓글",
                "userE",
                null,
                null,
                COMMENT_NO3,
                0,
                COMMENT_NO3);


        Comment comment5 = new Comment( COMMENT_NO4,
                article.getBoardNo(),
                "10번글_다섯번째_댓글",
                "userE",
                null,
                null,
                COMMENT_NO4,
                0,
                COMMENT_NO4);

        // 4. 댓글 저장
        commentRepository.insert(comment1);
        commentRepository.insert(comment2);
        commentRepository.insert(comment3);
        commentRepository.insert(comment4);
        commentRepository.insert(comment5);

        // when
        // 게시글에 달린 댓글 전체 조회
        List<Comment> comments = commentRepository.findAll(article.getBoardNo());

        // then
        // 조회 결고 검증
        assertThat(comments.size()).isEqualTo(5);
        assertThat(comments.get(0).getCommentBoardNo()).isEqualTo(10);
        assertThat(comments.get(1).getCommentBoardNo()).isEqualTo(10);
        assertThat(comments.get(2).getCommentBoardNo()).isEqualTo(10);
        assertThat(comments.get(3).getCommentBoardNo()).isEqualTo(10);
        assertThat(comments.get(4).getCommentBoardNo()).isEqualTo(10);
    }
}