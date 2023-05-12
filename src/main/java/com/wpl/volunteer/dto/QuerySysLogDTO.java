package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class QuerySysLogDTO extends QueryDTO {
    private String username;
    private String module;
}
