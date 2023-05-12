package com.wpl.volunteer;

import com.wpl.volunteer.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserDao userDao;

    @Test
    void getUserById(){
        System.out.println(userDao.selectById(1));
    }
}
