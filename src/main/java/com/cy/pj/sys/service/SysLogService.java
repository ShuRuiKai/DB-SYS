package com.cy.pj.sys.service;

import com.cy.pj.common.bo.PageObject;
import com.cy.pj.sys.entity.SysLog;

/**
 * 业务层接口:负责定义日志业务模块规则
 * 1.查询日志业务(添加分页业务实现)
 * 2.删除日志业务(后续会进行权限管理)
 * 3.添加日志业务(学会了AOP再实现)
 * @author UID
 *
 */

public interface SysLogService {//SysLogServiceImpl为实现类
	/**
	 * 定义日志的分页查询业务
	 * @param username 基于条件查询时的参数名	用户名(数据最终来源为Client)
	 * @param pageCurrent 当前的页码值	(数据最终来源为Client)		
	 * @return  封装当前页记录+分页信息的对象(pageObject)
	 */
	 PageObject<SysLog> findPageObjects( 
			 String username, 
			 Integer pageCurrent);
	 
	 int deleteObjects(Integer... ids);
	 
	 void saveObject(SysLog entity);
}
