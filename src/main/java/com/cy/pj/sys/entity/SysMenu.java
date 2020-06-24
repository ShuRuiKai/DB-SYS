package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class SysMenu  implements Serializable{

	private static final long serialVersionUID = -4558510462373706891L;
	private Integer id;
	private String name; 
	private String url;
	private Integer type;
	private Integer sort;
	private String note;
	private String permission;
	private Integer parentId;
	private String parentName;
	private String createdUser;
	private String modifiedUser;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date createdTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date modifiedTime;
}
