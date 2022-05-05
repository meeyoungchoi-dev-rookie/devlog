package com.blog.devlog.dto.request;

import lombok.Getter;

@Getter
public class ArticleRequest {

    private Integer boardNo;
    private String title;
    private String content;
    private String userName;

    public ArticleRequest(Integer boardNo, String title, String content, String userName) {
        this.boardNo = boardNo;
        this.title = title;
        this.content = content;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "ArticleRequest{" +
                "boardNo=" + boardNo +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
