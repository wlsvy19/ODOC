package com.app.myapp.dao;

import java.util.ArrayList;

import com.app.myapp.dto.UserDTO;

public interface UserDAO {
    // 인터페이스에 추상 메소드
    
    // 메소드는 mapper에서 id에 와 동일해야 함
    public ArrayList<UserDTO> list();
}
