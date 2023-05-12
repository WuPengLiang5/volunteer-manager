package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.dao.RoleDao;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.Menu;
import com.wpl.volunteer.entity.Role;
import com.wpl.volunteer.service.MenuService;
import com.wpl.volunteer.service.RoleService;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuService menuService;

    @Override
    public Role getRoleById(Integer id) {
        return roleDao.selectById(id);
    }

    @Override
    public ResultCountVo listRoles(QueryDTO queryDTO) {
        List<Role> roles;
        long total;
        if (queryDTO.getLimit() != -1){
            IPage<Role> iPage = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
            roleDao.selectPage(iPage, null);
            roles = iPage.getRecords();
            total = iPage.getTotal();
        }else{
            roles = roleDao.selectList(null);
            total = roles.size();
        }

//        roles = getRolesByState(roles);

        for (Role role: roles){
            List<Menu> menus = menuService.selectByRoleId(role.getId());
            if(menus != null){
                role.setMenus(menus);
            }
        }

        ResultVo resultVo = ResultVo.success("获取成功",roles);
        return new ResultCountVo(resultVo,total);
    }

    public List<Role> getRolesByState(List<Role> roleList){
        List<Role> roles = new ArrayList<>();
        for (Role role:roleList){
            if (role.getState()!=0){
                roles.add(role);
            }
        }
        return roles;
    }

    @Override
    public int saveRole(Role role) {
//        List<Menu> menus = role.getMenus();
        return roleDao.insert(role);
    }

    @Override
    public int removeRole(Integer id) {
        return roleDao.deleteById(id);
    }

    @Override
    public int updateRole(Role role) {
        return roleDao.updateById(role);
    }

    @Override
    public void saveRoleMenu(Integer roleId, List<Integer> menuIds) {
        if (menuIds != null){
            for (Integer mid : menuIds) {
                insertRoleMenu(roleId,mid);
            }
        }
    }

    @Override
    public void updateRoleMenu(Integer roleId, List<Integer> menuIds) {
        if (roleId != null && menuIds != null){
            removeRoleMenuByRoleId(roleId);
            saveRoleMenu(roleId,menuIds);
        }
    }

    @Override
    public int insertRoleMenu(int roleId, int menuId) {
        return roleDao.insertRoleMenu(roleId,menuId);
    }

    @Override
    public int removeRoleMenuByRoleId(int roleId) {
        return roleDao.removeRoleMenuByRoleId(roleId);
    }

    @Override
    public int removeRoleMenuByMenuId(int menuId) {
        return roleDao.removeRoleMenuByMenuId(menuId);
    }
}
