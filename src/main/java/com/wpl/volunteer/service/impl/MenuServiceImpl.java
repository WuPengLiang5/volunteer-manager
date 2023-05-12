package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpl.volunteer.dao.MenuDao;
import com.wpl.volunteer.dao.RoleDao;
import com.wpl.volunteer.entity.Menu;
import com.wpl.volunteer.entity.Role;
import com.wpl.volunteer.exception.GlobalException;
import com.wpl.volunteer.service.MenuService;
import com.wpl.volunteer.util.StringUtils;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Menu> selectByRoleId(Integer roleId) {
        List<Menu> rootMenu = menuDao.selectByRoleId(roleId);
        List<Menu> menuTree = buildMenuTree(rootMenu, 0);
        return menuTree;
    }

    @Override
    public int removeByCondition(Integer pid) {
//        QueryWrapper<Menu> entityWrapper=new QueryWrapper<>();
//        entityWrapper.eq("pid",pid);
//        return menuDao.delete(entityWrapper);
        return 1;
    }

    @Override
    public List<Menu> selectMenuTree() {
        List<Menu> rootMenu = menuDao.selectList(null);
        List<Menu> menuTree = buildMenuTree(rootMenu, 0);
        return menuTree;
    }

    @Override
    public List<Menu> selectMenus() {
        return menuDao.selectList(null);
    }

    @Override
    public List<Menu> selectChildMenus(Integer pid) {
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("pid",pid);
        return menuDao.selectList(queryWrapper);
    }

    @Override
    public Menu selectById(Integer id) {
        return menuDao.selectById(id);
    }

    @Override
    public int saveMenu(Menu menu) {
        int result = menuDao.insert(menu);
        if (roleDao.insertRoleMenu(1,menu.getId()) < 0){
            throw new GlobalException("新增菜单失败");
        }

        return result;
    }

    @Override
    public int removeMenu(Integer id) {
        Menu menu = menuDao.selectById(id);
        if (menu == null){
            throw new GlobalException("菜单不存在，删除失败");
        }
        List<Menu> childMenus = selectChildMenus(menu.getId());
        for (Menu menu1: childMenus){
            if (menuDao.deleteById(menu1.getId()) < 0
                    || roleDao.removeRoleMenuByMenuId(menu1.getId()) < 0){
                throw new GlobalException("删除子菜单失败");
            }
        }
        if (menuDao.deleteById(id) < 0
                || roleDao.removeRoleMenuByMenuId(id) < 0){
            throw new GlobalException("删除菜单失败");
        }
        return menuDao.deleteById(id);
    }

    @Override
    public int updateMenu(Menu menu) {
        return menuDao.updateById(menu);
    }

    /**
     * 构建菜单树
     *
     * @param menuList
     * @param pid
     * @return
     */
    private List<Menu> buildMenuTree(List<Menu> menuList, Integer pid) {
        List<Menu> treeList = new ArrayList<>();
        menuList.forEach(menu -> {
            if (pid.equals(menu.getPid())) {
                menu.setChildren(buildMenuTree(menuList, menu.getId()));
                treeList.add(menu);
            }
        });
        return treeList;
    }
}
