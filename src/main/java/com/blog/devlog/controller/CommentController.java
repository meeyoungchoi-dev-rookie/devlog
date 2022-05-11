package com.blog.devlog.controller;

import com.blog.devlog.domain.Comment;
import com.blog.devlog.dto.request.CommentRequest;
import com.blog.devlog.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping("/comment/new")
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest commentRequest) throws SQLException {
        Comment commentResponse = null;
        log.info("commentRequest: {}" + commentRequest.toString());
        try {
            commentResponse = commentService.createComment(commentRequest);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<Comment>(commentResponse , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Comment>(commentResponse , HttpStatus.OK);
    }

    @GetMapping("/comment/get/{commentNo}/{articleNo}")
    public ResponseEntity<Comment> findCommentByCommentNo(@PathVariable Integer commentNo , @PathVariable Integer articleNo) throws SQLException {
        Comment commentResponse = null;

        try {
            commentResponse = commentService.findOneComment(commentNo , articleNo);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<Comment>(commentResponse , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Comment>(commentResponse , HttpStatus.OK);
    }

    @PutMapping("/comment/update/{commentNo}")
    public ResponseEntity<Integer> updateComment(@RequestBody CommentRequest commentRequest) throws SQLException, URISyntaxException {
        Integer updateOkCount = 0;

        try {
            updateOkCount = commentService.updateComment(commentRequest);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<Integer>(updateOkCount , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Integer>(updateOkCount , HttpStatus.OK);
    }


    @DeleteMapping("/comment/delete/{commentNo}")
    public ResponseEntity<Integer> deleteComment(@PathVariable Integer commentNo) throws SQLException {

        Integer deleteOkCount = 0;

        try {
            deleteOkCount = commentService.deleteComment(commentNo);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<Integer>(deleteOkCount , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Integer>(deleteOkCount , HttpStatus.OK);
    }

}
