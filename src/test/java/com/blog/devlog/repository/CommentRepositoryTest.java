package com.blog.devlog.repository;

import com.blog.devlog.domain.Article;
import com.blog.devlog.domain.Comment;
import com.zaxxer.hikari.HikariDataSource;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.blog.devlog.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;


class CommentRepositoryTest {

    public static final Integer BOARD_NO = 10;
    public static final Integer COMMENT_NO = 6;

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
        commentRepository.delete(COMMENT_NO);
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

        // 5. 수정된 댓글 조회
        Comment updated = commentRepository.findOne(updateComment.getCommentBoardNo());
        System.out.println("댓글 수정된 결과: " + updated.toString());

        // then
        assertThat(updated.getCommentNo()).isEqualTo(findResult.getCommentNo());
        assertThat(updated.getCommentBoardNo()).isEqualTo(findResult.getCommentBoardNo());
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

}