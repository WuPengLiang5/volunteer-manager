package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class QueryDictDTO extends QueryDTO{
    private String type;
    private String typeName;
    private String code;
}
