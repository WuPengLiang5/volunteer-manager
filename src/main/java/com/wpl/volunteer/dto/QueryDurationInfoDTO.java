package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class QueryDurationInfoDTO extends QueryDTO{
    private Integer activityId;
    private Integer userId;
    private Integer state;
}
