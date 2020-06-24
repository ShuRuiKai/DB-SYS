package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.bo.PageObject;
import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

@RestController
@RequestMapping("/log/")
public class SysLogController {
	
	@Autowired
	private SysLogService sysLogService;
	
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String username,Integer pageCurrent){
//	public  PageObject<SysLog> doFindPageObjects(String username,Integer pageCurrent){
	 PageObject<SysLog> pageObject=
		sysLogService.findPageObjects(username,pageCurrent);
	return new JsonResult(pageObject);//DispatcherServlet
//	return pageObject;
	}
	
	 @RequestMapping("doDeleteObjects")

	  public JsonResult doDeleteObjects(Integer...  ids){
		  sysLogService.deleteObjects(ids);
		  return new JsonResult("delete ok");
	  }


}
