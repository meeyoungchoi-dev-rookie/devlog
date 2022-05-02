package com.blog.devlog.domain;

import lombok.Getter;

@Getter
public class Article {
    private Integer boardNo;
    private String title;
    private String content;
    private String userId;

    public Article(Integer boardNo, String title, String content, String userId) {
        this.boardNo = boardNo;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public Article update(Integer boardNo, String title, String content, String userId) {
        this.boardNo = boardNo;
        this.title = title;
        this.content = content;
        this.userId = userId;
        return this;
    }
}
