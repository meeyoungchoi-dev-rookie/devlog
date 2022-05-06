package com.blog.devlog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class Comment {
    private Integer commentNo;
    private Integer commentBoardNo;
    private String commentContent;
    private String userId;
    private Date commentCreatedAt;
    private Date commentUpdatedAt;
    private Integer commentIdx;
    private Integer commentGroupNo;

    public Comment(Integer commentNo, Integer commentBoardNo, String commentContent, String userId, Date commentCreatedAt, Date commentUpdatedAt, Integer commentIdx, Integer commentGroupNo) {
        this.commentNo = commentNo;
        this.commentBoardNo = commentBoardNo;
        this.commentContent = commentContent;
        this.userId = userId;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
        this.commentIdx = commentIdx;
        this.commentGroupNo = commentGroupNo;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentNo=" + commentNo +
                ", commentBoardNo=" + commentBoardNo +
                ", commentContent='" + commentContent + '\'' +
                ", userId='" + userId + '\'' +
                ", commentCreatedAt=" + commentCreatedAt +
                ", commentUpdatedAt=" + commentUpdatedAt +
                ", commentIdx=" + commentIdx +
                ", commentGroupNo=" + commentGroupNo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentNo, comment.commentNo) && Objects.equals(commentBoardNo, comment.commentBoardNo) && Objects.equals(commentContent, comment.commentContent) && Objects.equals(commentCreatedAt, comment.commentCreatedAt) && Objects.equals(commentUpdatedAt, comment.commentUpdatedAt) && Objects.equals(commentIdx, comment.commentIdx) && Objects.equals(commentGroupNo, comment.commentGroupNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentNo, commentBoardNo, commentContent, commentCreatedAt, commentUpdatedAt, commentIdx, commentGroupNo);
    }
}
