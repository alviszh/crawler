
//分页 设置默认值
var pageSize='',currentPage='',showPageNum='';
pageSize=10;  //显示页大小
currentPage=1; //当前页
showPageNum=5;     //一次显示的页数
var lineIndex = 0; //设置行号；
$(function(){
    //查询任务记录，在如下方法中调用第一页数据
	show_firstPage_task();
	
	 //点击搜索
    $("#search").on("click",function(){
        task_search();
    });
    
    //点击添加
    $("#addItem").on("click",function(){
        /*一次短信和二次短信的关键字是选填项目，根据实际情况填写*/
        if($("#province").val().trim()=="" | $("#developer").val().trim()==""
        		| $("#phonenum").val().trim()=="" | $("#servicepwd").val().trim()==""
        			| $("#name").val().trim()=="" | $("#idnum").val().trim()==""){
    		$("#bitian").text("*为必填项！请完善相关信息录入~");
    	} else{
    		var mobileNum = $('#phonenum').val().trim();//手机号码
    	    var idNum = $('#idnum').val();//身份证号码
    	    var mobileRegExp = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(166))+\d{8})$/;
    	    var idNumRegExp = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    	    if(!mobileRegExp.test(mobileNum)){
    	        /*$('#phonenum').val("请输入有效的手机号码").css({color:"red"});*/
    	    	$("#bitian").text("请输入有效的手机号码~");
    	    	//先清空不符合格式的内容
    	    	$("#phonenum").val("");   
    	    	//还原默认提示信息
    	    	$("#phonenum").attr("placeholder","*请输入有效的手机号码");
    	    	//将边框设置为红色
    	    	$("#phonenum").css({"border":"1px solid red"});
    	        return;
    	    }else{  //符合标准，恢复默认边框颜色
    	    	$("#phonenum").css({"border":"1px solid LightGrey"});
    	    }
    	    if(!idNumRegExp.test(idNum)){
    	    	$("#bitian").text("请输入有效的身份证号码~");
    	    	$("#idnum").val("");   
    	    	$("#idnum").attr("placeholder","*请输入有效的身份证号码");
    	    	$("#idnum").css({"border":"1px solid red"});
    	        return;
    	    }else{
    	    	$("#idnum").css({"border":"1px solid LightGrey"});
    	    }
    		addItem();
        	$('#addModal').modal('hide');
        	location.reload();
    	}
    });
    
    //点击删除
    $("#removeItem").on("click",function(){
    	var id = $("#id").val();
	    //调用删除监控项代码的具体方法
	    removeItem(id);  
    	$('#removeModalDiv').modal('hide');
    	location.reload();
    });
    
    //点击修改
	
});
/*-----------------------------查询功能start-----------------------------*/
//查询任务记录
function show_firstPage_task(){

    var taskPage='',taskList='';
    var totalElements = ''; //总记录
    var province='',developer='';

    province = $("#province_search").val();
    developer = $("#developer_search").val();

    taskPage=searchTask(false,pageSize,currentPage,province,developer);
    taskList = taskPage["content"];
    totalElements = taskPage["totalElements"];

    console.log("=====taskList=====" + taskList + "=====totalElements=====" + totalElements);

    //分页
    $("#page").html("");
    //显示首页内容
    query_taskList(taskList);

    //调用commonSelf_pageStyle
    commonSelf_pageStyle(pageSize,showPageNum,totalElements,"isNull","page",getTask__pageCallback);

}
//分页查询
function searchTask(async,pageSize,currentPage,province,developer){
    var taskPage='';
    /*如下位置减去1(currentPage-1),否则查询不到第一页，不知道为啥*/
    $.ajax({
        url: "/monitor/platform/crawler/getCarrierPages",
        type:"post",
        async:async,
        data:{
            pageSize:pageSize,
            currentPage:currentPage-1,
            province:province,
            developer:developer,
        },
        success:function(data){
            if(!async){
            	taskPage=data;
            }
        },
        error:function(){
        	taskPage="isError";
        }
    });
    if(!async){
        return taskPage;
    }
}

//生成任务列表
function query_taskList(taskList){

    var trs='',createtime='',btn1='',btn2='';
    var monitorflag;

    $.each(taskList,function(index,task){
    	/*行号,随着翻页变化*/
        lineIndex = (currentPage-1)*pageSize + index + 1;
        
        btn1="<a id='update"+(index+1)+"' style='cursor:pointer'>信息修改</a>";
        btn2="<a id='delete"+(index+1)+"' style='cursor:pointer'>信息删除</a>";
        
        monitorflag=(task.isneedmonitor == 1) ? '正常监控' : '暂停监控';
        
        trs +="<tr>"
            +"<td style=\"display:none\">"+task.id+"</td>"
            +"<td>"+lineIndex+"</td>"
            +"<td>"+task.province+"</td>"
            +"<td>"+task.developer+"</td>"
            +"<td>"+task.phonenum+"</td>"
            +"<td>"+task.servicepwd+"</td>"
            +"<td>"+task.name+"</td>"
            +"<td>"+task.idnum+"</td>"
            +"<td>"+task.oncesmskey+"</td>"
            +"<td>"+task.twicesmskey+"</td>"
            +"<td>"+monitorflag+"</td>"
            +"<td>"+btn1+"  "+btn2+"</td>"
            +"</tr>";
    });
    $("table >tbody").html(trs);
    
    $(function(){
	　　$("a[id^='update']").click(function(){
		 	console.log("update update update");
		    $('#authform')[0].reset();
		    $("#bitian").text("");
			$("#myModalLabel").html("修改监控项");
		    var tr=$(this).parent().parent();
	        var dataItem = tr.children();
	        
		    $('#addModal').modal('show');
		    
		    document.getElementById("province").value=dataItem.eq(1).text(); 
		    document.getElementById("developer").value=dataItem.eq(2).text(); 
		    document.getElementById("instancecount").value=dataItem.eq(3).text(); 
	　　});
	   //将要删除的记录的id放在隐藏域中，传给弹出页面，在确认删除的时候根据id删除
	   $("a[id^='delete']").click(function(){
			 console.log("delete delete delete");
			 var tr=$(this).parent().parent();
		     var dataItem = tr.children();
		     deleteData ="<h5>确定删除监控项 "+dataItem.eq(2).text()+" 吗？</h5>" 
		     + "<input type='hidden' id='id' value='"+ dataItem.eq(0).text() +"'>"
		     console.log(dataItem.eq(0).text());
		     $("#delete").html(deleteData);
		     $('#removeModalDiv').modal('show');
	　　});
    })
}

//搜索
function task_search(){
    var province='',developer='';
    var taskPage='',searchResult='';
    var noKeys='';

    province = $("#province_search").val();
    developer = $("#developer_search").val();

    noKeys="<tr style='text-align: center;'><td colspan='6'>没有搜索到相关信息~</td></tr>";

    currentPage = 1;
    taskPage=searchTask(false,pageSize,currentPage,province,developer);
    searchResult = taskPage["content"];
    var totalElements = taskPage["totalElements"];

    if(searchResult=="isError"){
        alert("服务器中断");
        return;
    }else{
        $("#page").html("");
        if(searchResult=='' ||searchResult.isEmpty){
            $("table >tbody").html(noKeys);
        }else{
            //分页
            query_taskList(searchResult);

            //调用commonSelf_pageStyle
            commonSelf_pageStyle(pageSize,showPageNum,totalElements,"isNull","page",getTask__pageCallback);
        }
    }
}
//分页回调函数
function getTask__pageCallback(){

    var currentPage_content='',taskPage='';
    var province='',developer='';

    province = $("#province_search").val();
    developer = $("#developer_search").val();

    //获取当前页
    currentPage=parseInt($("#page #currentPage").text());

    taskPage=searchTask(false,pageSize,currentPage,province,developer);
    currentPage_content = taskPage["content"];

    //生成查询结果列表
    query_taskList(currentPage_content);
}
/*-----------------------------查询功能end-----------------------------*/
//添加监控项
function addItem(){
	$.ajax({
        url: "/opendata/contactData/addItem",
        type:"POST",
        async:true,
        dataType:"json",
        data:$('#authform').serialize(),
        success:function(data){
        	$('.modal-backdrop').hide();
        	console.log("addSuccess");
        	$('#authform')[0].reset();
        	$("#bitian").text("");
        	return;
        }
    });	
}
//删除监控项
function removeItem(id,async){
	console.log("coming please"+id);
	$.ajax({
        url: "/monitor/platform/crawler/removeCarrierItem",
        type:"POST",
        async:true,
        dataType:"json",
        data:{id:id},
        success:function(data){
        	console.log("deleteSuccess");
        	return;
        }
    });
	
}
//修改监控项
function updateItem(){
	console.log("修改监控项");
	$.ajax({
        url: "/opendata/contactData/addItem",
        type:"POST",
        async:true,
        dataType:"json",
        data:$('#authform1').serialize(),
        success:function(data){
        	console.log("updateSuccess");
        	$('#authform')[0].reset();
        	$("#bitian").text("");
        	return;
        }
    });
}
//添加监控项
function addItem(){
	$.ajax({
        url: "/monitor/platform/crawler/addCarrierItem",
        type:"POST",
        async:true,
        dataType:"json",
        data:$('#authform').serialize(),
        success:function(data){
        	$('.modal-backdrop').hide();
        	console.log("addSuccess");
        	$('#authform')[0].reset();
        	$("#bitian").text("");
        	return;
        }
    });
}
