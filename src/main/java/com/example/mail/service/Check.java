package com.example.mail.service;

import com.example.mail.pojo.User;

public class Check {

    private static final String email = "csuycd@qq.com";
    private static final String pwd = "753159ddl";

    public static boolean checkPwd(String emailAddr, String password) {
        if(emailAddr.equals(email) && pwd.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

}
