package com.cy.pj.sys.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface SysRoleMenuDao {
	/**
	 * 基于多个角色id获取对应的菜单id
	 * @param roleId
	 * @return
	 */
	List<Integer> findMenuIdsByRoleIds(Integer[] roleIds);
	int deleteObjectsByMenuId(Integer menuId);
	//基于id统计子菜单的个数
	int getChildCount(Integer id);
	
	int deleteObject(Integer id);
	
	int deleteObjectsByRoleId(Integer roleId);
	
	int insertObjects(@Param("roleId")Integer roleId,@Param("menuIds")Integer[] menuIds);


}
