package com.hyd.zcar.cms.controller;

import com.example.demo.Exception.CustomerException;
import com.hyd.zcar.cms.utils.annotation.OperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc: 核心业务模块
 * @author: CSH
 **/
@RestController
@RequestMapping("/order")
public class OrderController {
    @OperLog(operModul = "销售管理", operType = "add", operDesc = "新增")
    @GetMapping(value = "/add")
    public String addinfo() throws Exception {
        System.out.println("ok");


        return "123" ;
    }


    @OperLog(operModul = "异常测试", operType = "error", operDesc = "测试")
    @GetMapping(value = "/testerror")
    public String testError() throws Exception {
        int a = 1 / 0;
        return "456" ;
    }

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/test18")
    public String test18N() {
        return messageSource.getMessage("welcome", null, LocaleContextHolder.getLocale());
    }

    @GetMapping("/customException")
    public String customException(){
        throw new CustomerException("用户自定义异常");
    }
}
