package com.cy.pj.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.bo.PageObject;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.utils.ShiroUtils;
import com.cy.pj.sys.Dao.SysUserDao;
import com.cy.pj.sys.Dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.entity.SysUserDept;
import com.cy.pj.sys.service.SysUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service //spring容器存储bean时会以类名(首字母作为key),对象作为value存储到spring容器
@Transactional(isolation = Isolation.READ_COMMITTED,//隔离等级
               rollbackFor = Throwable.class,//回滚
               readOnly = false,//读的时候不加锁
               timeout = 30,//超过30秒抛出异常
               propagation = Propagation.REQUIRED) //传播
//共性
public class SysUserServiceImpl implements SysUserService {

     
	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Transactional(readOnly = true)//个性(只读)readOnly事务中不允许执行更新操作
	@RequiredLog(operation = "分页查询")
	//@Override
	public PageObject<SysUserDept> findPageObjects(
			String username,Integer pageCurrent) {
		//1.对参数进行校验
		if(pageCurrent==null||pageCurrent<1)
		throw new IllegalArgumentException("当前页码值无效");
		//2.查询总记录数并进行校验
		int rowCount=sysUserDao.getRowCount(username);
		if(rowCount==0)
		throw new ServiceException("没有找到对应记录");
		//3.查询当前页记录
		int pageSize=3;
		Page<SysUserDept> page=PageHelper.startPage(pageCurrent,pageSize);
		//3.查询当前页记录
		List<SysUserDept> records=sysUserDao.findPageObjects(username);
		return new PageObject<>(pageCurrent, pageSize,(int)page.getTotal(), (int)page.getPages(),  records);
//		int startIndex=(pageCurrent-1)*pageSize;
//		List<SysUserDept> records=sysUserDao.findPageObjects(username,startIndex, pageSize);
//		//4.对查询结果进行封装并返回
//		return new PageObject<>(pageCurrent, pageSize, rowCount, records);

	}
	@RequiresPermissions("sys:user:update")
	//@Override
	public int validById(Integer id, Integer valid) {
		//1.合法性验证(校验参数)
				if(id==null||id<=0)
				throw new ServiceException("参数不合法,id="+id);
				if(valid!=1&&valid!=0)
				throw new ServiceException("参数不合法,valie="+valid);
				
			//	if(StringUtils.isEmpty(modifiedUser))
				//throw new ServiceException("修改用户不能为空");
				//2.执行禁用或启用操作(admin为后续登陆用户）
				int rows=sysUserDao.validById(id, valid,"admin");//后续会将用户名修改为登陆用户，现在是假数据
				//3.判定结果,并返回
				if(rows==0)
				throw new ServiceException("此记录可能已经不存在");
				return rows;

	}
//	@RequiresPermissions("sys:user:update")
//	@RequiredLog("禁用启用")
//	@Override
//	public int validById(Integer id, Integer valid) {
//		//1.校验参数
//		if(id==null||id<1) {
//			log.error("id value is {}", id);
//			throw new IllegalArgumentException("id值无效");
//		}
//		if(valid!=0&&valid!=1){
//			log.error("valid value is {}", valid);
//			throw new IllegalArgumentException(valid+" 状态值无效");
//		}
//		//2.更新用户状态
//		int rows=sysUserDao.validById(id, valid, "admin");//将来此用户为登陆
//		if(rows==0)
//			throw new ServiceException("记录可能已经不存在");
//		return rows;
//	}
	@RequiredLog(operation = "新增业务")
	//@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
		long start=System.currentTimeMillis();
    	log.info("start:"+start);
    	//1.参数校验
    	if(entity==null)
    	throw new IllegalArgumentException("保存对象不能为空");
    	if(StringUtils.isEmpty(entity.getUsername()))
    	throw new IllegalArgumentException("用户名不能为空");
    	if(StringUtils.isEmpty(entity.getPassword()))
    	throw new IllegalArgumentException("密码不能为空");
    	if(roleIds==null || roleIds.length==0)
    	throw new IllegalArgumentException("至少要为用户分配角色");
    	//2.保存用户自身信息
        //2.1对密码进行加密
   // 	DigestUtils.md5DigestAsHex(entity.getPassword().getBytes());
    	String source=entity.getPassword();
    	String salt=UUID.randomUUID().toString();//拿到随机字符串
    	SimpleHash sh=new SimpleHash(//Shiro框架
    			"MD5",//algorithmName 算法
    			 source,//原密码
    			 salt, //盐值
    			 1);//hashIterations表示加密次数
    	entity.setSalt(salt);
    	entity.setPassword(sh.toHex());
    	int rows=sysUserDao.insertObject(entity);
    	//3.保存用户角色关系数据
    	sysUserRoleDao.insertObjects(entity.getId(), roleIds);
    	long end=System.currentTimeMillis();
    	log.info("end:"+end);
    	log.info("total time :"+(end-start));
    	//4.返回结果
    	return rows;
	}
	@Transactional(readOnly = true)
	//@Override
	public Map<String, Object> findObjectById(Integer userId) {
		//1.合法性验证
				if(userId==null||userId<=0)
				throw new IllegalArgumentException(
				"参数数据不合法,userId="+userId);
				//2.业务查询
				SysUserDept user=
				sysUserDao.findObjectById(userId);
				if(user==null)
				throw new ServiceException("此用户已经不存在");
				List<Integer> roleIds=
				sysUserRoleDao.findRoleIdsByUserId(userId);
				//3.数据封装
				Map<String,Object> map=new HashMap<>();
				map.put("user", user);
				map.put("roleIds", roleIds);
				return map;
			}
	
	
	@RequiredLog(operation = "分页修改")
	//@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//1.参数有效性验证
				if(entity==null)
					throw new IllegalArgumentException("保存对象不能为空");
				if(StringUtils.isEmpty(entity.getUsername()))
					throw new IllegalArgumentException("用户名不能为空");
				if(roleIds==null||roleIds.length==0)
					throw new IllegalArgumentException("必须为其指定角色");
				//其它验证自己实现，例如用户名已经存在，密码长度，...
				//2.更新用户自身信息
				int rows=sysUserDao.updateObject(entity);
				//3.保存用户与角色关系数据
				sysUserRoleDao.deleteObjectsByUserId(entity.getId());
				sysUserRoleDao.insertObjects(entity.getId(),
						roleIds);
				//4.返回结果
				return rows;
			}
	//@Override
	public int updatePassword(String password, String newPassword, String cfgPassword) {
		//1.判定新密码与密码确认是否相同
				if(StringUtils.isEmpty(newPassword))
				throw new IllegalArgumentException("新密码不能为空");
				if(StringUtils.isEmpty(cfgPassword))
				throw new IllegalArgumentException("确认密码不能为空");
				if(!newPassword.equals(cfgPassword))
				throw new IllegalArgumentException("两次输入的密码不相等");
				//2.判定原密码是否正确
				if(StringUtils.isEmpty(password))
				throw new IllegalArgumentException("原密码不能为空");
				//获取登陆用户
			//	SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
				SysUser user=ShiroUtils.getUser();
				//方法1
				/*String sourceHashedPassword=user.getPassword();
				 *SimpleHash sh=new SimpleHash("MD5", sourcePassword, user.getSalt(), 1);
				 *String hashedInputPassword=sh.toHex();
				 *if(!sourceHashedPassword.equals(hashedInputPassword))
				 *throw new IllegalArgumentException("原密码输入的不正确");
				 */
				//方法2
				SimpleHash sh=new SimpleHash("MD5",password, user.getSalt(), 1);
				if(!user.getPassword().equals(sh.toHex()))
				throw new IllegalArgumentException("原密码不正确");
				//3.对新密码进行加密
				String salt=UUID.randomUUID().toString();
				sh=new SimpleHash("MD5",newPassword,salt, 1);
				//4.将新密码加密以后的结果更新到数据库
				int rows=sysUserDao.updatePassword(sh.toHex(), salt,user.getId());
				if(rows==0)
				throw new ServiceException("修改失败");
				return rows;
	}	
	@Override
	public int updatePasswords(String sourcePassword, String newPassword, String cfgPassword) {
		//1.参数校验
		if(StringUtils.isEmpty(sourcePassword))
			throw new IllegalArgumentException("原密码不能为空");
		if(StringUtils.isEmpty(newPassword))
			throw new IllegalArgumentException("新密码不能为空");
		if(!newPassword.equals(cfgPassword))
			throw new IllegalArgumentException("两次新密码输入不一致");
		//校验输入的原密码是否正确
		SysUser user=ShiroUtils.getUser();
		String sourceHashedPassword=user.getPassword();
		SimpleHash sh=new SimpleHash("MD5", sourcePassword, user.getSalt(), 1);
		String hashedInputPassword=sh.toHex();
		if(!sourceHashedPassword.equals(hashedInputPassword))
			throw new IllegalArgumentException("原密码输入的不正确");
		//2.更新密码
		String newSalt=UUID.randomUUID().toString();
		sh=new SimpleHash("MD5", newPassword,newSalt, 1);
		String newHashedPassword=sh.toHex();
		int rows=sysUserDao.updatePasswords(user.getUsername(), newHashedPassword, newSalt);
		if(rows==0)
			throw new ServiceException("用户可能已经不存在");
		return rows;
	}

	}


