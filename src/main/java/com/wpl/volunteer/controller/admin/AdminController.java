package com.wpl.volunteer.controller.admin;

import com.wpl.volunteer.annotation.Log;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.Admin;
import com.wpl.volunteer.enums.BusinessType;
import com.wpl.volunteer.service.AdminService;
import com.wpl.volunteer.util.FileUtil;
import com.wpl.volunteer.vo.AdminInfoVo;
import com.wpl.volunteer.dto.AdminProfile;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Log(module = "管理员管理",businessType = BusinessType.OTHER)
    @RequestMapping("/listAdmins")
    public ResultVo listAdmins(@RequestBody QueryDTO queryDTO){
        return adminService.listAdmins(queryDTO);
    }

    @Log(module = "管理员管理",businessType = BusinessType.OTHER)
    @RequestMapping("/getAdminById")
    public ResultVo getAdminInfoById(@RequestParam Integer id){
        AdminInfoVo adminInfo = adminService.getAdminInfoById(id);
        if (adminInfo == null){
            return ResultVo.error("查询的管理员不存在！");
        }
        return ResultVo.success("获取成功",adminInfo);
    }

    @Log(module = "管理员管理",businessType = BusinessType.OTHER)
    @RequestMapping("/getAdminProfile")
    public ResultVo getAdminProfile(@RequestParam Integer id){
        AdminProfile adminProfile = adminService.getAdminProfile(id);
        if (adminProfile == null){
            return ResultVo.error("查询的管理员信息不存在！");
        }
        return ResultVo.success("获取成功",adminProfile);
    }

    @Log(module = "管理员管理",businessType = BusinessType.INSERT)
    @RequestMapping("/saveAdmin")
    public ResultVo saveAdmin(@RequestBody Admin admin){
        Admin queryAdmin = adminService.getAdminByUserName(admin.getUsername());
        if (queryAdmin != null){
            return ResultVo.error("管理员已存在");
        }
        int result = adminService.saveAdmin(admin);
        if (result != 1){
            return ResultVo.error("添加管理员失败");
        }
        return ResultVo.success("添加管理员活动");
    }

    @Log(module = "管理员管理",businessType = BusinessType.DELETE)
    @RequestMapping("/removeAdmin")
    public ResultVo removeAdmin(Integer adminId){
        int result = adminService.removeAdmin(adminId);
        if (result != 1){
            return ResultVo.error("删除管理员失败");
        }
        return ResultVo.success("删除管理员成功！");
    }

    @Log(module = "管理员管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updateAdmin")
    public ResultVo updateAdmin(@RequestBody Admin admin){
        int result = adminService.updateAdmin(admin);
        if (result != 1){
            return ResultVo.error("更新失败");
        }
        return ResultVo.success("更新成功");
    }

    @Log(module = "管理员管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updatePassword")
    public ResultVo updatePassword(String username, String password,String newPassword){
        Admin admin = adminService.getAdminByUserName(username);
        String dbPwd = admin.getPassword();
        String tem = admin.getSalt() + password;
        String md5 = DigestUtils.md5DigestAsHex(tem.getBytes());

        if (!md5.equals(dbPwd)){
            return ResultVo.error("原密码错误！");
        }
        //随机生成盐值
        String create_salt = UUID.randomUUID().toString();
        String new_md5 = DigestUtils.md5DigestAsHex((create_salt + newPassword).getBytes());
        admin.setSalt(create_salt);
        admin.setPassword(new_md5);
        int res = adminService.updateAdmin(admin);
        if (res != 1){
            return ResultVo.error("密码更新失败！");
        }
        return ResultVo.success("密码更新成功！");
    }

    @Log(module = "管理员管理",businessType = BusinessType.OTHER)
    @GetMapping("/getAdminCount")
    public ResultVo getAdminCount(){
        return ResultVo.success("获取成功",adminService.selectCount());
    }
}
