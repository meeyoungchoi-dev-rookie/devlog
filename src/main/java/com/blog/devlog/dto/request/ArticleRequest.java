package com.blog.devlog.dto.request;

import lombok.Getter;

@Getter
public class ArticleRequest {

    private String title;
    private String content;
    private String userName;

    public ArticleRequest(String title, String content, String userName) {
        this.title = title;
        this.content = content;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ArticleRequest{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
