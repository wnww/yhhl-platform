package com.yhhl.wsts.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class WstsAspect {

	@Pointcut("@annotation(com.yhhl.wsts.spring.Wstsable)")
	public void pointCut() {

	}

	@After("pointCut()")
	public void after(JoinPoint joinPoint) {
		System.out.println("after aspect executed.");
	}

	@Before("pointCut()")
	public void before(JoinPoint joinPoint) {
		System.out.println("before aspect executing.");
	}

	@AfterReturning(pointcut = "pointCut()", returning = "returnVal")
	public void afterReturnint(JoinPoint joinPoint, Object returnVal) {
		System.out.println("after Returning executed, return result is： " + returnVal);
	}

	@Around("pointCut()")
	public void around(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("around start..");
		try {
			pjp.proceed();
		} catch (Throwable e) {
			System.out.println("执行异常");
			throw e;
		}
		System.out.println("around end.");
	}

	@AfterThrowing(pointcut = "pointCut()", throwing = "error")
	public void afterThrowing(JoinPoint joinPoint, Throwable error) {
		System.out.println("错误：" + error);
	}
}
