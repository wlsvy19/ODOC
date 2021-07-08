package com.lee.practice.mybatisdemo.repository;

import com.lee.practice.mybatisdemo.dto.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    List<User> getUserHistory();
    User findUser(String id);
}
