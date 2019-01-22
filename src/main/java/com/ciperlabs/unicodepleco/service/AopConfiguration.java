package com.ciperlabs.unicodepleco.service;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

//@Configuration
//@EnableAspectJAutoProxy
//@Aspect
public class AopConfiguration {
//    /** Pointcut for execution of methods on {@link org.springframework.stereotype.Service} annotation */
//    @Pointcut("execution(public * (@org.springframework.stereotype.Service org.ciperlabs.*).*(..))")
//    public void serviceAnnotation() { }
//
//    /** Pointcut for execution of methods on {@link org.springframework.stereotype.Repository} annotation */
//    @Pointcut("execution(public * (@org.springframework.stereotype.Repository org.ciperlabs.*).*(..))")
//    public void repositoryAnnotation() {}
//
//    /** Pointcut for execution of methods on {@link org.springframework.data.jpa.repository.JpaRepository} interfaces */
//    @Pointcut("execution(public * org.springframework.data.jpa.repository.JpaRepository+.*(..))")
//    public void jpaRepository() {}
//
//    @Pointcut("serviceAnnotation() || repositoryAnnotation() || jpaRepository()")
//    public void performanceMonitor() {}
//
//    @Bean
//    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
//        return new PerformanceMonitorInterceptor(true);
//    }
//
//    @Bean
//    public Advisor performanceMonitorAdvisor() {
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression("org.example.AopConfiguration.performanceMonitor()");
//        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
//    }
}