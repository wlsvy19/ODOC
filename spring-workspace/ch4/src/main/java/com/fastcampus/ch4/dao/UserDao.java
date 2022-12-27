package com.fastcampus.ch4.dao;

import com.fastcampus.ch4.domain.*;

/**
 * DAO: Persistence Layer, DB 조작 로직, 인터페이스로 구현
 */
public interface UserDao {
    User selectUser(String id) throws Exception;

    int deleteUser(String id) throws Exception;

    int insertUser(User user) throws Exception;

    int updateUser(User user) throws Exception;

    int count() throws Exception;

    void deleteAll() throws Exception;
}