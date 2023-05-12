package com.wpl.volunteer.controller.admin;

import com.wpl.volunteer.annotation.Log;
import com.wpl.volunteer.dto.QueryActivityDTO;
import com.wpl.volunteer.entity.Activity;
import com.wpl.volunteer.enums.BusinessType;
import com.wpl.volunteer.service.ActivityService;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @RequestMapping("/listActivities")
    public ResultCountVo listActivities(@RequestBody QueryActivityDTO queryActivityDTO){
        return activityService.listActivities(queryActivityDTO);
    }

    @GetMapping("/getActivityById")
    public ResultVo getActivityById(@RequestParam Integer id){
        Activity activity=activityService.getActivityById(id);
        if (activity==null){
            return ResultVo.error("活动不存在");
        }
        return ResultVo.success("获取成功",activity);
    }

    @Log(module = "活动管理",businessType = BusinessType.INSERT)
    @PostMapping("/saveActivity")
    public ResultVo saveActivity(@RequestBody Activity activity){
        int result=activityService.saveActivity(activity);
        if (result!=1){
            return ResultVo.error("添加活动失败");
        }
        return ResultVo.success("成功添加活动");
    }

    @Log(module = "活动管理",businessType = BusinessType.DELETE)
    @RequestMapping("/delActivity")
    public ResultVo removeActivity(@RequestParam Integer id){
        int result=activityService.removeActivity(id);
        if (result!=1){
            return ResultVo.error("删除活动失败");
        }
        return ResultVo.success("成功删除活动");
    }

    @Log(module = "活动管理",businessType = BusinessType.UPDATE)
    @RequestMapping("/updateActivity")
    public ResultVo updateActivity(@RequestBody Activity activity){
        int result=activityService.updateActivity(activity);
        if (result!=1){
            return ResultVo.error("更新活动失败");
        }
        return ResultVo.success("成功更新活动");
    }

    @Log(module = "活动管理",businessType = BusinessType.DELETE)
    @RequestMapping("/delBatchActivities")
    public ResultVo removeActivities(Integer[] ids){
        int result=activityService.removeActivities(ids);
        if (result < 0){
            return ResultVo.error("批量删除失败");
        }
        return ResultVo.success("批量删除成功");
    }

    @GetMapping("/getActivityCount")
    public ResultVo getActivityCount(){
        return ResultVo.success("获取成功",activityService.selectCount());
    }
}
