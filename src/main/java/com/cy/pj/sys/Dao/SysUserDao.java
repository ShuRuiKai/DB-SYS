package com.cy.pj.sys.Dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.entity.SysUserDept;
@Mapper
public interface SysUserDao {
	List<SysUserDept> findPageObjects(
		      String  username,
		      Integer startIndex,
		      Integer pageSize);

	List<SysUserDept> findPageObjects(String  username);
	
	int getRowCount( String username);
	
	int validById(
			@Param("id")Integer id,
			@Param("valid")Integer valid,
			@Param("modifiedUser")String modifiedUser);
	
	int insertObject(SysUser entity);
	
	SysUserDept findObjectById(Integer id);
	
	int updateObject(SysUser entity);
	
	@Select("select * from sys_users where username=#{username}")
	SysUser findUserByUserName(String username);
	
	int updatePassword(
			@Param("password")String password,
			@Param("salt")String salt,
			@Param("id")Integer id);
	/**
	 * 修改密码操作
	 * @param username
	 * @param password
	 * @param salt
	 * @return
	 */
	@Update("update sys_users set password=#{password},salt=#{salt},modifiedTime=now() where username=#{username}")
	int updatePasswords(String username,String password,String salt);
	
}
