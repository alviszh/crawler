/**
 * 调用接口
 */
function invokeTelecomApiTest(){
	var url = '/carrier/apitest/telecom/result/';
	var taskId = $("#taskId").val();
	if(taskId==null||taskId==undefined||taskId==""){
		$("#infoText").text("请输入TaskId!");
    	$("#infoModal").modal();
    	return;
	}
	url = url+taskId;
	$.ajax({  
	    url: url,
	    type:'post',  
	    dataType:'json',  
	    success:function(result) {
	        if(result.data){
	        	var userinfoDataList = result.data.listofTelecomCustomerThemResult;
	        	loadUserinfoTable(userinfoDataList);
	        	
	        	var userCallDataList = result.data.listOfTelecomCallThemResult;
	        	loadUserCallDataList(userCallDataList);
	        	
	        	var userPayDataList = result.data.listofTelecomPayMsgThemResult;
	        	loadUserPayDataList(userPayDataList);
	        }else{  
	        	$("#infoText").text("接口未返回数据");
	        	$("#infoModal").modal();
	        }  
	     },  
	     error : function() {  
	    	$("#infoText").text("调用接口发生异常!");
        	$("#infoModal").modal();
	     }  
	});
	
}


/**
 * 用户信息
 * @param userinfoDataList
 */
function loadUserinfoTable(userinfoDataList){
	var userinfoTrStr = '';
	for (var i = 0; i < userinfoDataList.length; i++) {
		userinfoTrStr += '<tr>';
		userinfoTrStr += '<td class="view-td">' + (i+1) + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].name + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].paydate + '</td>';
	/*	userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].paynum + '</td>';*/
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].bussoptional + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].bussrent + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].internetpay + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].internetchinapay + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].currentIntegra + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].lastIntegra + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].usingIntegra + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].riseIntegra + '</td>';
		userinfoTrStr += '</tr>';  
	} 
	$("#tbody_userinfo").html(userinfoTrStr);
}


/**
 * 通话详单
 */
function loadUserCallDataList(userCallDataList){
	var userCallTrStr = '';
	for (var i = 0; i < userCallDataList.length; i++) {
		userCallTrStr += '<tr>';
		userCallTrStr += '<td class="view-td">' + (i+1) + '</td>';
		/*userCallTrStr += '<td class="view-td">' + userCallDataList[i].usernumber + '</td>';*/
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].type + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].othernum + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].date + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].time + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].money + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].teletype + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].othermoney + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].guishudi + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].calltype + '</td>';
		userCallTrStr += '</tr>';  
	}
	$("#tbody_usercall").html(userCallTrStr);
}

/**
 * 缴费记录
 * @param userPayDataList
 */
function loadUserPayDataList(userPayDataList){
	var userPayTrStr = '';
	for (var i = 0; i < userPayDataList.length; i++) {
		userPayTrStr += '<tr>';
		userPayTrStr += '<td class="view-td">' + (i+1) + '</td>';
		/*userPayTrStr += '<td class="view-td">' + userPayDataList[i].telenumid + '</td>';*/
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].type + '</td>';
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].paydate + '</td>';
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].paynum + '</td>';
		userPayTrStr += '</tr>';  
	}
	$("#tbody_userpay").html(userPayTrStr);
}
