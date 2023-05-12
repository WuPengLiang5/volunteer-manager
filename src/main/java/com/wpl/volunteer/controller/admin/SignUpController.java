package com.wpl.volunteer.controller.admin;

import com.wpl.volunteer.annotation.Log;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.dto.QuerySignUpDTO;
import com.wpl.volunteer.entity.Activity;
import com.wpl.volunteer.entity.SecurityUser;
import com.wpl.volunteer.entity.SignUp;
import com.wpl.volunteer.enums.BusinessType;
import com.wpl.volunteer.service.ActivityService;
import com.wpl.volunteer.service.SignUpService;
import com.wpl.volunteer.util.SecurityUtils;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
public class SignUpController {
    @Autowired
    private SignUpService signUpService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private SecurityUtils securityUtils;

    @Log(module = "报名管理",businessType = BusinessType.OTHER)
    @RequestMapping("/listSignUpByPage")
    public ResultCountVo listSignUpByPage(@RequestBody QuerySignUpDTO querySignUpDTO){
        return signUpService.listSignUpByPage(querySignUpDTO);
    }

    @Log(module = "报名管理",businessType = BusinessType.OTHER)
    @GetMapping("/getSignUpById")
    public ResultVo getSignUpById(@RequestParam String id){
        SignUp SignUp = signUpService.getSignUpById(id);
        if (SignUp == null){
            return ResultVo.error("报名信息不存在");
        }
        return ResultVo.success("获取成功",SignUp);
    }

    @Log(module = "报名管理",businessType = BusinessType.INSERT)
    @PostMapping("/saveSignUp")
    public ResultVo saveSignUp(@RequestBody SignUp signUp){
        Integer activityId = signUp.getActivityId();
        Activity activity = activityService.getActivityById(activityId);
        if (activity.getRecruitNumber().compareTo(activity.getRegisterMax()) >= 0){
            return ResultVo.error("报名人数已满，添加报名信息失败");
        }
        int result = signUpService.saveSignUp(signUp);
        if (result != 1){
            return ResultVo.error("添加报名信息失败");
        }
        return ResultVo.success(true);
    }

    @Log(module = "报名管理",businessType = BusinessType.DELETE)
    @RequestMapping("/delSignUp")
    public ResultVo removeSignUp(@RequestParam String id){
        int result = signUpService.removeSignUp(id);
        if (result != 1){
            return ResultVo.error("删除报名信息失败");
        }
        return ResultVo.success("成功删除报名信息");
    }

    @Log(module = "报名管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updateSignUp")
    public ResultVo updateSignUp(@RequestBody SignUp signUp){
        int result = signUpService.updateSignUp(signUp);
        if (result!=1){
            return ResultVo.error("更新报名信息失败");
        }
        return ResultVo.success("成功更新报名信息");
    }

    @Log(module = "报名管理",businessType = BusinessType.DELETE)
    @RequestMapping("/delBatchSignUp")
    public ResultVo removeSignUp(String[] ids){
        int result = signUpService.removeSignUps(ids);
        if (result < 0){
            return ResultVo.error("批量删除失败");
        }
        return ResultVo.success("批量删除成功");
    }

//    @Log(module = "报名管理",businessType = BusinessType.OTHER)
    // 分页查询该用户所有的报名信息
    @RequestMapping("/getSignUpByUidByPage")
    public ResultCountVo getSignUpByUidByPage(@RequestBody QueryDTO queryDTO){
        SecurityUser securityUser = (SecurityUser)securityUtils.getCurrentUser();
        return signUpService.getSignUpByUidByPage(queryDTO,securityUser.getId());
    }
}
