package com.blog.devlog.controller;


import com.blog.devlog.domain.Article;
import com.blog.devlog.dto.request.ArticleRequest;
import com.blog.devlog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class BlogController {

    private final ArticleService articleService;

    @GetMapping("/")
    public String intro() {
        return "index";
    }


    // 글 등록
    @PostMapping("/insert")
    public String insertArticle(@RequestBody ArticleRequest articleRequest) {
        log.info("insert : {}", articleRequest.toString());
        Article articleEntity = new Article(0, articleRequest.getTitle(), articleRequest.getContent(), articleRequest.getUserName());
        try {
            articleService.insert(articleEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    // 글 조회
    @GetMapping("/{articleNo}")
    public Article getArticle(@PathVariable Integer articleNo) {
        Article responeEntity = null;
        log.info("get : {} ", articleNo);
        try {
            responeEntity =  articleService.findById(articleNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responeEntity;
    }

    // 글 수정
    @PutMapping("/{articleNo}")
    public ResponseEntity<String> updateArticle(@RequestBody Article articleRequest) {
        Article updateEntity = new Article(articleRequest.getBoardNo(), articleRequest.getTitle(), articleRequest.getContent(), articleRequest.getUserId());

        try {
            articleService.update(updateEntity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>("update OK", HttpStatus.OK);
    }

    // 글 삭제
    @DeleteMapping("/{articleNo}")
    public ResponseEntity<String> deleteArticle(@PathVariable Integer articleNo) {
        try {
            articleService.delete(articleNo);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>("delete OK", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Article>> getArticleList() {
        List<Article> articles = null;
        try {
            articles = articleService.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

       for (Article article : articles) {
           log.info("article {} " + article.toString());
       }
        return ResponseEntity.ok().body(articles);
    }
}
