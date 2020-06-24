package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.cy.pj.common.bo.PageObject;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.Dao.SysRoleDao;
import com.cy.pj.sys.Dao.SysRoleMenuDao;
import com.cy.pj.sys.Dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.entity.SysRoleMenu;
import com.cy.pj.sys.service.SysRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class SysRoleServiceImpl implements SysRoleService {
	
	@Autowired
	private SysRoleDao sysRoleDao; 
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	 
	
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
//参数校验
		if (pageCurrent == null || pageCurrent < 1)
			throw new IllegalArgumentException("当前页码不正确QAQ");
//查询当前页记录 , 设置参数查询
		int pageSize = 2;// 每页最多显示记录数
		Page<SysRole> page=PageHelper.startPage(pageCurrent,pageSize);
		//启动查询操作
		List<SysRole> records = sysRoleDao.findPageObjects(name);
		//4.封装查询结果并返回
	//return new PageObject<>(records, rowCount, pageSize, pageCurrent);
		return new PageObject<>(pageCurrent, pageSize, (int)page.getTotal(), (int)page.getPages(), records);

	}


	@Override
	public int deleteObject(Integer id) {
		//1.验证数据的合法性
		if(id==null||id<=0)
			throw new IllegalArgumentException("请先选择");
		//3.基于id删除关系数据
		sysRoleMenuDao.deleteObjectsByRoleId(id);//删除角色菜单关系数据
		sysUserRoleDao.deleteObjectsByRoleId(id);//删除角色和用户的关系数据
		//4.删除角色自身
		int rows=sysRoleDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("此记录可能已经不存在");
		//5.返回结果
		return rows;

	}


	@Transactional
	@Override
	public int saveObject(SysRole entity, Integer[] menuIds) {
		//1.参数有效性校验
				if(entity==null)
					throw new IllegalArgumentException("保存对象不能为空");
				if(StringUtils.isEmpty(entity.getName()))
					throw new IllegalArgumentException("角色名不允许为空");
				if(menuIds==null||menuIds.length==0)
					throw new ServiceException("必须为角色分配权限");
				//2.保存角色自身信息
				int rows=sysRoleDao.insertObject(entity);
				//3.保存角色菜单关系数据
				sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
				//4.返回业务结果
				return rows;

	}


	@Override
	public SysRoleMenu findObjectById(Integer id) {
		//1.合法性验证
    	if(id==null||id<=0)
    	throw new IllegalArgumentException("id的值无效");
    	//2.执行查询
    	SysRoleMenu result=sysRoleDao.findObjectById(id);
    	
  	//3.验证结果并返回
    	if(result==null)
    	throw new ServiceException("此记录已经不存在");
    	return result;

	}

	@Transactional
	@Override
	public int updateObject(SysRole entity, Integer[] menuIds) {
		//1.合法性验证
    	if(entity==null)
        throw new IllegalArgumentException("更新的对象不能为空");
    	if(entity.getId()==null)
    	throw new IllegalArgumentException("id的值不能为空");
    	
    	if(StringUtils.isEmpty(entity.getName()))
    	throw new IllegalArgumentException("角色名不能为空");
    	if(menuIds==null||menuIds.length==0)
    	throw new IllegalArgumentException("必须为角色指定一个权限");
    	
    	//2.更新数据
    	int rows=sysRoleDao.updateObject(entity);
    	if(rows==0)
        throw new ServiceException("对象可能已经不存在");
    	sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());//删除原有关系数据
    	sysRoleMenuDao.insertObjects(entity.getId(),menuIds);//添加新的关系数据
    	//3.返回结果
    	return rows;

	}


	@Override
	public List<CheckBox> findObjects() {
		return sysRoleDao.findObjects();
	}

}
