
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
    	
    	var tr=$(this).parent().parent();
        var dataItem = tr.children();
        var ps = dataItem.eq(6).text();
        var count=0;
        for (var i = 0; i < $('div[id^="introducediv"]').length; i++) {
        	if($('input[id^="introduce"]').eq(i).prop('checked')==true){
        		console.log("1");
        		count++;
	        }
		}
        if(count==0){
        	console.log("2");
        	$("#bitian").text("*为必填项！");
    	}
        else if($("#name").val().trim()==""| $("#email").val().trim()==""| $("#phone").val().trim()==""){
        	console.log("3");
    		$("#bitian").text("*为必填项！");
    	}
    	else if($("#getTypeEmail").prop('checked')==false & $("#getTypeMessage").prop('checked')==false){
    		console.log("4");
    			$("#bitian").text("*为必填项！");
    	}
    	else{
    		console.log("5");
    		addBusinessItem();
        	$('#addModal').modal('hide');
        	location.reload();
    	}
    });
    
    //点击删除
    $("#removeItem").on("click",function(){
    	/*var tr=$(this).parent();
    	var h=tr.prev();
    	var c = tr.children();
    	var id = c.eq(1).text();
    	alert("删除项对应的id==" + id);*/
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
    var appname='',developer='';

    appname = $("#appname_search").val();
    developer = $("#developer_search").val();

    taskPage=searchTask(false,pageSize,currentPage,appname,developer);
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
function searchTask(async,pageSize,currentPage,appname,developer){
    var taskPage='';
    /*如下位置减去1(currentPage-1),否则查询不到第一页，不知道为啥*/
    $.ajax({
        url: "/monitor/platform/system/getEurekaPages",
        type:"post",
        async:async,
        data:{
            pageSize:pageSize,
            currentPage:currentPage-1,
            appname:appname,
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
function query_taskList(eurekaList){

    var trs='',createtime='',btn1='',btn2='';
    var monitorflag;

    $.each(eurekaList,function(index,eureka){
    	/*行号,随着翻页变化*/
        lineIndex = (currentPage-1)*pageSize + index + 1;
        
        btn1="<a id='update"+(index+1)+"' style='cursor:pointer'>信息修改</a>";
        btn2="<a id='delete"+(index+1)+"' style='cursor:pointer'>信息删除</a>";
        
        monitorflag=(eureka.isneedmonitor == 1) ? '正常监控' : '暂停监控';
        
        trs +="<tr>"
            +"<td style=\"display:none\">"+eureka.id+"</td>"
            +"<td>"+lineIndex+"</td>"
            +"<td>"+eureka.appname+"</td>"
            +"<td>"+eureka.developer+"</td>"
            +"<td>"+eureka.instancecount+"</td>"
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
		    
		    document.getElementById("appname").value=dataItem.eq(1).text(); 
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
    var appname='',developer='';
    var taskPage='',searchResult='';
    var noKeys='';

    appname = $("#appname_search").val();
    developer = $("#developer_search").val();

    noKeys="<tr style='text-align: center;'><td colspan='6'>没有搜索到相关信息~</td></tr>";

    currentPage = 1;
    taskPage=searchTask(false,pageSize,currentPage,appname,developer);
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
    var appname='',developer='';

    appname = $("#appname_search").val();
    developer = $("#developer_search").val();

    //获取当前页
    currentPage=parseInt($("#page #currentPage").text());

    taskPage=searchTask(false,pageSize,currentPage,appname,developer);
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
        url: "/monitor/platform/system/removeItem",
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

