package com.example.demo;

import java.util.Date;
//这个时间代码后面要重写，或者从某一个项目中copy出来。
//时间处理，在项目中是必须也解决的

public class DateUtil {
    public static Date parse(long datetime){

        return new Date(datetime);
    }
    public static Date timeDifferlong(long startTime,long returnTime){
        return new Date(returnTime-startTime);
    }
}
