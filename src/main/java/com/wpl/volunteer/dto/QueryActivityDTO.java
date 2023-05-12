package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class QueryActivityDTO extends QueryDTO {
    private String title;
    private String introduction;
    private String location;
    private String registerMax;
    private Integer state;
}
