package com.example.mail.mapper;

import com.example.mail.pojo.Mail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MailMapper {

    List<Mail> queryMailList();

    Mail getMailById(int id);

    int addMail(Mail mail);

    int deleteMail(int id);

    int truncateTable();

    String getMailIdById(int id);

}
