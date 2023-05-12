package com.wpl.volunteer.config.security;

import com.wpl.volunteer.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Qualifier("adminServiceImpl")
    @Autowired
    private UserDetailsService adminServiceImpl;

    @Qualifier("userServiceImpl")
    @Autowired
    private UserDetailsService userServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
//        log.info("开启jwt认证");
        String token = request.getHeader("token");

        if (token != null && !"".equals(token)){
            String username = JwtUtils.getClaimByName(token,"username").asString();
            String userType = JwtUtils.getClaimByName(token,"userType").asString();

            //SecurityContextHolder.getContext().getAuthentication()==null 未认证则为true
//            System.out.println(SecurityContextHolder.getContext().getAuthentication());
//            log.info("jwt认证:检查用户名");
            if (null != username && SecurityContextHolder.getContext().getAuthentication( )== null) {
                if (userType.equals("Administrator")){
                    //认证
                    UserDetails userDetails = adminServiceImpl.loadUserByUsername(username);
                    //验证token是否有效，重新设置用户对象
                    boolean jwt = JwtUtils.verifyToken(token,username + userType);
                    if (jwt){
                        // 将用户信息存入 authentication，方便后续校验
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,null,userDetails.getAuthorities());
//                        log.info("通过jwt认证，设置Authentication,后续过滤器放行");
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }else{
                    //认证
                    UserDetails userDetails = userServiceImpl.loadUserByUsername(username);
                    //验证token是否有效，重新设置用户对象
                    boolean jwt = JwtUtils.verifyToken(token,username + userType);
                    if (jwt){
                        // 将用户信息存入 authentication，方便后续校验
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails,null,null);
//                        log.info("通过jwt认证，设置Authentication,后续过滤器放行");
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }else{
            log.info("首次登陆 jwt为空");
        }
        chain.doFilter(request, response);
    }
}
