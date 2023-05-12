package com.wpl.volunteer.dto;

import lombok.Data;

@Data
public class QueryDTO {
    private Integer page; // 当前页面
    private Integer limit; // 页面大小
}
