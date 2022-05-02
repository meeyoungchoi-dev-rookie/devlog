package com.blog.devlog.repository;


import com.blog.devlog.connection.DBConnectionUtil;
import com.blog.devlog.domain.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

@Slf4j
@Repository
public class ArticleRepository {

    public Article save(Article article) throws SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd KK:mm:ss");

        Date insertDate = new Date();
        Long timeInMilliSeconds = insertDate.getTime();
        java.sql.Date date = new java.sql.Date(timeInMilliSeconds);
        String sql = "insert into boards(board_no, title, content , user_id, board_created_at, board_updated_at) VALUES(? ,? ,? ,? ,? ,?)";

        Connection con = null;
        PreparedStatement pstmt = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, article.getBoardNo());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContent());
            pstmt.setString(4, article.getUserId());
            pstmt.setDate(5, date);
            pstmt.setDate(6, date);
            int count = pstmt.executeUpdate();
            log.info("insert ={} ", count);
            return article;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Article findById(int articleNo) throws SQLException {

        String sql = "select * from boards where board_no = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Article article = null;


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, articleNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int article_id = rs.getInt("board_no");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String userId = rs.getString("user_id");

                article = new Article(article_id, title, content, userId);
            } else {
                throw new NoSuchElementException("article not found articleNo=" + articleNo);
            }

            return article;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    void update(Article article) throws SQLException {

        String sql = "update boards set title = ? , content = ?  where board_no = ?";


        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setInt(3, article.getBoardNo());
            int resultSize =  pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }




    }

    void delete(int boardsNo) throws SQLException {
        String sql = "delete from boards where board_no = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,boardsNo);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }





    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }


        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }






    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }


}
