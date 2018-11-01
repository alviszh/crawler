
//分页 设置默认值
var pageSize='',currentPage='',showPageNum='';
pageSize=20;  //显示页大小
currentPage=1; //当前页
showPageNum=5;     //一次显示的页数
var lineIndex = 0; //设置行号；
$(function(){
	console.log("contact===");

    //查询第一页运营商任务记录
    show_fee_contact_Task();
  
    //点击添加
    $("#addConsumer").on("click",function(){
        if($("#name").val().trim()==""| $("#email").val().trim()==""| $("#phone").val().trim()==""){
    		$("#bitian").text("*为必填项！");
    	}
    	else{
    		addConsumer();
        	$('#addModal').modal('hide');
        	location.reload();
    	}
    });
    
    
//    点击修改
//    $("#updateConsumer").on("click",function(){
//    	updateConsumer(id,name,phone,email,job);
//    	$('#updateModalDiv').modal('hide');
//    	location.reload();
//    });
    
    //点击删除
    $("#removeConsumer").on("click",function(){
    	var tr=$(this).parent();
    	var h=tr.prev();
    	var c = h.children();
    	var id1 = c.eq(1).text();
	    console.log("==id1" + id1);
    	removeConsumer(id1);
    	$('#removeModalDiv').modal('hide');
    	location.reload();
    });
    
    
    //点击business
    $("#btn-fee").on("click",function(){
    	console.log("fefefefefefefefefeefefee");
    	$(this).addClass("class='active'").siblings().removeClass("class='active'");
    });
    
    
    $("#addM").click(function(){
		console.log("clearADdd");
		$("#myModalLabel").html("添加联系人");
		$('#authform')[0].reset();
　　});
});


//删除联系人
function removeConsumer(id,async){
	console.log("coming please"+id);
	$.ajax({
        url: "/opendata/contactData/removeConsumer",
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

//修改联系人
function updateConsumer(name,email,phone,job,async){
	console.log("coming please");
	$.ajax({
        url: "/opendata/contactData/addConsumer",
        type:"POST",
        async:true,
        dataType:"json",
        data:$('#authform2').serialize(),
        success:function(data){
        	console.log("updateSuccess");
        	$('#authform')[0].reset();
        	return;
        }
    });
	
}

//添加联系人
function addConsumer(name,email,phone,job,async){
	$.ajax({
        url: "/opendata/contactData/addConsumer",
        type:"POST",
        async:true,
        dataType:"json",
        data:$('#authform').serialize(),
        success:function(data){
        	$('.modal-backdrop').hide();
//        	console.log("addSuccess");
//        	$('#name').val('');
//        	$('#phone').val('');
//        	$('#job').val('');
//        	$('#email').val('');
        	$('#authform')[0].reset();
        	return;
        }
    });
	
}


//查询记录
function show_fee_contact_Task(){

    var insuranceTaskPage='',taskList='';
    var totalElements = ''; //总记录


    insuranceTaskPage=searchInsuranceTask(false,pageSize,currentPage);
    taskList = insuranceTaskPage["content"];
    totalElements = insuranceTaskPage["totalElements"];

    console.log("=====taskList=====" + taskList + "=====totalElements=====" + totalElements);

    //分页
    $("#page").html("");
    //显示首页内容
    insurance_productList(taskList);
    
    if(totalElements==0){
    	var trss='';
    	trss +="<tr>"
            +"<td></td>"
            +"<td></td>"
            +"<td><h5>您还没有余额告警联系人，可以<a id='tjlxr'>添加联系人</a>。</h5></td>"
            +"<td></td>"
            +"<td></td>"
            +"<td></td>"
            +"<td></td>"
            +"</tr>";
    	$("#list_div").html(trss);
    	$("#list_table").hide();
    	 //空联系人添加
        $("#tjlxr").on("click",function(){
        	$("#myModalLabel").html("添加联系人");
        	$('#addModal').modal('show');
        });
    }

    //调用commonSelf_pageStyle
    commonSelf_pageStyle(pageSize,showPageNum,totalElements,"isNull","page",getInsurance__pageCallback);

}


//分页查询
function searchInsuranceTask(async,pageSize,currentPage){
    var insuranceTaskPage='';

    $.ajax({
        url: "/opendata/contactData/tasks/getPages",
        type:"post",
        async:async,
        data:{
            pageSize:pageSize,
            currentPage:currentPage,
        },
        success:function(data){
            if(!async){
            	insuranceTaskPage=data;
            }
        },
        error:function(){
        	insuranceTaskPage="isError";
        }

    });
    if(!async){
        return insuranceTaskPage;
    }
}

//生成运营商任务列表
function insurance_productList(taskList){

    var trs='',createtime='',btn1='',btn2='',description='',deleteData='';
    $.each(taskList,function(index,task){
        lineIndex = (currentPage-1)*pageSize + index + 1;

        if(task.createtime==null){
            createtime="---"
        }else{
            createtime=task.createtime;
        }

        btn1="<a id='update"+(index+1)+"'>信息修改</a>";
        btn2="<a id='delete"+(index+1)+"' >信息删除</a>";
        if (task.description != null) {
            description=task.description;
        }

        trs +="<tr>"
            +"<td style=\"display:none\">"+task.id+"</td>"
            +"<td>"+task.name+"</td>"
            +"<td>"+task.phone+"</td>"
            +"<td>"+task.email+"</td>"
            +"<td>"+task.job+"</td>"
            +"<td>"+createtime+"</td>"
            +"<td>"+btn1+"  "+btn2+"</td>"
            +"</tr>";

    });

    $("table >tbody").html(trs);

    $(function(){
	　　$("a[id^='update']").click(function(){
			$("#myModalLabel").html("修改联系人");
			$('#authform')[0].reset();
		    var tr=$(this).parent().parent();
	        var dataConsumer = tr.children();
//	        console.log("==" + dataConsumer.eq(0).text());
//	        console.log("==" + dataConsumer.eq(1).text());
//	        console.log("==" + dataConsumer.eq(2).text());
//	        console.log("==" + dataConsumer.eq(3).text());
//	        console.log("==" + dataConsumer.eq(4).text());
		    $('#addModal').modal('show');//
		    document.getElementById("id").value=dataConsumer.eq(0).text(); 
		    document.getElementById("name").value=dataConsumer.eq(1).text(); 
		    document.getElementById("phone").value=dataConsumer.eq(2).text(); 
		    document.getElementById("email").value=dataConsumer.eq(3).text(); 
		    document.getElementById("job").value=dataConsumer.eq(4).text(); 
	　　});
	
		$("a[id^='delete']").click(function(){
			 console.log("deletedeletedeletedeletedeletedeletedeletedelete");
			 var tr=$(this).parent().parent();
		     var dataConsumer = tr.children();
//		     console.log("==" + dataConsumer.eq(0).text());
		     deleteData ="<h5>确定删除联系人"+dataConsumer.eq(1).text()+"吗？</h5>"+"<h5 style=\"display:none\">"+dataConsumer.eq(0).text()+"<h5>"
		     $("#delete").html(deleteData);
		     $('#removeModalDiv').modal('show');
	　　});
		
    })
}

//分页回调函数
function getInsurance__pageCallback(){

    var currentPage_content='',insuranceTaskPage='';
    var taskid='';

    taskid = $("#taskid").val();

    //获取当前页
    currentPage=parseInt($("#page #currentPage").text());

    insuranceTaskPage=searchInsuranceTask(false,pageSize,currentPage,taskid);
    currentPage_content = insuranceTaskPage["content"];

    //console.log("=====currentPage_content=====" + currentPage_content);

    //生成查询结果列表
    insurance_productList(currentPage_content);

}

