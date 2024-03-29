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
    private Integer commentParentNo;
    private Integer commentIdx;
    private Integer commentGroupNo;


    public Comment(Integer commentNo, Integer commentBoardNo, String commentContent, String userId, Date commentCreatedAt, Date commentUpdatedAt, Integer commentParentNo, Integer commentIdx, Integer commentGroupNo) {
        this.commentNo = commentNo;
        this.commentBoardNo = commentBoardNo;
        this.commentContent = commentContent;
        this.userId = userId;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
        this.commentParentNo = commentParentNo;
        this.commentIdx = commentIdx;
        this.commentGroupNo = commentGroupNo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentNo, comment.commentNo) && Objects.equals(commentBoardNo, comment.commentBoardNo) && Objects.equals(commentContent, comment.commentContent) && Objects.equals(userId, comment.userId) && Objects.equals(commentCreatedAt, comment.commentCreatedAt) && Objects.equals(commentUpdatedAt, comment.commentUpdatedAt) && Objects.equals(commentParentNo, comment.commentParentNo) && Objects.equals(commentIdx, comment.commentIdx) && Objects.equals(commentGroupNo, comment.commentGroupNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentNo, commentBoardNo, commentContent, userId, commentCreatedAt, commentUpdatedAt, commentParentNo, commentIdx, commentGroupNo);
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
                ", commentParentNo=" + commentParentNo +
                ", commentIdx=" + commentIdx +
                ", commentGroupNo=" + commentGroupNo +
                '}';
    }

    public void update(Comment commentNineUpdate) {
        this.commentNo = commentNineUpdate.getCommentNo();
        this.commentBoardNo = commentNineUpdate.getCommentBoardNo();
        this.commentContent = commentNineUpdate.getCommentContent();
        this.userId = commentNineUpdate.getUserId();
        this.commentCreatedAt = commentNineUpdate.getCommentCreatedAt();
        this.commentUpdatedAt = commentNineUpdate.getCommentUpdatedAt();
        this.commentParentNo = commentNineUpdate.getCommentParentNo();
        this.commentIdx = commentNineUpdate.getCommentIdx();
        this.commentGroupNo = commentNineUpdate.getCommentGroupNo();
    }
}


