package com.example.demo.Entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode()
//@JsonIgnoreProperties(ignoreUnknown = true)

public class WEntityVO {

    @NotBlank(message = "文件的id不能为空")
    private String fileid;//=System.currentTimeMillis();//时间点作为版本号
    @NotBlank(message = "文件的MD5不能为空")
    private String md5;

}
