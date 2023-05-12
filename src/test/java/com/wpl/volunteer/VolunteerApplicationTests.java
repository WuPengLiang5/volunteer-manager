package com.wpl.volunteer;

import com.wpl.volunteer.dao.ActivityDao;
import com.wpl.volunteer.entity.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VolunteerApplicationTests {

    @Autowired
    ActivityDao activityDao;

    @Test
    void contextLoads() {
    }

    @Test
    void getActvity(){
//        Activity activity=activityDao.findAll().get(0);
        Activity activity=activityDao.selectById(0);
        System.out.println(activity);
    }
}
