package com.wpl.volunteer.service;

import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.dto.QuerySignUpDTO;
import com.wpl.volunteer.entity.SignUp;
import com.wpl.volunteer.vo.ResultCountVo;

import java.util.List;

public interface SignUpService {
    SignUp getSignUpById(String id);
    SignUp getSignUpByAIdAndUId(Integer userId,Integer activityId);
    List<SignUp> getSignUpByActivityId(Integer activityId);
    List<SignUp> listSignUp();
    ResultCountVo listSignUpByPage(QuerySignUpDTO querySignUpDTO);
    int saveSignUp(SignUp signUp);
    int removeSignUp(String id);
    int removeSignUps(String[] ids);
    int updateSignUp(SignUp signUp);
    ResultCountVo getSignUpByActivityId(QuerySignUpDTO querySignUpDTO);
    ResultCountVo getSignUpByUidByPage(QueryDTO queryDTO,Integer uid);
    List<SignUp> getSignUpByUid(Integer uid);
}
