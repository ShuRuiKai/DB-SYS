package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.Dao.SysMenuDao;
import com.cy.pj.sys.Dao.SysRoleMenuDao;
import com.cy.pj.sys.Dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.entity.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao; 
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	//Spring中的cache：Map<String,Cache>
		//将查询结果进行cache
		@Cacheable(value = "menuCache") //具体cache操作由aop实现(@Cacheable描述的方法为切入点方法)
	@Override
	public List<SysMenu> findObjects() {
		List<SysMenu> list=sysMenuDao.findObjects();
		if(list==null||list.size()==0)
			throw new ServiceException("没有找到对应菜单哟！"); 
			
		return list;
	}
		
	@CacheEvict(value = "menuCache")
	@Override
	public int deleteObject(Integer id) {
		//1.验证数据的合法性(参数校验)
				if(id==null||id<=0)
				throw new IllegalArgumentException("请先选择好吗？0v0");
				//2.基于id进行子元素查询(基于id统计子菜单数进行校验)
				int count=sysRoleMenuDao.getChildCount(id);
				if(count>0)
				throw new ServiceException("请先删除子菜单哦");
				//3.删除角色,菜单关系数据
				sysRoleMenuDao.deleteObjectsByMenuId(id);
				//4.删除菜单元素
				int rows=sysRoleMenuDao.deleteObject(id);
				if(rows==0)
				throw new ServiceException("这个此菜单可能已经不存在了啦！");
				//5.返回结果
				return rows;

	}
	@Override
	public List<Node> findZtreeMenuNodes() {
		return sysMenuDao.findZtreeMenuNodes();
	}
	@CacheEvict(value = "menuCache")
	@Override
	public int saveObject(SysMenu entity) {
		//1.参数校验
				if(entity==null)
				throw new ServiceException("保存对象不能为空");
				if(StringUtils.isEmpty(entity.getName()))
				throw new ServiceException("菜单名不能为空");
				int rows;
				//2.将数据保存到数据库
				try{
				rows=sysMenuDao.insertObject(entity);
				}catch(Exception e){
				e.printStackTrace();
				throw new ServiceException("保存失败");
				}
				//3.返回数据
				return rows;

	}
	@CacheEvict(value = "menuCache")
	@Override
	public int updateObject(SysMenu entity) {
		//1.合法验证
				if(entity==null)
				throw new ServiceException("保存对象不能为空");
				if(StringUtils.isEmpty(entity.getName()))
				throw new ServiceException("菜单名不能为空");
				
				//2.更新数据
				int rows=sysMenuDao.updateObject(entity);
				if(rows==0)
				throw new ServiceException("记录可能已经不存在");
				//3.返回数据
				return rows;
		}

	@Override
	public List<SysUserMenu> findUserMenusByUserId(Integer id) {
		//1.对用户id进行判断
		//2.基于用户id查找用户对应的角色id
		List<Integer> roleIds=
				sysUserRoleDao.findRoleIdsByUserId(id);
		//3.基于角色id获取角色对应的菜单信息,并进行封装.
		List<Integer> menuIds=
				sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[] {}));
		//4.基于菜单id获取用户对应的菜单信息并返回
		return sysMenuDao.findMenusByIds(menuIds);
	}
	}


