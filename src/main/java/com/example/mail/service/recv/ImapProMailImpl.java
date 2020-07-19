package com.example.mail.service.recv;

import com.example.mail.mapper.MailMapper;
import com.example.mail.pojo.Mail;
import com.sun.mail.imap.IMAPMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.annotation.PostConstruct;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
@Component
public class ImapProMailImpl implements ImapProMail{

    @Autowired
    MailMapper mailMapper;

    private static final String dataFormat = "yyyy-MM-dd HH:mm";

    @Override
    public void flushTable() {
        System.out.println("flush table");
        mailMapper.truncateTable();
    }

    private String path = System.getProperty("user.dir") + "\\output\\";

    /**
     * The PostConstruct annotation is used on a method that needs to be executed
     * after dependency injection is done to perform any initialization.
     * The init() method will not be called until any autowiring is done for the bean service.
     */

    @PostConstruct
    @Override
    public void getMailIfo() {

        Folder folder = null;

        try {
            folder = ImapUtils.getFolder();
            folder.open(Folder.READ_WRITE);

            Message[] messages = folder.getMessages();

            System.out.println(messages.length);
            System.out.println(folder.getUnreadMessageCount());

            for (Message message: messages) {
                IMAPMessage msg = (IMAPMessage) message;
                Mail mail = new Mail();

                boolean flag = false;

                String subject = MimeUtility.decodeText(msg.getSubject());

                log.info("subject: {}", subject);
                mail.setMailsubject(subject);
                log.info("sender:{}",getFrom(msg));
                mail.setAddr(getFrom(msg));
                log.info("sendTime:{}",getSendDate(msg));
                mail.setFromtime(getSendDate(msg));
                log.info("messageId:{}", msg.getMessageID());

                mail.setMailid(msg.getMessageID());

                flag = saveParts(msg.getContent(), subject, mail);
                log.info("flag:{}", flag);
                if (flag) {
                    mail.setAttach(1);
                } else {
                    mail.setAttach(0);
                }

                mailMapper.addMail(mail);

                System.out.println("-----------------------------");

            }

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }

    /**
     * 从服务器中删除邮件
     * @param id 本地数据库中邮件的id
     */
    @Override
    public void deleteMail(int id) {
        String mailid = mailMapper.getMailIdById(id);

        System.out.println("删除" + mailid);

        Folder folder = null;

        try {
            folder = ImapUtils.getFolder();

            folder.open(Folder.READ_WRITE);

            Message[] messages = folder.getMessages();

            System.out.println(messages.length);

            for (Message message : messages) {
                IMAPMessage msg = (IMAPMessage) message;

                if (msg.getMessageID().equals(mailid)) {
                    System.out.println("删除了：" + msg.getSubject());
                    msg.setFlag(Flags.Flag.DELETED, true);
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            folder.close(true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将发信时间格式化
     * @param msg
     * @return
     */
    private String getSendDate(IMAPMessage msg) {
        Date sendDate = null;
        try {
            sendDate = msg.getSentDate();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        String strSendDate = format.format(sendDate);
//        System.out.println("接收时间" + strSendDate);

        return strSendDate;
    }

    /**
     * 获得邮件的主题内容
     * 将附件保存到/output
     * @param content
     * @param filename
     * @param mail
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    private boolean saveParts(Object content, String filename, Mail mail)
            throws IOException, MessagingException
    {
        boolean flag = false;
        OutputStream out = null;
        InputStream in = null;
        try {
            if (content instanceof Multipart) {

                Multipart multi = ((Multipart)content);
                int parts = multi.getCount();

                for (int j=0; j < parts; ++j) {
                    MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);

                    if (part.getContent() instanceof Multipart) {
                        // part-within-a-part, do some recursion...
                        flag = true;
                        saveParts(part.getContent(), filename, mail);
                    }
                    else {
                        String extension = "";
                        if (part.isMimeType("text/html")) {
                            continue;
                        }
                        else {
                            if (part.isMimeType("text/plain")) {
                                log.info("content:{}",part.getContent());
                                mail.setContent((String) part.getContent());
                            }
                            else {
                                //  Try to get the name of the attachment
                                extension = part.getDataHandler().getName();
                                filename = filename + "_" + extension;
                                mail.setFilename(filename);
                                mail.setUrl(path + filename);
                                System.out.println("... " + path + filename);

                                out = new FileOutputStream(new File( path + filename));
                                in = part.getInputStream();
                                int k;
                                while ((k = in.read()) != -1) {
                                    out.write(k);
                                }
                            }
                        }
                    }
                }
            }
        }
        finally {
            if (in != null) { in.close(); }
            if (out != null) { out.flush(); out.close(); }
        }

        return flag;
    }

    /**
     * 获得发送方的邮箱地址
     * @param msg
     * @return
     */
    private String getFrom(IMAPMessage msg) {
        InternetAddress[] addresses = new InternetAddress[0];
        try {
            addresses = (InternetAddress[]) msg.getFrom();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        String from = addresses[0].getAddress();

        if (from == null) {
            System.out.println("无法获得发送者");
            return null;
        } else {
//            System.out.println(from);
            return from;
        }
    }

}
