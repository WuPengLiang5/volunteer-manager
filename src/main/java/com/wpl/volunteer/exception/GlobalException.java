package com.wpl.volunteer.exception;

import com.wpl.volunteer.constant.CodeMsg;

public class GlobalException extends RuntimeException {

    private CodeMsg codeMsg;

    public CodeMsg getCodeMsg(){
        return codeMsg;
    }
    public GlobalException(CodeMsg codeMsg){
        super(codeMsg.toString());
        this.codeMsg = codeMsg;
    }

    public GlobalException(String msg){
        super(msg);
        this.codeMsg = new CodeMsg(500,msg);
    }

}
