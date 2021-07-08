package com.app.myapp.service;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.myapp.dao.UserDAO;
import com.app.myapp.dto.UserDTO;

// 구현 클래스
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public ArrayList<UserDTO> list() {
        System.out.println("UserServiceImpl.list() start...");

        // xml namespace에 저장된 sql들 가져옴
        // xml -> UserDAO -> Service
        UserDAO userDAO = sqlSession.getMapper(UserDAO.class);

        // mapper에서 id값에 해당하는 쿼리문 가져옴
        ArrayList<UserDTO> list = userDAO.list();

        System.out.println("UserServiceImpl.list() end...");

        return list;
    }

}
