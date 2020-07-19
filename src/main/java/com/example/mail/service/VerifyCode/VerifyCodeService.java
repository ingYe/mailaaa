package com.example.mail.service.VerifyCode;

import com.example.mail.pojo.VerifyCode;

import java.io.IOException;
import java.io.OutputStream;

public interface VerifyCodeService {

    String generate(int width, int height, OutputStream outputStream) throws IOException;

    VerifyCode generate(int width, int height);
}
