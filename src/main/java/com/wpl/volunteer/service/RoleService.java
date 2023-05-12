package com.wpl.volunteer.service;

import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.Role;
import com.wpl.volunteer.vo.ResultCountVo;

import java.util.List;

public interface RoleService {
    Role getRoleById(Integer id);
    ResultCountVo listRoles(QueryDTO queryDTO);
    int saveRole(Role role);
    int removeRole(Integer id);
    int updateRole(Role role);

    void saveRoleMenu(Integer roleId,List<Integer> menuIds);

    void updateRoleMenu(Integer roleId,List<Integer> menuIds);

    int insertRoleMenu(int roleId, int menuId);

    int removeRoleMenuByRoleId(int roleId);

    int removeRoleMenuByMenuId(int menuId);
}
