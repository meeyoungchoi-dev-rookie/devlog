package com.blog.devlog.repository;

import com.blog.devlog.domain.Article;
import com.blog.devlog.domain.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final DataSource dataSource;


    // 단일 댓글 추가
    public Comment insert(Comment comment) throws SQLException {
        Date insertDate = new Date();
        Long timeInMilliSeconds = insertDate.getTime();
        java.sql.Date date = new java.sql.Date(timeInMilliSeconds);
        String sql = "insert into comments(comment_no," +
                "comment_board_no," +
                "comment_content, " +
                "COMMENT_USER_ID," +
                "comment_created_at," +
                "comment_updated_at," +
                "comment_parent_no," +
                "comment_idx," +
                "comment_group_no) VALUES(? ,? ,? ,? ,? ,?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, comment.getCommentNo());
            pstmt.setInt(2, comment.getCommentBoardNo());
            pstmt.setString(3, comment.getCommentContent());
            pstmt.setString(4, comment.getUserId());
            pstmt.setDate(5, date);
            pstmt.setDate(6, date);
            pstmt.setInt(7, comment.getCommentParentNo());
            pstmt.setInt(8, comment.getCommentIdx());
            pstmt.setInt(9, comment.getCommentGroupNo());
            int count = pstmt.executeUpdate();
            log.info("insert ={} ", count);
            return comment;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }


    // 댓글 전체 조회
    public List<Comment> findAll(Integer boardNo) throws SQLException {
        String sql = "select * from comments " +
                "where comment_board_no = ? " +
                "order by COMMENT_GROUP_NO ASC , COMMENT_IDX ASC , COMMENT_NO DESC";




        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Comment commentEntity = null;
        List<Comment> comments = new ArrayList<>();


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, boardNo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int commentNo = rs.getInt("comment_no");
                int commentBoardNo = rs.getInt("comment_board_no");
                String commentConent = rs.getString("comment_content");
                String userId = rs.getString("COMMENT_USER_ID");
                Date commentCreatedAt = rs.getDate("comment_created_at");
                Date commentUpdatedAt = rs.getDate("comment_updated_at");
                int commentParentNo = rs.getInt("COMMENT_PARENT_NO");
                int commentIdx = rs.getInt("comment_idx");
                int commentGroupNo = rs.getInt("comment_group_no");

                commentEntity = new Comment(commentNo, commentBoardNo, commentConent, userId, commentCreatedAt, commentUpdatedAt, commentParentNo  , commentIdx, commentGroupNo);
                comments.add(commentEntity);
            }
            if (rs.equals(null)){
                throw new NoSuchElementException("there are no articles");
            }

            return comments;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    // 게시글에 댓글이 하나만 달린경우 댓글 조회
    public Comment findOne(Integer boardNo) throws SQLException {
        String sql = "select * from comments where COMMENT_BOARD_NO  = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Comment commentEntity = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, boardNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int commentNoPk = rs.getInt("COMMENT_NO");
                int commentBoardNo = rs.getInt("comment_board_no");
                String commentConent = rs.getString("comment_content");
                String userId = rs.getString("COMMENT_USER_ID");
                Date commentCreatedAt = rs.getDate("comment_created_at");
                Date commentUpdatedAt = rs.getDate("comment_updated_at");
                int commentParentNo = rs.getInt("COMMENT_PARENT_NO");
                int commentIdx = rs.getInt("comment_idx");
                int commentGroupNo = rs.getInt("comment_group_no");

                commentEntity = new Comment(commentNoPk, commentBoardNo, commentConent, userId, commentCreatedAt, commentUpdatedAt, commentParentNo ,commentIdx, commentGroupNo);
            } else {
                throw new NoSuchElementException("there are no comment");
            }

            return commentEntity;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }


    // 댓글 수정
    public int update(Comment comment) throws SQLException {
        Date insertDate = new Date();
        Long timeInMilliSeconds = insertDate.getTime();
        java.sql.Date date = new java.sql.Date(timeInMilliSeconds);
        String sql = "update comments set comment_no = ?," +
                     "                     comment_board_no = ?," +
                     "                     comment_content = ?," +
                     "                     COMMENT_USER_ID = ?," +
                     "                     comment_created_at = ?," +
                     "                     comment_updated_at = ?," +
                     "                    comment_parent_no = ?," +
                     "                    comment_idx = ?," +
                     "                    comment_group_no = ?" +
                     "                where comment_no = ?";

        Connection con = null;
        PreparedStatement pstmt = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, comment.getCommentNo());
            pstmt.setInt(2, comment.getCommentBoardNo());
            pstmt.setString(3, comment.getCommentContent());
            pstmt.setString(4, comment.getUserId());
            pstmt.setDate(5, (java.sql.Date) comment.getCommentCreatedAt());
            pstmt.setDate(6, date);
            pstmt.setInt(7,comment.getCommentParentNo());
            pstmt.setInt(8, comment.getCommentIdx());
            pstmt.setInt(9, comment.getCommentGroupNo());
            pstmt.setInt(10, comment.getCommentNo());
            int count = pstmt.executeUpdate();
            log.info("update ={} ", count);
            return count;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }


    // 댓글 수정일 위한 단일 조회
    public Comment findOneComment(Integer commentNo , Integer boardNo) throws SQLException {
        String sql = "select * from comments where comment_no = ? and comment_board_no = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Comment commentEntity = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, commentNo);
            pstmt.setInt(2, boardNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int commentNoPk = rs.getInt("COMMENT_NO");
                int commentBoardNo = rs.getInt("comment_board_no");
                String commentConent = rs.getString("comment_content");
                String userId = rs.getString("COMMENT_USER_ID");
                Date commentCreatedAt = rs.getDate("comment_created_at");
                Date commentUpdatedAt = rs.getDate("comment_updated_at");
                int commentParentNo = rs.getInt("COMMENT_PARENT_NO");
                int commentIdx = rs.getInt("comment_idx");
                int commentGroupNo = rs.getInt("comment_group_no");

                commentEntity = new Comment(commentNoPk, commentBoardNo, commentConent, userId, commentCreatedAt, commentUpdatedAt, commentParentNo ,commentIdx, commentGroupNo);
            } else {
                throw new NoSuchElementException("there are no comment");
            }

            return commentEntity;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }


    // 댓글 삭제
    public void delete(int commentNo) throws SQLException {
        String sql = "delete from comments where comment_no = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,commentNo);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }


    // 게시글과 연고나된 댓글 전부 삭제
    public void deleteCommentsRelatedWithArticle(int boardNo) throws SQLException {
        String sql = "delete from comments where COMMENT_BOARD_NO = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,boardNo);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }




    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource);
    }




    private Connection getConnection() throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        return connection;
    }
}
