package com.blog.devlog.service;

import com.blog.devlog.domain.Comment;
import com.blog.devlog.dto.request.CommentRequest;
import com.blog.devlog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final PlatformTransactionManager transactionManager;
    private final CommentRepository commentRepository;

    // 댓글 등록
    public Comment createComment(CommentRequest commentRequest) throws SQLException {
        Comment commentResponseEntity = null;
        commentResponseEntity = commentRequest.makeEntity(commentRequest);

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            commentRepository.insert(commentResponseEntity);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
        return commentResponseEntity;
    }

    // 댓글 목록 조회
    public List<Comment> getCommentList(Integer commentBoardNo) throws SQLException {

        List<Comment> responseCommentList = null;

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());


        try {
            responseCommentList = commentRepository.findAll(commentBoardNo);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException(e);
        }
        return responseCommentList;
    }

    // 댓글 수정을 위한 조회
    public Comment findOneComment(Integer commentNo , Integer commentBoardNo) throws SQLException {
        Comment responseCommentEntity = null;

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            responseCommentEntity = commentRepository.findOneComment(commentNo , commentBoardNo);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException();

        }
        return responseCommentEntity;
    }

    // 댓글 수정
    public Integer updateComment(CommentRequest commentRequest) throws SQLException {
       Comment commentEntity = null;
        Integer updateCountResult = 0;

       // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            Comment entity = commentRequest.makeEntity(commentRequest);
            updateCountResult = commentRepository.update(entity);
            transactionManager.commit(status);

        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException();
        }
        return updateCountResult;
    }

    // 댓글 삭제시
    public Integer deleteComment(Integer commentNo) throws SQLException {

        Integer deleteResultCount = 0;

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            deleteResultCount = commentRepository.updateDeleteCommentStatus(commentNo);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new IllegalStateException();
        }
        return deleteResultCount;
    }
}
