package com.cy.pj.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cy.pj.common.cache.DefaultMapCache;

@Aspect
@Component
public class SysCacheAspect {
	@Autowired
	private DefaultMapCache mapCache;
	    /**
	     * 定义切入点：由RequiredCache注解描述的方法为切入点方法
	     */
	  @Pointcut("@annotation(com.cy.pj.common.annotation.RequiredCache)")
	  public void doCache() {}
	  
	  @Pointcut("@annotation(com.cy.pj.common.annotation.ClearCache)")
	  public void doClear() {}
	 
	  @AfterReturning("doClear()")
	  public void doAfterReturning(){
		  mapCache.clear();
	  }
	  
	  @Around("doCache()")
	  public Object doAround(ProceedingJoinPoint jp)throws Throwable{
		 // System.out.println("Get data from cache");
		  Object obje=mapCache.getObject("deptCache");//key现在是随意给的一个固定值
		  if(obje!=null)return obje;
		  Object obj=jp.proceed();//目标方法的执行结果会给obj赋值
		//  System.out.println("Put data to cache");
		  mapCache.putObject("deptCache", obj);
		  return obj;
	  }
    
}
