package com.blog.devlog.service;

import com.blog.devlog.domain.Comment;
import com.blog.devlog.dto.request.CommentRequest;
import com.blog.devlog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

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
        log.info("commentRequest: " + commentRequest.toString());
        Comment commentEntity = commentRequest.makeEntity(commentRequest);
        log.info("commentEntity: " + commentEntity.toString());

        return commentRepository.insert(commentEntity);
    }

    // 댓글 목록 조회
    public List<Comment> getCommentList(Integer commentBoardNo) throws SQLException {
        return commentRepository.findAll(commentBoardNo);
    }

    // 댓글 수정을 위한 조회
    public Comment findOneComment(Integer commentNo , Integer commentBoardNo) throws SQLException {
        return commentRepository.findOneComment(commentNo , commentBoardNo);
    }

    // 댓글 수정
    public Integer updateComment(CommentRequest commentRequest) throws SQLException {
       Comment commentEntity = commentRequest.makeEntity(commentRequest);
       log.info("update : commentEntity: {}" , commentEntity.toString());
        return commentRepository.update(commentEntity);
    }

    // 댓글 삭제시
    public Integer deleteComment(Integer commentNo) throws SQLException {
        return commentRepository.updateDeleteCommentStatus(commentNo);
    }

}
