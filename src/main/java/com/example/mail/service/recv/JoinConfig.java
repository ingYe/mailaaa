package com.example.mail.service.recv;

import com.example.mail.pojo.Mail;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class JoinConfig implements PopMail{
    private static final String host = "pop.qq.com";
    private static final String username = "csuycd@qq.com";
    private static final String password = "nwwfmsbdrvsecjif";

    private static Message[] message;
    private static Store store;
    private static Properties properties;

    static {
        properties = new Properties();
        properties.setProperty("mail.pop3.host", "pop.qq.com");
        properties.setProperty("mail.pop2.port", "995");
        properties.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "true");
        properties.setProperty("mail.pop3.socketFactory.port", "995");
    }

    public void getInfo() {

        try {
            Session session = Session.getDefaultInstance(properties, null);
            store = session.getStore("pop3");
            store.connect(host, username, password);
            Folder folder = store.getFolder("inbox");
            folder.open(Folder.READ_WRITE);

            message = folder.getMessages();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        List<Mail> list = getMailInfo(message);

    }

    public static List<Mail> getMailInfo(Message[] message) {
        MailInfo rec = null;
        int attach = 0;

        List<Mail> list = new ArrayList<>();
        if (null != message) {
            for (int i = 0; i < message.length; i++) {

                try {

                    rec = new MailInfo((MimeMessage) message[i]);

                    //邮件正文
                    rec.getMailContext((Part) message[i]);
                    String str = rec.getBodyText();
                    str = toDist(str);


                    if(rec.isContainAttach((Part) message[i]))  {
                        attach = 1;
                    }

                    Mail mail = new Mail(null, rec.getMessageId(), rec.getFrom(), rec.getSubject(), rec.getSendDate(), str, attach, null, null);
//                    Mail mail = new Mail(id,"cssycd@gmail.com", "tp", "2020-07-17 00:57", "tpp", 1);
                    System.out.println(mail);

                    list.add(mail);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private static String toDist(String str) {
        Pattern pattern = Pattern.compile(">.*<");

        Matcher matcher = pattern.matcher(str);

        String dist = null;
        if (matcher.find()) {
            dist = matcher.group(0);
        }

        StringBuffer stringBuffer = new StringBuffer(dist);

        stringBuffer.deleteCharAt(0);
        stringBuffer.deleteCharAt(stringBuffer.length()-1);

        return stringBuffer.toString();
    }

}
