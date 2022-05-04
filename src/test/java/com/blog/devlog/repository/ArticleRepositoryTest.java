package com.blog.devlog.repository;

import com.blog.devlog.domain.Article;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.blog.devlog.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;


class ArticleRepositoryTest {

    public static final Integer BOARD_NO = 1;

    private ArticleRepository articleRepository;


    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource  = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        articleRepository = new ArticleRepository(dataSource);
    }

    @AfterEach
    void after() throws SQLException {
        articleRepository.delete(BOARD_NO);
    }

    @Test
    void crudTest() throws SQLException {

        // insert
        Article article = new Article(BOARD_NO, "test", "content701", "userB");
        articleRepository.save(article);

        // read
        Article findedArticle = articleRepository.findById(article.getBoardNo());
        assertThat(article.getBoardNo()).isEqualTo(findedArticle.getBoardNo());

        // update
        Article updateArticle = new Article(BOARD_NO, "update_test701", "update_content_701", findedArticle.getUserId());
        Article updated = findedArticle.update(updateArticle);
        articleRepository.update(updated);
        assertThat(article.getBoardNo()).isEqualTo(updated.getBoardNo());

        // delete
        articleRepository.delete(updated.getBoardNo());
        assertThatThrownBy(() -> articleRepository.findById(article.getBoardNo())).isInstanceOf(NoSuchElementException.class);
    }
}