package com.wpl.volunteer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class SysLog {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String module; //操作模块
    private Integer businessType; //业务类型（0其它 1新增 2修改 3删除）
    private String method; //方法名称
    private String requestMethod; //请求方式
    private String username; //操作人员
    private String url; //请求URL
    private String ip; //IP地址
    private String params; //操作参数
    private String jsonResult; //返回参数
    private Integer status; // 操作状态（0正常 1异常）
    private String errorMsg; // 错误消息
    private Date createTime; //操作时间
}
