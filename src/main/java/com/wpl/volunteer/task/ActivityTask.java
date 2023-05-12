//package com.wpl.volunteer.task;
//
//import com.wpl.volunteer.dao.ActivityDao;
//import com.wpl.volunteer.entity.Activity;
//import com.wpl.volunteer.service.ActivityService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//public class ActivityTask {
//
//    //日志类
//    private static final Logger loggerTask = LoggerFactory.getLogger(ActivityTask.class);
//
//    @Autowired
//    private ActivityDao activityDao;
//
//
//
//    public void updateActivityState(){
//        List<Activity> activities = activityDao.selectList(null);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        for (Activity activity:activities){
////            System.out.println(activity.getId());
//            if (localDateTime.compareTo(activity.getRegistrationBegin()) < 0){
////                System.out.println("招募待启动");
//                if (activity.getState() != 0) activity.setState(0);
//            }
//            if (localDateTime.compareTo(activity.getRegistrationBegin()) > 0
//                    && localDateTime.compareTo(activity.getRegistrationEnd()) < 0){
////                System.out.println("招募中");
//                if (activity.getState() != 1) activity.setState(1);
//            }
//            if (localDateTime.compareTo(activity.getRegistrationEnd()) > 0
//                    && localDateTime.compareTo(activity.getEndTime()) < 0){
////                System.out.println("招募已结束");
//                if (activity.getState() != 2) activity.setState(2);
//            }
//            if (localDateTime.compareTo(activity.getEndTime()) > 0){
////                System.out.println("已结项");
//                if (activity.getState() != 3) activity.setState(3);
//            }
//            activityDao.updateById(activity);
//        }
//    }
//
//    @Scheduled(initialDelay = 1000, fixedRate = Long.MAX_VALUE)
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void activityTask() {
//        List<Activity> activities = activityDao.selectList(null);
//        LocalDateTime localDateTime = LocalDateTime.now();
//        for (Activity activity:activities){
//            if (localDateTime.compareTo(activity.getRegistrationBegin()) < 0){
//                activity.setState(0);
//            }
//            if (localDateTime.compareTo(activity.getRegistrationBegin()) > 0
//                    && localDateTime.compareTo(activity.getRegistrationEnd()) < 0){
//                activity.setState(1);
//            }
//            if (localDateTime.compareTo(activity.getRegistrationEnd()) > 0
//                    && localDateTime.compareTo(activity.getEndTime()) < 0){
//                activity.setState(2);
//            }
//            if (localDateTime.compareTo(activity.getEndTime()) > 0){
//                activity.setState(3);
//            }
//            activityDao.updateById(activity);
//        }
//        System.out.println("定时任务，更新完成！");
//    }
//}
