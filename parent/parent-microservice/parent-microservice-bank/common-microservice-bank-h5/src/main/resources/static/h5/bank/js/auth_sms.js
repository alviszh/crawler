/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if($('#bankType').val()=="招商银行"||$('#bankType').val()=="浦发银行"){
        smsVerfiyD(); //触发爬虫
    }
    else if($('#bankType').val()=="渤海银行"&& $(".qaspan").length > 0){
        nextFirstV()
    }
    else if($('#bankType').val()=="渤海银行"&& $(".qaspan").length == 0){
        nextSec()
    }
    //中信credit
    else if($('#bankType').val()=="中信银行"&& $(".qaspan").length == 0){
    	nextLoginD()
    }
    else{
        nextSendSmsCodeD()
    }
});

//中信credit
$('#sendSmsBtn').click( function() {
	if($('#loginCiticName').val()!=""){
		if((/^[1][3,4,5,7,8][0-9]{9}$/.test($('#loginCiticName').val()))){
			
			alert($('#bankType').val());
			$('#bitian').text("");
			nextSendSmsCodeD(); //点击发送验证码
		}else{
			$('#bitian').text("手机号输入有误");
		}
	}
	else{
		$('#bitian').text("手机号不能为空");
	}
});

//表单验证
function verifyForm() {
    var sms_code = $('#sms_code').val().trim();//姓名
    if (sms_code == null || sms_code == "") {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    verifyForm();
});

$(function(){
    if($('#bankType').val()=="招商银行"||$('#bankType').val()=="浦发银行"){
        nextSendSmsCodeD();
    }
    if($('#bankType').val()=="兴业银行"){
        $("#pwd").show().find('input').removeAttr("disabled");
    }
    if($('#bankType').val()=="中信银行"){
        var interval_sendSmsD = setInterval(function (){
        $.ajax({
            type: "POST",
            url:"/h5/bank/tasks/status",
            data:{
                taskid:  $("#taskid").val(),
            },
            async: false,
            dataType : "json",
            beforeSend: function () {
                $('#message').text('正在发送...');
                $('#myModal').modal('show');
            },
            error: function(request) {
                console.log("error");
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                if ((data.phase == "LOGIN")&& (data.phase_status == "ERROR")) {
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }
                if ((data.phase == "SEND_CODE")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//爬取失败
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }

                if ((data.phase == "WAIT_CODE")&& (data.phase_status == "SUCCESS")) {
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                }
                if (data.phase == "VALIDATE" && data.phase_status == "SUCCESS") {//验证成功！
                    console.log("验证成功");
                    // $('#myModal').modal('hide');
                    // $('#smsCode').modal('show');
                    if(bankType=="中信银行" && cardType =="CREDIT_CARD"){
                        console.log("中信crawler");
//                        crawlerD(); //调用数据采集接口
                        clearInterval(interval_sendSmsD);

                        var taskid = $("#taskid").val();
                        var bankType = $("#bankType").val();
                        var loginName = $("input[name='loginName']").not(':disabled').val();
                        var cardType = $("#cardType").val();
                        var sms_code = $("#sms_code").val();
                        window.location.href = "/h5/bank/login?taskid=" + taskid + "&bankType=" + bankType
                        + "&loginName=" + loginName + "&cardType=" + cardType + "&loginType=" + loginType + "&sms_code=" + sms_code
                    }
                    return;
                }
                if (data.phase == "VALIDATE" && (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }
                if ((data.phase == "CRAWLER")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//爬取失败
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }

                if (data.finished && data.phase == "CRAWLER") { //数据采集成功
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    result(taskid);
                    $('#success_modal').modal('show');
                    return;
                }
                if(data.finished && data.phase != "CRAWLER"){
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text("系统超时，请重新登录");
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }
            }
        })
        }, 1000);
    }

});