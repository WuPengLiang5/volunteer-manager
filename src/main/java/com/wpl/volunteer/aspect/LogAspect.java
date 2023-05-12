package com.wpl.volunteer.aspect;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpl.volunteer.annotation.Log;
import com.wpl.volunteer.dao.SysLogDao;
import com.wpl.volunteer.entity.SysLog;
import com.wpl.volunteer.enums.BusinessStatus;
import com.wpl.volunteer.util.IPUtil;
import com.wpl.volunteer.util.JwtUtils;
import com.wpl.volunteer.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LogAspect {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SysLogDao sysLogDao;

    public LogAspect(){log.info("初始化接口日志切面类...");}

    @Pointcut("@annotation(com.wpl.volunteer.annotation.Log)")
    public void logPointCut() {
    }

//    @Around("logPointCut()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        Date start = new Date();
//        try {
//            //数据库对应的实体
//            SysLog sysLog = new SysLog();
//            Log _log = method.getAnnotation(Log.class);
//            if (_log != null) {
//                //注解上的描述
//                sysLog.setModule(_log.module());
//                sysLog.setMethod(_log.remark());
//            }
//            //请求的方法名
//            String clazzName = joinPoint.getTarget().getClass().getName();
//            Class<?> clazz = Class.forName(clazzName);
//            String clazzSimpleName = clazz.getSimpleName();
//            String methodName = signature.getName();
//            sysLog.setOperationDesc(clazzSimpleName + "." + methodName);
//
//            //请求的参数
//            String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
//            StringBuilder sb = null;
//            if (Objects.nonNull(parameterNames)) {
//                sb = new StringBuilder();
//                for (int i = 0; i < parameterNames.length; i++) {
//                    Object param = joinPoint.getArgs()[i] != null ? joinPoint.getArgs()[i] : "";
//                    if (StringUtils.isNotEmpty(param.toString()) && !"request".equals(parameterNames[i]) && !"response".equals(parameterNames[i])
//                            && !"modelMap".equals(parameterNames[i])) {
//                        if (param instanceof Integer) {
//                            sb.append(parameterNames[i] + ":" + param + "; ");
//                        } else if (param instanceof String) {
//                            sb.append(parameterNames[i] + ":" + param + "; ");
//                        } else if (param instanceof Double) {
//                            sb.append(parameterNames[i] + ":" + param + "; ");
//                        } else if (param instanceof Float) {
//                            sb.append(parameterNames[i] + ":" + param + "; ");
//                        } else if (param instanceof Long) {
//                            sb.append(parameterNames[i] + ":" + param + "; ");
//                        } else if (param instanceof Boolean) {
//                            sb.append(parameterNames[i] + ":" + param + "; ");
//                        } else if (param instanceof Date) {
//                            sb.append(parameterNames[i] + ":" + param + "; ");
//                        } else if (param instanceof Timestamp) {
//                            sb.append(parameterNames[i] + ":" + param + "; ");
//                        } else {
//                            sb.append(parameterNames[i] + ":" + getString(param) + "; ");
//                        }
//                    }
//                }
//            }
//            sb = sb == null ? new StringBuilder() : sb;
//            sysLog.setParams(sb.toString());
//            //设置IP地址
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            sysLog.setIp(IPUtil.getIp(request));
//
//            sysLog.setUsername(getDecodeUserName(request));
//            sysLog.setCreateTime(new Date());
//            log.debug("interface request startTime " + start.getTime());
//            Object o = joinPoint.proceed();
//            String response = objectMapper.writeValueAsString(o);
//
////            sysLog.setTimeMin(System.currentTimeMillis() - start.getTime());
////            log.debug(getString(sysLog));
//            //保存系统日志
//            sysLogDao.insert(sysLog);
//            return o;
//        } catch (Exception ex) {
//            log.error("保存系统日志失败"+ex.getMessage());
//        }
//        return null;
//    }
    public static String getDecodeUserName(HttpServletRequest request) {
        String token = request.getHeader("token");
        String username = token != null? JwtUtils.getClaimByName(token,"username").asString():"";

        if(StringUtils.isNotEmpty(username)) {
            try {
                username = URLDecoder.decode(username, "utf-8");
            } catch (UnsupportedEncodingException var3) {
                log.error(var3.getMessage(), var3);
            }
        }

        return username;
    }
//    public static String getString(Object o) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        StringBuffer sb = new StringBuffer();
//        sb.append("entity[");
//        Field[] farr = o.getClass().getDeclaredFields();
//        for (Field field : farr) {
//            try {
//                field.setAccessible(true);
////                if (!ValidatorUtils.empty(field.get(o))) {
//                    sb.append(field.getName());
//                    sb.append("=");
//                    if (field.get(o) instanceof Date) {
//                        // 日期的处理
//                        sb.append(sdf.format(field.get(o)));
//                    } else {
//                        sb.append(field.get(o));
//                    }
//                    sb.append("|");
////                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        sb.append("]");
//        return sb.toString();
//    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // *========数据库日志=========*//
            SysLog sysLog = new SysLog();
            sysLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = IPUtil.getIp(request);
            sysLog.setIp(ip);
            sysLog.setJsonResult(JSON.toJSONString(jsonResult));
            sysLog.setUrl(request.getRequestURI());
            sysLog.setUsername(getDecodeUserName(request));
            sysLog.setCreateTime(new Date());
            if (e != null) {
                sysLog.setStatus(BusinessStatus.FAIL.ordinal());
                sysLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
//                System.out.println(e);
//                System.out.println(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            sysLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            sysLog.setRequestMethod(request.getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, sysLog);
            sysLogDao.insert(sysLog);
        }catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * @param joinPoint
     * @param log
     * @param sysLog
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysLog sysLog) throws Exception {
        // 设置action动作
        sysLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        sysLog.setModule(log.module());
        // 获取参数的信息，传入到数据库中。
        setRequestValue(joinPoint, sysLog);
    }

    /**
     * 获取请求的参数，放到log中
     * @param joinPoint
     * @param sysLog
     * @throws Exception
     */
    private void setRequestValue(JoinPoint joinPoint, SysLog sysLog) throws Exception {
        String requestMethod = sysLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            sysLog.setParams(StringUtils.substring(params, 0, 2000));
        } else {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            sysLog.setParams(StringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }
}
