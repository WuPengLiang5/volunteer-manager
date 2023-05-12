package com.wpl.volunteer.constant;

public interface CodeMsgConstant {

    //通用的错误码
    CodeMsg SUCCESS = new CodeMsg(200   , "操作成功");
    CodeMsg SERVER_ERROR = new CodeMsg(500, "服务器错误");
    CodeMsg FORBIDDEN = new CodeMsg(403,"拒绝访问");
    CodeMsg UNAUTHORIZED = new CodeMsg(401, "未登录或token过期");

    // 身份校验
    CodeMsg NONE_USER = new CodeMsg(400101, "用户不存在");
    CodeMsg NONE_ADMIN = new CodeMsg(400102, "管理员不存在");
    CodeMsg ACCOUNT_PASSWORD_WRONG = new CodeMsg(4001021, "账号或密码错误");
    CodeMsg PASSWORD_ERROR = new CodeMsg(4001022, "密码错误");
    CodeMsg NONE_VERIFY_CODE = new CodeMsg(400103, "验证码已过期，不存在");
    CodeMsg WRONG_VERIFY_CODE = new CodeMsg(400104, "验证码错误");

    CodeMsg BIND_ERROR = new CodeMsg(50011, "参数校验异常：%s");
    CodeMsg REQUEST_ILLEGAL = new CodeMsg(50012, "请求非法");
    CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(50013, "访问太频繁！");
    CodeMsg DATABASE_DELETE_FAILED = new CodeMsg(50014, "删除数据失败");
    CodeMsg DATABASE_WRITE_FAILED = new CodeMsg(50015,"写入数据库失败");
    CodeMsg NO_UPLOAD = new CodeMsg(50020,"上传文件为空");
    CodeMsg UPLOAD_FAILED = new CodeMsg(50021,"上传失败");
    CodeMsg ROLE_DISABLEDRole = new CodeMsg(50022,"角色被禁用，请联系超级管理员");



//    CodeMsg ACCOUNT_EXISTS = new CodeMsg(50030, "手机号已存在");
//
//    CodeMsg LOGINFAIL = new CodeMsg(4011, "登陆失败");




//    //登录模块 5002XX
//    CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
//    CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
//    CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
//    CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");



}
