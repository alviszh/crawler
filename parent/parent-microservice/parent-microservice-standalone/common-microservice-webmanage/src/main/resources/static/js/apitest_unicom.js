/**
 * 调用接口
 */
function invokeUnicomApiTest(){
	var url = '/carrier/apitest/unicom/result/';
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
	        	var userinfoDataList = result.data.listofUnicomUserInfo;
	        	loadUserinfoTable(userinfoDataList);
	        	
	        	var userCallDataList = result.data.listOfUnicomCallResult;
	        	loadUserCallDataList(userCallDataList);
	        	
	        	var userSmsDataList = result.data.listOfUnicomNoteResult;
	        	loadUserNoteDataList(userSmsDataList);
	        	
	        	var userDetailDataList = result.data.listOfUnicomDetailList;
	        	loadUserDetailDataList(userDetailDataList);
	        	
	        	var userIntegralDataList = result.data.listOfUnicomIntegraThemlResult;
	        	loadUserIntegralDataList(userIntegralDataList);
	        	
	        	var userPayDataList = result.data.listOfUnicomPayMsgStatusResult;
	        	loadUserPayDataList(userPayDataList);
	        	
	        	var userActivityDataList = result.data.listOfUnicomUserActivityInfo;
	        	loadUserActivityDataList(userActivityDataList);
	        	
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
		userinfoTrStr += '<td class="view-td">' + 1 + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].password + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].packageName + '</td>';
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].provincecode + '</td>';*/
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].usernumber + '</td>';
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].expireTime + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].nettype + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].areaCode + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].certnum + '</td>';*/
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].opendate + '</td>';
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].citycode + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].paytype + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].productId + '</td>';*/
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].custName + '</td>';
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].brand + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].productType + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].packageID + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].currentID + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].customid + '</td>';*/
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].custlvl + '</td>';
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].loginType + '</td>';*/
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].nickName + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].subscrbstat + '</td>';
		/*userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].laststatdate + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].brand_name + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].is_wo + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].is_20 + '</td>';
		userinfoTrStr += '<td class="view-td">' + userinfoDataList[i].is_36 + '</td>';*/
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
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].usernumber + '</td>';
		/*userCallTrStr += '<td class="view-td">' + userCallDataList[i].md5 + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].businesstype + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].longtype + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].thtype + '</td>';*/
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].calledhome + '</td>';
		/*userCallTrStr += '<td class="view-td">' + userCallDataList[i].cellid + '</td>';*/
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].thtypeName + '</td>';
		/*userCallTrStr += '<td class="view-td">' + userCallDataList[i].twoplusfee + '</td>';*/
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].totalfee + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].calllonghour + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].calldate + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].calltime + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].calltype + '</td>';
		/*userCallTrStr += '<td class="view-td">' + userCallDataList[i].othernum + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].otherarea + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].romatype + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].homearea + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].homenum + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].calltypeName + '</td>';*/
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].landtype + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].romatypeName + '</td>';
		/*userCallTrStr += '<td class="view-td">' + userCallDataList[i].homeareaName + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].otherareaName + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].nativefee + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].landfee + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].roamfee + '</td>';*/
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].deratefee + '</td>';
		userCallTrStr += '<td class="view-td">' + userCallDataList[i].otherfee + '</td>';
		userCallTrStr += '</tr>';  
	}
	$("#tbody_usercall").html(userCallTrStr);
}


/**
 * 短信信息
 * @param userSmsDataList
 */
function loadUserNoteDataList(userSmsDataList){
	var userSmsTrStr = '';
	for (var i = 0; i < userSmsDataList.length; i++) {
		userSmsTrStr += '<tr>';
		userSmsTrStr += '<td class="view-td">' + (i+1) + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].usernumber + '</td>';
		/*userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].amount + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].fee + '</td>';*/
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].smsdate + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].smstime + '</td>';
		/*userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].businesstype + '</td>';*/
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].othernum + '</td>';
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].smstype + '</td>';
		/*userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].otherarea + '</td>';*/
		userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].homearea + '</td>';
		/*userSmsTrStr += '<td class="view-td">' + userSmsDataList[i].deratefee + '</td>';*/
		userSmsTrStr += '</tr>';  
	}
	$("#tbody_usersms").html(userSmsTrStr);
}

/**
 * 历史账单
 * @param userDetailDataList
 */
function loadUserDetailDataList(userDetailDataList){
	var userDetailTrStr = '';
	for (var i = 0; i < userDetailDataList.length; i++) {
		userDetailTrStr += '<tr>';
		userDetailTrStr += '<td class="view-td">' + (i+1) + '</td>';
		userDetailTrStr += '<td class="view-td">' + userDetailDataList[i].name + '</td>';
		userDetailTrStr += '<td class="view-td">' + userDetailDataList[i].value + '</td>';
		userDetailTrStr += '<td class="view-td">' + userDetailDataList[i].fee + '</td>';
		userDetailTrStr += '<td class="view-td">' + userDetailDataList[i].integrateitem + '</td>';
		userDetailTrStr += '<td class="view-td">' + userDetailDataList[i].integrateitemcode + '</td>';
		userDetailTrStr += '</tr>';  
	}
	$("#tbody_userdetail").html(userDetailTrStr);
}


/**
 * 积分
 */
function loadUserIntegralDataList(userIntegralDataList){
	var userIntegralTrStr = '';
	for (var i = 0; i < userIntegralDataList.length; i++) {
		userIntegralTrStr += '<tr>';
		userIntegralTrStr += '<td class="view-td">' + (i+1) + '</td>';
		userIntegralTrStr += '<td class="view-td">' + userIntegralDataList[i].date + '</td>';
		userIntegralTrStr += '<td class="view-td">' + userIntegralDataList[i].type + '</td>';
		userIntegralTrStr += '<td class="view-td">' + userIntegralDataList[i].calltype + '</td>';
		userIntegralTrStr += '<td class="view-td">' + userIntegralDataList[i].integral + '</td>';
		userIntegralTrStr += '<td class="view-td">' + userIntegralDataList[i].scoreinvaliddate + '</td>';
		userIntegralTrStr += '<td class="view-td">' + userIntegralDataList[i].scoretype + '</td>';
		userIntegralTrStr += '<td class="view-td">' + userIntegralDataList[i].scorevalue + '</td>';
		userIntegralTrStr += '</tr>';  
	}
	$("#tbody_userintegral").html(userIntegralTrStr); 
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
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].payfee + '</td>';
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].paydate + '</td>';
		userPayTrStr += '<td class="view-td">' + userPayDataList[i].paychannel + '</td>';
		userPayTrStr += '</tr>';  
	}
	$("#tbody_userpay").html(userPayTrStr); 
}

/**
 * 合约信息
 * @param userActivityDataList
 */
function loadUserActivityDataList(userActivityDataList){
	var userActivityTrStr = '';
	for (var i = 0; i < userActivityDataList.length; i++) {
		userActivityTrStr += '<tr>';
		userActivityTrStr += '<td class="view-td">' + (i+1) + '</td>';
		userActivityTrStr += '<td class="view-td">' + userActivityDataList[i].endTime + '</td>';
		userActivityTrStr += '<td class="view-td">' + userActivityDataList[i].activityName + '</td>';
		/*userActivityTrStr += '<td class="view-td">' + userActivityDataList[i].effectTime + '</td>';
		userActivityTrStr += '<td class="view-td">' + userActivityDataList[i].activityId + '</td>';*/
		userActivityTrStr += '<td class="view-td">' + userActivityDataList[i].effectTimeFmt + '</td>';
		userActivityTrStr += '<td class="view-td">' + userActivityDataList[i].endTimeFmt + '</td>';
		userActivityTrStr += '</tr>';  
	}
	$("#tbody_useractivity").html(userActivityTrStr); 
}