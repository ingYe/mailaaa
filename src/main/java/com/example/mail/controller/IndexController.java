package com.example.mail.controller;

import com.example.mail.mapper.MailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class IndexController {

    @Autowired
    private MailMapper mailMapper;

    @GetMapping("/index")
    public String index() {

        return "login";
    }
}
