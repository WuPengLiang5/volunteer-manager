package com.wpl.volunteer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wpl.volunteer.dto.QueryActivityDTO;
import com.wpl.volunteer.entity.Activity;
import com.wpl.volunteer.vo.ResultCountVo;

import java.util.List;

public interface ActivityService extends IService<Activity> {
    Activity getActivityById(Integer id);
    ResultCountVo listActivities(QueryActivityDTO queryActivityDTO);
    int saveActivity(Activity activity);
    int removeActivity(Integer id);
    int updateActivity(Activity activity);
    int removeActivities(Integer[] ids);
    int selectCount();
    /**
     * 执行定时任务，做到更新活动状态信息
     */
    void updateActivityState();
}
