package com.arjun.subjective.demo.annotation;

import com.arjun.subjective.demo.entity.SysAccessLog;
import com.arjun.subjective.demo.entity.User;
import com.arjun.subjective.demo.utils.HttpContextUtils;
import com.arjun.subjective.demo.utils.WebUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 系统日志切面处理类.
 *
 * @author arjun
 * @date 2021/03/30
 */
@Slf4j
@Aspect
@Component
public class SyslogAspect {

    private Long beginTime;

//    @Autowired
//    private SysAccessLogService sysAccessLogService;

    @Resource(name = "asyncTaskExecutor")
    Executor asyncTaskExecutor;

    /**
     * 定义切入点.
     */
    @Pointcut("@annotation(com.arjun.subjective.demo.annotation.Syslog)")
    public void logPointCut() {

    }

    /**
     * 前置通知 (在方法执行之前返回)用于拦截Controller层记录用户的操作的开始时间
     */
    @Before("logPointCut()")
    public void before() {
        beginTime = System.currentTimeMillis();
    }

    /**
     * 环绕通知（执行方法前后进行增强处理），控制执行方法，修改参数，更改返回值.
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //获取请求参数，详见接口类
        Object[] objects = point.getArgs();

        // 将修改后的参数传入
        return point.proceed(objects);
    }

    /**
     * 后置通知(在方法执行之后并返回数据) 用于拦截Controller层无异常的操作
     *
     * @param joinPoint 切点
     * @param result    方法执行的结果
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void after(JoinPoint joinPoint, Object result) {
        System.out.println("获取目标返回值" + result);
        log.info("正常执行，保存日志信息");
        HttpServletRequest request = HttpContextUtils.getRequest();
        asyncTaskExecutor.execute(() -> saveSysAccessLog(joinPoint, request, null));
    }

    /**
     * 异常通知 用于拦截异常日志
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info("系统抛出异常，保存日志信息");
        HttpServletRequest request = HttpContextUtils.getRequest();
        asyncTaskExecutor.execute(() -> saveSysAccessLog(joinPoint, request, e));
    }


    /**
     * 用于记录用户的操作日志描述
     */
    private void saveSysAccessLog(JoinPoint joinPoint, HttpServletRequest request, Throwable e) {
        SysAccessLog accessLog = new SysAccessLog();

        // 执行时间
        long time = System.currentTimeMillis() - beginTime;
        accessLog.setElapsedTime(time);

        // 用户操作描述
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String value = method.getAnnotation(Syslog.class).value();
        accessLog.setOperation(value);

        // 请求方法名
        String typeName = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        accessLog.setMethod(typeName + "." + methodName + "()");

        // 请求参数
        Map<String, String> parameterMap = WebUtils.getParameterMap(request);
        accessLog.setParams(parameterMap.toString());

        // 请求路径
        String requestPath = request.getRequestURI();
        accessLog.setPath(requestPath);

        // 请求方式
        String requestType = request.getMethod();
        accessLog.setRequestType(requestType);

        // 请求时间
        Date date = new Date();
        accessLog.setRequestTime(date);

        // ip地址
        String ip = WebUtils.getIpAddress(request);
        accessLog.setIp(ip);

        User user = (User) request.getSession().getAttribute("user");
        accessLog.setUsername(user == null ? null : user.getName());
//        accessLog.setUsername(null);

        if (e != null) {
            System.out.println("日志信息 -- 异常信息：" + e);
            accessLog.setMessage(e.toString());
        }
//        System.out.println("日志信息 -- 执行时间：" + time);
//        System.out.println("日志信息 -- 操作描述：" + value);
//        System.out.println("日志信息 -- 请求方法名：" + typeName + "." + methodName + "()");
//        System.out.println("请求参数：" + parameterMap);
//        System.out.println("日志信息 -- 请求路径：" + requestPath);
//        System.out.println("日志信息 -- 请求方式：" + requestType);
//        System.out.println("日志信息 -- ：" + date);
//        System.out.println("日志信息 -- ip地址：" + ip);
//        System.out.println("日志信息 -- 用户名：" + user);
        log.info(accessLog.toString());
        //保存系统日志
//        sysAccessLogService.insert(accessLog);

    }

}
