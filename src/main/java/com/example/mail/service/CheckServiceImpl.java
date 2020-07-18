package com.example.mail.service;

import com.example.mail.mapper.UserMapper;
import com.example.mail.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class CheckServiceImpl implements CheckService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        return userMapper.login(username, password);
    }
}
