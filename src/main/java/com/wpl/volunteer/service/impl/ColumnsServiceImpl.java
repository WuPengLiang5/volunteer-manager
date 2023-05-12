package com.wpl.volunteer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.JsonArray;
import com.wpl.volunteer.entity.Columns;
import com.wpl.volunteer.dao.ColumnsMapper;
import com.wpl.volunteer.entity.News;
import com.wpl.volunteer.service.IColumnsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wpl.volunteer.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WuPengLiang
 * @since 2022-05-14
 */
@Service
public class ColumnsServiceImpl extends ServiceImpl<ColumnsMapper, Columns> implements IColumnsService {

    @Autowired
    private ColumnsMapper columnsMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<Columns> getColumnsByPid(String pid) {
        QueryWrapper<Columns> wrapper = new QueryWrapper<>();
        wrapper.eq("pid",pid);
        wrapper.orderByAsc("sort");

        return columnsMapper.selectList(wrapper);
    }

    @Override
    public List<Columns> listWebColumns() {
        List<Columns> columns = JSONObject.parseArray((String) redisUtil.get("listWebColumns"),Columns.class);
        if (columns != null){
            return columns;
        }

        QueryWrapper<Columns> wrapper = new QueryWrapper<>();
        wrapper.eq("pid","-1");
        wrapper.orderByAsc("sort");
        columns = columnsMapper.selectList(wrapper);

        redisUtil.set("listWebColumns", JSONObject.toJSON(columns).toString(),7 * 24 * 60 * 60 * 1000L);

        return columns;
    }
}
