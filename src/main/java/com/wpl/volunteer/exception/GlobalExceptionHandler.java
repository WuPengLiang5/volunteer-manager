package com.wpl.volunteer.exception;

import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultVo globalExceptionHandler(Exception e) {
        e.printStackTrace(System.out);

        if (e instanceof GlobalException) {
            GlobalException ge = (GlobalException) e;
            return ResultVo.error(ge.getCodeMsg());
        }else {
            // 其他异常，统一返回 code: 500
            return ResultVo.error(CodeMsgConstant.SERVER_ERROR);
        }
    }
}
