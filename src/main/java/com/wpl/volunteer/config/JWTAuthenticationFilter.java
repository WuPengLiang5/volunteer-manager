//package com.wpl.volunteer.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wpl.volunteer.dto.LoginDTO;
//import com.wpl.volunteer.entity.SecurityUser;
//import com.wpl.volunteer.util.JwtUtils;
//import com.wpl.volunteer.util.VerifyCodeUtil;
//import com.wpl.volunteer.vo.ResultVo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    @Autowired
//    private VerifyCodeUtil verifyCodeUtil;
//
//    // 固定代码
//    private AuthenticationManager authenticationManager;
//
//    // 一个构造方法
//    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//        // 这里指定登录请求的接口(我们可以把这个类当作登录功能的controller层)不指定默认为 /login
//        super.setFilterProcessesUrl("/user/login");
//    }
//
//    // 核心方法
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request,
//                                                HttpServletResponse response) throws AuthenticationException {
//
//        // 从输入流中获取到登录的信息
//        try {
//            LoginDTO loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
//            verifyCodeUtil.checkVerifyCode(request,response,loginUser.getVerifyCode());
//            return authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginUser.getUsername(),
//                            loginUser.getPassword(),
//                            new ArrayList<>())
//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//
//    // 成功验证后调用的方法
//    // 如果验证成功，就生成token并返回
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//
//        // 调用getPrincipal()方法会返回一个实现了UserDetails接口的对象,所以这里我们就返回上文定义的DetailUser
//        response.setContentType("application/json;charset=utf-8");
//        SecurityUser securityUser = (SecurityUser) authResult.getPrincipal();
//
//        // 获取用户角色信息
//        String role = "";
//        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
//        for (GrantedAuthority authority : authorities){
//            role = authority.getAuthority();
//        }
//        // 登陆成功，生成JWT并放在响应头里，并封装成JSON格式返回给前台
//        String token = JwtUtils.createToken(securityUser.getCurrentUserInfo().getId(),securityUser.getUsername(), role);
//        response.setHeader("token", JwtUtils.TOKEN_PREFIX + token);
//
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        // 我们前面定义的模板类,把密码置空，防止隐私信息暴露
//        writer.write(new ObjectMapper().writeValueAsString(ResultVo.success(securityUser)));
//        writer.flush();
//        writer.close();
//
//    }
//
//    // 这是验证失败时候调用的方法（登陆失败）
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        response.setContentType("application/json;charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        // 提示用户名或密码错误就行了
//        writer.write(new ObjectMapper().writeValueAsString(ResultVo.error("用户名或密码错误")));
//        writer.flush();
//        writer.close();
//    }
//}
