package com.wpl.volunteer.enums;

import lombok.Getter;

@Getter
public enum SignUpStatus implements BaseEnum {
    FAILED(-1,"审核未通过"),
    NOT_APPROVED(0,"待审核"),
    PASS(1,"审核通过");

    private final Integer code;
    private final String name;

    SignUpStatus(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
