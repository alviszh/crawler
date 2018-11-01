
$(function(){
	
	$("button[id^='showconfigure']").on("click",function(){
		var appProductListId = $(this).attr("data-appProductListId");
//		console.log(appProductListId);
		showAppProductList(appProductListId);
    });
	
	
	
	$("#modifyApp").on("click",function(){
    	modifyAppProductList();
    	$('#appProductListModal').modal('hide');
    	location.reload();
    });


});

















function showAppProductList(appProductListId){
	
	$.ajax({
        url: "/opendata/developer/appdata/appproductlistdetails",
        type:"POST",
        async:true,
        dataType:"json",
        data: {"appId":"1","appProductListId":appProductListId},
        success:function(data){
        	console.log(data);
        	$("#appProductListId").val(data.id);
        	$("#task_notice_url").val(data.task_notice_url);
        	$("#login_notice_url").val(data.task_notice_url);
        	$("#crawler_notice_url").val(data.task_notice_url);
        	$("#report_notice_url").val(data.task_notice_url);
        	$.each(data.callbackparams,function(index,callbackParams){
        		console.log(index);
        		$("#callbackParamsId"+(index+1)).val(callbackParams.id);
        		$("#callbackParamsKey"+(index+1)).val(callbackParams.paramsKey);
        		$("#callbackParamsValue"+(index+1)).val(callbackParams.paramsValue);
            });
        }
    });
	
}



function modifyAppProductList(){
	
	var list=[];
	var appProductListId = $("#appProductListId").val();
	var task_notice_url = $("#task_notice_url").val();
	var login_notice_url = $("#login_notice_url").val();
	var crawler_notice_url = $("#crawler_notice_url").val();
	var report_notice_url = $("#report_notice_url").val();
	
	for (var i = 1; i < 6; i++) {
		var callbackParamsId1 = $("#callbackParamsId"+i).val();
		var callbackParamsKey1 = $("#callbackParamsKey"+i).val();
		var callbackParamsValue1 = $("#callbackParamsValue"+i).val();
		list.push({
			callbackParamsId1 : callbackParamsId1,
			callbackParamsKey1 : callbackParamsKey1,
			callbackParamsValue1 : callbackParamsValue1
		});
	}
	
	$.ajax({
        url: "/opendata/developer/appdata/modifyappproductlistdetails",
        type:"POST",
        async:true,
        dataType:"json",
		        data : {
			"appProductListId" : appProductListId,
			"task_notice_url" : task_notice_url,
			"login_notice_url" : login_notice_url,
			"crawler_notice_url" : crawler_notice_url,
			"report_notice_url" : report_notice_url,
			"param":JSON.stringify(list)
		},
		success:function(data){
//        	console.log();
        }
    });
	
}