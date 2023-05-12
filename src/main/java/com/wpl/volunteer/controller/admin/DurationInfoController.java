package com.wpl.volunteer.controller.admin;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.dto.QueryDurationInfoDTO;
import com.wpl.volunteer.entity.*;
import com.wpl.volunteer.service.ActivityService;
import com.wpl.volunteer.service.IDurationInfoService;
import com.wpl.volunteer.service.UserService;
import com.wpl.volunteer.service.VolunteerService;
import com.wpl.volunteer.util.SecurityUtils;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author WuPengLiang
 * @since 2022-05-18
 */
@RestController
@RequestMapping("/durationInfo")
public class DurationInfoController {

    @Autowired
    private IDurationInfoService durationInfoService;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private SecurityUtils securityUtils;

    // 根据时长信息的状态（待审核、已审核）、获取时长信息记录
    // 后台管理页面
    @RequestMapping("/getList")
    public ResultVo getDurationInfo(@RequestBody QueryDurationInfoDTO queryDTO){
        return durationInfoService.getDurationInfoList(queryDTO);
    }

    // 根据用户Id,获取时长信息记录（前台页面展示）
    @RequestMapping("/getListByUserId")
    public ResultVo getDurationInfoByUserId(@RequestBody QueryDurationInfoDTO queryDTO){
        SecurityUser securityUser = (SecurityUser) securityUtils.getCurrentUser();
        QueryWrapper<DurationInfo> wrapper = new QueryWrapper<>();

        wrapper.eq("user_id",securityUser.getId());

        IPage<DurationInfo> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        durationInfoService.page(page, wrapper);
        List<DurationInfo> durationInfos = page.getRecords();
        for (DurationInfo durationInfo:durationInfos){
            durationInfo.setTitle(activityService.getActivityById(durationInfo.getActivityId()).getTitle());
        }

        return new ResultCountVo(ResultVo.success("获取成功",durationInfos),page.getTotal());
    }

    // 根据活动Id获取报名信息，返回该活动下志愿者的基本信息（录入时长信息页面）
    @PostMapping("/getInputDurationOfVolunteer")
    public ResultVo getInputDurationOfVolunteer(@RequestBody QueryDurationInfoDTO queryDTO){
        return durationInfoService.getInputDurationOfVolunteer(queryDTO);
    }

    // 个人申请时长，未审核
    @PostMapping("/saveDurationInfo")
    public ResultVo saveDurationInfo(@RequestBody DurationInfo durationInfo){
        durationInfo.setCreateTime(LocalDateTime.now());
        boolean result = durationInfoService.save(durationInfo);
        if (result){
            return ResultVo.success("成功新增时长记录");
        }
        return ResultVo.error("失败新增时长记录");
    }

    // 团体录入时长信息，不需要审核
    @PostMapping("/saveDurationInfoByGroup")
    public ResultVo saveDurationInfoByGroup(@RequestBody Map<String,Object> map){
        List<Integer> userIds = (List<Integer>) map.get("ids");
        DurationInfo durationInfo = JSON.parseObject(String.valueOf(map.get("durationInfo")),DurationInfo.class);
        durationInfo.setCreateTime(LocalDateTime.now());

        QueryWrapper<Volunteer> wrapper = new QueryWrapper<>();
        wrapper.in("uid",userIds);
        List<Volunteer> volunteers = volunteerService.list(wrapper);
        for (Volunteer volunteer:volunteers){
            durationInfo.setUserId(volunteer.getUid());
            volunteer.setDurations(volunteer.getDurations().add(durationInfo.getDuration()));
            volunteerService.updateVolunteer(volunteer);
        }
        boolean result = durationInfoService.save(durationInfo);
        if (result){
            return ResultVo.success("成功录入");
        }
        return ResultVo.error("录入失败");
    }

    // 删除时长记录
    @PostMapping("/delDurationInfo")
    public ResultVo removeDurationInfo(@RequestParam Integer id){
        // 删除时长记录前，扣除志愿者的总时长
        DurationInfo durationInfo = durationInfoService.getById(id);
        Volunteer volunteer = volunteerService.getVolunteerByUid(durationInfo.getUserId());
        volunteer.setDurations(volunteer.getDurations().subtract(durationInfo.getDuration()));
        volunteerService.updateVolunteer(volunteer);

        boolean result = durationInfoService.removeById(id);
        if (result){
            return ResultVo.success("删除成功");
        }
        return ResultVo.error("删除失败");
    }

    // 多条删除时长记录
    @RequestMapping("/delBatchDurationInfos")
    public ResultVo removeDurationInfo(Integer[] ids){
        for (Integer id:ids){
            DurationInfo durationInfo = durationInfoService.getById(id);
            Volunteer volunteer = volunteerService.getVolunteerByUid(durationInfo.getUserId());
            volunteer.setDurations(volunteer.getDurations().subtract(durationInfo.getDuration()));
            volunteerService.updateVolunteer(volunteer);
        }
        boolean result = durationInfoService.removeByIds(Arrays.asList(ids));
        if (!result){
            return ResultVo.error("批量删除失败");
        }
        return ResultVo.success("批量删除成功");
    }

    // 个人申请时长，审核时长
    @RequestMapping("/examineDuration")
    public ResultVo examineDuration(@RequestParam("state") Integer state,
                                    @RequestParam("activityId") Integer activityId,
                                    @RequestParam("ids")Integer[] ids){
        //更新时长
        QueryWrapper<DurationInfo> wrapper1 = new QueryWrapper<>();
        wrapper1.in("id",ids);
        wrapper1.eq("activity_id",activityId);
        // 查询该活动下，通过审核的志愿时长记录
        List<DurationInfo> durationInfos = durationInfoService.list(wrapper1);
        Set<Integer> userIds = new HashSet<>();
        durationInfos.forEach(item ->{
            userIds.add(item.getUserId());
        });
        QueryWrapper<Volunteer> wrapper2 = new QueryWrapper<>();
        wrapper2.in("uid",userIds.toArray());
        List<Volunteer> volunteers = volunteerService.list(wrapper2);
        for (DurationInfo durationInfo:durationInfos){
            for (Volunteer volunteer:volunteers){
                // 只有通过审核的时候，志愿者的总时长才会增加
                if (durationInfo.getUserId().equals(volunteer.getUid()) && state == 1){
                    volunteer.setDurations(volunteer.getDurations().add(durationInfo.getDuration()));
                    volunteerService.updateVolunteer(volunteer);
                }
            }
        }
        // 更新状态
        UpdateWrapper<DurationInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("state",state).in("id",ids);
        boolean result = durationInfoService.update(null,updateWrapper);
        if (result && state == 1){
            return ResultVo.success("通过审核");
        }
        return ResultVo.error("审核不通过");
    }
}
