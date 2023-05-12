package com.wpl.volunteer.vo;

import com.wpl.volunteer.constant.CodeMsg;
import com.wpl.volunteer.constant.CodeMsgConstant;
import lombok.Data;

@Data
public class ResultVo {
    //返回状态码
    private Integer code;
    //返回前端的消息
    private String msg;
    //返回给前端的数据
    private Object data;

    public ResultVo() {
    }

    public ResultVo(Integer code) {
        this.code = code;
    }

    public ResultVo(CodeMsg codeMsg, Object data) {
        if (codeMsg != null){
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
        this.data = data;
    }

    public ResultVo(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //成功获取数据
    public static ResultVo success(Object data) {
        return new ResultVo(CodeMsgConstant.SUCCESS, data);
    }

    public static ResultVo success(String msg){
        return new ResultVo(new CodeMsg(200,msg),null);
    }

    public static ResultVo success(String msg,Object data){
        return new ResultVo(new CodeMsg(200,msg),data);
    }

    //通用错误信息
    public static ResultVo error(String message){
        return new ResultVo(500, message,null);
    }

    public static ResultVo error(CodeMsg codeMsg){
        return new ResultVo(codeMsg,null);
    }

    public static ResultVo error(CodeMsg codeMsg,Object data){
        return new ResultVo(codeMsg,data);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }

}
