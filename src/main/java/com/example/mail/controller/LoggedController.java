package com.example.mail.controller;

import com.example.mail.mapper.MailMapper;
import com.example.mail.pojo.Mail;
import com.example.mail.service.recv.ImapProMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/logged")
public class LoggedController {

    @Autowired
    private MailMapper mailMapper;

    @Autowired
    ImapProMail imapProMail;

    @GetMapping("/send")
    public String send(Model model) {

        List<Mail> list = mailMapper.queryMailList();
        model.addAttribute("mails", list);

        return "send";
    }

    Mail mail = null;
    @GetMapping("/del")
    public String delete(int id) {
        imapProMail.deleteMail(id);

        mail = mailMapper.getMailById(id);
        mailMapper.deleteMail(id);
//        DeleteMail.deleteMail(mail);

        return "redirect:/logged/send";
    }

    /**
     * 展示邮件详细信息
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/show")
    public String show(int id, Model model) {

        mail = mailMapper.getMailById(id);
        model.addAttribute("mails", mail);

        return "show";
    }

    @GetMapping("/flush")
    public String flush() {
        mailMapper.truncateTable();
//        List<Mail> list = JoinConfig.getInfo();

        imapProMail.getMailIfo();
//        for (Mail mail : list) {
//            System.out.println(mail);
//        }

        return  "redirect:/logged/send";
    }
}
