package com.blog.devlog.service;

import com.blog.devlog.domain.Article;
import com.blog.devlog.repository.ArticleRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

class ArticleServiceTest {

    ArticleService articleService = new ArticleService(new ArticleRepository());

    ArticleRepository articleRepository = new ArticleRepository();
    
    @Test
    @DisplayName("게시글 등록 서비스 테스트")
    void createTest() throws SQLException {
        Article article = new Article(308, "title308", "content308", "userA");

        Article entity = articleService.insert(article);

        assertThat(article.getBoardNo()).isEqualTo(entity.getBoardNo());

        Article finded = articleRepository.findById(entity.getBoardNo());
        assertThat(entity).isEqualTo(finded);
    }
    
    
    @Test
    @DisplayName("게시글 조회 서비스 테스트")
    void findOneTest() throws SQLException {
        Article article = new Article(309, "title309", "content309", "userA");

        Article entity = articleService.insert(article);

        assertThat(article.getBoardNo()).isEqualTo(entity.getBoardNo());

        Article finded = articleRepository.findById(entity.getBoardNo());
        assertThat(entity).isEqualTo(finded);
        assertThat(entity.getBoardNo()).isEqualTo(finded.getBoardNo());
    }
    
    @Test
    @DisplayName("게시글 수정 서비스 테스트")
    void update() throws SQLException {
        Article article = new Article(312, "title312", "content312", "userB");

        Article entity = articleService.insert(article);

        entity.update(new Article(312, "312_title_수정", "312_content_수정", "userB"));
        articleService.update(entity);

        Article updated = articleService.findById(entity.getBoardNo());
        assertThat(entity).isEqualTo(updated);
        assertThat(entity.getBoardNo()).isEqualTo(updated.getBoardNo());
    }
    
    @Test
    @DisplayName("게시글 삭제 서비스 테스트")
    void deleteTest() throws SQLException {
        Article article = new Article(313, "title313", "content313", "userB");
        Article entity = articleService.insert(article);

        articleService.delete(entity.getBoardNo());
        assertThatThrownBy(() -> articleRepository.findById(entity.getBoardNo())).isInstanceOf(NoSuchElementException.class);
    }
}