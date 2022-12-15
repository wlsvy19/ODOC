package com.fif.cf.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fif.cf.dao.LoginDAO;
import com.fif.cf.dto.LoginDTO;

@Service("LoginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public ArrayList<LoginDTO> list() {
        LoginDAO loginDAO = sqlSession.getMapper(LoginDAO.class);

        ArrayList<LoginDTO> list = loginDAO.list();
        return list;
    }

}
