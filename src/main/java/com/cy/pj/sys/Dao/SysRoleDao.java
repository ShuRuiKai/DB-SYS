package com.cy.pj.sys.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.entity.SysRoleMenu;

@Mapper
public interface SysRoleDao {
/**
 * 基于角色名统计角色个数
 * @param name
 * @return
 */
	int getRowCount(@Param("name") String name);
	
	 List<SysRole> findPageObjects(String name);
	//List<SysRole> findPageObjects(@Param("name")String  name, @Param("startIndex")Integer startIndex,  @Param("pageSize")Integer pageSize);
	
	
	int deleteObject(Integer id);
	
	int insertObject(SysRole entity);
	
//@Select("select id,name,note from sys_roles where id=#{id}")
	SysRoleMenu findObjectById(Integer id);
	
	int updateObject(SysRole entity);
	
	List<CheckBox> findObjects();
}
