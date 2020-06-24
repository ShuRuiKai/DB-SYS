package com.cy.pj.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect; 
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
/**
 * 通过如下切面中的通知方法，实现异常日志信息的记录
 * @author qilei
 *
 */
@Slf4j
@Aspect
@Component
public class SysExceptionAspect {
	
	//@Pointcut("bean(*ServiceImpl)")
	//public void  doExceptionPointCut() {}
	//通过如下方法记录异常日志,但是异常还要抛出,
	//所以在afterThrowing注解中要添加throwing属性,他的值为方法参数 e的名字
	@AfterThrowing(pointcut="bean(*ServiceImpl)",throwing = "e")
	public void doHandleException(JoinPoint jp,Throwable e) {
		MethodSignature ms=(MethodSignature)jp.getSignature();
		log.error("{}exception msg is {}",ms.getName(),e.getMessage());
	}
}
