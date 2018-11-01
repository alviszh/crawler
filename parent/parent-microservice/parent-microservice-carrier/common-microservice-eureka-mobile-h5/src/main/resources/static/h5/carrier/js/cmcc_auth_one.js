var themeColor = $("#themeColor").val();
var count = 0;
var errorCount = 0;
/**
 * 间隔一秒调用获取task状态的方法
 */
function interval() {
    count = 0;
    errorCount = 0;
    var interval = setInterval(function (){
        console.log("********** "+$("#task_id").val()+", online=" + onLine);
        var taskid = $("#task_id").val();
        var password = $("#password").val();
        var key = $("#key").val();
        var redirectUrl = $("#redirectUrl").val();
        var owner = $("#owner").val();
        if (!onLine) {
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                $("#megBtn").click( function() {
                    window.history.go(-2);
                });
            }
            count ++;
        }
        $.ajax({
            type: "GET",
            url:"/h5/carrier/tasks/status",
            data:{
                taskid: taskid
            },
            dataType : "json",
            error: function(request) {
                console.log("interval.error, online=" + onLine+", errorCount=" + errorCount);
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
                console.log(data.description);

                if(data.description != null && data.description != "") {
                    if (appActive == "prod" && (data.description.indexOf("采集中") > 0)){
                        $('#message').text("数据采集中，请耐心等待...");
                    } else {
                        $('#message').text(data.description); //反馈信息
                    }
                }
                if (data.phase == "READY" && data.phase_status == "FAIL") {//开始准备
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);
                    return;
                } /*else if (data.phase == "LOGIN" && data.phase_status == "DOING") {//正在认证，请耐心等待...
                 $('#message').text('正在认证，请耐心等待...');

                 }*/ else if (data.phase == "LOGIN" && data.phase_status == "SUCCESS") {//认证成功！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    console.log("login=="+themeColor);
                    if (themeColor.indexOf("#") != -1) {
                        themeColor = themeColor.substr(1, themeColor.length);
                    }
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    window.location.href="/h5/carrier/cmcc/authTwo?owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                        +"&themeColor="+themeColor+"&isTopHide="+isTopHide+"&phoneNum=" + phoneNum  + "&task_id=" + taskid + "&password=" + password;
                    return;

                }  else if (data.phase == "LOGIN" && data.phase_status == "FAIL") {//认证失败！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);

                    $("#megBtn").click( function() {
                        $('#sms_code').val("");//清空验证码输入框
                    });
                    return;

                } else if (data.phase == "SEND_CODE" && data.phase_status == "FAIL") {//短信验证码发送失败！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);
                    return;

                } else if (data.phase == "WAIT_CODE" && data.phase_status == "FAIL") {//短信验证码验证失败!
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);

                    $("#megBtn").click( function() {
                        $('#sms_code').val("");//清空验证码输入框
                    });
                    return;

                }else if (data.phase == "LOGIN" && data.phase_status == "FAIL" && data.error_code == "1014") {//重复登录！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    if (themeColor.indexOf("#") != -1) {
                        themeColor = themeColor.substr(1, themeColor.length);
                    }
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    window.location.href="/h5/carrier/cmcc/authTwo?owner="+owner +"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                        + "&themeColor="+themeColor+"&isTopHide="+isTopHide+"&phoneNum=" + phoneNum  + "&task_id=" + taskid
                        + "&password=" + password;
                    return;

                }
            }
        });
    }, 1000);
}

/**
 * 移动认证（触发第一次认证接口）
 * 登录
 */
function nextSubmit(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/cmcc/login",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            $('#myModal').modal('hide');
            console.log('error:'+XMLHttpRequest.status); 
            console.log('error:'+XMLHttpRequest.readyState);
            console.log('error:'+textStatus);
            console.log('error:'+errorThrown);
            $('#msg').modal('show');
            $('#alertMsg').text('认证超时，请尝试重新认证！');
        },
        success: function(data) {
            interval();
            console.log(data);
            return;
        }
    });
}

/**
 * 点击忘记密码
 */
$("#forgetPwdBtn").click(function(){
    $("#authform").attr("action","/h5/carrier/forgetpasswd");
    $("#authform").attr("method","POST");
    $("#authform").submit();
});

/**
* 发送验证码
* @type {*|jQuery|HTMLElement}
*/
// 定义按钮btn
var btn = $("#sendCodeBtn");

// 定义发送时间间隔(s)
var SEND_INTERVAL = 60;
var timeLeft = SEND_INTERVAL;

/**
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
            url:"/h5/carrier/cmcc/sendCode",
            data:$('#authform').serialize(),// 你的formid
            async: false,
            dataType : "json",
            beforeSend: function () {
                btn.attr('disabled', 'disabled');
            },
            error: function(request) {
                console.log("error");
                // ** 重要：因为发送失败，所以要恢复发送按钮的监听 **
                bindBtn();
                btn.attr('disabled', false);

                $('#msg').modal('show');
                $('#alertMsg').text('短信验证码发送超时，请尝试重新发送！');
            },
            success: function(data) {
                console.log(data);
                //成功
                timeLeft = SEND_INTERVAL;
                timeCount();
                if (data.error_code != 0) {
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);
                }
            }
        })
    })
};

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
 * 点击发送登录短信验证码
 * （第一次短信认证）
 */
btn.click(bindBtn());

/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if (!onLine) {
        $('#msg').modal('show');
        $('#alertMsg').text(errInfo);
        return;
    }
    var smsCode = $('#sms_code').val().trim(); //短信验证码
    console.log(smsCode);
    var regExp=/^\d{6}$/;
    if(!regExp.test(smsCode)){
        $('#msg').modal('show');
        $('#alertMsg').text('请输入6位数字的短信验证码');
        return;
    }
    nextSubmit();
});
//表单验证
function verifyForm() {
    var password = $('#password').val().trim(); //服务密码
    var smsCode = $('#sms_code').val().trim(); //短信验证码
    if ((smsCode == null || smsCode == "") ||
        (password == null || password == "")) {
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
    verifyForm();
});