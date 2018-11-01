//分页 设置默认值
var pageSize = '', currentPage = '', showPageNum = '';
pageSize = 10; // 显示页大小
currentPage = 1; // 当前页
showPageNum = 5; // 一次显示的页数
var lineIndex = 0; // 设置行号；
var productId;

$(function() {
	// 点击搜索
	$("#search").on("click", function() {
		$("#head").html("");
		show_bank_Task();
	});
});

// 查询网银任务记录
function show_bank_Task() {

	var bankTaskPage = '', taskList = '';
	var totalElements = ''; // 总记录
	var taskid = '';

	// 应用
	var appId = $('#appId input:radio:checked').val();
	// 环境
	var environmentId = $('#environmentId input:radio:checked').val();
	// 产品
	productId = $('#productId input:radio:checked').val();
	// 开始时间
	var beginTime = $('#datepicker1').val();
	// 结束时间
	var endTime = $('#datepicker2').val();
	var myDate = new Date();//获取系统当前时间
	var nowDate = myDate.toLocaleDateString(); //获取当前日期
	var d3 = new Date(nowDate.replace(/\//g, "\/"));  
	var d1 = new Date(beginTime.replace(/\-/g, "\/"));  
	var d2 = new Date(endTime.replace(/\-/g, "\/"));
	// 任务id
	var taskId = $('#taskId').val();
	// 登录账号
	var loginNumber = $('#loginNumber').val();
	// 用户id
	var userId = $('#userId').val();

	if (appId == "" || appId == undefined || appId == null) {
		alert("请选择对应的应用！");
		return false;  
	}
	if (environmentId == "" || environmentId == undefined
			|| environmentId == null) {
		alert("请选择对应的环境！");
		return false;  
	}
	if (productId == "" || productId == undefined || productId == null) {
		alert("请选择对应的产品！");
		return false;  
	}
	if(d1>d2){  
		alert("开始时间不能大于结束时间！");  
		return false;  
	}
	if(d1>d3){  
		alert("开始时间不能超过当前时间，请重新选择！");  
		return false;  
	}
	if(d2>d3){  
		alert("结束时间不能超过当前时间，请重新选择！");  
		return false;  
	}
	bankTaskPage = searchMobileTask(false, pageSize, currentPage, appId,
			environmentId, productId, beginTime, endTime, taskId, loginNumber,
			userId);

	taskList = bankTaskPage["content"];
	totalElements = bankTaskPage["totalElements"];

	console.log("=====taskList=====" + taskList + "=====totalElements====="
			+ totalElements);

	noKeys = "<tr style='text-align: center;'><td colspan='9'>没有搜索到相关信息！</td></tr>";

	if (totalElements == 0) {
		$("#add").html(noKeys);
		// 分页
		$("#page").html("");
	} else {
		/*if (totalElements < 10) {
		}*/
		// 分页
		//$("#page").html("");
		// 显示首页内容
		bank_productList(taskList);

		// 调用commonSelf_pageStyle
		commonSelf_pageStyle(pageSize, showPageNum, totalElements, "isNull",
				"page", getMobile__pageCallback);
	}
}

// 分页查询运营商task任务
function searchMobileTask(async, pageSize, currentPage, appId, environmentId,
		productId, beginTime, endTime, taskId, loginNumber, userId) {
	
	if (beginTime == "" || beginTime == undefined || beginTime == null) {
	}else{
		beginTime = beginTime+" 00:00:00";
		
	}
	if (endTime == "" || endTime == undefined || endTime == null) {
	}else{
		endTime = endTime+" 23:59:59";
		
	}
	
	var bankTaskPage = '';
	
	$.ajax({
		url : "/opendata/dataCenter/queryByCondition",
		type : "post",
		async : async,
		dataType: "json",
		data : {
			pageSize : pageSize,
			currentPage : currentPage,
			appId : appId,
			environmentId : environmentId,
			productId : productId,
			beginTime : beginTime,
			endTime : endTime,
			taskId : taskId,
			loginNumber : loginNumber,
			userId : userId,
		},
		success : function(data) {
			console.log(data);
			if (!async) {
				bankTaskPage = data;
			}
		},
		error : function() {
			bankTaskPage = "isError";
		}

	});
	if (!async) {
		return bankTaskPage;
	}
}

// 生成运营商任务列表
function bank_productList(taskList) {
	var trs = '';
	var head = '';
	if(productId=="housingfund"){
		//公积金
		head +='<tr>'
		+'<th>任务ID</th>'
		+'<th>登录账号</th>'
		+'<th>地区</th>'
		+'<th>任务时间</th>'
		+'<th>任务描述</th>'
		+'<th>是否完成</th>'
		+'<th>报告时间</th>'
		+'<th>报告描述</th>'
		+'<th>操作</th>'
	    +'</tr>'
		$.each(taskList,function(index, app) {
			var taskid = app.taskid;
			var basicUserHousing = app.basicUserHousing
			var phonenum;
			if (basicUserHousing == "" || basicUserHousing == undefined || basicUserHousing == null) {
				phonenum = "暂无";
			}else{
				phonenum = basicUserHousing.idnum;
			}
			var city = app.city;
			if (city == "" || city == undefined || city == null) {
				city = "暂无";
			}
			var createtime = app.createtime;
			if (createtime == "" || createtime == undefined || createtime == null) {
				createtime = "暂无";
			}
			var description = app.description;
			if (description == "" || description == undefined || description == null) {
				description = "暂无";
			}
			var finished = app.finished;
			if (finished == "" || finished == undefined || finished == null) {
				finished = "暂无";
			}else if(finished == true){
				finished = "完成";
			}else if(finished == false){
				finished = "未完成";
			}
			var report_time = app.reportTime;
			if (report_time == "" || report_time == undefined || report_time == null) {
				report_time = "暂无";
			}else{
				var n = report_time.split("T")
				var m = n[1].split(".")
				report_time = n[0]+" "+m[0];
			}
			var report_status = app.reportStatus;
			if (report_status == "" || report_status == undefined || report_status == null) {
				report_status = "暂无";
			}
			if(report_status == "finished"){
				report_status = "完成";
			}
			//https://blog.csdn.net/qq_35357001/article/details/55505659
			//之前app.basicUserBank得不到，是因为实体中@JsonBackReference和@JsonManagedReference的原因，将ETL中返回过来的实体中把app.basicUserBank忽略了
			trs += '<tr>' + '<td>'
			+ taskid
			+ '</td>'
			+ '<td style="word-wrap:break-word;">'
			+ phonenum
			+ '</td>'
			+ '<td>'
			+ city
			+ '</td>'
			+ '<td>'
			+ createtime
			+ '</td>'
			+ '<td>'
			+ description
			+ '</td>'
			+ '<td>'
			+ finished
			+ '</td>'
			+ '<td>'
			+ report_time
			+ '</td>'
			+ '<td>'
			+ report_status
			+ '</td>'
			+ '<td>'
			+ '<a href="http://10.167.211.155:4322/opendata/docs/carrier/v2/reportdata?taskid='+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-primary btn-xs">报告页面</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/developer/app/carrier/v2/report/'+taskid+'" target="_blank"">'
			+ '<button type="button" class="btn btn-success btn-xs">报告json</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/docs/carrier/v2/rawreportdata?taskid='+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-primary btn-xs">数据页面</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/developer/app/carrier/v2/rawreport/'+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-success btn-xs">数据json</button>'
			+ '</a>' + '</td>' + '</tr>';
			
		});
		
	}
	if(productId=="insurance"){
		//社保
		head +='<tr>'
			+'<th>任务ID</th>'
			+'<th>登录账号</th>'
			+'<th>地区</th>'
			+'<th>任务时间</th>'
			+'<th>任务描述</th>'
			+'<th>是否完成</th>'
			+'<th>报告时间</th>'
			+'<th>报告描述</th>'
			+'<th>操作</th>'
		    +'</tr>'
		$.each(taskList,function(index, app) {
			
			var taskid = app.taskid;
			var basicUserInsurance = app.basicUserInsurance;
			var phonenum;
			if (basicUserInsurance == "" || basicUserInsurance == undefined || basicUserInsurance == null) {
				phonenum = "暂无";
			}else{
				phonenum = basicUserInsurance.idnum;
			}
			
			var city = app.city;
			if (city == "" || city == undefined || city == null) {
				city = "暂无";
			}
			var createtime = app.createtime;
			if (createtime == "" || createtime == undefined || createtime == null) {
				createtime = "暂无";
			}
			var description = app.description;
			if (description == "" || description == undefined || description == null) {
				description = "暂无";
			}
			var finished = app.finished;
			if (finished == "" || finished == undefined || finished == null) {
				finished = "暂无";
			}else if(finished == true){
				finished = "完成";
			}else if(finished == false){
				finished = "未完成";
			}
			var report_time = app.reportTime;
			if (report_time == "" || report_time == undefined || report_time == null) {
				report_time = "暂无";
			}else{
				var n = report_time.split("T")
				var m = n[1].split(".")
				report_time = n[0]+" "+m[0];
			}
			var report_status = app.reportStatus;
			if (report_status == "" || report_status == undefined || report_status == null) {
				report_status = "暂无";
			}
			if(report_status == "finished"){
				report_status = "完成";
			}
			//https://blog.csdn.net/qq_35357001/article/details/55505659
			//之前app.basicUserBank得不到，是因为实体中@JsonBackReference和@JsonManagedReference的原因，将ETL中返回过来的实体中把app.basicUserBank忽略了
			trs += '<tr>' + '<td>'
			+ taskid
			+ '</td>'
			+ '<td style="word-wrap:break-word;">'
			+ phonenum
			+ '</td>'
			+ '<td>'
			+ city
			+ '</td>'
			+ '<td>'
			+ createtime
			+ '</td>'
			+ '<td>'
			+ description
			+ '</td>'
			+ '<td>'
			+ finished
			+ '</td>'
			+ '<td>'
			+ report_time
			+ '</td>'
			+ '<td>'
			+ report_status
			+ '</td>'
			+ '<td>'
			+ '<a href="http://10.167.211.155:4322/opendata/docs/carrier/v2/reportdata?taskid='+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-primary btn-xs">报告页面</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/developer/app/carrier/v2/report/'+taskid+'" target="_blank"">'
			+ '<button type="button" class="btn btn-success btn-xs">报告json</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/docs/carrier/v2/rawreportdata?taskid='+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-primary btn-xs">数据页面</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/developer/app/carrier/v2/rawreport/'+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-success btn-xs">数据json</button>'
			+ '</a>' + '</td>' + '</tr>';
			
		});
		
	}
	if(productId=="carrier"){
	//运营商
		
		head +='<tr>'
			+'<th>任务ID</th>'
			+'<th>登录账号</th>'
			+'<th>运营商</th>'
			+'<th>地区</th>'
			+'<th>任务时间</th>'
			+'<th>任务描述</th>'
			+'<th>是否完成</th>'
			+'<th>报告时间</th>'
			+'<th>报告描述</th>'
			+'<th>操作</th>'
		    +'</tr>'
		
	$.each(taskList,function(index, app) {
		
		var taskid = app.taskid;
		
		var phonenum = app.phonenum;
		if (phonenum == "" || phonenum == undefined || phonenum == null) {
			phonenum = "暂无";
		}
		var carrier = app.carrier;
		if (carrier == "" || carrier == undefined || carrier == null) {
			carrier = "暂无";
		}else if(carrier == "CHINA_TELECOM"){
			carrier = "电信";
		}else if(carrier == "CMCC"){
			carrier = "移动";
			
		}else if(carrier == "UNICOM"){
			carrier = "联通";
		}
		var city = app.city;
		if (city == "" || city == undefined || city == null) {
			city = "暂无";
		}
		var createtime = app.createtime;
		if (createtime == "" || createtime == undefined || createtime == null) {
			createtime = "暂无";
		}
		
		var description = app.description;
		if (description == "" || description == undefined || description == null) {
			description = "暂无";
		}
		var finished = app.finished;
		if (finished == "" || finished == undefined || finished == null) {
			finished = "暂无";
		}else if(finished == true){
			finished = "完成";
		}else if(finished == false){
			finished = "未完成";
		}
		var report_time = app.reportTime;
		if (report_time == "" || report_time == undefined || report_time == null) {
			report_time = "暂无";
		}else{
			var n = report_time.split("T")
			var m = n[1].split(".")
			report_time = n[0]+" "+m[0];
		}
		
		var report_status = app.reportStatus;
		if (report_status == "" || report_status == undefined || report_status == null) {
			report_status = "暂无";
		}
		if(report_status == "finished"){
			report_status = "完成";
		}
		//https://blog.csdn.net/qq_35357001/article/details/55505659
        //之前app.basicUserBank得不到，是因为实体中@JsonBackReference和@JsonManagedReference的原因，将ETL中返回过来的实体中把app.basicUserBank忽略了
						trs += '<tr>' + '<td>'
								+ taskid
								+ '</td>'
								+ '<td style="word-wrap:break-word;">'
								+ phonenum
								+ '</td>'
								+ '<td>'
								+ carrier
								+ '</td>'
								+ '<td>'
								+ city
								+ '</td>'
								+ '<td>'
								+ createtime
								+ '</td>'
								+ '<td>'
								+ description
								+ '</td>'
								+ '<td>'
								+ finished
								+ '</td>'
								+ '<td>'
								+ report_time
								+ '</td>'
								+ '<td>'
								+ report_status
								+ '</td>'
								+ '<td>'
								+ '<a href="/opendata/docs/carrier/v2/reportdata?taskid='+taskid+'" target="_blank">'
								+ '<button type="button" class="btn btn-primary btn-xs">报告页面</button>'
								+ '</a>'
								+ '<a href="/opendata/developer/app/carrier/v2/report/'+taskid+'" target="_blank"">'
								+ '<button type="button" class="btn btn-success btn-xs">报告json</button>'
								+ '</a>'
								+ '<a href="/opendata/docs/carrier/v2/rawreportdata?taskid='+taskid+'" target="_blank">'
								+ '<button type="button" class="btn btn-primary btn-xs">数据页面</button>'
								+ '</a>'
								+ '<a href="/opendata/developer/app/carrier/v2/rawreport/'+taskid+'" target="_blank">'
								+ '<button type="button" class="btn btn-success btn-xs">数据json</button>'
								+ '</a>' + '</td>' + '</tr>';

					});
	
	
	
	}
	if(productId=="bank"){
	    //网银	
		head +='<tr>'
			+'<th>任务ID</th>'
			+'<th>登录账号</th>'
			+'<th>银行</th>'
			+'<th>卡类型</th>'
			+'<th>任务时间</th>'
			+'<th>任务描述</th>'
			+'<th>是否完成</th>'
			+'<th>报告时间</th>'
			+'<th>报告描述</th>'
			+'<th>操作</th>'
		    +'</tr>'
		$.each(taskList,function(index, app) {
			
			var taskid = app.taskid;
			
			var loginName = app.loginName;
			if (loginName == "" || loginName == undefined || loginName == null) {
				loginName = "暂无";
			}
			var bankType = app.bankType;
			if (bankType == "" || bankType == undefined || bankType == null) {
				bankType = "暂无";
			}
			var cardType = app.cardType;
			if (cardType == "" || cardType == undefined || cardType == null) {
				cardType = "暂无";
			}else if(cardType == "DEBIT_CARD"){
				cardType = "储蓄卡";
			}else if(cardType == "CREDIT_CARD"){
				cardType = "信用卡";
			}
			var createtime = app.createtime;
			if (createtime == "" || createtime == undefined || createtime == null) {
				createtime = "暂无";
			}
			var description = app.description;
			if (description == "" || description == undefined || description == null) {
				description = "暂无";
			}
			var finished = app.finished;
			if (finished == "" || finished == undefined || finished == null) {
				finished = "暂无";
			}else if(finished == true){
				finished = "完成";
			}else if(finished == false){
				finished = "未完成";
			}
			var report_time = app.reportTime;
			if (report_time == "" || report_time == undefined || report_time == null) {
				report_time = "暂无";
			}else{
				var n = report_time.split("T")
				var m = n[1].split(".")
				report_time = n[0]+" "+m[0];
			}
			var report_status = app.reportStatus;
			if (report_status == "" || report_status == undefined || report_status == null) {
				report_status = "暂无";
			}
			if(report_status == "finished"){
				report_status = "完成";
			}
			
			
			
			//https://blog.csdn.net/qq_35357001/article/details/55505659
	        //之前app.basicUserBank得不到，是因为实体中@JsonBackReference和@JsonManagedReference的原因，将ETL中返回过来的实体中把app.basicUserBank忽略了
							trs += '<tr>' + '<td>'
									+ taskid
									+ '</td>'
									+ '<td style="word-wrap:break-word;">'
									+ loginName
									+ '</td>'
									+ '<td>'
									+ bankType
									+ '</td>'
									+ '<td>'
									+ cardType
									+ '</td>'
									+ '<td>'
									+ createtime
									+ '</td>'
									+ '<td>'
									+ description
									+ '</td>'
									+ '<td>'
									+ finished
									+ '</td>'
									+ '<td>'
									+ report_time
									+ '</td>'
									+ '<td>'
									+ report_status
									+ '</td>'
									+ '<td>'
									+ '<a href="http://10.167.211.155:4322/opendata/docs/carrier/v2/reportdata?taskid='+taskid+'" target="_blank">'
									+ '<button type="button" class="btn btn-primary btn-xs">报告页面</button>'
									+ '</a>'
									+ '<a href="http://10.167.211.155:4322/opendata/developer/app/carrier/v2/report/'+taskid+'" target="_blank"">'
									+ '<button type="button" class="btn btn-success btn-xs">报告json</button>'
									+ '</a>'
									+ '<a href="http://10.167.211.155:4322/opendata/docs/carrier/v2/rawreportdata?taskid='+taskid+'" target="_blank">'
									+ '<button type="button" class="btn btn-primary btn-xs">数据页面</button>'
									+ '</a>'
									+ '<a href="http://10.167.211.155:4322/opendata/developer/app/carrier/v2/rawreport/'+taskid+'" target="_blank">'
									+ '<button type="button" class="btn btn-success btn-xs">数据json</button>'
									+ '</a>' + '</td>' + '</tr>';
	
						});

	}
	if(productId=="pbccrc-v2"||productId=="pbccrc"){
		//人行征信
		head +='<tr>'
			+'<th>任务ID</th>'
			+'<th>登录账号</th>'
			+'<th>任务时间</th>'
			+'<th>任务描述</th>'
			+'<th>是否完成</th>'
			+'<th>操作</th>'
		    +'</tr>'
		$.each(taskList,function(index, app) {
			
			var taskid = app.taskid;
			
			var username = app.testhtml;
			if(username == "null" || username == undefined || username == null){
				username = "暂无";
			}
			var createtime = app.createtime;
			if (createtime == "" || createtime == undefined || createtime == null) {
				createtime = "暂无";
			}
			var description = app.description;
			if (description == "" || description == undefined || description == null) {
				description = "暂无";
			}
			var finished = app.finished;
			if (finished == "" || finished == undefined || finished == null) {
				finished = "暂无";
			}else if(finished == true){
				finished = "完成";
			}else if(finished == false){
				finished = "未完成";
			}
		
			//https://blog.csdn.net/qq_35357001/article/details/55505659
			//之前app.basicUserBank得不到，是因为实体中@JsonBackReference和@JsonManagedReference的原因，将ETL中返回过来的实体中把app.basicUserBank忽略了
			trs += '<tr>' + '<td>'
			+ taskid
			+ '</td>'
			+ '<td style="word-wrap:break-word;">'
			+ username
			+ '</td>'
			+ '<td>'
			+ createtime
			+ '</td>'
			+ '<td>'
			+ description
			+ '</td>'
			+ '<td>'
			+ finished
			+ '</td>'
			+ '<td>'
			+ '<a href="/opendata/data/pbccrc/reportdetail?taskId='+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-primary btn-xs">报告页面</button>'
			+ '</a>'
			+ '<a href="/opendata/data/pbccrc/report/'+taskid+'" target="_blank"">'
			+ '<button type="button" class="btn btn-success btn-xs">报告json</button>'
			+ '</a>'
			+ '</td>' + '</tr>';
			
		});
		
	}
	
	if(productId=="e_commerce"){
		//电商
		head +='<tr>'
			+'<th>任务ID</th>'
			+'<th>登录账号</th>'
			+'<th>类别</th>'
			+'<th>任务时间</th>'
			+'<th>任务描述</th>'
			+'<th>是否完成</th>'
			+'<th>报告时间</th>'
			+'<th>报告描述</th>'
			+'<th>操作</th>'
		    +'</tr>'
		$.each(taskList,function(index, app) {
			
			var taskid = app.taskid;
			var loginName = app.loginName;
			if (loginName == "" || loginName == undefined || loginName == null) {
				loginName = "暂无";
			}
			var websiteType = app.websiteType;
			if (websiteType == "" || websiteType == undefined || websiteType == null) {
				websiteType = "暂无";
			}else{
				if(websiteType == "tb"){
					websiteType = "淘宝";
				}
				if(websiteType == "sn_num"){
					websiteType = "苏宁";
				}
				if(websiteType == "jd"){
					websiteType = "京东";
				}
				if(websiteType == "elemo"){
					websiteType = "饿了么";
				}
			}
			var createtime = app.createtime;
			if (createtime == "" || createtime == undefined || createtime == null) {
				createtime = "暂无";
			}
			var description = app.description;
			if (description == "" || description == undefined || description == null) {
				description = "暂无";
			}
			var finished = app.finished;
			if (finished == "" || finished == undefined || finished == null) {
				finished = "暂无";
			}else if(finished == true){
				finished = "完成";
			}else if(finished == false){
				finished = "未完成";
			}
			var report_time = app.reportTime;
			if (report_time == "" || report_time == undefined || report_time == null) {
				report_time = "暂无";
			}else{
				var n = report_time.split("T")
				var m = n[1].split(".")
				report_time = n[0]+" "+m[0];
			}
			var report_status = app.reportStatus;
			if (report_status == "" || report_status == undefined || report_status == null) {
				report_status = "暂无";
			}
			if(report_status == "finished"){
				report_status = "完成";
			}
			//https://blog.csdn.net/qq_35357001/article/details/55505659
			//之前app.basicUserBank得不到，是因为实体中@JsonBackReference和@JsonManagedReference的原因，将ETL中返回过来的实体中把app.basicUserBank忽略了
			trs += '<tr>' + '<td>'
			+ taskid
			+ '</td>'
			+ '<td style="word-wrap:break-word;">'
			+ loginName
			+ '</td>'
			+ '<td>'
			+ websiteType
			+ '</td>'
			+ '<td>'
			+ createtime
			+ '</td>'
			+ '<td>'
			+ description
			+ '</td>'
			+ '<td>'
			+ finished
			+ '</td>'
			+ '<td>'
			+ report_time
			+ '</td>'
			+ '<td>'
			+ report_status
			+ '</td>'
			+ '<td>'
			+ '<a href="http://10.167.211.155:4322/opendata/docs/carrier/v2/reportdata?taskid='+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-primary btn-xs">报告页面</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/developer/app/carrier/v2/report/'+taskid+'" target="_blank"">'
			+ '<button type="button" class="btn btn-success btn-xs">报告json</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/docs/carrier/v2/rawreportdata?taskid='+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-primary btn-xs">数据页面</button>'
			+ '</a>'
			+ '<a href="http://10.167.211.155:4322/opendata/developer/app/carrier/v2/rawreport/'+taskid+'" target="_blank">'
			+ '<button type="button" class="btn btn-success btn-xs">数据json</button>'
			+ '</a>' + '</td>' + '</tr>';
			
		});
		
	}
	
	
	$("#add").html(trs);
	$("#head").html(head);
}

// 分页回调函数
function getMobile__pageCallback() {

	var currentPage_content = '', bankTaskPage = '';

	// 应用
	var appId = $('#appId input:radio:checked').val();
	// 环境
	var environmentId = $('#environmentId input:radio:checked').val();
	// 产品
	var productId = $('#productId input:radio:checked').val();
	// 开始时间
	var beginTime = $('#datepicker1').val();
	// 结束时间
	var endTime = $('#datepicker2').val();
	// 任务id
	var taskId = $('#taskId').val();
	// 登录账号
	var loginNumber = $('#loginNumber').val();
	// 用户id
	var userId = $('#userId').val();

	// 获取当前页
	currentPage = parseInt($("#page #currentPage").text());

	bankTaskPage = searchMobileTask(false, pageSize, currentPage, appId,
			environmentId, productId, beginTime, endTime, taskId, loginNumber,
			userId);

	currentPage_content = bankTaskPage["content"];

	// console.log("=====currentPage_content=====" + currentPage_content);

	// 生成查询结果列表
	bank_productList(currentPage_content);

}
