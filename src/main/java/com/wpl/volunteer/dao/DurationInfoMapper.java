package com.wpl.volunteer.dao;

import com.wpl.volunteer.entity.DurationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WuPengLiang
 * @since 2022-05-18
 */
@Mapper
@Component
public interface DurationInfoMapper extends BaseMapper<DurationInfo> {

}
