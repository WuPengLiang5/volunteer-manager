package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpl.volunteer.dao.ActivityDao;
import com.wpl.volunteer.dto.QueryActivityDTO;
import com.wpl.volunteer.entity.Activity;
import com.wpl.volunteer.entity.SecurityUser;
import com.wpl.volunteer.service.ActivityService;
import com.wpl.volunteer.util.SecurityUtils;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityDao , Activity> implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    @Override
    public Activity getActivityById(Integer id) {
        return activityDao.selectById(id);
    }

    @Override
    public ResultCountVo listActivities(QueryActivityDTO queryActivityDTO) {

        QueryWrapper<Activity> wrapper = new QueryWrapper<>();
        if(queryActivityDTO.getTitle()!=null && !"".equals(queryActivityDTO.getTitle()))
            wrapper.like("title",queryActivityDTO.getTitle());
        if(queryActivityDTO.getIntroduction()!=null && !"".equals(queryActivityDTO.getIntroduction()))
            wrapper.like("introduction",queryActivityDTO.getIntroduction());
        if(queryActivityDTO.getLocation()!=null && !"".equals(queryActivityDTO.getLocation()))
            wrapper.like("location",queryActivityDTO.getLocation());
        if(queryActivityDTO.getRegisterMax()!=null && !"".equals(queryActivityDTO.getRegisterMax()))
            wrapper.like("register_max",queryActivityDTO.getRegisterMax());
        if(queryActivityDTO.getState() != null)
            wrapper.eq("state",queryActivityDTO.getState());

        IPage<Activity> page = new Page<>(queryActivityDTO.getPage(), queryActivityDTO.getLimit());
        activityDao.selectPage(page, wrapper);

        return new ResultCountVo(ResultVo.success("获取成功",page.getRecords()), page.getTotal());
    }

    @Override
    public int saveActivity(Activity activity) {
        // 活动为保存状态，但未发布
        activity.setState(-1);
        return activityDao.insert(activity);
    }

    @Override
    public int removeActivity(Integer id) {
        return activityDao.deleteById(id);
    }

    @Override
    public int updateActivity(Activity activity) {
        LocalDateTime localDateTime = LocalDateTime.now();
//        if (localDateTime.compareTo(activity.getRegistrationBegin()) < 0){
//            activity.setState(0);
//        }
//        if (localDateTime.compareTo(activity.getRegistrationBegin()) > 0
//                && localDateTime.compareTo(activity.getRegistrationEnd()) < 0){
//            activity.setState(1);
//        }
//        if (localDateTime.compareTo(activity.getRegistrationEnd()) > 0
//                && localDateTime.compareTo(activity.getEndTime()) < 0){
//            activity.setState(2);
//        }
        if (localDateTime.compareTo(activity.getEndTime()) > 0){
            activity.setState(2);
        }
        return activityDao.updateById(activity);
    }

    @Override
    public int removeActivities(Integer[] ids) {
        return activityDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public int selectCount() {
        return activityDao.selectCount(null);
    }

    @Override
    @Scheduled(initialDelay = 1000, fixedRate = Long.MAX_VALUE)
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateActivityState() {
        List<Activity> activities = activityDao.selectList(null);
        LocalDateTime localDateTime = LocalDateTime.now();
        for (Activity activity:activities){
            // 活动发布审核后才可以定时更新活动的状态
            if (activity.getState() != -1 && activity.getState() != 0){
//                if (localDateTime.compareTo(activity.getRegistrationBegin()) < 0){
//                    activity.setState(0);
//                }
//                if (localDateTime.compareTo(activity.getRegistrationBegin()) > 0
//                        && localDateTime.compareTo(activity.getRegistrationEnd()) < 0){
//                    activity.setState(1);
//                }
//                if (localDateTime.compareTo(activity.getRegistrationEnd()) > 0
//                        && localDateTime.compareTo(activity.getEndTime()) < 0){
//                    activity.setState(2);
//                }
                if (localDateTime.compareTo(activity.getEndTime()) > 0){
                    activity.setState(2);
                }
                activityDao.updateById(activity);
            }
        }
        System.out.println("定时任务，更新完成！");
    }
}
