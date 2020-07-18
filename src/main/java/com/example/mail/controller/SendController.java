package com.example.mail.controller;


import com.example.mail.mapper.MailMapper;
import com.example.mail.pojo.Mail;
import com.example.mail.service.send.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/logged")
public class SendController {

    @Autowired
    private MailService mailService;

    @Autowired
    MailMapper mailMapper;

    @PostMapping("/send")
    public String senMail(String distaddr, String topic, String context, boolean flag,
                           @RequestParam("file") MultipartFile file, Model model)
    {

        if (!flag) {
            mailService.sendMail(distaddr, topic, context);
        } else {
            mailService.sendMail(distaddr, topic, context, file);
        }

//        List<Mail> recList = new Inbox().getInfo();
//        model.addAttribute("mails", recList);

        List<Mail> recList = mailMapper.queryMailList();
        model.addAttribute("mails", recList);

        return "send";
    }
}
