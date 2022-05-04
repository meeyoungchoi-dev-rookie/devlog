package com.blog.devlog.service;

import com.blog.devlog.domain.Article;
import com.blog.devlog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository repository;

    // 등록
    public Article insert(Article entity) throws SQLException {
        return  repository.save(entity);
    }

    // 조회
    public Article findById(int articleNo) throws SQLException {
        return repository.findById(articleNo);
    }

    // 수정
    public void update(Article article) throws SQLException {
        article.update(article);
        repository.update(article);
    }

    // 삭제
    public void delete(int boardNo) throws SQLException {
        repository.delete(boardNo);
    }

    public List<Article> findAll() throws SQLException {
        return repository.findAll();
    }
}
