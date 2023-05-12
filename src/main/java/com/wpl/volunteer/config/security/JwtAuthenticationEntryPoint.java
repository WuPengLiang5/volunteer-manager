package com.wpl.volunteer.config.security;

import com.alibaba.fastjson.JSON;
import com.wpl.volunteer.constant.CodeMsgConstant;
import com.wpl.volunteer.vo.ResultVo;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 未登录或登录过期
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(JSON.toJSON(ResultVo.error(CodeMsgConstant.UNAUTHORIZED)));
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
