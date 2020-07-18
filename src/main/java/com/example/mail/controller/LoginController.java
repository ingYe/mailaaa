package com.example.mail.controller;

import com.example.mail.pojo.User;
import com.example.mail.service.Check;
import com.example.mail.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    CheckService checkService;

    @PostMapping(value = "/login")
    public String login(String username, String password) {
//        User user = new User(username, password);

        User user = checkService.login(username, password);

        if (user != null ) {
            System.out.println(username + " has logined successfully");
            return "redirect:/logged/send";
        } else {
            return "redirect:/index";
        }
    }
}
