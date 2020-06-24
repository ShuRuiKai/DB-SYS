package com.cy.pj.common.vo;//(view Ibject)

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class JsonResult implements Serializable {

	private static final long serialVersionUID = 2080943344863436345L;
	/**消息状态码*/
	private int state=1;//1表示SUCCESS,0表示ERROR
	/**状态码对应的具体信息*/
	private String message="ok";
	/**正确数据(基于此属性封装业务层返回的数据)*/
	private Object data;
	public JsonResult(String message){
		this.state=1;
		this.message=message;
		/*方法2
		 * setState(1);
		 * setMessage(message);
		 */
	}
	/**一般查询时调用，封装查询结果*/
	public JsonResult(Object data) {
		this.state=1;
		this.data=data;
	}
	/**出现异常时时调用*/
	public JsonResult(Throwable t){//基于此构造方法进行错误信息的初始化,Throwable是所有异常类的父类
		this.state=0;//0表示ERROR
		this.message=t.getMessage();//获取异常信息
	}

}
