package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.dao.DurationInfoMapper;
import com.wpl.volunteer.dao.SignUpDao;
import com.wpl.volunteer.dao.VolunteerDao;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.dto.QuerySignUpDTO;
import com.wpl.volunteer.entity.Activity;
import com.wpl.volunteer.entity.DurationInfo;
import com.wpl.volunteer.entity.SignUp;
import com.wpl.volunteer.entity.Volunteer;
import com.wpl.volunteer.enums.SignUpStatus;
import com.wpl.volunteer.service.ActivityService;
import com.wpl.volunteer.service.IDurationInfoService;
import com.wpl.volunteer.service.SignUpService;
import com.wpl.volunteer.service.VolunteerService;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import com.wpl.volunteer.vo.SignUpVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private SignUpDao signUpDao;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private DurationInfoMapper durationInfoMapper;

    @Override
    public SignUp getSignUpById(String id) {
        return signUpDao.selectById(id);
    }

    @Override
    public SignUp getSignUpByAIdAndUId(Integer userId, Integer activityId) {
        QueryWrapper<SignUp> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_id",activityId).eq("user_id",userId);

        return signUpDao.selectOne(queryWrapper);
    }

    @Override
    public List<SignUp> getSignUpByActivityId(Integer activityId) {
        QueryWrapper<SignUp> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id",activityId);
        return signUpDao.selectList(wrapper);
    }

    @Override
    public List<SignUp> listSignUp() {
        return signUpDao.selectList(null);
    }

    @Override
    public ResultCountVo listSignUpByPage(QuerySignUpDTO querySignUpDTO) {
        QueryWrapper<SignUp> wrapper = new QueryWrapper<>();
        if(querySignUpDTO.getState() != null)
            wrapper.eq("state",querySignUpDTO.getState());
        IPage<SignUp> page = listByPage(wrapper, querySignUpDTO.getPage(), querySignUpDTO.getLimit());

        List<SignUp> signUps = page.getRecords();
        List<SignUpVo> signUpVos = new ArrayList<>();
        for (SignUp signUp:signUps){
            Volunteer volunteer = volunteerService.getVolunteerByUid(signUp.getUserId());
            SignUpVo signUpVo = new SignUpVo();
            signUpVo.setName(volunteer.getName());
            BeanUtils.copyProperties(signUp, signUpVo);
            signUpVo.setDuration(calculationDuration(signUp.getActivityId(),signUp.getUserId()));
            Activity activity = activityService.getActivityById(signUp.getActivityId());
            BeanUtils.copyProperties(activity,signUpVo,"id","state");
            signUpVos.add(signUpVo);
        }
        return new ResultCountVo(ResultVo.success("获取成功",signUpVos), page.getTotal());
    }

    @Override
    public int saveSignUp(SignUp signUp) {
        // 创建报名信息，创建时间，报名活动状态为待审核
        signUp.setRegistrationTime(LocalDateTime.now());
        signUp.setState(SignUpStatus.NOT_APPROVED.getCode());
        return signUpDao.insert(signUp);
    }

    @Override
    @Transactional
    public int removeSignUp(String id) {
        SignUp signUp = getSignUpById(id);
        // 当报名信息通过后，删除报名信息，参加活动的报名人数相应-1
        if (signUp.getState().equals(SignUpStatus.PASS.getCode())){
            Activity activity = activityService.getActivityById(signUp.getActivityId());
            int recruitNumber = Integer.parseInt(activity.getRecruitNumber()) - 1;
            activity.setRecruitNumber(String.valueOf(recruitNumber));
            activityService.updateActivity(activity);

            // 报名信息删除后，待审核的时长记录也要删除
            if (signUp.getDuration().compareTo(new BigDecimal("0.00")) == 0){
                QueryWrapper<DurationInfo> wrapper = new QueryWrapper<>();
                wrapper.eq("activity_id",signUp.getActivityId());
                wrapper.eq("user_id",signUp.getUserId());
                durationInfoMapper.delete(wrapper);
            }
        }
        return signUpDao.deleteById(id);
    }

    @Override
    @Transactional
    public int removeSignUps(String[] ids) {
        for (String id: ids){
            SignUp signUp = getSignUpById(id);
            // 当报名信息通过后，删除报名信息，参加活动的报名人数相应-1
            // 若当前未记录服务时长，删除该用户下，该活动待审核的时长信息
            if (signUp.getState().equals(SignUpStatus.PASS.getCode())){
                Activity activity = activityService.getActivityById(signUp.getActivityId());
                int recruitNumber = Integer.parseInt(activity.getRecruitNumber()) - 1;
                activity.setRecruitNumber(String.valueOf(recruitNumber));
                activityService.updateActivity(activity);

                if (signUp.getDuration().compareTo(new BigDecimal("0.00")) == 0){
                    QueryWrapper<DurationInfo> wrapper = new QueryWrapper<>();
                    wrapper.eq("activity_id",signUp.getActivityId());
                    wrapper.eq("user_id",signUp.getUserId());
                    durationInfoMapper.delete(wrapper);
                }
            }
        }
        return signUpDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public int updateSignUp(SignUp signUp) {
        if (signUp.getState().equals(SignUpStatus.PASS.getCode())){
            Activity activity = activityService.getActivityById(signUp.getActivityId());
            int recruitNumber = Integer.parseInt(activity.getRecruitNumber()) + 1;
            activity.setRecruitNumber(String.valueOf(recruitNumber));
            activityService.updateActivity(activity);
        }
        return signUpDao.updateById(signUp);
    }

    @Override
    public ResultCountVo getSignUpByActivityId(QuerySignUpDTO querySignUpDTO) {
        QueryWrapper<SignUp> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id",querySignUpDTO.getActivityId());

        IPage<SignUp> page = listByPage(wrapper,querySignUpDTO.getPage(),querySignUpDTO.getLimit());

        List<SignUp> signUps = page.getRecords();
        List<SignUpVo> signUpVos = new ArrayList<>();
        for (SignUp signUp:signUps){
            if (signUp.getState().equals(SignUpStatus.PASS.getCode())){
                Volunteer volunteer = volunteerService.getVolunteerByUid(signUp.getUserId());
                SignUpVo signUpVo = new SignUpVo();
                BeanUtils.copyProperties(signUp,signUpVo);
                signUpVo.setDuration(calculationDuration(signUp.getActivityId(),signUp.getUserId()));
                signUpVo.setName(volunteer.getName());
                signUpVos.add(signUpVo);
            }
        }
        return new ResultCountVo(ResultVo.success("查询成功",signUpVos),signUpVos.size());
    }

    @Override
    public ResultCountVo getSignUpByUidByPage(QueryDTO queryDTO, Integer uid) {
        QueryWrapper<SignUp> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",uid);
        IPage<SignUp> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        signUpDao.selectPage(page, wrapper);

        List<SignUpVo> signUpVos = new ArrayList<>();
        for (SignUp signUp:page.getRecords()){
            Activity activity = activityService.getActivityById(signUp.getActivityId());
            SignUpVo signUpVo = new SignUpVo();
            BeanUtils.copyProperties(signUp,signUpVo);
            BeanUtils.copyProperties(activity,signUpVo, "id","state");
            signUpVo.setDuration(calculationDuration(signUp.getActivityId(),signUp.getUserId()));
            signUpVos.add(signUpVo);
        }

        return new ResultCountVo(ResultVo.success("获取成功",signUpVos), signUpVos.size());
    }

    @Override
    public List<SignUp> getSignUpByUid(Integer uid) {
        QueryWrapper<SignUp> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",uid);
        return signUpDao.selectList(wrapper);
    }

    public IPage<SignUp> listByPage(QueryWrapper<SignUp> wrapper,Integer pageIndex,Integer limit){
        IPage<SignUp> page = new Page<>(pageIndex, limit);
        signUpDao.selectPage(page, wrapper);

        return page;
    }

    public BigDecimal calculationDuration(Integer activityId, Integer userId) {
        QueryWrapper<DurationInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("activity_id",activityId);
        wrapper.eq("user_id",userId);
        wrapper.eq("state",1);
        List<DurationInfo> durationInfos = durationInfoMapper.selectList(wrapper);
        BigDecimal totalDurations = new BigDecimal(0);
        for (DurationInfo durationInfo : durationInfos) {
            totalDurations = totalDurations.add(durationInfo.getDuration());
        }
        return totalDurations;
    }

}
