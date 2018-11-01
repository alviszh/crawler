
//分页 设置默认值
var pageSize='',currentPage='',showPageNum='';
pageSize=20;  //显示页大小
currentPage=1; //当前页
showPageNum=5;     //一次显示的页数
var lineIndex = 0; //设置行号；
$(function(){

    //查询第一页运营商任务记录
    show_insurance_Task();

    //点击搜索
    $("#search").on("click",function(){
    	insuranceTask_search();
    });

});

//查询运营商任务记录
function show_insurance_Task(){

    var insuranceTaskPage='',taskList='';
    var totalElements = ''; //总记录
    var taskid='';

    taskid = $("#taskid").val();

    insuranceTaskPage=searchInsuranceTask(false,pageSize,currentPage,taskid);
    taskList = insuranceTaskPage["content"];
    totalElements = insuranceTaskPage["totalElements"];

    console.log("=====taskList=====" + taskList + "=====totalElements=====" + totalElements);

    //分页
    $("#page").html("");
    //显示首页内容
    insurance_productList(taskList);

    //调用commonSelf_pageStyle
    commonSelf_pageStyle(pageSize,showPageNum,totalElements,"isNull","page",getInsurance__pageCallback);

}


//分页查询运营商task任务
function searchInsuranceTask(async,pageSize,currentPage,taskid){
    var insuranceTaskPage='';

    $.ajax({
        url: "/webmanage/data/insurance/tasks/getInsuranceTaskPages",
        type:"post",
        async:async,
        data:{
            pageSize:pageSize,
            currentPage:currentPage,
            taskid:taskid,
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

    var trs='',createtime='',btn='',description='';

    $.each(taskList,function(index,task){

        lineIndex = (currentPage-1)*pageSize + index + 1;

        if(task.createtime==null){
            createtime="---"
        }else{
            createtime=task.createtime;
        }

        btn="<a href='javascript:void(0)' id='query_"+(index+1)+"'>查看详情</a>";
        if (task.description != null) {
            description=task.description;
        }

        trs +="<tr>"
            //+"<td>"+lineIndex+"</td>"
            +"<td>"+task.id+"</td>"
            +"<td>"+createtime+"</td>"
            +"<td>"+task.taskid+"</td>"
            +"<td>"+description+"</td>"
            +"<td>"+task.city+"</td>"
            +"<td>"+btn+"</td>"
            +"</tr>";

    });

    $("table >tbody").html(trs);

    $("table").on("click","a[id^='query_']",function(){

        var tr=$(this).parent().parent();
        var tdArr = tr.children();
        var taskid = tdArr.eq(2).text();
        console.log("=========taskid" + taskid);
        windowOpenPostDetail(taskid);
    });

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

function windowOpenPostDetail(taskid) {

    var form = $("#insuranceTaskForm");
    //form.attr("style", "display:none");
    form.attr("target", "_blank");

    $("#taskidSearch").val(taskid);

    form.submit();
    form.removeAttribute("input");
    $("#taskidSearch").val("");
}

//搜索
function insuranceTask_search(){

    var taskid='';
    var insuranceTaskPage='',searchResult='';
    var noKeys='';

    taskid = $("#taskid").val();

    noKeys="<tr style='text-align: center;'><td colspan='6'>没有搜索到相关信息</td></tr>";

    currentPage = 1;
    insuranceTaskPage=searchInsuranceTask(false,pageSize,currentPage,taskid);
    searchResult = insuranceTaskPage["content"];
    var totalElements = insuranceTaskPage["totalElements"];

    if(searchResult=="isError"){
        alert("服务器中断");
        return;
    }else{
        $("#page").html("");
        if(searchResult=='' ||searchResult.isEmpty){
            $("table >tbody").html(noKeys);
        }else{
            //分页
        	insurance_productList(searchResult);

            //调用commonSelf_pageStyle
            commonSelf_pageStyle(pageSize,showPageNum,totalElements,"isNull","page",getInsurance__pageCallback);

        }
    }

}
