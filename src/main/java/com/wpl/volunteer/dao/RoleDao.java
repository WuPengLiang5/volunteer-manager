package com.wpl.volunteer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpl.volunteer.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface RoleDao extends BaseMapper<Role> {
    public int insertRoleMenu(int roleId, int menuId);

    public int removeRoleMenuByRoleId(int roleId);

    public int removeRoleMenuByMenuId(int menuId);
}
