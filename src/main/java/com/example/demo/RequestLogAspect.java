package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.Dao.ExceptionLogRepository;
import com.example.demo.Dao.RequestLogRepository;
import com.example.demo.Entity.ExceptionLogExt;
import com.example.demo.Entity.RequestLogExt;
import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

//import com.example.demo.Dao.ExceptionLogRepository;

/**
 * 切面处理类,操作日志异常日志记录处理
 *
 * @author wu
 * @date 2019/03/21
 */
@Aspect
@Component
@Slf4j
public class RequestLogAspect {
    private RequestLogExt requestLogExt =new RequestLogExt();
    private ExceptionLogExt exceptionLog=new ExceptionLogExt();
    private long startTime;
    private long returnTime;
    @Autowired
    private RequestLogRepository requestLogRepository;
    @Autowired
    private ExceptionLogRepository exceptionLogRepository;
        /*<!-- 切入点 -->*/
    @Autowired
    private HttpServletRequest request;
    /** *定义请求日志切入点
     *@param operation
     */
    @Pointcut(value="@annotation(operation)" )
    public void serviceStatistics(Operation operation){
    }

    /*  *//*  <!-- 前置通知 -->*//*
    *//**
     *前置通知
     *@param joinPoint
     *@Param operation
     *//* @Before("serviceStatistics(operation)")
    public void doBefore(JoinPoint joinPoint,Operation operation){
        //茶取切入点参数
        Map<String,Object> joinPointInfo=RequestUti1.getJoinPointInfoMap(joinPoint);
        //设置请尖信息
        startTime=System.currentTimeMillis();
        requestLogExt.setStartTime(DateUtil.parse(startTime));
        requestLogExt.setId(UUID.randomUUID().toString());
        requestLogExt.setIp(RequestUti1.getRequestIp(request));
        requestLogExt.setClassPath(joinPointInfo.get("classPath").toString());
        requestLogExt.setMethodName(joinPointInfo.get("methodName").toString());
        requestLogExt.setWay(request.getMethod());
        requestLogExt.setParam((String)joinPointInfo.get("paramMap"));
        requestLogExt.setType(RequestUti1.getRequestType(request));
        requestLogExt.setSessionId(request.getSession().getId());
        requestLogExt.setUrl(request.getRequestURL().toString());
        requestLogExt.setOperation(operation.value());
    }*/
       /*     <!-- 返回通知 -->*/
    /**
     *返回通知
     *@param operation
     *@param returnValue
     */
    @AfterReturning(value="serviceStatistics(operation)",returning="returnValue")
    public void doAfterReturning(Operation operation,Object returnValue){
        //完善请求信息
        returnTime=System.currentTimeMillis();
        requestLogExt.setId(UUID.randomUUID().toString());
        requestLogExt.setReturnTime(DateUtil.parse(returnTime));
        //requestLogExt.setFinishTime(DateUtil.timeDifferlong(startTime,returnTime));
        requestLogExt.setReturnData(JSON.toJSONString(returnValue,
        SerializerFeature.DisableCircularReferenceDetect,
        SerializerFeature.WriteMapNullValue));

        //保存请求日志数据
        requestLogRepository.save(requestLogExt);
        log.info("当前的记录是:"+requestLogExt.getMethodName()+requestLogExt.getParam());
    }

         /*   <!-- 异常通知 -->*/
    /**
     *异常通知
     *@param operation
     *@param
     */
    @AfterThrowing(value="serviceStatistics(operation)",throwing="e")
    public void doAfterThrowing(Operation operation,Throwable e){
        //设置异常信息
        long happenTime=System.currentTimeMillis();
        exceptionLog.setId(UUID.randomUUID().toString());
        exceptionLog.setHappenTime(DateUtil.parse(happenTime));
        exceptionLog.setId(UUID.randomUUID().toString());
        exceptionLog.setExceptionJson(JSON.toJSONString(e,
        SerializerFeature.DisableCircularReferenceDetect,
        SerializerFeature.WriteMapNullValue));
        exceptionLog.setExceptionMessage(e.getMessage());
        //保存异常日志记录
        exceptionLogRepository.save(exceptionLog);
        log.info("当前的异常是:"+exceptionLog.getHappenTime()+exceptionLog.getExceptionMessage());
    }





}
                                        
    /*private RequestLogExt requestLogExt=new RequestLogExt();
    private ExceptionLogExt excetionLog=new ExceptionLogExt();
    private long startTime;
    private long returnTime;
*//*
    @Autowired
    private RequestLogRepository requestLogRepository;

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;*//*

    @Autowired
    private HttpServletRequest httpServletRequest;



    *//**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     *//*
    @Pointcut(value="@annotation(operation)")
    public void serviceStatistics(Operation operation) {
    }

    @Before("serviceStatistics(operation)")
    public void doBefore(JoinPoint joinPoint,Operation operation) throws Exception{
        Map<String,Object> joinPointInfo=getJoinPointInfoMap(joinPoint);
        startTime=System.currentTimeMillis();
//        requestLogExt.setStartTime(DateUtil.Parse(startTime));
        requestLogExt.setMethodName(joinPointInfo.get("methodName").toString());
    }
    public static Map<String,Object> getJoinPointInfoMap(JoinPoint joinPoint) throws NotFoundException, ClassNotFoundException {
        Map<String,Object> joinPointInfo=new HashMap<>();
        String classPath=joinPoint.getTarget().getClass().getName();
        String methodName=joinPoint.getSignature().getName();
        joinPointInfo.put("classPath",classPath);
        joinPointInfo.put("methodName",methodName);
        Class<?> clazz=null;
        CtMethod ctMethod=null;
        LocalVariableAttribute attr=null;
        int length=0;
        int pos=0;
        try {
            clazz=Class.forName(classPath);
            String clazzName=clazz.getName();
            ClassPool pool=ClassPool.getDefault();
            ClassClassPath classClassPath=new ClassClassPath(clazz);
            pool.insertClassPath(classClassPath);
            CtClass ctClass=pool.get(clazzName);
            ctMethod=ctClass.getDeclaredMethod(methodName);
            MethodInfo methodInfo=ctMethod.getMethodInfo();
            CodeAttribute codeAttribute=methodInfo.getCodeAttribute();
            attr=(LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if(attr==null){
                return  joinPointInfo;
            }
            length=ctMethod.getParameterTypes().length;
            pos=Modifier.isStatic(ctMethod.getModifiers())?0:1;
        }catch (ClassNotFoundException e){
            throw new ClassNotFoundException("获取类实例失败");
        }catch (NotFoundException e){
            throw new NotFoundException("未找到参数类型");
        }
        Map<String,Object> paramMap=new HashMap<>();
        Object[] paramsArgsValues=joinPoint.getArgs();
        String[] paramsArgsNames=new String[length];
        for(int i=0;i<length;i++){
            paramsArgsNames[i]=attr.variableName(i+pos);
            String paramsArgsName=attr.variableName(i+pos);
            if(paramsArgsName.equalsIgnoreCase("request")||paramsArgsName.equalsIgnoreCase("response")
                ||paramsArgsName.equalsIgnoreCase("session")){
                break;
            }
            Object paramsArgsValue=paramsArgsValues[i];
            paramMap.put(paramsArgsName,paramsArgsValue);
        }
        joinPointInfo.put("paramMap",JSON.toJSONString(paramMap,SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue));
        return joinPointInfo;
    }
    *//**
     * 正常返回通知,拦截用户操作日志,连接点正常执行完成后执行, 如果连接点抛出异常,则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     *//*
 *//*   @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        OperationLog operlog = new OperationLog();
        try {
            operlog.setOperId(UuidUtil.get32UUID()); // 主键ID

            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            Operation opLog = method.getAnnotation(Operation.class);
            if (opLog != null) {
                String operModul = opLog.operModul();
                String operType = opLog.operType();
                String operDesc = opLog.operDesc();
                operlog.setOperModul(operModul); // 操作模块
                operlog.setOperType(operType); // 操作类型
                operlog.setOperDesc(operDesc); // 操作描述
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;

            operlog.setOperMethod(methodName); // 请求方法

            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);

            operlog.setOperRequParam(params); // 请求参数
            operlog.setOperRespParam(JSON.toJSONString(keys)); // 返回结果
            operlog.setOperUserId(UserShiroUtil.getCurrentUserLoginName()); // 请求用户ID
            operlog.setOperUserName(UserShiroUtil.getCurrentUserName()); // 请求用户名称
            operlog.setOperIp(IPUtil.getRemortIP(request)); // 请求IP
            operlog.setOperUri(request.getRequestURI()); // 请求URI
            operlog.setOperCreateTime(new Date()); // 创建时间
            operlog.setOperVer(operVer); // 操作版本
            operationLogService.insert(operlog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*//*

    *//**
     * 异常返回通知,用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     *//*
  *//*  @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);

        ExceptionLog excepLog = new ExceptionLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            excepLog.setExcId(UuidUtil.get32UUID());
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);
            excepLog.setExcRequParam(params); // 请求参数
            excepLog.setOperMethod(methodName); // 请求方法名
            excepLog.setExcName(e.getClass().getName()); // 异常名称
            excepLog.setExcMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
            excepLog.setOperUserId(UserShiroUtil.getCurrentUserLoginName()); // 操作员ID
            excepLog.setOperUserName(UserShiroUtil.getCurrentUserName()); // 操作员名称
            excepLog.setOperUri(request.getRequestURI()); // 操作URI
            excepLog.setOperIp(IPUtil.getRemortIP(request)); // 操作员IP
            excepLog.setOperVer(operVer); // 操作版本号
            excepLog.setOperCreateTime(new Date()); // 发生异常时间

            exceptionLogService.insert(excepLog);

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }*//*

    *//**
     * 转换request 请求参数
     *
     * @param paramMap request获取的参数数组
     *//*
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    *//**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     *//*
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }*/
//}