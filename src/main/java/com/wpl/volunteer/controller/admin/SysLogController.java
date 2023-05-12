package com.wpl.volunteer.controller.admin;

import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.dto.QuerySysLogDTO;
import com.wpl.volunteer.entity.SysLog;
import com.wpl.volunteer.service.SysLogService;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sysLog")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("/listSysLogsByPage")
    public ResultCountVo listSysLogsByPage(@RequestBody QuerySysLogDTO querySysLogDTO){
        return sysLogService.listSysLogsByPage(querySysLogDTO);
    }

    @GetMapping("/getSysLogById")
    public ResultVo getSysLogById(Integer id){
        SysLog SysLog = sysLogService.getSysLogById(id);
        if (SysLog == null){
            return ResultVo.error("日志不存在");
        }
        return ResultVo.success("获取成功",SysLog);
    }

    @PostMapping("/saveSysLog")
    public ResultVo saveSysLog(@RequestBody SysLog SysLog){
        int result = sysLogService.saveSysLog(SysLog);
        if (result != 1){
            return ResultVo.error("添加日志失败");
        }
        return ResultVo.success("成功添加日志");
    }

    @RequestMapping("/delSysLog")
    public ResultVo removeSysLog(@RequestParam Integer SysLogId){
        int result = sysLogService.removeSysLog(SysLogId);
        if (result != 1){
            return ResultVo.error("删除日志失败");
        }
        return ResultVo.success("成功删除日志");
    }

    @RequestMapping("/updateSysLog")
    public ResultVo updateSysLog(@RequestBody SysLog SysLog){
        int result = sysLogService.updateSysLog(SysLog);
        if (result != 1){
            return ResultVo.error("更新日志失败");
        }
        return ResultVo.success("成功更新日志");
    }

    @RequestMapping("/delBatchSysLogs")
    public ResultVo removeSysLogs(Integer[] ids){
        int result = sysLogService.removeSysLog(ids);
        if (result < 0){
            return ResultVo.error("批量删除失败");
        }
        return ResultVo.success("批量删除成功");
    }
}
