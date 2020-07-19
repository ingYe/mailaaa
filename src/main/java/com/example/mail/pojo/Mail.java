package com.example.mail.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mail {

    private Integer id;
    private String mailid;
    private String addr;
    private String mailsubject;
    private String fromtime;
    private String content;
    private Integer attach;
    private String filename;
    private String url;
}
