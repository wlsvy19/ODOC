package com.fif.app.finalexam;

import java.util.List;

public interface UserDAO {
    public List<UserDTO> selectUser() throws Exception;
}
