package com.example.mail.service.recv;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MailInfo {

    private MimeMessage mimeMessage = null;
    private String savePath = "G:\\summer\\front\\save\\";
    private StringBuffer bodyText = new StringBuffer();
    private String dataFormat = "yyyy-MM-dd HH:mm";

    public MailInfo(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public void setMimeMessage(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
    }

    public String getFrom() throws MessagingException {
        InternetAddress[] addresses = (InternetAddress[]) mimeMessage.getFrom();
        String from = addresses[0].getAddress();

        if (from == null) {
            System.out.println("无法获得发送者");
            return null;
        } else {
//            System.out.println(from);
            return from;
        }
    }

    public String getFromName() throws MessagingException {
        InternetAddress[] addresses = (InternetAddress[]) mimeMessage.getFrom();
        String person = addresses[0].getPersonal();

        if (person == null) {
            System.out.println("无法获得发送者姓名");
            return null;
        } else {
            return person;
        }
    }

    public String getSubject() throws MessagingException {

        String subject = "";
        try {
            subject = MimeUtility.decodeText(mimeMessage.getSubject());
//            System.out.println("转换之后的subject");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (subject == null) {
            return "么得";
        } else {
            return subject;
        }
    }

    public String getSendDate() throws MessagingException {
        Date sendDate = mimeMessage.getSentDate();
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        String strSendDate = format.format(sendDate);
//        System.out.println("接收时间" + strSendDate);

        return strSendDate;
    }

    public String getBodyText() {
        return bodyText.toString();
    }

    public void getMailContext(Part part) throws Exception {
        String contentType = part.getContentType();
        System.out.println("邮件mimeType的类型：" + contentType);

        int nameIndex = contentType.indexOf("name");

        boolean conName = false;

        if (nameIndex != -1) {
            conName = true;
        }

        if (part.isMimeType("text/plain") && conName == false) {
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("text/html") && conName == false) {
            bodyText.append((String) part.getContent());
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int counts = multipart.getCount();
            for (int i = 0; i < counts; i++) {
                getMailContext(multipart.getBodyPart(i));
            }
        } else if (part.isMimeType("message/rfc832")) {
            getMailContext((Part) part.getContent());
        }
    }
    public String getMessageId() throws MessagingException{

        String messageId = mimeMessage.getMessageID();
        System.out.println("邮件ID" + messageId);

        return messageId;
    }

    public boolean isContainAttach(Part part) throws Exception {

        boolean attachFlag = false;

        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();

                if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))))
                    attachFlag = true;
                else if (bodyPart.isMimeType("multipart/*"))
                {
                    attachFlag = isContainAttach((Part) bodyPart);
                }
                else
                {
                    String conType = bodyPart.getContentType();

                    if (conType.toLowerCase().indexOf("application") != -1)
                        attachFlag = true;
                    if (conType.toLowerCase().indexOf("name") != -1)
                        attachFlag = true;
                }
            }
        }
        else if (part.isMimeType("message/rfc822"))
        {
            attachFlag = isContainAttach((Part) part.getContent());
        }
        return attachFlag;

    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**
     * 保存附件
     */
    public void saveAttachMent(Part part) throws Exception{

        String fileName = "";
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();

            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disposition = bodyPart.getDisposition();

                if (disposition != null && (disposition.equals(Part.ATTACHMENT)) ||
                        (disposition.equals(Part.INLINE))) {
                    fileName = bodyPart.getFileName();
                    if (fileName.toLowerCase().indexOf("gb2312") != -1) {
                        fileName = MimeUtility.decodeText(fileName);
                    }

                    saveFile(fileName, bodyPart.getInputStream());
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachMent(bodyPart);
                } else {
                    fileName = bodyPart.getFileName();
                    if (fileName != null && fileName.toLowerCase().indexOf("GB2312") != -1) {
                        fileName = MimeUtility.decodeText(fileName);
                        saveFile(fileName, bodyPart.getInputStream());
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachMent((Part) part.getContent());
        }

    }


    public String getSavePath() {
        return savePath;
    }

    public void saveFile(String filename, InputStream inputStream) throws Exception{

        String storeDir = getSavePath();

        File storeFile = new File(storeDir + filename);
        System.out.println("文件的保存位置" + storeFile.toString());

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(storeFile));
            bis = new BufferedInputStream(inputStream);

            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件报错失败");
        } finally {
            bos.close();
            bis.close();
        }
    }
}
