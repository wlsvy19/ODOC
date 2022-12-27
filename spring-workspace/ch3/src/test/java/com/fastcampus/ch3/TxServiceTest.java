package com.fastcampus.ch3;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class TxServiceTest {
    @Autowired
    TxService txService;

    @Test
    public void insertA1WithoutTxTest() throws Exception {
        txService.insertA1WithoutTx();
    }

    @Test
    public void insertA1WithTxFailTest() throws Exception {
        txService.insertA1WithTxFail();
    }

    @Test
    public void insertA1WithTxSuccessTest() throws Exception {
        txService.insertA1WithTxSuccess();
    }

    // propagation 실습
    @Test
    public void insertA1WithTx() throws Exception {
        txService.insertA1WithTx();
    }
}