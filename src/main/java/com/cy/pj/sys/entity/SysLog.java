package com.cy.pj.sys.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class SysLog implements Serializable {

	private static final long serialVersionUID = -4280190254096649250L;

private Integer id;
private String username;//用户名
private String operation;//用户操作
private String method;	//请求方法
private String params;	//请求参数
private Long time;	//执行时长(毫秒)
private String ip;	//IP地址
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
private Date createdTime;//创建时间
	
	
}
