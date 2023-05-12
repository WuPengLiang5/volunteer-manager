package com.wpl.volunteer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpl.volunteer.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MenuDao extends BaseMapper<Menu> {
    List<Menu> selectByRoleId(Integer roleId);
}
