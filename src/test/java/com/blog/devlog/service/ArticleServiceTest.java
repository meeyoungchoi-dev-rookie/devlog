package com.blog.devlog.service;

import com.blog.devlog.domain.Article;
import com.blog.devlog.repository.ArticleRepository;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.blog.devlog.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

class ArticleServiceTest {

    public static final Integer BOARD_NO = 2;

    private ArticleService articleService;

    private ArticleRepository articleRepository;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource  = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        articleRepository = new ArticleRepository(dataSource);
        articleService = new ArticleService(articleRepository);
    }

    @AfterEach
    void after() throws SQLException {
        articleRepository.delete(BOARD_NO);
    }


    @Test
    @DisplayName("게시글 등록 서비스 테스트")
    void createTest() throws SQLException {
        Article article = new Article(BOARD_NO, "title", "content", "userA");

        Article entity = articleService.insert(article);

        assertThat(article.getBoardNo()).isEqualTo(entity.getBoardNo());

        Article finded = articleRepository.findById(entity.getBoardNo());
        assertThat(entity).isEqualTo(finded);
    }
    
    
    @Test
    @DisplayName("게시글 조회 서비스 테스트")
    void findOneTest() throws SQLException {
        Article article = new Article(BOARD_NO, "title", "content", "userA");

        Article entity = articleService.insert(article);

        assertThat(article.getBoardNo()).isEqualTo(entity.getBoardNo());

        Article finded = articleRepository.findById(entity.getBoardNo());
        assertThat(entity).isEqualTo(finded);
        assertThat(entity.getBoardNo()).isEqualTo(finded.getBoardNo());
    }
    
    @Test
    @DisplayName("게시글 수정 서비스 테스트")
    void update() throws SQLException {
        Article article = new Article(BOARD_NO, "title", "content", "userB");

        Article entity = articleService.insert(article);

        entity.update(new Article(BOARD_NO, "title_수정", "content_수정", "userB"));
        articleService.update(entity);

        Article updated = articleService.findById(entity.getBoardNo());
        assertThat(entity).isEqualTo(updated);
        assertThat(entity.getBoardNo()).isEqualTo(updated.getBoardNo());
    }
    
    @Test
    @DisplayName("게시글 삭제 서비스 테스트")
    void deleteTest() throws SQLException {
        Article article = new Article(BOARD_NO, "title", "content", "userB");
        Article entity = articleService.insert(article);

        articleService.delete(entity.getBoardNo());
        assertThatThrownBy(() -> articleRepository.findById(entity.getBoardNo())).isInstanceOf(NoSuchElementException.class);
    }
}