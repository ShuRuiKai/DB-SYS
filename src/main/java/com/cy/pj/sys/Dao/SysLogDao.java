package com.cy.pj.sys.Dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.cy.pj.sys.entity.SysLog;
/**
 * 日志模板块数据层接口:定义数据访问范围
 */
@Mapper
public interface SysLogDao{
	/**
	 * 基于查询条件统计记录总数
	 * @param username 查询条件
	 * @return 查询到的记录总数
	 */
	int getRowCount(@Param("username") String username);
	
/**
 * 基于条件查询当前页的记录
 * @param username 查询条件
 * @param startIndex 起始位置(当前页的起始位置)
 * @param pageSize 页面大小(每页最多显示多少条)
 * @return 查询到的当前的日志记录
 */
	List<SysLog> findPageObjects(
		      @Param("username")String  username,
		      @Param("startIndex")Integer startIndex,
		      @Param("pageSize")Integer pageSize);
	 /**
	  * 基于多个id删除日志信息
	  * @param ids 日志记录id，假如没有使用这个@Params注解进行参数描述，
	  * 对于可变参数而言，在映射sql中接收此值时需要使用array。
	  * @return
	  */
	int deleteObjects(@Param("ids")Integer... ids);

	 /**
	  * 将日志信息写入到数据库
	  * @param entity
	  * @return
	  */
	 int insertObject(SysLog entity);
}
