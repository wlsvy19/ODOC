package com.fastcampus.ch3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

/**
 * 서비스: 비즈니스 로직
 */
@Service
public class TxService {
    @Autowired
    A1Dao a1Dao;

    @Autowired
    B1Dao b1Dao;

    @Autowired
    DataSource ds;

    public void insertA1WithoutTx() throws Exception {
        a1Dao.insert(1, 100);
        a1Dao.insert(2, 200);
    }

    // @Transactional // RuntimeException, Error만 rollback 
    @Transactional(rollbackFor = Exception.class) // rollbackFor을 써줘야 Exception을 rollback함
    public void insertA1WithTxFail() throws Exception {
        a1Dao.insert(1, 100);
        // throw new RuntimeException(); // 롤백처리o, 값 안들어감
        // throw new Exception(); // 롤백처리 x, 값 들어감
        a1Dao.insert(1, 200);
    }

    @Transactional
    public void insertA1WithTxSuccess() throws Exception {
        a1Dao.insert(1, 100);
        a1Dao.insert(2, 200);
    }

    // propagation 실습 - !! 같은 클래스에 속한 메서드 끼리 호출(내부호출)은 @Transactional 동작하지 않음
    // REQUIRED: 하나의 Tx로 묶임
    // REQUIRED_NEW: 새로운 Tx로 묶임

/*    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertA1WithTx() throws Exception {
        a1Dao.insert(1, 100);
        insertB1WithTx();
        a1Dao.insert(1, 100);
    }*/

/*    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertB1WithTx() throws Exception {
        b1Dao.insert(1, 100);
        b1Dao.insert(2, 100);
    }*/

    public void insertA1WithTx() throws Exception {
        PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
        DefaultTransactionDefinition txd = new DefaultTransactionDefinition();
        txd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = tm.getTransaction(txd);

        try {
            a1Dao.insert(1, 100);
            insertB1WithTx();
            a1Dao.insert(2, 200);
            tm.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            tm.rollback(status);
        } finally {
        }
    }

    public void insertB1WithTx() throws Exception {
        PlatformTransactionManager tm = new DataSourceTransactionManager(ds);
        DefaultTransactionDefinition txd = new DefaultTransactionDefinition();
        txd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 새로운 Tx 경계 설정
        TransactionStatus status = tm.getTransaction(txd);

        try {
            b1Dao.insert(1, 100);
            b1Dao.insert(1, 200);
            tm.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            tm.rollback(status);
        } finally {
        }
    }

}
