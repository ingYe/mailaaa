package com.example.mail.service.recv;

import javax.mail.*;
import java.util.Properties;

public class ImapUtils {


    private static Properties properties;

    static {
        String imapServer = "imap.qq.com";
        String user = "csuycd@qq.com";
        String pwd = "nwwfmsbdrvsecjif";

        properties = new Properties();
        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", imapServer);
        properties.setProperty("mail.imap.port", "143");
    }

    public static Folder getFolder() {

        Session session = Session.getInstance(properties);

        Store store = null;
        Folder folder = null;
        try {
            store = session.getStore("imap");
            store.connect("csuycd@qq.com", "nwwfmsbdrvsecjif");
            folder = store.getFolder("INBOX");
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return folder;
    }

    public static void close(Folder folder) {

        try {
            folder.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
