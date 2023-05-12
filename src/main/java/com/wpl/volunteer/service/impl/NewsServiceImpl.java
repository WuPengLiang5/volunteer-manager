package com.wpl.volunteer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wpl.volunteer.dao.AdminDao;
import com.wpl.volunteer.dao.NewsDao;
import com.wpl.volunteer.dto.QueryNewsDTO;
import com.wpl.volunteer.entity.Activity;
import com.wpl.volunteer.entity.Admin;
import com.wpl.volunteer.entity.News;
import com.wpl.volunteer.entity.SecurityUser;
import com.wpl.volunteer.service.NewsService;
import com.wpl.volunteer.util.SecurityUtils;
import com.wpl.volunteer.vo.ResultCountVo;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private SecurityUtils securityUtils;

    @Override
    public News getNewsById(Integer id) {
        return newsDao.selectById(id);
    }

    @Override
    public ResultCountVo listNews(QueryNewsDTO queryNewsDTO) {
        QueryWrapper<News> wrapper = new QueryWrapper<>();

        if(queryNewsDTO.getTitle()!=null && !"".equals(queryNewsDTO.getTitle()))
            wrapper.like("title",queryNewsDTO.getTitle());
        if(queryNewsDTO.getSource()!=null && !"".equals(queryNewsDTO.getSource()))
            wrapper.like("source",queryNewsDTO.getSource());
        if(queryNewsDTO.getState()!=null && !"".equals(queryNewsDTO.getState()))
            wrapper.like("state",queryNewsDTO.getState());
        if(queryNewsDTO.getSort()!=null && !"".equals(queryNewsDTO.getSort()))
            wrapper.orderBy(true,queryNewsDTO.getSort().equals("+id"),"id");

        IPage<News> page = new Page<>(queryNewsDTO.getPage(), queryNewsDTO.getLimit());
        newsDao.selectPage(page, wrapper);

        return new ResultCountVo(ResultVo.success("获取成功",page.getRecords()), page.getTotal());
    }

    @Override
    public int saveNews(News news) {
        news.setState(0);
        news.setCreateTime(LocalDateTime.now());
        SecurityUser securityUser = (SecurityUser) securityUtils.getCurrentUser();
        String userName = securityUtils.getCurrentUserName();
        news.setCreator(securityUtils.getCurrentUserName());
        return newsDao.insert(news);
    }

    @Override
    public int removeNews(Integer id) {
        return newsDao.deleteById(id);
    }

    @Override
    public int updateNews(News news) {
        return newsDao.updateById(news);
    }

    @Override
    public int removeNews(Integer[] ids) {
        return newsDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public int selectCount() {
        return newsDao.selectCount(null);
    }

    @Override
    public Map<String,Object> getNewsByType(QueryNewsDTO queryNewsDTO) {
        Map<String,Object> map = new HashMap<String,Object>();
        QueryWrapper<News> wrapper = new QueryWrapper<>();

        if(queryNewsDTO.getNewsType() != null)
            wrapper.eq("news_type",queryNewsDTO.getNewsType());
        wrapper.eq("state",1);
        wrapper.orderByDesc("create_time");

        IPage<News> page = new Page<>(queryNewsDTO.getPage(), queryNewsDTO.getLimit());
        newsDao.selectPage(page, wrapper);

        map.put("newsList",page.getRecords());
        map.put("total",page.getTotal());

        return map;
    }

    @Override
    public ResultCountVo getIndexesNewsList(QueryNewsDTO queryNewsDTO) {
       return new ResultCountVo();
    }
}
