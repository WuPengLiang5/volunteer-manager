package com.wpl.volunteer.service;

import com.wpl.volunteer.dto.QuerySysLogDTO;
import com.wpl.volunteer.entity.SysLog;
import com.wpl.volunteer.vo.ResultCountVo;

public interface SysLogService {
    SysLog getSysLogById(Integer id);
    ResultCountVo listSysLogsByPage(QuerySysLogDTO querySysLogDTO);
    int saveSysLog(SysLog sysLog);
    int removeSysLog(Integer id);
    int updateSysLog(SysLog sysLog);
    int removeSysLog(Integer[] ids);
}
