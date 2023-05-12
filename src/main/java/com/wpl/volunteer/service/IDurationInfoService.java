package com.wpl.volunteer.service;

import com.wpl.volunteer.dto.QueryDurationInfoDTO;
import com.wpl.volunteer.entity.DurationInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wpl.volunteer.vo.ResultCountVo;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author WuPengLiang
 * @since 2022-05-18
 */
public interface IDurationInfoService extends IService<DurationInfo> {
    ResultCountVo getDurationInfoList(QueryDurationInfoDTO queryDTO);

    ResultCountVo getInputDurationOfVolunteer(QueryDurationInfoDTO queryDTO);
}
