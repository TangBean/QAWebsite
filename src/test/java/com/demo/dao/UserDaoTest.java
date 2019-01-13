package com.demo.dao;

import com.demo.QAWebsiteApplication;
import com.demo.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void addUser() {
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("User" + (i + 1));
            user.setPassword("1234_" + (i + 1));
            user.setSalt("salt_" + (i + 1));
            Random random = new Random();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                    random.nextInt(1000)));
            userDao.addUser(user);
        }
    }

    @Test
    public void searchById() {
    }

    @Test
    public void updatePassword() {
    }

    @Test
    public void deleteById() {
    }
}