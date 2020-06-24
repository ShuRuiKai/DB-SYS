package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.bo.PageObject;
import com.cy.pj.sys.Dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private SysLogDao sysLogDao;

	@Override
	public PageObject<SysLog> findPageObjects(String username, Integer pageCurrent) {
		// 1.验证参数合法性
		// 1.1验证pageCurrent的合法性，
		// 不合法抛出IllegalArgumentException异常
		// 下面的参数校验是否可以颠倒"||"符合两侧的顺序?(不可以)
		if (pageCurrent == null || pageCurrent < 1)
			throw new IllegalArgumentException("当前页码不正确QAQ");
		// 2.基于条件查询总记录数
		// 2.1) 执行查询
		int rowCount = sysLogDao.getRowCount(username);// 假如出现空指针,可能是什么原因?sysLogDao变量为空
		// 2.2) 验证查询结果，假如结果为0不再执行如下操作
		if (rowCount == 0)
			throw new ServiceException("宝宝查不到对应数据记录T_T");

		// 3.基于条件查询当前页记录(pageSize定义为2)
		// 3.1)定义pageSize
		int pageSize = 10;// 每页最多显示记录数
		// 3.2)计算startIndex当前页查询的起始位置(当前页的页码值-1*每页显示多少条记录)
		int startIndex = (pageCurrent - 1) * pageSize;
		// 3.3)执行当前数据的查询操作
		List<SysLog> records = sysLogDao.findPageObjects(username, startIndex, pageSize);

		// 4.对分页信息以及当前页记录进行处理和封装
		// 主意顺序与构造方法一直
		return new PageObject<>(pageCurrent, pageSize, rowCount, records);

//		  //4.1)构建PageObject对象
//		PageObject<SysLog> pageObject = new PageObject<>();
//		//4.2)封装数据
//		pageObject.setPageCurrent(pageCurrent);
//		pageObject.setPageSize(pageSize);
//		pageObject.setRowCount(rowCount);
//		pageObject.setRecords(records);
//		pageObject.setPageCount((rowCount - 1) / pageSize + 1);
//		// 5.返回封装结果。
//		return pageObject;
	}

	@Override
	public int deleteObjects(Integer... ids) {
		// 1.判断参数合法性
		if (ids == null || ids.length == 0)
			throw new IllegalArgumentException("请选择一个好吗0.0!!!");
		// 2.执行删除操作
		int rows;
		try {
			rows = sysLogDao.deleteObjects(ids);
		} catch (Throwable e) {
			e.printStackTrace();
			// 发出报警信息(例如给运维人员发短信)
			throw new ServiceException("系统故障，正在恢复中...");
		}
		// 4.对结果进行验证
		if (rows == 0)
			throw new ServiceException("记录可能已经不存在了  0v0");
		// 5.返回结果
		return rows;
	}
	//这里异步写日志操作，同样使用的是AOP
	//@Async描述的方法为切入点
	//这个切入点上执行的异步操作为通知(Advice)
	@Async //由此注解描述的方法，用于告诉spring框架这个方法要运行一个异步线程上(此线程由spring线程池提供)。
	//	@Transactional(propagation=Propagation.REQUIRED)
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void saveObject(SysLog entity) {
		String tName=Thread.currentThread().getName();
		System.out.println("Sys"+tName);
		try {Thread.sleep(1000);}catch(Exception e) {}
		sysLogDao.insertObject(entity);
	}

	

}
