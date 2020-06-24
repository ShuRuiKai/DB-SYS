package com.cy.pj.sys.service;

import java.util.List;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;


/**
 * 此接口中定义菜单模块的业务操作标准
 */
public interface SysMenuService {
/**
 * 查询所有菜单以及菜单对应的上级菜单
 */

	List<SysMenu> findObjects();
	
	//基于菜单id删除菜单业务
	int deleteObject(Integer id);
	
	List<Node> findZtreeMenuNodes();

	int saveObject(SysMenu entity);
	
	int updateObject(SysMenu entity);
}
