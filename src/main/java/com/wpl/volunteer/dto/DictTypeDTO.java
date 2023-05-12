package com.wpl.volunteer.dto;

import lombok.Data;

import java.util.List;

@Data
public class DictTypeDTO {
    private List<Integer> ids;
    private String type;
    private String typeName;
}
