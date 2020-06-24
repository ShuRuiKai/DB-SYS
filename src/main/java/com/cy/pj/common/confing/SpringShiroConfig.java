package com.cy.pj.common.confing;

import java.util.LinkedHashMap;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringShiroConfig {//http1.1
	
	//配置Session会话对象
	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(60*60*1000);
		return sessionManager;
	}
	//实现记住我功能
	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager rememberMeManager=new CookieRememberMeManager();
		SimpleCookie cookie=new SimpleCookie("rememberMe");
		cookie.setMaxAge(7*24*3600);
		rememberMeManager.setCookie(cookie);
		return rememberMeManager;
	}

	//配置授权cache
	@Bean
	public CacheManager shiroCacheManager() {
		return new MemoryConstrainedCacheManager();
	}
	/**
	 * 配置SecurityManager(注意包名)，此对象用户实现用户身份认证和授权等功能。
	 * @Bean注解一般要结合@Configuration注解使用，用于描述方法，表示这个方法的
	 * 返回值要交给spring管理，key默认为方法名或者直接由@Bean注解指定
	 * @return
	 */
	@Bean
	//@Bean("securityManager")
	//@Scope("singleton") 默认为单例
	public SecurityManager securityManager(Realm realm,
			CacheManager cacheManager,
			RememberMeManager rememberMeManager,
			SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager=
				new DefaultWebSecurityManager();
		securityManager.setRealm(realm);
		securityManager.setCacheManager(cacheManager);
		securityManager.setRememberMeManager(rememberMeManager);
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager) {
		//1.创建ShiroFilterFactoryBean对象
		//1.1 构建对象
		ShiroFilterFactoryBean fBean=new ShiroFilterFactoryBean();
		//1.2设置安全认证授权对象
		fBean.setSecurityManager(securityManager);
		//1.3设置登陆页面
		fBean.setLoginUrl("/doLogin");
		//2.设置过滤规则
		LinkedHashMap<String,String> map= new LinkedHashMap<>();
		//静态资源允许匿名访问(例如项目static目录下的资源):"anon"表示匿名
		map.put("/bower_components/**","anon");
		map.put("/modules/**","anon");
		map.put("/dist/**","anon");
		map.put("/plugins/**","anon");
		map.put("/user/doLogin","anon");
		map.put("/doLogout","logout");//logout也是shiro框架提供
		//除了匿名访问的资源,其它都要认证("authc")后访问
		//map.put("/**","authc");
		map.put("/**","user");//当选择了记住我功能，认证方式进行修改
		fBean.setFilterChainDefinitionMap(map);
		return fBean;
	}
	//底层要为哪些方法进行权限控制，需要有一定的标识，例如@RequiresPermissions
	//问题：谁封装了对这些标识的理解呢？
	//Spring底层借助Advisor对象获取切入点并在切入点上应用Advice
	@Bean
	public AuthorizationAttributeSourceAdvisor 
	authorizationAttributeSourceAdvisor (
		    		    SecurityManager securityManager) {
			        AuthorizationAttributeSourceAdvisor advisor=
					new AuthorizationAttributeSourceAdvisor();
	     advisor.setSecurityManager(securityManager);
		return advisor;
	}

}











