package com.blog.devlog.repository;

import com.blog.devlog.domain.Article;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.blog.devlog.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;


class ArticleRepositoryTest {

    ArticleRepository articleRepository;


    @BeforeEach
    void beforeEach() {
        HikariDataSource dataSource  = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        articleRepository = new ArticleRepository(dataSource);
    }

    @Test
    void crudTest() throws SQLException {

        // insert
        Article article = new Article(702, "test702", "content702", "userB");
        articleRepository.save(article);

        // read
        Article findedArticle = articleRepository.findById(article.getBoardNo());
        assertThat(article.getBoardNo()).isEqualTo(findedArticle.getBoardNo());

        // update
        Article updateArticle = new Article(702, "update_test702", "update_content_702", findedArticle.getUserId());
        Article updated = findedArticle.update(updateArticle);
        articleRepository.update(updated);
        assertThat(article.getBoardNo()).isEqualTo(updated.getBoardNo());

        // delete
        articleRepository.delete(updated.getBoardNo());
        assertThatThrownBy(() -> articleRepository.findById(article.getBoardNo())).isInstanceOf(NoSuchElementException.class);
    }
}