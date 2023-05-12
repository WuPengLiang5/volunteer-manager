package com.wpl.volunteer.controller.admin;

import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.Role;
import com.wpl.volunteer.exception.GlobalException;
import com.wpl.volunteer.service.RoleService;
import com.wpl.volunteer.vo.ResultVo;
import com.wpl.volunteer.dto.RoleAndMenusId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/listRoles")
    public ResultVo listRoles(@RequestBody QueryDTO queryDTO){
        return roleService.listRoles(queryDTO);
    }

    @RequestMapping("/getRoleById")
    public ResultVo getRoleById(@RequestParam Integer id){
        Role role = roleService.getRoleById(id);
        if (role == null){
            return ResultVo.error("查询的角色不存在！");
        }
        if (role.getState() != 1){
            throw new GlobalException(CodeMsgConstant.ROLE_DISABLEDRole);
        }
        return ResultVo.success("获取成功",role);
    }

    @RequestMapping("/saveRole")
    public ResultVo saveRole(@RequestBody RoleAndMenusId role){
        int result = roleService.saveRole(role);
        if (result != 1){
            return ResultVo.error("添加角色失败");
        }
        roleService.updateRoleMenu(role.getId(),role.getMenuIds());
        return ResultVo.success("添加角色活动");
    }

    @RequestMapping("/removeRole")
    public ResultVo removeRole(@RequestParam Integer roleId){
        Role role = roleService.getRoleById(roleId);
        if (role == null){
            return ResultVo.error("删除失败！角色不存在");
        }
        int result = roleService.removeRole(roleId);
        if (result != 1){
            return ResultVo.error("删除角色失败");
        }
        roleService.removeRoleMenuByRoleId(roleId);
        return ResultVo.success("删除角色成功！");
    }

    @RequestMapping("/updateRole")
    public ResultVo updateRole(@RequestBody RoleAndMenusId role){
        int result = roleService.updateRole(role);
        if (result != 1){
            return ResultVo.error("角色信息更新失败");
        }
        roleService.updateRoleMenu(role.getId(),role.getMenuIds());
        return ResultVo.success("成功更新角色信息！");
    }

}
