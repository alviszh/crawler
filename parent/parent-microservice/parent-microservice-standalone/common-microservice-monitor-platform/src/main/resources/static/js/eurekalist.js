
//分页 设置默认值
var pageSize='',currentPage='',showPageNum='';
pageSize=20;  //显示页大小
currentPage=1; //当前页
showPageNum=5;     //一次显示的页数
var lineIndex = 0; //设置行号；
$(function(){
	console.log("business_contact______");

    //查询第一页运营商任务记录
    show_Task();
  
    //点击添加
    $("#addItem").on("click",function(){
    	addBusinessConsumer();
    	$('#addModal').modal('hide');
    	location.reload();
    });
    

//    //点击修改
    $("#updateConsumer").on("click",function(){
    	updateBusinessConsumer();
    	$('#updateModalDiv').modal('hide');
    	location.reload();
    });
    
    //点击删除
    $("#removeConsumer").on("click",function(){
    	var tr=$(this).parent();
    	var h=tr.prev();
    	var c = h.children();
    	var id1 = c.eq(1).text();
	    console.log("==id1" + id1);
	    removeBusinessConsumer(id1);
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
function removeBusinessConsumer(id,async){
	console.log("coming please"+id);
	$.ajax({
        url: "/opendata/contactData/removeBusinessConsumer",
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
function updateBusinessConsumer(){
	console.log("coming please");
	$.ajax({
        url: "/opendata/contactData/addBusinessConsumer",
        type:"POST",
        async:true,
        dataType:"json",
        data:$('#authform1').serialize(),
        success:function(data){
        	console.log("updateSuccess");
        	$('#authform')[0].reset();
        	return;
        }
    });
	
}

//添加联系人
function addBusinessConsumer(){
	$.ajax({
        url: "/opendata/contactData/addBusinessConsumer",
        type:"POST",
        async:true,
        dataType:"json",
        data:$('#authform').serialize(),
        success:function(data){
        	$('.modal-backdrop').hide();
        	console.log("addSuccess");
        	$('#authform')[0].reset();
        	return;
        }
    });
	
}


//查询记录
function show_Task(){

    var insuranceTaskPage='',taskList='';
    var totalElements = ''; //总记录


    insuranceTaskPage=searchInsuranceTask(false,pageSize,currentPage);
    taskList = insuranceTaskPage["content"];
    totalElements = insuranceTaskPage["totalElements"];

    console.log("=====taskList=====" + taskList + "=====totalElements=====" + totalElements);

    //分页
    $("#page").html("");
    //显示首页内容
    productList(taskList);
    
    if(totalElements==0){
    	var trss='';
    	trss +="<tr>"
            +"<td></td>"
            +"<td></td>"
            +"<td><h5>您还没有业务联系人，可以<a id='tjlxr'>添加联系人</a>。</h5></td>"
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
    commonSelf_pageStyle(pageSize,showPageNum,totalElements,"isNull","page",get_pageCallback);

}


//分页查询
function searchInsuranceTask(async,pageSize,currentPage){
    var insuranceTaskPage='';

    $.ajax({
        url: "/opendata/contactData/tasks/getBusinessPages",
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

//生成任务列表
function productList(taskList){

    var trs='',createtime='',btn1='',btn2='',description='',deleteData='';

    $.each(taskList,function(index,task){
    	var productList = task.productList;
        lineIndex = (currentPage-1)*pageSize + index + 1;

    	var introduces = "";
    	$.each(productList,function(index, product){
    		introduces += product.name + "&nbsp,&nbsp";
    	});
    	introduces = introduces.substr(0, introduces.length - 6);
    	
        btn1="<a id='update"+(index+1)+"'>信息修改</a>";
        btn2="<a id='delete"+(index+1)+"'>信息删除</a>";
        
        trs +="<tr>"
            +"<td style=\"display:none\">"+task.id+"</td>"
            +"<td>"+task.name+"</td>"
            +"<td>"+task.phone+"</td>"
            +"<td>"+task.email+"</td>"
            +"<td>"+task.job+"</td>"
            +"<td>"+task.getType+"</td>"
            +"<td>"+introduces+"</td>"
            +"<td>"+btn1+"  "+btn2+"</td>"
            +"</tr>";

    });

    $("table >tbody").html(trs);

    $(function(){
    	selectProduct();
	　　$("a[id^='update']").click(function(){
		    $('#authform')[0].reset();
			$("#myModalLabel").html("修改联系人");
		    var tr=$(this).parent().parent();
	        var dataConsumer = tr.children();
	        
		    $('#addModal').modal('show');
		    
		    document.getElementById("id").value=dataConsumer.eq(0).text(); 
		    document.getElementById("name").value=dataConsumer.eq(1).text(); 
		    document.getElementById("phone").value=dataConsumer.eq(2).text(); 
		    document.getElementById("email").value=dataConsumer.eq(3).text(); 
		    document.getElementById("job").value=dataConsumer.eq(4).text(); 
		    
		    if(dataConsumer.eq(5).text().indexOf("邮件")==0|dataConsumer.eq(5).text().indexOf("邮件")==3){
	        	$('#getTypeEmail').prop('checked', true);
	        }
	        if(dataConsumer.eq(5).text().indexOf("短信")==0|dataConsumer.eq(5).text().indexOf("短信")==3){
	        	$('#getTypeMessage').prop('checked', true)
	        }
	        //业务复选框
	        var ps = dataConsumer.eq(6).text();
//	        console.log("pspspsspspsps"+ps);
//	                                                                      所有的业务循环
	        for (var i = 0; i < $('div[id^="introducediv"]').length; i++) {
//	        			选中对象 匹配      所有的业务
	        	if(ps.indexOf($('div[id^="introducediv"]').eq(i).text().trim())!=-1){
	        		$('input[id^="introduce"]').eq(i).prop('checked',true);
		        }
			}
	        
	　　});
	
		$("a[id^='delete']").click(function(){
			 console.log("deletedeletedeletedeletedeletedeletedeletedelete");
			 var tr=$(this).parent().parent();
		     var dataConsumer = tr.children();
		     deleteData ="<h5>确定删除联系人"+dataConsumer.eq(1).text()+"吗？</h5>"+"<h5 style=\"display:none\">"+dataConsumer.eq(0).text()+"<h5>"
		     $("#delete").html(deleteData);
		     $('#removeModalDiv').modal('show');
	　　});
		
		$("a[id^='add']").click(function(){
			console.log("clearADdd");
//			clear();
			$('#authform')[0].reset();
	　　});
		
    })
}

//分页回调函数
function get_pageCallback(){

    var currentPage_content='',insuranceTaskPage='';
    var taskid='';

    taskid = $("#taskid").val();

    //获取当前页
    currentPage=parseInt($("#page #currentPage").text());

    insuranceTaskPage=searchInsuranceTask(false,pageSize,currentPage,taskid);
    currentPage_content = insuranceTaskPage["content"];


    //生成查询结果列表
    productList(currentPage_content);

}

//查询所有业务
function selectProduct(){
	$.ajax({
        url: "/opendata/contactData/selectProduct",
        type:"POST",
        async:true,
        dataType:"json",
        success:function(data){
//        	console.log("selectProduct"+data);
        	var pro='';
        	$.each(data,function(index,product){
        		pro+='<div id="introducediv'+product.id+'" class="checkbox-inline"  style="margin-left: 15px;"><input type="checkbox" id="introduce'+product.id+'" name="StringProductList" value='+product.id+'>'+product.name+'</div>'
        	});
//        	console.log("selectProduct"+pro);
        	$(".pro").html(pro);
        	return;
        }
    });
}

function clear(){
	console.log("clear");
	$('input[id^="isneedmonitor"]').prop('checked',false);
}
