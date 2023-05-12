package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class QueryNewsDTO extends QueryDTO {
    private String title;
    private String source;
    private String state;
    private String sort;

    private String newsType;
}
