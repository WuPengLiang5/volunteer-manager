package com.wpl.volunteer.controller.admin;

import com.wpl.volunteer.annotation.Log;
import com.wpl.volunteer.entity.Menu;
import com.wpl.volunteer.entity.Role;
import com.wpl.volunteer.enums.BusinessType;
import com.wpl.volunteer.service.AdminService;
import com.wpl.volunteer.service.MenuService;
import com.wpl.volunteer.service.RoleService;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/getMenuByRoleId")
    public ResultVo getMenuByRoleId(@RequestParam Integer roleId){
        Role role = roleService.getRoleById(roleId);
        if (role == null){
            return ResultVo.error("角色不存在");
        }

        return ResultVo.success("查询成功",menuService.selectByRoleId(roleId));
    }

    @RequestMapping("/getAllMenuTree")
    public ResultVo getAllMenuTree(){
        return ResultVo.success("查询成功",menuService.selectMenuTree());
    }

    @Log(module = "菜单管理",businessType = BusinessType.OTHER)
    @GetMapping("/getMenuById")
    public ResultVo getMenuById(@RequestParam Integer id){
        return ResultVo.success(menuService.selectById(id));
    }

    @Log(module = "菜单管理",businessType = BusinessType.INSERT)
    @PostMapping("/saveMenu")
    public ResultVo saveMenu(@RequestBody Menu menu){
        List<Menu> menus = menuService.selectMenus();
//        for (Menu dbMenu:menus){
//            if (dbMenu.getPerms().equals(menu.getPerms())){
//                return ResultVo.error("权限字段不能相同");
//            }
//        }
        int result=menuService.saveMenu(menu);
        if (result!=1){
            return ResultVo.error("添加菜单失败");
        }
        return ResultVo.success("成功添加菜单");
    }

    @Log(module = "菜单管理",businessType = BusinessType.DELETE)
    @RequestMapping("/delMenu")
    public ResultVo removeMenu(@RequestParam Integer id){
        return ResultVo.success("成功删除菜单",menuService.removeMenu(id));
    }

    @Log(module = "菜单管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updateMenu")
    public ResultVo updateMenu(@RequestBody Menu menu){
        List<Menu> menus = menuService.selectMenus();
//        for (Menu dbMenu:menus){
//            if (dbMenu.getPerms().equals(menu.getPerms())){
//                return ResultVo.error("权限字段不能相同");
//            }
//        }
        int result=menuService.updateMenu(menu);
        if (result!=1){
            return ResultVo.error("更新菜单失败");
        }
        return ResultVo.success("成功更新菜单");
    }
}
