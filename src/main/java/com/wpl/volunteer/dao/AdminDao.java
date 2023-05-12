package com.wpl.volunteer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpl.volunteer.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminDao extends BaseMapper<Admin> {
}
