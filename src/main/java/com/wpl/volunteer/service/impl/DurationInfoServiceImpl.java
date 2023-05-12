package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.dao.ActivityDao;
import com.wpl.volunteer.dao.UserDao;
import com.wpl.volunteer.dao.VolunteerDao;
import com.wpl.volunteer.dto.QueryDurationInfoDTO;
import com.wpl.volunteer.entity.*;
import com.wpl.volunteer.dao.DurationInfoMapper;
import com.wpl.volunteer.enums.SignUpStatus;
import com.wpl.volunteer.service.IDurationInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpl.volunteer.service.SignUpService;
import com.wpl.volunteer.vo.DurationInfoVo;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import com.wpl.volunteer.vo.VolunteerVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WuPengLiang
 * @since 2022-05-18
 */
@Service
public class DurationInfoServiceImpl extends ServiceImpl<DurationInfoMapper, DurationInfo> implements IDurationInfoService {

    @Autowired
    private DurationInfoMapper durationInfoMapper;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VolunteerDao volunteerDao;

    @Autowired
    private SignUpService signUpService;

    @Override
    public ResultCountVo getDurationInfoList(QueryDurationInfoDTO queryDTO) {
        QueryWrapper<DurationInfo> wrapper = new QueryWrapper<>();
        if (queryDTO.getState() != null){
            wrapper.eq("state",queryDTO.getState());
        }
        if (queryDTO.getActivityId() != null){
            wrapper.eq("activity_id",queryDTO.getActivityId());
        }
        IPage<DurationInfo> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        durationInfoMapper.selectPage(page, wrapper);

        List<DurationInfoVo> durationInfoList = new ArrayList<>();
        for (DurationInfo durationInfo:page.getRecords()){
            DurationInfoVo durationInfoVo = new DurationInfoVo();
            Activity activity = activityDao.selectById(durationInfo.getActivityId());

            // 根据UserId查出志愿者
            User user = userDao.selectById(durationInfo.getUserId());
            QueryWrapper<Volunteer> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("uid",user.getId());
            Volunteer volunteer = volunteerDao.selectOne(wrapper1);

            BeanUtils.copyProperties(durationInfo,durationInfoVo);
            BeanUtils.copyProperties(user,durationInfoVo,"id");
            BeanUtils.copyProperties(volunteer,durationInfoVo,"id");
            durationInfoList.add(durationInfoVo);
        }
        return new ResultCountVo(ResultVo.success("获取成功",durationInfoList),page.getTotal());
    }

    @Override
    public ResultCountVo getInputDurationOfVolunteer(QueryDurationInfoDTO queryDTO) {
        List<SignUp> signUps = signUpService.getSignUpByActivityId(queryDTO.getActivityId());
        List<Integer> userIds = new ArrayList<>();
        signUps.forEach(item ->{
            if (item.getState().equals(SignUpStatus.PASS.getCode())){
                userIds.add(item.getUserId());
            }
        });
        QueryWrapper<Volunteer> wrapper = new QueryWrapper<>();
        if (userIds.size() == 0){
            return new ResultCountVo(ResultVo.success("获取成功！",new ArrayList<>()),0);
        }
        wrapper.in("uid",userIds);
        IPage<Volunteer> page = new Page<>(queryDTO.getPage(),queryDTO.getLimit());
        volunteerDao.selectPage(page,wrapper);

        List<VolunteerVo> volunteerVos = new ArrayList<>();
        for (Volunteer volunteer:page.getRecords()){
            VolunteerVo volunteerVo = new VolunteerVo();
            BeanUtils.copyProperties(volunteer,volunteerVo);
            User user = userDao.selectById(volunteer.getUid());
            BeanUtils.copyProperties(user,volunteerVo,"id");
            volunteerVos.add(volunteerVo);
        }
        return new ResultCountVo(ResultVo.success("获取成功！",volunteerVos),page.getTotal());
    }
}
