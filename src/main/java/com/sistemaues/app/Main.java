package com.sistemaues.app;

import com.sistemaues.app.dao.UserDao;
import com.sistemaues.app.service.UserService;
import com.sistemaues.app.ui.AuthFrame;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        UserService userService = new UserService(userDao);
        AuthFrame.launch(userService);
    }
}
