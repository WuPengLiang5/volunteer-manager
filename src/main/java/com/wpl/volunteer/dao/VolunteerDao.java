package com.wpl.volunteer.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpl.volunteer.entity.Volunteer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface VolunteerDao extends BaseMapper<Volunteer> {
}
