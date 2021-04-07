package com.arjun.subjective.demo.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 测试执行顺序，copy的Aspect.
 *
 * @author arjun
 * @date 2021/03/31
 */
//@Aspect
//@Component
public class TestAspect implements Ordered {

    /**
     * 开始时间
     */
    private Long beginTime;

    /**
     * 定义切入点.
     */
    @Pointcut("@annotation(com.arjun.subjective.demo.annotation.Syslog)")
    public void logPointCut() {

    }


    @Before("logPointCut()")
    public void before2() {
        System.out.println("自定义注解生效了。。。。。。。。");
    }

    /**
     * 环绕通知 用于拦截指定的切点，记录用户的操作.
     *
     * @param point
     * @return
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        System.out.println("开始统计时间................");
        long beginTime = System.currentTimeMillis();

        // 执行方法
        Object result = point.proceed();

        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        System.out.println("方法执行时间：" + time + "ms..............");


        System.out.println("直接获取：" + point.getSignature().toShortString());

        String className = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        System.out.println("间接获取：" + className + "." + methodName + "()");

        System.out.println("简洁获取：" + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + "()");

        System.out.println("======================================================");
        // 获取被代理的对象
        point.getTarget();
        System.out.println("调用getTarget()：" + point.getTarget());
        // 获取代理对象自己
        point.getThis();
        System.out.println("调用getThis()：" + point.getThis());

        System.out.println("调用toString()：" + point.toString());
        System.out.println("调用toShortString()：" + point.toShortString());
        System.out.println("调用toLongString()：" + point.toLongString());


        System.out.println("###########################################################");

        point.getSignature().toString();//String com.arjun.subjective.demo.controller.DemoController.hi(String)
        System.out.println("调用toString()：" + point.getSignature().toString());
        point.getSignature().toShortString();//DemoController.hi(..)
        System.out.println("调用toShortString()：" + point.getSignature().toShortString());
        point.getSignature().toLongString();//public java.lang.String com.arjun.subjective.demo.controller.DemoController.hi(java.lang.String)
        System.out.println("调用toLongString()：" + point.getSignature().toLongString());
        point.getSignature().getName();//hi
        System.out.println("调用getName()：" + point.getSignature().getName());
        // 获取目标方法声明类型(public、private、protected)
        point.getSignature().getModifiers();//1
        System.out.println("调用getModifiers()：" + point.getSignature().getModifiers());
        String s = Modifier.toString(point.getSignature().getModifiers());//public
        System.out.println("目标方法声明类型:" + s);

        point.getSignature().getDeclaringType();//class com.arjun.subjective.demo.controller.DemoController
        System.out.println("调用getDeclaringType()：" + point.getSignature().getDeclaringType());
        point.getSignature().getDeclaringTypeName();//com.arjun.subjective.demo.controller.DemoController
        System.out.println("目标方法所属类的类名:" + point.getSignature().getDeclaringTypeName());
        point.getSignature().getDeclaringType().getSimpleName(); //DemoController
        System.out.println("目标方法所属类的简单类名:" + point.getSignature().getDeclaringType().getSimpleName());

        System.out.println("======================================================");


        return result;
    }

    @After("logPointCut()")
    public void after(JoinPoint joinPoint) throws Exception {
        System.out.println("方法执行结束...........");

        // 方法执行结束时间
        long endTime = System.currentTimeMillis();

        // 获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method methodClass = targetClass.getMethod(methodName, parameterTypes);
        Annotation[] annotations = methodClass.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();
            String simpleName = aClass.getSimpleName();
            if ("Syslog".equals(simpleName)) {
                Syslog timeConsume = (Syslog) annotation;
                String value = timeConsume.value();
                System.out.println("方法一：" + value + "[" + methodName + "] 执行耗时：" + (endTime - beginTime) + "ms");
                break;
            }
        }
        // 直接获取
        Syslog syslog1 = methodClass.getAnnotation(Syslog.class);
        System.out.println("操作描述2：" + syslog1.value());

        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //clazz
        Syslog syslog = method.getAnnotation(Syslog.class);
        if (syslog != null) {
            //注解上的描述
            String value = syslog.value();
            System.out.println("操作描述：" + value);
            System.out.println("方法二：" + value + "[" + methodName + "] 执行耗时：" + (endTime - beginTime) + "ms");
        }
    }

    @AfterReturning("logPointCut()")
    public void afterReturning() {
        System.out.println("方法返回值修改.........");
    }

    @AfterThrowing("logPointCut()")
    public void afterThrowing() {
        System.out.println("方法异常处理...........");

    }

    @Override
    public int getOrder() {
        return 2;
    }
}
