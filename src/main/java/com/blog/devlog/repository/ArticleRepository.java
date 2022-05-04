package com.blog.devlog.repository;


import com.blog.devlog.connection.DBConnectionUtil;
import com.blog.devlog.domain.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleRepository {

    private final DataSource dataSource;

    public Article save(Article article) throws SQLException {
        Date insertDate = new Date();
        Long timeInMilliSeconds = insertDate.getTime();
        java.sql.Date date = new java.sql.Date(timeInMilliSeconds);
        String sql = "insert into boards(board_no, title, content , user_id, board_created_at, board_updated_at) VALUES(board_seq.NEXTVAL ,? ,? ,? ,? ,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getUserId());
            pstmt.setDate(4, date);
            pstmt.setDate(5, date);
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

    public List<Article> findAll() throws SQLException {

        String sql = "select * from boards";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Article article = null;
        List<Article> articles = new ArrayList<>();


        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int article_id = rs.getInt("board_no");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String userId = rs.getString("user_id");

                article = new Article(article_id, title, content, userId);
                articles.add(article);
            }
            if (rs.equals(null)){
                throw new NoSuchElementException("there are no articles");
            }

            return articles;
        } catch (SQLException e) {
            log.error("db error", e);
            e.printStackTrace();
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }






    public void update(Article article) throws SQLException {

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

    public void delete(int boardsNo) throws SQLException {
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
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeConnection(con);
        JdbcUtils.closeStatement(stmt);
    }

    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        return connection;
    }


}
