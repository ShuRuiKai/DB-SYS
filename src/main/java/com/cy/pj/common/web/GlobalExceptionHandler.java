package com.cy.pj.common.web;


import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cy.pj.common.vo.JsonResult;
/**
 * @ControllerAdvice描述的类为全局异常处理类
 * 1.加如XxxController类的方法出现了异常,此异常没有try{}catch操作,而是继续抛出了,
 * 这个异常会抛出给Controller方法调用者(DispatcherServlet),此对象会检测抛出异常Controller类中
 * 是否有定义异常处理方法,这个方法能够处理抛出异常,假如不可以,那么DispatcherServlet对象
 * 还会检测是否有全局处理的异常处理类,假如有则调用全局异常处理类中相关异常处理方法处理异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	// JDK中的自带的日志API
/**
 * 	@ExceptionHandler 注解描述的方法为控制层的异常处理方法为控制层异常处理方法,此注解
 * 中传入的异常类型,为它描述的方法可以处理的异常.
 * @param e
 * @return
 */
	@ExceptionHandler(value=RuntimeException.class)
	@ResponseBody  //将返回值转换成为json格式的字符串
	public JsonResult doHandleRuntimeException(RuntimeException e) {
//		System.out.println("GlobalExceptionHandler.doHandleRuntimeException()");
		e.printStackTrace();//也可以写日志异常信息
		return new JsonResult(e);
		}//封装
	
	
	 @ExceptionHandler(ShiroException.class) 
	    @ResponseBody
		public JsonResult doHandleShiroException(
				ShiroException e) {
			JsonResult r=new JsonResult();
			r.setState(0);
			if(e instanceof UnknownAccountException) {
				r.setMessage("账户不存在");
			}else if(e instanceof LockedAccountException) {
				r.setMessage("账户已被禁用");
			}else if(e instanceof IncorrectCredentialsException) {
				r.setMessage("密码不正确");
			}else if(e instanceof AuthorizationException) {
				r.setMessage("没有此操作权限");
			}else {
				r.setMessage("系统维护中");
			}
			e.printStackTrace();
			return r;
		}
}
