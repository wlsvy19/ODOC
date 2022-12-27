package com.lee.practice.mybatisdemo.service;

import com.lee.practice.mybatisdemo.dto.Member;
import com.lee.practice.mybatisdemo.dto.User;
import com.lee.practice.mybatisdemo.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getUserHistory() {
        return userMapper.getUserHistory();
    }

    public User findUser(String id) {
        return userMapper.findUser(id);
    }
}
