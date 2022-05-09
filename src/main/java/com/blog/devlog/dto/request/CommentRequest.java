package com.blog.devlog.dto.request;

import com.blog.devlog.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class CommentRequest {

    private Integer commentNo;
    private Integer commentBoardNo;
    private Date commentCreatedAt;
    private Date commentUpdatedAt;

    private Integer commentGroupNo;
    private Integer commentIdx;
    private String commentContent;
    private Integer commentParentNo;
    private String commentUserId;


    public CommentRequest(Integer commentNo, Integer commentBoardNo, Date commentCreatedAt, Date commentUpdatedAt, Integer commentGroupNo, Integer commentIdx, String commentContent, Integer commentParentNo, String commentUserId) {
        this.commentNo = commentNo;
        this.commentBoardNo = commentBoardNo;
        this.commentCreatedAt = commentCreatedAt;
        this.commentUpdatedAt = commentUpdatedAt;
        this.commentGroupNo = commentGroupNo;
        this.commentIdx = commentIdx;
        this.commentContent = commentContent;
        this.commentParentNo = commentParentNo;
        this.commentUserId = commentUserId;
    }

    public Comment makeEntity(CommentRequest commentRequestDto) {

        Comment commentEntity = new Comment( commentRequestDto.getCommentNo() ,
                                             commentRequestDto.getCommentBoardNo() ,
                                             commentRequestDto.getCommentContent() ,
                                             commentRequestDto.getCommentUserId() ,
                                             commentRequestDto.getCommentCreatedAt() ,
                                             null ,
                                             commentRequestDto.getCommentParentNo() ,
                                             commentRequestDto.getCommentIdx() ,
                                             commentRequestDto.getCommentGroupNo());
        return commentEntity;
    }





    @Override
    public String toString() {
        return "CommentRequest{" +
                "commentBoardNo=" + commentBoardNo +
                ", commentGroupNo=" + commentGroupNo +
                ", commentIdx=" + commentIdx +
                ", commentContent='" + commentContent + '\'' +
                ", commentParentNo=" + commentParentNo +
                ", commentUserId='" + commentUserId + '\'' +
                '}';
    }
}
