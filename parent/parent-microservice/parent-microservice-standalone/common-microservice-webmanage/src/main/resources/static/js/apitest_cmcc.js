/**
 * 调用接口
 */
function invokeCmccApiTest(){
	var url = '/carrier/apitest/cmcc/result/';
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
	        	var userinfoDataList = result.data.listOfCmccUserInfo;
	        	loadUserinfoTable(userinfoDataList);
	        	
	        	var userCallDataList = result.data.listOfCmccUserCallResult;
	        	loadUserCallDataList(userCallDataList);
	        	
	        	var userSmsDataList = result.data.listOfCmccSMSMsgResult;
	        	loadUserSmsDataList(userSmsDataList);
	        	
	        	var userCheckDataList = result.data.listOfCmccCheckMsgResult;
	        	loadUserCheckDataList(userCheckDataList);
	        	
	        	var userPayDataList = result.data.listOfCmccPayMsgResult;
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
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].brand + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].level + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].status + '</td>';*/
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].inNetDate + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].email + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].address + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].zipCode + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].contactNum + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].starLevel + '</td>';
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].starScore + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].starTime + '</td>';*/
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].realNameInfo + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].vipInfo + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].pointValue + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].curFee + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].curFeeTotal + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].realFee + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].brandName + '</td>';
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].curPlanId + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].curPlanName + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].nextPlanId + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].nextPlanName + '</td>';*/
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
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].startTime + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].startYear + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].commPlac + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].commMode + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].anotherNm + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].commTime + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].commType + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].mealFavorable + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].commFee + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].mobileNum + '</td>';
		userCallTrStr += '</tr>';  
	}
	$("#tbody_usercall").html(userCallTrStr);
}


/**
 * 短信信息
 * @param userSmsDataList
 */
function loadUserSmsDataList(userSmsDataList){
	var userSmsTrStr = '';
	for (var i = 0; i < userSmsDataList.length; i++) {
		userSmsTrStr += '<tr>';
		userSmsTrStr += '<td class="view-td">' + (i+1) + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].anotherNm + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].busiName + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].commFee + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].commMode + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].commPlac + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].infoType + '</td>';
		/*userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].meal + '</td>';*/
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].startTime + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].startYear + '</td>';
		/*userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].mobileNum + '</td>';*/
		userSmsTrStr += '</tr>';  
	}
	$("#tbody_usersms").html(userSmsTrStr);
}

/**
 * 月账单
 * @param userCheckDataList
 */
function loadUserCheckDataList(userCheckDataList){
	var userCheckTrStr = '';
	for (var i = 0; i < userCheckDataList.length; i++) {
		userCheckTrStr += '<tr>';
		userCheckTrStr += '<td class="view-td">' + (i+1) + '</td>';
		userCheckTrStr += '<td class="view-td">' + userCheckDataList[i].billMonth + '</td>';
		userCheckTrStr += '<td class="view-td">' + userCheckDataList[i].billStartDate + '</td>';
		userCheckTrStr += '<td class="view-td">' + userCheckDataList[i].billEndDate + '</td>';
		userCheckTrStr += '<td class="view-td">' + userCheckDataList[i].billFee + '</td>';
		userCheckTrStr += '</tr>';  
	}
	$("#tbody_usercheck").html(userCheckTrStr);
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
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].payChannel + '</td>';
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].payDate + '</td>';
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].payFee + '</td>';
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].payType + '</td>';
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].payTypeName + '</td>';
		userPayTrStr += '</tr>';  
	}
	$("#tbody_userpay").html(userPayTrStr);
}
