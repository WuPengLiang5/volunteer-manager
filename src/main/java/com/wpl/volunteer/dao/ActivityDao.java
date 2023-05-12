package com.wpl.volunteer.dao;

//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpl.volunteer.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Component
//@Repository
public interface ActivityDao extends BaseMapper<Activity> {
    public List<Activity> findAll();
}
