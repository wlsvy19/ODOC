package com.app.myapp.service;

import java.util.ArrayList;

import com.app.myapp.dto.UserDTO;

// 추상 메소드
public interface UserService {
    // 유저 ID 조회
    public ArrayList<UserDTO> list();
}
