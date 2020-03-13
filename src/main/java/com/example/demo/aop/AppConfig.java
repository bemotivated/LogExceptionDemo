package com.example.demo.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @desc:
 * @author: CuiShiHao
 **/
@Configuration
@ComponentScan("com.example.demo.aop")
@EnableAspectJAutoProxy
public class AppConfig {
}
