package com.fif.app.finalexam;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Inject
    private UserDAO dao;

    @Override
    public List<UserDTO> selectUser() throws Exception {
        return dao.selectUser();
    } // selectUser()

}
