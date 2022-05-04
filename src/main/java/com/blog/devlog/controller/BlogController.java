package com.blog.devlog.controller;


import com.blog.devlog.domain.Article;
import com.blog.devlog.dto.request.ArticleRequest;
import com.blog.devlog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
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

    @PostMapping("/article/insert")
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

    @GetMapping("/article/{articleNo}")
    public ResponseEntity<Article> getArticle(@PathVariable Integer articleNo) {
        Article articleResponse = null;
        try {
            articleResponse = articleService.findById(articleNo);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<Article>((Article) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Article>(articleResponse, HttpStatus.OK);
    }

    @PutMapping("/article/{articleNo}")
    public ResponseEntity<String> updateArticle(@RequestBody Article articleRequest) {
        Article updateEntity = new Article(articleRequest.getBoardNo(), articleRequest.getTitle(), articleRequest.getContent(), articleRequest.getUserId());

        try {
            articleService.findById(articleRequest.getBoardNo());
            articleService.update(updateEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("update 진행중 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("update OK", HttpStatus.OK);
    }

    @DeleteMapping("/article/{articleNo}")
    public ResponseEntity<String> deleteArticle(@PathVariable Integer articleNo) {
        try {
            articleService.findById(articleNo);
            articleService.delete(articleNo);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("삭제 진행중 예외 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>("delete OK", HttpStatus.OK);
    }

    @GetMapping("/article/all")
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
