package com.example.mail.controller;

import com.example.mail.mapper.MailMapper;
import com.example.mail.pojo.Mail;
import com.example.mail.service.recv.ImapProMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
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
    public String delete(Integer id) {
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
    public String show(Integer id, Model model) {

        mail = mailMapper.getMailById(id);
        model.addAttribute("infos", mail);

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

    @ResponseBody
    @GetMapping("/download")
    public void downloadFile(Integer id, HttpServletResponse response) {

        String url = mailMapper.getUrlById(id);

        File file = new File(url);

        if (file.exists()) {

            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);

            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

            response.setContentLength((int) file.length());

            try {
                InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
