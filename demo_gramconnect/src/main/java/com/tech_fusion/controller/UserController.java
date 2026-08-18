package com.tech_fusion.controller;

import com.tech_fusion.dao.UserDao;
import com.tech_fusion.model.user.User;

public class UserController {
    
    UserDao userDao = new UserDao();

    public void createUsers(String email, String name, String mobile, String villagename, String role) {

        User user = new User(name,mobile,email,villagename,role);
        userDao.setUsers(user);
    }
}
