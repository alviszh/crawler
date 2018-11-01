/*主题色*/
var themeColor = $("#themeColor").val();
var count = 0;
var errorCount = 0;

/**
 * 发送验证码
 * @type {*|jQuery|HTMLElement}
 */
// 定义按钮btn
var btn;
// 定义发送时间间隔(s)
var SEND_INTERVAL = 60;
var timeLeft = SEND_INTERVAL;

/**
 * 触发数据抓取接口(中国移动)
 */
function cmcc_getAllData(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/cmcc/getAllData",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        error: function(request) {
            console.log("error");
            //$('#message').text('出错了...');
            clearInterval(interval); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data);
            return;
        }
    });
}
/**
 * 间隔一秒调用获取task状态的方法
 */
function interval() {
    count = 0;
    errorCount = 0;
    var interval = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        if (!onLine) {
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                $("#megBtn").click( function() {
                    window.history.go(-4);
                });
            }
            count ++;
            return;
        }
        var taskid = $("#task_id").val();
        $.ajax({
            type: "GET",
            url:"/h5/carrier/tasks/status",
            data:{
                taskid: taskid
            },
            dataType : "json",
            error: function(request) {
                console.log("interval.error, online=" + onLine+",errorCount=" +errorCount);
                 if (errorCount > 10) {
                 $('#myModal').modal('hide');
                 clearInterval(interval); //停止间隔一秒调用
                 //显示错误信息
                 $('#msg').modal('show');
                 $('#alertMsg').text("网络超时请重试！");
                 }
                errorCount ++;
                return;
            },
            success: function(data) {
                var phoneNum = data.phonenum;
                console.log("**"+phoneNum);
                console.log(data.phase +","+data.phase_status);
                console.log(data.description);
                console.log("*********" + data.finished);

                if(data.description != null && data.description != "") {
                    if (appActive == "prod" && (data.description.indexOf("采集中") > 0)){
                        $('#message').text("数据采集中，请耐心等待...");
                    } else {
                        $('#message').text(data.description); //反馈信息
                    }
                }
                if (data.phase == "VALIDATE" && data.phase_status == "SUCCESS") {//二次验证成功！
                    cmcc_getAllData(); //调用数据采集接口

                } else if (data.phase == "VALIDATE" && data.phase_status == "FAIL") {//二次验证失败！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);

                    $("#megBtn").click( function() {
                        $('#sms_code').val("");//清空验证码输入框
                    });
                    return;
                    //} else if (data.finished == "true" && data.phase_status == "SUCCESS") { //数据采集成功
                } else if (data.phase == "WAIT_CODE" && data.phase_status == "FAIL") {//短信验证码验证失败!
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);

                    $("#megBtn").click( function() {
                        $('#sms_code').val("");//清空验证码输入框
                    });
                    return;

                } else if (data.phase == "CRAWLER" && data.phase_status == "FAIL") {//数据采集失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);
                    return;
                } else if (data.phase == "LOGIN" && data.phase_status == "INVALID") {//当前登录已失效，请重新登录！
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);

                    $("#megBtn").click( function() {
                        console.log("themeColor:" + themeColor);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        var key = $("#key").val();
                        var redirectUrl = $("#redirectUrl").val();
                        var owner = $("#owner").val();
                        //实名验证失败，跳转到第一个页面
                        window.location.href="/h5/carrier/auth?owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+"&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });
                    return;
                }

                if (data.finished) { //数据采集成功
                    if (data.phase == "CRAWLER" && data.phase_status == "SUCCESS") {
                        clearInterval(interval); //停止间隔一秒调用
                        $('#myModal').modal('hide');
                        if (themeColor.indexOf("#") != -1) {
                            themeColor = themeColor.substr(1, themeColor.length);
                        }
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        result(taskid);
                        $('#success_modal').modal('show');
                        //window.location.href="/h5/success?themeColor="+themeColor+"&isTopHide="+isTopHide+"&taskid=" + taskid;
                        return;
                    }
                }
            }
        });
    }, 1000);
}


/**
 * 发送第二次短信
 * 间隔一秒调用获取task状态的方法
 */
function interval_send() {
    count = 0;
    errorCount = 0;
    var interval_send = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        if (!onLine) {
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval_send); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                $("#megBtn").click( function() {
                    window.history.go(-4);
                });
            }
            count ++;
            return;
        }
        var taskid = $("#task_id").val();
        $.ajax({
            type: "GET",
            url:"/h5/carrier/tasks/status",
            data:{
                taskid: taskid
            },
            dataType : "json",
            error: function(request) {
                console.log("interval_send.error, online=" + onLine+",errorCount="+errorCount);
                if (errorCount > 10) {
                    $('#myModal').modal('hide');
                    clearInterval(interval_send); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text("网络超时请重试！");
                }
                errorCount ++;
                return;
            },
            success: function(data) {
                var phoneNum = data.phonenum;
                console.log("**"+phoneNum);
                console.log(data.phase +","+data.phase_status);
                console.log("description:"+data.description);

                //反馈信息
                $('#message').text(data.description);

                if (data.phase == "WAIT_CODE_SECOND" && data.phase_status == "SUCCESS") {
                    //成功
                    timeLeft = SEND_INTERVAL;
                    timeCount();
                    $('#myModal').modal('hide');
                    clearInterval(interval_send); //停止间隔一秒调用
                    return;

                } else if (data.phase == "WAIT_CODE_SECOND" && data.phase_status == "FAIL") {
                    // ** 重要：因为发送失败，所以要恢复发送按钮的监听 **
                    bindBtn();
                    btn.attr('disabled', false);
                    $('#myModal').modal('hide');
                    clearInterval(interval_send); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;


                } else if (data.phase == "LOGIN" && data.phase_status == "NEED") {//登录已超时，请先登录！
                    // ** 重要：因为发送失败，所以要恢复发送按钮的监听 **
                    bindBtn();
                    btn.attr('disabled', false);
                    $('#myModal').modal('hide');
                    clearInterval(interval_send); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);

                    $("#megBtn").click( function() {
                        console.log("themeColor:" + themeColor);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        var key = $("#key").val();
                        var redirectUrl = $("#redirectUrl").val();
                        var owner = $("#owner").val();
                        //跳转到第一个页面
                        window.location.href="/h5/carrier/auth?owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+"&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });
                    return;
                }
            }
        });
    }, 1000);
}

/**
 * 移动认证（触发第二次认证接口）
 */
function nextSubmit(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/cmcc/loginTwo",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            $('#myModal').modal('hide');
            console.log("error");
            $('#msg').modal('show');
            $('#alertMsg').text('二次短信认证超时，请尝试重新认证！');
        },
        success: function(data) {
            interval();
            console.log(data);
            return;
        }
    });
}

/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if (!onLine) {
        $('#msg').modal('show');
        $('#alertMsg').text(errInfo);
        return;
    }
    var smsCode = $('#sms_code').val().trim();
    var regExp=/^\d{6}$/;
    if(!regExp.test(smsCode)){
        $('#msg').modal('show');
        $('#alertMsg').text('请输入6位数字的短信验证码');
        return;
    }else{
        nextSubmit();
    }
});
//表单验证
function verifyForm() {
    var smsCode = $('#sms_code').val().trim();
    if (smsCode == null || smsCode == "") {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}
/**
 * 表单改变事件
 */
$('#sms_code').bind('input propertychange', function() {
    verifyForm();
});

/**
 * 重新发送计时
 **/
var timeCount = function() {
    btn.text(timeLeft + "s后重发");
    window.setTimeout(function() {
        if(timeLeft > 1) {
            timeLeft -= 1;
            btn.text(timeLeft + "s后重发");
            timeCount();
        } else {
            btn.text("发送短信验证码");
            btn.attr('disabled', false);
            bindBtn();
        }
    }, 1000);
};

/**
 * 发送短信
 * 绑定btn按钮的监听事件
 */
var bindBtn = function(){
    btn.click(function(){
        if (!onLine) {
            $('#msg').modal('show');
            $('#alertMsg').text(errInfo);
            return;
        }
        // 需要先禁用按钮（为防止用户重复点击）
        btn.unbind('click');
        $.ajax({
            type: "POST",
            url:"/h5/carrier/cmcc/sendCodeTwo",
            data:$('#authform').serialize(),// 你的formid
            async: false,
            dataType : "json",
            beforeSend: function () {
                btn.attr('disabled', 'disabled');
                $('#message').text("开始准备...");
                $('#myModal').modal('show');
            },
            error: function(request) {
                $('#message').text("短信验证码发送超时！");
                console.log("error");
                // ** 重要：因为发送失败，所以要恢复发送按钮的监听 **
                bindBtn();
                btn.attr('disabled', false);
                $('#myModal').modal('hide');
                clearInterval(interval_send);

                $('#msg').modal('show');
                $('#alertMsg').text('短信验证码发送超时，请尝试重新发送！');
            },
            success: function(data) {
                console.log(data);
                interval_send();
                //$('#message').text("二次验证短信验证码发送中，请注意查收"); //反馈信息

            }
        })
    })
};

/**
 * 点击发送登录短信验证码
 * （第一次短信认证）
 */
btn = $("#sendCodeBtn");
btn.click(bindBtn());

$(function(){
    verifyForm();

    //显示提示信息
    $('#msg').modal('show');
    $('#alertMsg').text("请进行第二次短信验证!");

});

