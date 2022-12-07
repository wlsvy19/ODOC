package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// DAO: 데이터 접근 계층
@Repository
public class B1Dao {
    @Autowired
    DataSource ds;

    public int insert(int key, int value) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // conn = ds.getConnection();
            conn = DataSourceUtils.getConnection(ds); // 트랜잭션은 하나의 커넥션으로 관리 -> TxManager 사용
            System.out.println("conn = " + conn);
            pstmt = conn.prepareStatement("INSERT INTO b1 VALUES(?, ?)");

            pstmt.setInt(1, key);
            pstmt.setInt(2, value);

            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            // close(conn, pstmt);
            close(pstmt);
            DataSourceUtils.releaseConnection(conn, ds); // TxManager가 connection을 끊어야 하는지 판단함
        }
    }

    public void deleteAll() throws SQLException {
        Connection conn = ds.getConnection(); // deleteAll()은 Tx와 별개 동작 이므로 TxManager가 필요 없음
        String sql = "DELETE FROM a1";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();
        close(pstmt);
    }

    private void close(AutoCloseable... acs) { // 가변인자
        for (AutoCloseable ac : acs)
            try {
                if (ac != null) ac.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
