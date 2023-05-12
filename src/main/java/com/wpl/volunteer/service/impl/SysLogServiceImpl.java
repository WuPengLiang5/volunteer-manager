package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.dao.SysLogDao;
import com.wpl.volunteer.dto.QueryDTO;
import com.wpl.volunteer.dto.QuerySysLogDTO;
import com.wpl.volunteer.entity.Admin;
import com.wpl.volunteer.entity.News;
import com.wpl.volunteer.entity.SysLog;
import com.wpl.volunteer.service.SysLogService;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public SysLog getSysLogById(Integer id) {
        return sysLogDao.selectById(id);
    }

    @Override
    public ResultCountVo listSysLogsByPage(QuerySysLogDTO querySysLogDTO) {
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();

        if(querySysLogDTO.getUsername()!=null && !"".equals(querySysLogDTO.getUsername()))
            wrapper.like("username",querySysLogDTO.getUsername());
        if(querySysLogDTO.getModule()!=null && !"".equals(querySysLogDTO.getModule()))
            wrapper.like("module",querySysLogDTO.getModule());
        wrapper.orderByDesc("id");

        IPage<SysLog> page = new Page<>(querySysLogDTO.getPage(), querySysLogDTO.getLimit());
        sysLogDao.selectPage(page, wrapper);

        List<SysLog> sysLogs = page.getRecords();

        return new ResultCountVo(ResultVo.success("获取成功",sysLogs), page.getTotal());
    }

    @Override
    public int saveSysLog(SysLog sysLog) {
        return sysLogDao.insert(sysLog);
    }

    @Override
    public int removeSysLog(Integer id) {
        return sysLogDao.deleteById(id);
    }

    @Override
    public int updateSysLog(SysLog sysLog) {
        return sysLogDao.updateById(sysLog);
    }

    @Override
    public int removeSysLog(Integer[] ids) {
        return sysLogDao.deleteBatchIds(Arrays.asList(ids));
    }
}
