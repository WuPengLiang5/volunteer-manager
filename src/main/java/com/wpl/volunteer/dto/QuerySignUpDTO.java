package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class QuerySignUpDTO extends QueryDTO {
    private String activityId;
    private Integer state;
}
