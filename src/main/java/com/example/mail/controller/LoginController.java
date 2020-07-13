package com.example.mail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index() {
        return "login.html";
    }


    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    public void check(HttpServletRequest request, HttpSession session) {

        String addr = request.getParameter("email");
        String pass = request.getParameter("password");

        System.out.println(addr + "  " + pass);
    }

}
