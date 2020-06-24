package com.cy.pj.common.bo;//value object//
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//view object视图层封装数据
//business object业务层封装数据的(这个类属于bo)
//通常是web向模板渲染引擎(Thymeleaf)传输对象
/**
 *  POJO(VO/BO/DTO/DO)中的BO对象:
 *  1.	所有的POJO对象属性定义都用对象类型
 *  2.    所有的POJO对象属性默认值无序指定
 *  3.    所有的POJO对象需要提供get/set/toString方法,对Boolean提供isxxx方法
 *  4.	 所有的POJO对象都需要提供无参构造函数
 *  5.	 所有的POJO对象的构造方法不要写任何业务逻辑
 * 此对象为业务层向外输出一个bo对象,用于封装业务层执行的结果
 * 泛型:类上定义的泛型用于约束类中属性,方法参数,方法返回值类型.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageObject<T> implements Serializable {
 
	private static final long serialVersionUID = 4382188489836324755L;
	private Integer pageCurrent=1;//当前页的页码值
	private Integer pageSize=10;//每页显示多少条记录
	private Integer rowCount=0;//总行数
	private Integer pageCount=0;//总页数
	private List<T> records;//当前页记录

	public PageObject(Integer pageCurrent, Integer pageSize, Integer rowCount, List<T> records) {
		super();
		this.pageCurrent = pageCurrent;
		this.pageSize = pageSize;
		this.rowCount = rowCount;
		this.records = records;
//		this.pageCount=rowCount/pageSize;
//		if(rowCount%pageSize!=0) {
//			pageCount++;
//		}
		this.pageCount=(rowCount-1)/pageSize+1;
	}
}

