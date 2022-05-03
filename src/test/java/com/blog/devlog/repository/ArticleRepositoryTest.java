package com.blog.devlog.repository;

import com.blog.devlog.domain.Article;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;


class ArticleRepositoryTest {

    ArticleRepository articleRepository = new ArticleRepository();

    @Test
    void crudTest() throws SQLException {

        // insert
        Article article = new Article(701, "test701", "content701", "userB");
        articleRepository.save(article);

        // read
        Article findedArticle = articleRepository.findById(article.getBoardNo());
        assertThat(article.getBoardNo()).isEqualTo(findedArticle.getBoardNo());

        // update
        Article updateArticle = new Article(701, "update_test701", "update_content_701", findedArticle.getUserId());
        Article updated = findedArticle.update(updateArticle);
        articleRepository.update(updated);
        assertThat(article.getBoardNo()).isEqualTo(updated.getBoardNo());

        // delete
        articleRepository.delete(updated.getBoardNo());
        assertThatThrownBy(() -> articleRepository.findById(article.getBoardNo())).isInstanceOf(NoSuchElementException.class);
    }
}