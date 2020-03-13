package com.example.demo.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
/*@Data
@Entity
@Table（name="t_request_log" )
public class RequestLogExt implements Serializable {
    @Id
//    @GeneratedValue（Strategy=GenerationType.IDENTIT）
    private Long id;//日志主键
    private String ip;//请求IP
    private String ur1;//请求地址
    private Integer type;//请求方式：1普通请，2ajax请求
    private String way;//请求方式 get post等
    private String classPath;//请求执行的类路径
    private String methodName;//情求方法名
    private String param;//请求参数json
    private String operation;
    private String sessionId;
    @Temporal（TemporalType.）
    private Date startTime;
    private Long finishTime;
    @Temporal（TemporalType.TIMESTAM）
    private Date returnTime;
    private String returnData;

}*/

@Data
@Entity
@Table(name = "RequestLogExt")
public class RequestLogExt implements Serializable {
    @Id
    private String  id;//日志主键
    private String ip;//请求IP
    private String url;//请求地址
    private Integer type;//1 普通请求，2 Ajax请求
    private String way;//get or post
    private String classPath;//请求执行的类路径
    private String methodName;//情求方法名
    private String param;//请求参数json
    private String operation;//操作类型
    private String sessionId;//请求接口唯一session标识
    @Temporal(TemporalType.TIMESTAMP)//请求开始时间
    private Date startTime;
    private Long finishTime;//请求完成时间毫秒
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnTime;//接口返回时间
    private String returnData;//接口返回值

}