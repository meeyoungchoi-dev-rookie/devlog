package com.blog.devlog.service;

import com.blog.devlog.domain.Article;
import com.blog.devlog.repository.ArticleRepository;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.blog.devlog.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

class ArticleServiceTest {

    public static final Integer BOARD_NO = 1;
    public static final Integer BOARD_NO2 = 2;
    public static final Integer BOARD_NO3 = 3;
    public static final Integer BOARD_NO4 = 4;
    public static final Integer BOARD_NO5 = 5;
    public static final Integer BOARD_NO6 = 6;
    public static final Integer BOARD_NO7 = 7;
    public static final Integer BOARD_NO8 = 8;
    public static final Integer BOARD_NO9 = 9;
    public static final Integer BOARD_NO10 = 10;

    private ArticleService articleService;

    private ArticleRepository articleRepository;

    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource  = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        articleRepository = new ArticleRepository(dataSource);
        articleService = new ArticleService(transactionManager, articleRepository);
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
    
    @Test
    @DisplayName("게시글 전체 조회 테스트")
    void findAll() throws SQLException {

        Article article = new Article(BOARD_NO, "title", "content", "userB");
        Article article2 = new Article(BOARD_NO2, "title", "content", "userB");
        Article article3 = new Article(BOARD_NO3, "title", "content", "userB");
        Article article4 = new Article(BOARD_NO4, "title", "content", "userB");
        Article article5 = new Article(BOARD_NO5, "title", "content", "userB");
        Article article6 = new Article(BOARD_NO6, "title", "content", "userB");
        Article article7 = new Article(BOARD_NO7, "title", "content", "userB");
        Article article8 = new Article(BOARD_NO8, "title", "content", "userB");
        Article article9 = new Article(BOARD_NO9, "title", "content", "userB");
        Article article10 = new Article(BOARD_NO10, "title", "content", "userB");

        articleService.insert(article);
        articleService.insert(article2);
        articleService.insert(article3);
        articleService.insert(article4);
        articleService.insert(article5);
        articleService.insert(article6);
        articleService.insert(article7);
        articleService.insert(article8);
        articleService.insert(article9);
        articleService.insert(article10);

        List<Article> articles = articleService.findAll();
        assertThat(articles.size()).isEqualTo(10);
    }
}