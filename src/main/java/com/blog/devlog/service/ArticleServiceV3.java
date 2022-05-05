package com.blog.devlog.service;

import com.blog.devlog.domain.Article;
import com.blog.devlog.repository.ArticleRepository;
import com.blog.devlog.repository.ArticleRepositoryV3;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceV3 {

    private final PlatformTransactionManager transactionManager;
    private final ArticleRepositoryV3 repository;

    // 등록
    public Article insert(Article entity) throws SQLException {
        Article responseEntity = null;

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            responseEntity = repository.save(entity);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }

        return  responseEntity;
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
