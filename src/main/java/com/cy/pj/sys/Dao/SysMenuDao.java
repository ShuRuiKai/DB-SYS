package com.cy.pj.sys.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.entity.SysUserMenu;


@Mapper
public interface SysMenuDao {//sysMenuMapper
     /**
      * 查询菜单表中所有的菜单记录
      * 一行菜单记录映射为一个map对象(key为字段名，值为字段对应值)
      * @return
      */
	 List<SysMenu> findObjects();
	 /**
		 * 查找所有的菜单节点信息
		 * @return
		 */
	 List<Node> findZtreeMenuNodes();
	 /**
		 * 将菜单信息持久化到数据库
		 * @param entity
		 * @return
		 */
	 int insertObject(SysMenu entity);
	 /**
		 * 将菜单信息更新到数据库
		 * @param entity
		 * @return
		 */
	 int updateObject(SysMenu entity);
		/**
		 * 基于多个菜单id找到对应的授权标识
		 * @param menuIds
		 * @return
		 */
	List<String> findPermissions(Integer[] menuIds);
	
	/**
	 * 基于菜单获取菜单信息
	 * @param menuIds
	 * @return
	 */
	List<SysUserMenu> findMenusByIds(
	@Param("menuIds")List<Integer> menuIds);

}
