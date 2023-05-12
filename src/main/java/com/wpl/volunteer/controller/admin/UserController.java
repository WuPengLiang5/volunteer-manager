package com.wpl.volunteer.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpl.volunteer.annotation.Log;
import com.wpl.volunteer.dto.LoginDTO;
import com.wpl.volunteer.dto.Registrants;
import com.wpl.volunteer.entity.Admin;
import com.wpl.volunteer.entity.SignUp;
import com.wpl.volunteer.entity.User;
import com.wpl.volunteer.entity.Volunteer;
import com.wpl.volunteer.enums.BusinessType;
import com.wpl.volunteer.service.SignUpService;
import com.wpl.volunteer.service.UserService;
import com.wpl.volunteer.service.VolunteerService;
import com.wpl.volunteer.util.FileUtil;
import com.wpl.volunteer.util.SecurityUtils;
import com.wpl.volunteer.util.VerifyCodeUtil;
import com.wpl.volunteer.vo.ResultVo;
import com.wpl.volunteer.vo.VolunteerInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Security;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private SecurityUtils securityUtils;

    @Log(module = "用户管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updatePassword")
    public ResultVo updatePassword(String username, String password,String newPassword){
        User user = userService.getUserByUserName(username);
        String dbPwd = user.getPassword();
        String tem = user.getSalt() + password;
        String md5 = DigestUtils.md5DigestAsHex(tem.getBytes());

        if (!md5.equals(dbPwd)){
            return ResultVo.error("原密码错误！");
        }
        //随机生成盐值
        String create_salt = UUID.randomUUID().toString();
        String new_md5 = DigestUtils.md5DigestAsHex((create_salt + newPassword).getBytes());
        user.setSalt(create_salt);
        user.setPassword(new_md5);
        int res = userService.updateUser(user);
        if (res != 1){
            return ResultVo.error("密码更新失败！");
        }
        return ResultVo.success("密码更新成功！");
    }

    @RequestMapping("/updateAvatar")
    public ResultVo updateAvatar(Integer userId, MultipartFile file){
        User user = userService.getUserById(userId);
        if (Objects.equals(file.getOriginalFilename(), user.getAvatar())){
            return ResultVo.success("头像更新成功！");
        }
        String imageUrl = FileUtil.uploadFile(file);
        user.setAvatar(imageUrl);
        int res = userService.updateUser(user);
        if (res != 1){
            return ResultVo.error("头像更新失败");
        }
        return ResultVo.success("头像更新成功！");
    }

    @RequestMapping("/updateUserInfo")
    public ResultVo updateUserInfo(@RequestBody VolunteerInfoVo volunteerInfoVo){
        User user = userService.getUserByUserName(volunteerInfoVo.getUsername());
        BeanUtils.copyProperties(volunteerInfoVo,user,"id");
        Volunteer volunteer = new Volunteer();
        BeanUtils.copyProperties(volunteerInfoVo,volunteer);
        if (userService.updateUser(user) == 0 || volunteerService.updateVolunteer(volunteer) == 0){
            return ResultVo.error("更新失败");
        }
        return ResultVo.success("更新成功");
    }

    @RequestMapping("/getUserInfo")
    public ResultVo getUserInfo(){
        String username = securityUtils.getCurrentUserName();
        User user = userService.getUserByUserName(username);
        Volunteer volunteer = volunteerService.getVolunteerByUid(user.getId());

        // 根据报名信息 获取用户参加的活动
        List<Integer> arrayList = new ArrayList<>();
        for (SignUp signUp:signUpService.getSignUpByUid(user.getId())){
            if (signUp.getState() == 1){
                arrayList.add(signUp.getActivityId());
            }
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("id",user.getId());
        resultMap.put("avatar",user.getAvatar());
        resultMap.put("loginTime",user.getLoginTime());
        resultMap.put("loginIp",user.getLoginIp());
        resultMap.put("registerTime",user.getRegisterTime());
        resultMap.put("name",volunteer.getName());
        resultMap.put("durations",volunteer.getDurations());
        resultMap.put("activityNum",arrayList.size());
        return ResultVo.success("查询成功",resultMap);
    }
}
