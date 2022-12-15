package com.fastcampus.ch3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.assertTrue;

// ApplicationContext 자동 생성
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class DBConnectionTest2Test {
    @Autowired
    DataSource ds;

    @Test
    public void insertUserTest() throws SQLException {
        User user = new User("asdf1", "1234", "leejp", "bbb@bbb.com", new Date(), "instagram", new Date());
        deleteAll();
        int rowCnt = insertUser(user);

        assertTrue(rowCnt == 1);
    }

    @Test
    public void selectUserTest() throws SQLException {
        deleteAll();
        User user = new User("asdf1", "1234", "leejp", "bbb@bbb.com", new Date(), "instagram", new Date());
        int rowCnt = insertUser(user);
        User user2 = selectUser("asdf1");

        assertTrue(user2.getId().equals("asdf1"));
    }

    @Test
    public void deleteUserTest() throws SQLException {
        deleteAll();
        int rowCnt = deleteUser("asdf");
        assertTrue(rowCnt == 0);
        User user = new User("asdf1", "1234", "leejp", "bbb@bbb.com", new Date(), "instagram", new Date());

        rowCnt = insertUser(user);
        assertTrue(rowCnt == 1);

        rowCnt = deleteUser(user.getId());
        assertTrue(rowCnt == 1);

        assertTrue(selectUser(user.getId()) == null);
    }

    public int updateUser(User user) throws SQLException {
        Connection conn = ds.getConnection();
        String sql = "UPDATE user_info SET id = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection 공격 방지, 성능 향상(SQL문 재사용 가능)
        return 0;
    }

    public int deleteUser(String id) throws SQLException {
        Connection conn = ds.getConnection();
        String sql = "DELETE FROM user_info WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection 공격 방지, 성능 향상(SQL문 재사용 가능)
        pstmt.setString(1, id);
        // int rowCnt = pstmt.executeUpdate();
        // return rowCnt;
        return pstmt.executeUpdate(); // INSERT, DELETE, UPDATE 할때 사용

    }

    public User selectUser(String id) throws SQLException {
        Connection conn = ds.getConnection();
        String sql = "SELECT * FROM user_info WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection 공격 방지, 성능 향상(SQL문 재사용 가능)
        pstmt.setString(1, id);
        ResultSet rs = pstmt.executeQuery(); // 단순조회: executeQuery(), ResultSet 은 테이블 형태
        if (rs.next()) { // 쿼리 결과가 있으면
            User user = new User();
            user.setId(rs.getString(1));
            user.setPwd((rs.getString(2)));
            user.setName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setBirth(new Date(rs.getDate(5).getTime()));
            user.setSns(rs.getString(6));
            user.setReg_date(new Date(rs.getTimestamp(7).getTime()));
            return user;

        }
        return null;
    }

    private void deleteAll() throws SQLException {
        Connection conn = ds.getConnection();
        String sql = "DELETE FROM user_info";
        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection 공격 방지, 성능 향상(SQL문 재사용 가능)
        pstmt.executeUpdate(); // INSERT, DELETE, UPDATE 할때 사용
    }

    @Test
    public void transactionTest() throws Exception {
        Connection conn = null;
        try {
            deleteAll();
            conn = ds.getConnection();
            conn.setAutoCommit(false);//트랜잭션 테스트를 위해 자동커밋 x


            String sql = "INSERT INTO user_info VALUES(?, ?, ?, ?, ?, ?, now())";

            PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection 공격 방지, 성능 향상(SQL문 재사용 가능)
            pstmt.setString(1, "qwer");
            pstmt.setString(2, "1234");
            pstmt.setString(3, "abc");
            pstmt.setString(4, "aaa@aaa.com");
            pstmt.setDate(5, new java.sql.Date(new Date().getTime()));
            pstmt.setString(6, "facebook");

            int rowCnt = pstmt.executeUpdate();

            pstmt.setString(1, "qwer");
            rowCnt = pstmt.executeUpdate();

            conn.commit(); // 2번의 insert 가 성공하면 커밋
        } catch (SQLException e) {
            conn.rollback(); // 실패하면 롤백
            throw new RuntimeException(e);
        } finally {
        }
    }

    public int insertUser(User user) throws SQLException {
        Connection conn = ds.getConnection();

        //INSERT INTO user_info VALUES('qwer3', '1234', '이진표', 'aaa@aaa.com', '1992.05.19', 'fb', now());

        String sql = "INSERT INTO user_info VALUES(?, ?, ?, ?, ?, ?, now())";

        PreparedStatement pstmt = conn.prepareStatement(sql); // SQL Injection 공격 방지, 성능 향상(SQL문 재사용 가능)
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getPwd());
        pstmt.setString(3, user.getName());
        pstmt.setString(4, user.getEmail());
        pstmt.setDate(5, new java.sql.Date(user.getBirth().getTime()));
        pstmt.setString(6, user.getSns());

        int rowCnt = pstmt.executeUpdate(); // INSERT, DELETE, UPDATE 할때 사용, 단순조회: executeQuery()


        return rowCnt;
    }

    @Test // @Test 사용 시 메소드가 public void 여야함
    public void main() throws SQLException {
//        ApplicationContext ac = new GenericXmlApplicationContext("file:src/main/webapp/WEB-INF/spring/**/root-context.xml");
//        DataSource ds = ac.getBean(DataSource.class);

        Connection conn = ds.getConnection(); // 데이터베이스의 연결을 얻는다.

        System.out.println("conn = " + conn);
        assertTrue(conn != null); // assert가 무조건 들어가야 테스트 됨, 괄호안의 조건식이 true면 테스트 성공 아니면 성공
    }
}