	//JS 问题如何解决? console.log(),debugger,排除法
	$(function() {
		//$("#pageId").load("doPageUI",doGetObjects);
		doGetObjects();
		
		
		$(".input-group-btn")
		 .on("click",".btn-search",doQueryObjects)
		 .on("click",".btn-delete",doDeleteObjects)
		//thread中checkbox处理对象事件
		$("#checkAll").click(doChangeTBodyCheckBoxState);
		//tbody中checkbox对象事件注册
		$("#tbodyId")
		 .on("change","input:checkbox",doChangeTHeadCheckBoxState);
	});

	//通过此函数异步加载服务端的日志信息
	function doGetObjects(){ 
		//初始化全选checkbox对象状态
		 $("#checkAll").prop("checked",false);
		//1.定义请求参数
	   var pageCurrent=$("#pageId").data("pageCurrent");
	   //pageCurrent参数在没有赋值的情况下，默认初始化pageCurrent的值为1
	   if(!pageCurrent) pageCurrent=1;
	 			//参数封装: 方法 一
	   			//let params="pageCurrent="+pageCurrent;
	   			//if(uname)params=parama+"&username="+uname;
	   var uname=$("#searchNameId").val();
	   let params={"pageCurrent":pageCurrent};//json格式的js对象
	   //如下语句的含义是什么？动态在json格式的js对象中添加key/value,
  		 if(uname) params.username=uname;//查询时需要
  		//console.log(username);
  		 var url="log/doFindPageObjects"
	   //2.发起异步请求
	   //请问如下ajax请求的回调函数参数名可以是任意吗？可以,必须符合标识符的规范
       $.getJSON(url,params,doHandleQueryResult)//特殊的ajax函数
	   }

	function doHandleQueryResult(result) { //JsonResult
		//console.log("result",result);
		if (result.state == 1) {//ok
			//更新table中tbody内部的数据(当前页的日志信息更新到页面上)
				//console.log(result.data.records);
			
			doSetTableBodyRows(result.data.records);//将数据呈现在页面上 
			
			//更新页面page.html分页数据,分页信息更新到页面上
			doSetPagination(result.data); 
			//此方法写到page.html中
		} else {
			//alert(result.message);
			 doSetQueryErrors(result.message);
		}
	}
	 function doSetQueryErrors(message){
		   $("#tbodyId").html(`<tr><td colspan='7'>${message}</td></tr>`);
	   }
	function doSetTableBodyRows(records) {
		//1.获取tbody对象，并清空对象
		var tBody = $("#tbodyId");
		tBody.empty();
	
	 	//2.迭代records记录，并将其内容追加到tbody
	for (let i=0 ;i<records.length;i++){tBody.append(doCreateRow(records[i]));}
	//优势:可读性好			//劣势:i的作用域不严谨
//	records.forEach(records=>tBody.append(doCreateRow(records)))
	/* 	for ( var i in records) {//优势:结构简单			//劣势:i的作用域不严谨,可读性差.性能相对也不太好
			//2.1 构建tr对象
			var tr = $("<tr></tr>");
			//2.2 构建tds对象
			var tds = doCreaxteTds(records[i]);
			//2.3 将tds追加到tr中
			tr.append(tds);
			//2.4 将tr追加到tbody中
			tBody.append(tr);
		} */
	}
	 function doCreateRow(data) {
	return `<tr>
				<td><input type='checkbox'  class='cItem'  value=${data.id}></td>
				<td> ${data.id}</td>
				<td> ${data.username}</td>
				<td> ${data.operation} </td>
				<td> ${data.method}</td>
				<td> ${ data.params}</td>
				<td> ${ data.ip}</td>
				<td> ${ data.time} </td>
				<td> ${ data.createdTime} </td>
	</tr>`;
	}
	/*  function doCreateTds(data){
		   var tds="<td><input type='checkbox' class='cBox' name='cItem' value='"+data.id+"'></td>"+
		    	 "<td>"+data.id+"</td>"+
			     "<td>"+data.username+"</td>"+
			     "<td>"+data.operation+"</td>"+
			     "<td>"+data.method+"</td>"+
			     "<td>"+data.params+"</td>"+
			     "<td>"+data.ip+"</td>"+
			     "<td>"+data.time+"</td>"+
			     "<td>"+data.createdTime+"</td>";
	return tds;
	   } */

	 
	   
	   
	  //处理tbody中checkbox对象的事件
	   function doChangeTHeadCheckBoxState(){
			  //1.设定默认状态值
			  var flag=true;
			  //2.迭代所有tbody中的checkbox值并进行与操作
			 // $("#tbodyId input[type='checkbox']").each(function(){
			 $("#tbodyId input:checkbox")
			 .each(function(){
				  flag=flag&&$(this).prop("checked");
			  });
			  //3.修改全选元素checkbox的值为flag
			  $("#checkAll").prop("checked",flag);
	   }
	//定义获取用户选中的记录id的函数。
	   function doChangeTBodyCheckBoxState(){
		   //1.获取当前点击对象的checked属性的值
		   var flag=$(this).prop("checked");//true or false
		   //2.将tbody中所有checkbox元素的值都修改为flag对应的值。
		   //第一种方案
		   /* $("#tbodyId input[name='cItem']")
		   .each(function(){
			   $(this).prop("checked",flag);
		   }); */
		   //第二种方案
		     $("#tbodyId input:checkbox").prop("checked",flag);
		  // $("#tbodyId input[type='checkbox']").prop("checked",flag);
	   }
//定义日子删除事件处理函数
	   function doDeleteObjects(){
		   //1.获取选中的id值
		   var idArray=doGetCheckedIds();
		   if(idArray.length==0){
			   alert("好歹选一个呀!");
			   return;
		   }
		   if(!confirm("确认删除吗？"))return;
		   //2.发异步请求执行删除操作（构建参数对象、定义url）
		   // var params={"ids":ids.toString()};
		   var params={"ids":idArray.toString()};//[1,2,3]-->1,2,3(变成酱汁)
		   var url="log/doDeleteObjects";
		   // console.log(params);
		   $.post(url,params,doDelete)
	   }
	   //处理删除结果
	   function doDelete(result){
		   if(result.state==1){
			   alert(result.message);
			   // doGetObjects();//刷新
			   doRefreshAfterDeleteOK();
		   }else{
			   alert(result.message);
		   }
	   }
	   function doGetCheckedIds(){
		   //定义一个数组,用于存储选中的checkbox的id值
		   var idArray=[];//new Array();
		   //获取tbody中所有类型为checkbox的input元素
		   //$("#tbodyId input[type=checkbox]").
		   $("#tbodyId input:checkbox:checked")
		   //迭代这些元素，每发现一个元素都会执行如下回调函数
		   .each(function(){
			   //调用数组对象的push方法将选中对象的值存储到数组
			   idArray.push($(this).val());
		   });
		   //返回数组
		   return idArray;
	   }
	   //执行删除以后的刷新操作
	   function doRefreshAfterDeleteOK(){
		   var checked=$("#checkAll").prop("checked");
		   var pageCurrent=$("#pageId").data("pageCurrent");
		   var pageCount=$("#pageId").data("pageCount");
		   if(pageCurrent==pageCount&&checked&&pageCurrent>1){
			   pageCurrent--;
			   $("#pageId").data("pageCurrent",pageCurrent);
		   }
		   doGetObjects();
	   }
 function doQueryObjects(){
	   //初始化pageCurrent的值为1,数据查询时页码的初始位置也应该是第一页
	   $("#pageId").data("pageCurrent",1);
	   //执行查询
	   doGetObjects();
   }
	 	
	   
	   
	
	 