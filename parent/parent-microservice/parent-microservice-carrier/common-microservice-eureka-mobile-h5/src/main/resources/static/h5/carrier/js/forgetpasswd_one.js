//定义全局变量
var flag = false; //图片验证码验证登录成功（忘记密码第一步）
var count = 0;
var errorCount = 0;

/**
 * 触发密码变更接口
 */
function changePassword(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/unicom/password/changePassword",
        data:$('#resetPwdForm').serialize(),// 你的formid
        async: true,
        dataType : "json",
        error: function(request) {
            console.log("error");
            //$('#message').text('出错了...');
            clearInterval(interval); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}

/**
 * 间隔一秒调用获取task状态的方法 (下一步)
 */
function interval() {
    count = 0;
    errorCount = 0;
    var interval = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var taskid = $("#task_id").val();
        if (!onLine) {
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                /*//跳转到第一个页面
                $("#megBtn").click( function() {
                    window.history.go(-4);
                });*/
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
                console.log(data.description);
                console.log(data.phase + " ,"+ data.phase_status);

                if(data.description != null && data.description != "") {
                    if (appActive == "prod" && (data.description.indexOf("采集中") > 0)){
                        $('#message').text("数据采集中，请耐心等待...");
                    } else {
                        $('#message').text(data.description); //反馈信息
                    }
                }
                //下一步
                /*if (data.phase == "PASSWORD" && data.phase_status == "DOING") {//账号验证中。。。
                    $('#message').text(data.description);

                } else*/
                if (data.phase == "WAIT_CODE" && data.phase_status == "FAIL") {//短信验证码验证失败!
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                }if (data.phase == "VALIDATE" && data.phase_status == "FAIL") {//短信验证码验证失败!
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } else if (data.phase == "PASSWORD" && data.phase_status == "SUCCESS") {//账号验证成功！
                    $('#message').text(data.description);
                    changePassword();

                } else if (data.phase == "PASSWORD" && data.phase_status == "FAIL") {//账号验证失败!
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } /*else if (data.phase == "PASSWORD_CHANGE" && data.phase_status == "DOING") {//密码修改中。。。
                    $('#message').text(data.description);

                } */else if (data.phase == "PASSWORD_CHANGE" && data.phase_status == "FAIL") {//密码修改失败!
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } else if (data.phase == "PASSWORD_CHANGE" && data.phase_status == "SUCCESS") {//密码修改成功！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    /*$('#msg').modal('show');
                    $('#alertMsg').text(data.description);*/
                    //window.location.href="/static/cmcc/authTwo?themeColor="+themeColor+"&isTopHide="+isTopHide+"&phoneNum=" + phoneNum  + "&task_id=" + taskid;
                    $("#resetPwdForm").attr("action","/h5/carrier/check");
                    $("#resetPwdForm").attr("method","POST");
                    $("#resetPwdForm").submit();
                    return;

                } else if(data.phase == "LOGIN" && data.phase_status == "FAIL"){
                    clearInterval(interval); //停止间隔一秒调用
                    $('#msg').modal('show');
                    //$('#alertMsg').text('图片验证码验证失败！');
                    $('#alertMsg').text(data.description);
                    return;
                }
            }
        });
    }, 1000);
}

/**
 *触发验证短信随机码接口
 */
function nextSubmit(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/unicom/password/verifiCode",
        data:$('#resetPwdForm').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
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
 * 间隔一秒调用获取task状态的方法 (发送验证码)
 *
 */
function intervalSendCode() {
    count = 0;
    errorCount = 0;
    var intervalSendCode = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var taskid = $("#task_id").val();
        if (!onLine) {
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(intervalSendCode); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                /*$("#megBtn").click( function() {
                    window.history.go(-4);
                });*/
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
                console.log("intervalSendCode.error, online=" + onLine+", errorCount=" + errorCount);
                if (errorCount > 10) {
                    $('#myModal').modal('hide');
                    clearInterval(intervalSendCode); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text("网络超时请重试！");
                }
                errorCount ++;
                return;
            },
            success: function(data) {
                console.log(data.description);
                console.log(data.phase + " ,"+ data.phase_status);
                if (data.phase == "PASSWORD" && data.phase_status == "FAIL") {//账号验证失败！24小时内发送5次信息！、账号验证失败！图片验证码错误
                    clearInterval(intervalSendCode); //停止间隔一秒调用
                    //$('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } else if (data.phase == "SEND_CODE" && data.phase_status == "FAIL") {//短信验证码发送失败
                    clearInterval(intervalSendCode); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);
                    return;

                } else if (data.phase == "WAIT_CODE" && data.phase_status == "SUCCESS") {// 短信验证码已发送，请注意查收
                    clearInterval(intervalSendCode); //停止短信验证码调用的间隔一秒获取task状态
                    return;
                }
            }
        });
    }, 1000);
}

/**
 * 发送短信随机码
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
    /*console.log("flag="+flag);
    if (!flag) {
        $('#msg').modal('show');
        $('#alertMsg').text('图片验证码验证失败！');
        return;
    }*/
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
            url:"/h5/carrier/unicom/password/sendCode",
            data:$('#resetPwdForm').serialize(),// 你的formid
            async: false,
            dataType : "json",
            beforeSend: function () {
                btn.attr('disabled', 'disabled');
                intervalSendCode();
            },
            error: function(request) {
                console.log("error");
                // ** 重要：因为发送失败，所以要恢复发送按钮的监听 **
                bindBtn();
                btn.attr('disabled', false);
                clearInterval(intervalSendCode); //停止间隔一秒调用

                $('#msg').modal('show');
                $('#alertMsg').text('验证码发送失败！');
            },
            success: function(data) {
                console.log(data);
                //成功
                timeLeft = SEND_INTERVAL;
                timeCount();
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
            btn.text("发送短信随机码");
            btn.attr('disabled', false);
            bindBtn();
        }
    }, 1000);
};

/**
 * 点击发送短信验证码 (图片验证码认证登录成功之后)
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
    var certNum = $('#certNum').val().trim(); //身份证后6位
    var mobileMsg = $('#mobileMsg').val().trim(); //短信验证码
    var newpassword = $('#newpassword').val().trim(); //新密码

    var certNumRegExp =/^\d{5}(\d|X|x)$/;
    if(!certNumRegExp.test(certNum)){
        $('#msg').modal('show');
        $('#alertMsg').text('身份证号码输入有误');
        return;
    }
    var mobileMsgRegExp=/^\d{6}$/;
    if(!mobileMsgRegExp.test(mobileMsg)){
        $('#msg').modal('show');
        $('#alertMsg').text('请输入6位数字的短信验证码');
        return;
    }
    var pwdRegExp1=/^\d{6}$/; //六位数字
    if(!pwdRegExp1.test(newpassword)){
        $('#msg').modal('show');
        $('#alertMsg').text('请输入6位数字的新密码');
        return;
    }
    /*var pwdRegExp2 = /^\d{6,}$/; //连续
    if(pwdRegExp2.test(newpassword)){
        $('#msg').modal('show');
        $('#alertMsg').text('密码设置有误');
        return;
    }*/

    nextSubmit();
    interval();
});
//表单验证
function verifyForm() {
    var certNum = $('#certNum').val().trim(); //身份证后6位
    var mobileMsg = $('#mobileMsg').val().trim(); //短信验证码
    var newpassword = $('#newpassword').val().trim(); //新密码

    if ((certNum == null || certNum == "") ||
        (mobileMsg == null || mobileMsg == "") ||
        (newpassword == null || newpassword == "")) {
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