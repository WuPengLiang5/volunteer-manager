package com.wpl.volunteer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wpl.volunteer.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NewsDao extends BaseMapper<News> {
}
