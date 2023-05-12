package com.wpl.volunteer.service;

import com.wpl.volunteer.dto.QueryNewsDTO;
import com.wpl.volunteer.dto.QuerySignUpDTO;
import com.wpl.volunteer.entity.News;
import com.wpl.volunteer.vo.ResultCountVo;

import java.util.List;
import java.util.Map;

public interface NewsService {
    News getNewsById(Integer id);
    ResultCountVo listNews(QueryNewsDTO queryNewsDTO);
    int saveNews(News news);
    int removeNews(Integer id);
    int updateNews(News news);
    int removeNews(Integer[] ids);
    int selectCount();
    Map<String,Object> getNewsByType(QueryNewsDTO queryNewsDTO);
    ResultCountVo getIndexesNewsList(QueryNewsDTO queryNewsDTO);
}
