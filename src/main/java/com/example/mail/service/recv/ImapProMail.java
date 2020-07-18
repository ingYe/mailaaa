package com.example.mail.service.recv;

public interface ImapProMail {

    public void flushTable();

    public void getMailIfo();

    public void deleteMail(int id);
}
