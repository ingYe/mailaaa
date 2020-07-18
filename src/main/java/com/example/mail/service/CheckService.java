package com.example.mail.service;

import com.example.mail.pojo.User;

public interface CheckService {

    User login(String username, String password);
}
