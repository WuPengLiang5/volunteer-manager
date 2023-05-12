package com.wpl.volunteer.constant;

import lombok.Data;

@Data
public class CodeMsg {
    private Integer code;
    private String msg;

    public CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "code: " + code + ", msg: " + msg;
    }
}
