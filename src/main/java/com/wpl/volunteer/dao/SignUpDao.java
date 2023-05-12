package com.wpl.volunteer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpl.volunteer.entity.SignUp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SignUpDao extends BaseMapper<SignUp> {
}
