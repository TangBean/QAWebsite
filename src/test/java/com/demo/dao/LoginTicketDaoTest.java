package com.demo.dao;

import com.demo.model.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.rmi.runtime.Log;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTicketDaoTest {
    @Autowired
    private LoginTicketDao loginTicketDao;

    @Test
    public void addTicket() {
        LoginTicket loginTicket = new LoginTicket(1, "test", new Date());
        loginTicketDao.addTicket(loginTicket);
    }

    @Test
    public void selectByTicket() {
        LoginTicket loginTicket = loginTicketDao.selectByTicket("test");
        System.out.println(loginTicket);
    }

    @Test
    public void updateStatus() {
        loginTicketDao.updateStatus("test", -1);
    }
}