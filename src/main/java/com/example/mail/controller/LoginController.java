package com.example.mail.controller;

import com.example.mail.pojo.User;
import com.example.mail.service.CheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    CheckService checkService;

    @PostMapping(value = "/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("code") String  code,
                        Map<String, Object> map, HttpSession session) {
//        User user = new User(username, password);

        if (!code.equals(CodeController.code)) {

            log.info(username + "的验证码错误");
            map.put("msg", "验证码错误");
            return "login";
        }

        User user = checkService.login(username, password);

        if (user != null ) {
            session.setAttribute("loginUser", username);
            System.out.println(username + " has logined successfully");
            return "redirect:/logged/send";
        } else {
            log.info(username + "密码错误");
            map.put("msg", "用户名或密码错误");
            return "login";
        }

    }
}
