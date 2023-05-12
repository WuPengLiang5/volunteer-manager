package com.wpl.volunteer.controller.common;

import com.wpl.volunteer.dto.LoginDTO;
import com.wpl.volunteer.dto.Registrants;
import com.wpl.volunteer.service.AdminService;
import com.wpl.volunteer.service.UserService;
import com.wpl.volunteer.util.VerifyCodeUtil;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1.0/out")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private VerifyCodeUtil verifyCodeUtil;

    // 网站用户登录
    @RequestMapping("/user/doLogin")
    public ResultVo userLogin(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestBody LoginDTO loginDTO){
        return userService.login(request,response,loginDTO);
    }

    // 网站用户注册
    @PostMapping("/user/register")
    public ResultVo register(HttpServletRequest request,HttpServletResponse response,@RequestBody Registrants registrants) {
        return userService.register(request,response,registrants);
    }

    // 管理员登录
    @RequestMapping("/admin/doLogin")
    public ResultVo login(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestBody LoginDTO loginDTO){
        return adminService.login(request,response,loginDTO);
    }

    // 获取验证码
    @GetMapping("/verifyCode")
    public void generateVerifyCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        verifyCodeUtil.verifyCodeResponse(request,response);
    }
}
