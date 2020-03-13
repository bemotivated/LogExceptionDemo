package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Exception.ClassException;
import com.example.demo.Exception.NotFindException;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class RequestUti1 {
    /*            <!-- 获取切入点的参数信息 -->*/

    /**
     *获我切入点方法信息
     *@param joinPoint
     *@return joinPointInfo
     *key:classPath 切入点方法类路径
     *ker:methodName 切入点方法名
     *ker:paramlap方法参数
     */
    public static Map<String,Object> getJoinPointInfoMap(JoinPoint joinPoint){
        Map<String,Object>joinPointInfo=new HashMap<>();
        String classPath=joinPoint.getTarget().getClass().getName();
        String methodName=joinPoint.getSignature().getName();
        joinPointInfo.put("classPath",classPath);
        joinPointInfo.put("methodName",methodName);
        Class<?>clazz=null;
        CtMethod ctMethod=null;
        LocalVariableAttribute attr=null;
        int length=0;
        int pos=0;
        try{
            clazz=Class.forName(classPath);
            String clazzName=clazz.getName();
            ClassPool pool=ClassPool.getDefault();
            ClassClassPath classClassPath=new ClassClassPath(clazz);
            pool.insertClassPath(classClassPath);
            CtClass ctClass=pool.get(clazzName);
            ctMethod=ctClass.getDeclaredMethod(methodName);
            MethodInfo methodInfo=ctMethod.getMethodInfo();
            CodeAttribute codeAttribute=methodInfo.getCodeAttribute();
            attr=(LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if(attr==null){
                return joinPointInfo;
            }
            /* <!-- 获取切入点的参数信息 -->*/
            length=ctMethod.getParameterTypes().length;
            pos=Modifier.isStatic(ctMethod.getModifiers())?0:1;
        }catch(ClassNotFoundException e){
            throw new ClassException("获取类实例失败");
        }catch(NotFoundException e){
            throw new NotFindException("未找到参数类型");
        }
        Map<String,Object>paramMap=new HashMap<>();
        Object[] paramsArgsValues=joinPoint.getArgs();
        String[] paramsArgsNames=new String[length];
        for(int i=0;i<length;i++){
            paramsArgsNames[i]=attr.variableName(i+pos);
            String paramsArgsName=attr.variableName(i+pos);
            if(paramsArgsName.equalsIgnoreCase("request")||
                    paramsArgsName.equalsIgnoreCase("response")|| paramsArgsName.equalsIgnoreCase("session")){
                break;
            }
            Object paramsArgsValue=paramsArgsValues[i];
            paramMap.put(paramsArgsName, paramsArgsValue);
        }
        joinPointInfo.put("paramMap",JSON.toJSONString(paramMap, SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue));
        return joinPointInfo;
    }
    /*            <!-- 获取请求方式 -->*/
    /**
     *获取请求方式;普通请求ajax清求
     *@param request
     *@return
     */
    public static Integer getRequestType(HttpServletRequest request){
        if(request==null){
            throw new RequestException("HttpServletRequest对象为空");
        }
        String xRequestWith=request.getHeader("X-Requested-With");
        return xRequestWith==null?1:2;
    }
    /**
     *获取请求D
     *@param request
     *@return
     */

    /* <!-- 获取请求IP -->*/

    public static String getRequestIp(HttpServletRequest request){
        if(request==null){
            throw new RequestException("HttpServletRequest对象为空");
        }
        String ip=request.getHeader("x-forwarded-for");
        if(ip==null || ip.trim()==""||"unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("Proxy-Client-IP");
        }
        if(ip==null || ip.trim()==""||"unknown".equalsIgnoreCase(ip)){
            ip=request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip==null|| ip.trim()==""||"unknown".equalsIgnoreCase(ip)){
            ip=request.getRemoteAddr();
        }
        final String[]arr=ip.split(",");
        for(final String str:arr){
            if(!"unknown".equalsIgnoreCase(str)){
                ip=str;
                break;
            }
        }
        return ip;
    }

}
