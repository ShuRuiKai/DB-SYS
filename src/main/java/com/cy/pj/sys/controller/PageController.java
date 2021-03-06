package com.cy.pj.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cy.pj.common.utils.ShiroUtils;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.entity.SysUserMenu;
import com.cy.pj.sys.service.SysMenuService;

/**
 * 计划所有涉及到页面的方法都放到这个类里面
 * @author UID
 *
 */
@RequestMapping("/")
@Controller
public class PageController {
	@Autowired
	private SysMenuService sysMenuService;

	
	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
		  //获取登陆用户
		  SysUser user=ShiroUtils.getUser();
		  model.addAttribute("user", user);
		  //查询用户对应的菜单信息(一级，二级)并存储到model，然后在页面上进行呈现。
		  List<SysUserMenu> userMenus=
				  sysMenuService.findUserMenusByUserId(user.getId());
		  model.addAttribute("userMenus",userMenus);
		return "starter";
	}
	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}
	 @RequestMapping("doLoginUI")
	  public String doLogin() {
		  return "login";
	  }
	 


	 
//	@RequestMapping("/log/log_list") 
//	public String dologUI() {
//		return "sys/log_list";
//	}
	
	
	
	

	//rest风格的url定义
	//语法格式：/{a}/{b}/...;其中{}中内容可以理解为一个变量
	//@PathVariable 注解可以描述方法参数，用于获取url中与方法参数相同的变量值
	@RequestMapping("/{module}/{moduleUI}")
	public String doModuleUI(@PathVariable String moduleUI)  {
		return "sys/"+moduleUI;
	}
	/*
	 * @RequestMapping("dept_list") public String dodeptUI() { return
	 * "sys/dept_list"; }
	 * 
	 * @RequestMapping("menu_list") public String domenuUI() { return
	 * "sys/menu_list"; }
	 * 
	 * @RequestMapping("role_list") public String doroleUI() { return
	 * "sys/role_list"; }
	 * 
	 * @RequestMapping("user_list") public String douserUI() { return
	 * "sys/user_list"; }
	 * 
	 * @RequestMapping("pwd_list") public String dopwdUI() { return "sys/pwd_list";
	 * }
	 */ 
	
	
}
