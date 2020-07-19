package com.example.mail.mapper;

import com.example.mail.pojo.Mail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MailMapper {

    List<Mail> queryMailList();

    Mail getMailById(Integer id);

    int addMail(Mail mail);

    int deleteMail(Integer id);

    int truncateTable();

    String getMailIdById(Integer id);

    String getUrlById(Integer id);

}
