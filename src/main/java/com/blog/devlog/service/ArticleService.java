package com.blog.devlog.service;

import com.blog.devlog.domain.Article;
import com.blog.devlog.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final PlatformTransactionManager transactionManager;
    private final ArticleRepository repository;

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


        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Article responseEntity = null;

        try {
            responseEntity = repository.findById(articleNo);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException();
        }

        return responseEntity;
    }

    // 수정
    public void update(Article article) throws SQLException {
        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // 비즈니스 로직 시작
            article.update(article);
            repository.update(article);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException();
        }
    }

    // 삭제
    public void delete(int boardNo) throws SQLException {
        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            repository.delete(boardNo);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException();
        }
    }

    public List<Article> findAll() throws SQLException {
        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        List<Article> articles = null;

        try {
             articles = repository.findAll();
             transactionManager.commit(status);

        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException();
        }

        return articles;
    }
}
