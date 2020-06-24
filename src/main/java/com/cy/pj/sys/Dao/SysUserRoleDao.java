package com.cy.pj.sys.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserRoleDao {
	//基于角色id删除用户和角色关系
	@Delete("delete from sys_user_roles where role_id=#{roleId} ")
	int deleteObjectsByRoleId(Integer roleId);
	
	int insertObjects(
			@Param("userId")Integer userId,
			@Param("roleIds")Integer[] roleIds);
	
	
	List<Integer> findRoleIdsByUserId(Integer id);
	
	int deleteObjectsByUserId(Integer userId);
}
