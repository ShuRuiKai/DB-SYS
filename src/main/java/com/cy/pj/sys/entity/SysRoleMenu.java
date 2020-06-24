package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class SysRoleMenu implements Serializable {
	
	private static final long serialVersionUID = -1474260376636546874L;
	private Integer id;
	private String name;
	private String note;
	/**角色对应的菜单id*/
	private List<Integer> menuIds;
}
