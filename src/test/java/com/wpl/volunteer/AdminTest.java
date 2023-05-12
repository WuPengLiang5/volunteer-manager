package com.wpl.volunteer;

import com.wpl.volunteer.dao.AdminDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminTest {

    @Autowired
    AdminDao adminDao;

    @Test
    void getAdminById(){
        System.out.println(adminDao.selectById(1));
    }
}
