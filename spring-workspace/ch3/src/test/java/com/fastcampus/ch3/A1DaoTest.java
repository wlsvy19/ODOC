package com.fastcampus.ch3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

/**
 * 트랜잭션 매니저 테스트
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class A1DaoTest {
    @Autowired
    A1Dao a1Dao;

    @Autowired
    DataSource ds;

    @Autowired
    DataSourceTransactionManager tm;

    @Test
    public void insertTest() throws Exception {
        // 1. TxManager 생성
        //PlatformTransactionManager tm = new DataSourceTransactionManager(ds); -> root-context.xml에 bean으로 등록
        TransactionStatus status = tm.getTransaction(new DefaultTransactionDefinition());

        // 2. Tx 시작
        try {
            a1Dao.deleteAll();

            // 아래 두개의 코드가 하나의 트랜잭션으로 처리 하고 싶음
            a1Dao.insert(1, 100); // 성공
            a1Dao.insert(1, 200); // 실패 -> 트랜잭션의 결과는 실패 나와야 함
            tm.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            tm.rollback(status); // 트랜잭션 실패시 롤백
        } finally {
        }
    }
}