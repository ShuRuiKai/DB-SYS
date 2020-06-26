package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class SysUserMenu implements Serializable{
	private static final long serialVersionUID =   
      -8126757329276920059L;
	private Integer id;
	private String name;
	private String url;
	//二级菜单
	private List<SysUserMenu> childs;
}
