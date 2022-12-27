package com.fif.app.finalexam;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO {

    @Inject
    private SqlSession sqlSession;


    @Override
    public List<UserDTO> selectUser() throws Exception {
        System.out.println("UserDAOImpl...selectUser()");
        
        return sqlSession.selectList("com.fif.app.mappers.userMapper.selectUser");
    } // selectMember()

}
