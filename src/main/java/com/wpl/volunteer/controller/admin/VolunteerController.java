package com.wpl.volunteer.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.entity.Volunteer;
import com.wpl.volunteer.service.VolunteerService;
import com.wpl.volunteer.util.SecurityUtils;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private SecurityUtils securityUtils;

    @RequestMapping("/listVolunteers")
    public ResultVo listVolunteersByPage(@RequestBody QueryDTO queryDTO){
        return volunteerService.listVolunteersByPage(queryDTO);
    }

    @RequestMapping("/getVolunteerById")
    public ResultVo getVolunteerById(Integer id){
        Volunteer volunteer = volunteerService.getVolunteerById(id);
        if (volunteer == null){
            return ResultVo.error("查询的志愿者不存在！");
        }
        return ResultVo.success("获取成功",volunteer);
    }

    @RequestMapping("/saveVolunteer")
    public ResultVo saveVolunteer(@RequestBody Volunteer volunteer){
        int result = volunteerService.saveVolunteer(volunteer);
        if (result != 1){
            return ResultVo.error("添加志愿者失败");
        }
        return ResultVo.success("添加志愿者活动");
    }

    @RequestMapping("/removeVolunteer")
    public ResultVo removeVolunteer(@RequestParam Integer id){
        int result = volunteerService.removeVolunteer(id);
        if (result != 1){
            return ResultVo.error("删除志愿者失败");
        }
        return ResultVo.success("删除志愿者成功！");
    }

    @RequestMapping("/delBatchVolunteers")
    public ResultVo removeVolunteers(@RequestBody Integer[] ids){
        int result=volunteerService.removeVolunteers(ids);
        if (result < 0){
            return ResultVo.error("批量删除失败");
        }
        return ResultVo.success("批量删除成功");
    }

    @RequestMapping("/updateVolunteer")
    public ResultVo updateVolunteer(@RequestBody Volunteer volunteer){
        Integer result = volunteerService.updateVolunteer(volunteer);
        if (result != 1){
            return ResultVo.error("更新志愿者失败");
        }
        return ResultVo.success("更新志愿者成功！");
    }

    @GetMapping("/getVolunteerCount")
    public ResultVo getVolunteerCount(){
        return ResultVo.success("获取成功",volunteerService.selectCount());
    }

    @GetMapping("/getVolunteerInfoByUid")
    public ResultVo getVolunteerInfo(){
        String username = securityUtils.getCurrentUserName();
        return ResultVo.success("获取成功",volunteerService.getVolunteerInfo(username));
    }

    @GetMapping("/getVolunteerPie")
    public ResultVo getVolunteerPie(){
        Map<String,Object> resultMap = new HashMap<>();

        QueryWrapper<Volunteer> volunteerQueryWrapper = new QueryWrapper<>();
        volunteerQueryWrapper.select("gender as name","count(*) as value");
        volunteerQueryWrapper.groupBy("gender");
        List<Map<String,Object>> sexList = volunteerService.listMaps(volunteerQueryWrapper);
        resultMap.put("sexList",sexList);

        volunteerQueryWrapper = new QueryWrapper<>();
        volunteerQueryWrapper.select("politic as name","count(*) as value");
        volunteerQueryWrapper.groupBy("politic");
        List<Map<String,Object>> politic = volunteerService.listMaps(volunteerQueryWrapper);
        resultMap.put("politic",politic);

        volunteerQueryWrapper = new QueryWrapper<>();
        volunteerQueryWrapper.select("occupation as name","count(*) as value");
        volunteerQueryWrapper.groupBy("occupation");
        List<Map<String,Object>> occupation = volunteerService.listMaps(volunteerQueryWrapper);
        resultMap.put("occupation",occupation);

        return ResultVo.success("获取成功",resultMap);
    }
}
