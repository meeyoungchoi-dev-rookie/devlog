package com.blog.devlog.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
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

    public Article update(Article updateEntity) {
        this.boardNo = updateEntity.getBoardNo();
        this.title = updateEntity.getTitle();
        this.content = updateEntity.getContent();
        this.userId = updateEntity.getUserId();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(boardNo, article.boardNo) && Objects.equals(title, article.title) && Objects.equals(content, article.content) && Objects.equals(userId, article.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardNo, title, content, userId);
    }
}
