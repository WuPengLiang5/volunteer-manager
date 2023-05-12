package com.wpl.volunteer.controller.common;

import com.wpl.volunteer.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/1")
    public String sayHello(){
        return "Hello";
    }

//    @RequestMapping("/getActivityList")
//    public List<Activity> findAll(){
//        return activityService.findAll();
//    }
}
