//package com.wpl.volunteer.config;
//
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wpl.volunteer.constant.CodeMsg;
//import com.wpl.volunteer.constant.CodeMsgConstant;
//import com.wpl.volunteer.entity.Admin;
//import com.wpl.volunteer.entity.User;
//import com.wpl.volunteer.exception.GlobalException;
//import com.wpl.volunteer.service.AdminService;
//import com.wpl.volunteer.service.UserService;
//import com.wpl.volunteer.util.JwtUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Component
//public class JwtAuthenticationInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private AdminService adminService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object object)throws Exception{
//        // 从请求头中取出 token  这里需要和前端约定好把jwt放到请求头一个叫token的地方
//        String token = httpServletRequest.getHeader("token");
//        if (!(httpServletRequest.getRequestURI().equalsIgnoreCase("/error"))){
//            // 如果不是映射到方法直接通过
//            if (!(object instanceof HandlerMethod)) {
//                return true;
//            }
//            if (token == null) {
//                //这里其实是登录失效,没token了
//                throw new GlobalException(CodeMsgConstant.UNAUTHORIZED);
//            }
//
//            // 获取 token 中的 用户的ID
//            Integer id = null;
//            String username = null;
//            String role = null;
//
//            id = Integer.valueOf(JwtUtils.getAudience(token));
//            role = JwtUtils.getClaimByName(token,"role").asString();
//            username = JwtUtils.getClaimByName(token,"username").asString();
////            } catch (JWTDecodeException e){
////                throw new GlobalException(CodeMsgConstant.UNAUTHORIZED);
//////                return false;
////            }
//
//
//            Admin admin = null;
//            User user = null;
//            if (role.equals("admin")){
//                admin = adminService.getAdminById(id);
//            }else{
//                user = userService.getUserById(id);
//            }
//
//            if (user == null && role.equals("user")) {
//                throw new GlobalException(CodeMsgConstant.NONE_USER);
//            }
//
//            if (admin == null && role.equals("admin")) {
//                throw new GlobalException(CodeMsgConstant.NONE_ADMIN);
//            }
//
//            // 验证 token
//            JwtUtils.verifyToken(token, id + username + role);
//        }
//        return true;
//    }
//}
