package com.cy.pj.common.confing;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cy.pj.common.web.TimeAccessInterceptor;

@Configuration
public class SpringWebConfig implements WebMvcConfigurer{//web.xml
 
	 //配置spring mvc 拦截器
	 @Override
	 public void addInterceptors(InterceptorRegistry registry) {
		 registry.addInterceptor(new TimeAccessInterceptor())
		         .addPathPatterns("/user/doLogin");
	 }
}
