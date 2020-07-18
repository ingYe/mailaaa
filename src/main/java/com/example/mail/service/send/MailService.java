package com.example.mail.service.send;

import org.springframework.web.multipart.MultipartFile;

public interface MailService {

    void sendMail(String distaddr, String topic, String context);

    void sendMail(String distaddr, String topic, String context, MultipartFile multipartFile);
}
