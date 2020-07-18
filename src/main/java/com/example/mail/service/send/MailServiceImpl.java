package com.example.mail.service.send;

import com.example.mail.service.send.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    JavaMailSender mailSender;

    @Override
    public void sendMail(String distaddr, String topic, String context) {

        System.getProperties().setProperty("mail.mime.splitlongparameters", "false");
        MimeMessageHelper message;
        try {
            MimeMessage mimeMailMessage = this.mailSender.createMimeMessage();
            message = new MimeMessageHelper(mimeMailMessage);

            message.setFrom("csuycd@qq.com");
            message.setTo(distaddr);
            message.setSubject(topic);
            message.setText(context);

            this.mailSender.send(mimeMailMessage);
            System.out.println("发往 " + distaddr + " 的邮件已发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendMail(String distaddr, String topic, String context, MultipartFile multipartFile) {
        File file = null;
        try {
            file = multipartFileToFile(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.getProperties().setProperty("mail.mime.splitlongparameters", "false");
        MimeMessageHelper message;
        try {
            MimeMessage mimeMailMessage = this.mailSender.createMimeMessage();
            message = new MimeMessageHelper(mimeMailMessage, true);

            message.setFrom("csuycd@qq.com");
            message.setTo(distaddr);
            message.setSubject(topic);
            message.setText(context);

            message.addAttachment(file.getName(), file);

            this.mailSender.send(mimeMailMessage);
            System.out.println("发往 " + distaddr + " 的邮件已发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

