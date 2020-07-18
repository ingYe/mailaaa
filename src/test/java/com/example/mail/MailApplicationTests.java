package com.example.mail;

import com.example.mail.mapper.MailMapper;
import com.example.mail.service.CheckService;
import com.example.mail.service.recv.ImapProMail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailApplicationTests {

	@Autowired
	MailMapper mailMapper;

	@Autowired
	ImapProMail imapProMail;

	@Autowired
	CheckService checkService;

	@Test
	public void getMail() {
		imapProMail.getMailIfo();
	}

	@Test
	public void showUser() {

		System.out.println(checkService.login("11","22"));
	}

	@Test
	public void dele() {

		System.out.println(mailMapper.getMailIdById(1));
	}


}
