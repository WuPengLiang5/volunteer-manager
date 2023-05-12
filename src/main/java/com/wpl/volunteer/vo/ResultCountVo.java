package com.wpl.volunteer.vo;

import lombok.Data;

@Data
public class ResultCountVo extends ResultVo {

    /**
     * 数据总条数
     */
    private long total;

    public ResultCountVo() {
    }

    public ResultCountVo(long total) {
        this.total = total;
    }

    public ResultCountVo(Integer code, String msg, Object data, Integer total) {
        super(code, msg, data);
        this.total = total;
    }

    public ResultCountVo(ResultVo resultVo, long total){
        super(resultVo.getCode(), resultVo.getMsg(), resultVo.getData());
        this.total = total;
    }
}
