package com.example.demo.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "excetionlogExt")
public class ExceptionLogExt implements Serializable {
    @Id
    private String id;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String exceptionJson;//异常对象ison形式
    @Lob
    private String exceptionMessage;//异常信息等同于e.getMessase()
    @Temporal(TemporalType.TIMESTAMP)//精确到时分秒
    private Date happenTime;//异常产生时间

}
