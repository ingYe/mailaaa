package com.example.mail.service.recv.deletetest;

import com.example.mail.service.recv.ImapUtils;
import com.sun.mail.imap.IMAPMessage;

import javax.mail.*;
import java.util.Properties;

public class Delete {

    public static void main(String[] args) {

        String imapServer = "imap.qq.com";
        String user = "csuycd@qq.com";
        String pwd = "nwwfmsbdrvsecjif";

        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", imapServer);
        properties.setProperty("mail.imap.port", "143");

        Session session = Session.getInstance(properties);

        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore("imap");
            store.connect("csuycd@qq.com", "nwwfmsbdrvsecjif");
            folder = store.getFolder("INBOX");


            folder = ImapUtils.getFolder();
            folder.open(Folder.READ_WRITE);

            Message[] messages = folder.getMessages();

            System.out.println(messages.length);

            for (Message message : messages) {
                IMAPMessage msg = (IMAPMessage) message;


//                    msg.setFlag(Flags.Flag.DELETED, true);
                System.out.println(msg.getSubject());
                System.out.println(msg.getMessageID());

            }

            folder.close(true);

            store.close();

        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

