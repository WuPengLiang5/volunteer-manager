package com.wpl.volunteer.config.security;

import com.alibaba.fastjson.JSON;
import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(JSON.toJSON(ResultVo.error(CodeMsgConstant.FORBIDDEN)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
