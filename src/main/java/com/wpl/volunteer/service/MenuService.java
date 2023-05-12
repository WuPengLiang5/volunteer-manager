package com.wpl.volunteer.service;

import com.wpl.volunteer.entity.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> selectByRoleId(Integer roleId);
    List<Menu> selectMenuTree();
    List<Menu> selectMenus();
    List<Menu> selectChildMenus(Integer pid);
    Menu selectById(Integer id);
    int saveMenu(Menu menu);
    int removeMenu(Integer id);
    int removeByCondition(Integer pid);
    int updateMenu(Menu menu);
}
