var province;
var count = 0;
var errorCount = 0;
/**
 * 间隔一秒调用获取task状态的方法
 */
function interval_send() {
    count = 0;
    errorCount = 0;
    var interval_send = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var taskid = $("#task_id").val();
        var key = $("#key").val();
        var redirectUrl = $("#redirectUrl").val();
        var owner = $("#owner").val();
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
        }
        $.ajax({
            type: "GET",
            url:"/h5/carrier/tasks/status",
            data:{
                taskid: taskid
            },
            dataType : "json",
            error: function(request) {
                console.log("interval_send.error, online=" + onLine+", errorCount=" + errorCount);
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

                if(data.description != null && data.description != "") {
                    if (appActive == "prod" && (data.description.indexOf("采集中") > 0)){
                        $('#message').text("数据采集中，请耐心等待...");
                    } else {
                        $('#message').text(data.description); //反馈信息
                    }
                }
                console.log(data.description);
                console.log(data.phase +","+data.phase_status);

                if (data.phase == "SEND_CODE" && data.phase_status == "FAIL") {//短信验证码发送失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval_send); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } else if (data.phase == "WAIT_CODE" && data.phase_status == "SUCCESS") {// 短信验证码已发送，请注意查收
                    $('#myModal').modal('hide');
                    clearInterval(interval_send); //停止间隔一秒调用
                    return;
                } else if (data.phase == "LOGIN" && data.phase_status == "ERROR") {//登陆失败，联通有点小波动，请稍后再试!
                    clearInterval(interval_send); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);

                    $("#megBtn").click( function() {
                        console.log("themeColor:" + themeColor);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        //实名验证失败，跳转到第一个页面
                        window.location.href="/h5/carrier/auth?owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+
                            "&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });
                    return;
                }
            }
        });
    }, 1000);
}

/**
 * 间隔一秒调用获取task状态的方法
 */
function interval_ver() {
    count = 0;
    errorCount = 0;
    var interval_ver = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var taskid = $("#task_id").val();
        var key = $("#key").val();
        var redirectUrl = $("#redirectUrl").val();
        var owner = $("#owner").val();
        if (!onLine) {
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval_ver); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                $("#megBtn").click( function() {
                    window.history.go(-4);
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
                console.log("interval_ver.error, online=" + onLine+", errorCount=" + errorCount);
                if (errorCount > 10) {
                    $('#myModal').modal('hide');
                    clearInterval(interval_ver); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text("网络超时请重试！");
                }
                errorCount ++;
                return;
            },
            success: function(data) {

                if(data.description != null && data.description != "") {
                    if (appActive == "prod" && (data.description.indexOf("采集中") > 0)){
                        $('#message').text("数据采集中，请耐心等待...");
                    } else {
                        $('#message').text(data.description); //反馈信息
                    }
                }
                console.log(data.description);
                console.log(data.phase +","+data.phase_status);

                if (data.phase == "READY_CODE_SECOND" && data.phase_status == "DONING" ) {//短信验证码验证成功， 跳转到第二个短信验证码页面
                    //unicom();
                    clearInterval(interval_ver); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    var password = $("#password").val();
                    var user_id = $("#user_id").val();
                    var key = $("#key").val();
                    var owner = $("#owner").val();
                    var redirectUrl = $("#redirectUrl").val();
                    console.log("redirectUrl="+redirectUrl);
                    console.log("************");
                    window.location.href="/h5/carrier/unicom/unicomVerifTwo?phoneNum=" + data.phonenum + "&owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+"&themeColor="+themeColor
                        +"&isTopHide="+isTopHide+"&task_id=" + taskid + "&user_id=" + user_id +  "&password=" + password;
                    return;

                } else if (data.phase == "CRAWLER" && data.phase_status == "FAIL") {//数据采集失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval_ver); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                } else if (data.phase == "WAIT_CODE" && data.phase_status == "FAIL") {//短信验证码验证失败!
                    clearInterval(interval_ver); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } else if ((data.phase == "LOGIN" && data.phase_status == "ERROR") || //登陆失败，联通有点小波动，请稍后再试!
                    (data.phase == "LOGIN" && data.phase_status == "FAIL") ) {//密码错误
                    clearInterval(interval_ver); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);

                    $("#megBtn").click( function() {
                        console.log("themeColor:" + themeColor);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        //实名验证失败，跳转到第一个页面
                        window.location.href="/h5/carrier/auth?owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+
                            "&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });
                    return;

                } else if (data.phase == "AGENT" && data.phase_status == "ERROR") {//系统繁忙，请稍后再试（没有闲置可用的实例）
                    $('#myModal').modal('hide');
                    clearInterval(interval_ver); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                }

                if (data.finished && (data.phase == "CRAWLER" && data.phase_status == "SUCCESS")) { //数据采集成功
                    $('#myModal').modal('hide');
                    clearInterval(interval_ver); //停止间隔一秒调用
                    themeColor = themeColor.substr(1,themeColor.length);
                    result(taskid);
                    $('#success_modal').modal('show');
                    return;
                }
            }
        });
    }, 1000);
}
/**
 * 提交动态验证码（触发爬虫爬取电信通话详单）
 */
function nextSubmit(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/unicom/verifiCode",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#myModal').modal('show');
            $('#message').text('开始准备');
        },
        error: function(request) {
            console.log("error");
            $('#myModal').modal('hide');
            clearInterval(interval_ver); //停止间隔一秒调用
            $('#message').text('短信验证码验证失败');
        },
        success: function(data) {
            console.log(data.description);
            interval_ver();
        }
    });
}

//表单验证
function verifyForm() {
    var sms_code = $("input[name='sms_code']").val(); //短信验证码
    if ((sms_code == null || sms_code == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

/**
 * 点击下一步（爬取电信通话详单）
 */
$('#nextBtn').click( function() {
    if (!onLine) {
        $('#msg').modal('show');
        $('#alertMsg').text(errInfo);
        return;
    }
    nextSubmit();
});

$(function(){
    province = $('#province').val();
    diffProvinceHtml();
    verifyForm();

    /**
     * 表单改变事件
     */
    $('input').bind('input propertychange', function() {
        verifyForm();
    });
});

function diffProvinceHtml(){
    //设置短信验证码输入框
    $("#codeDiv").append("<input type='text' class='form-control' id='sms_code' name='sms_code' aria-describedby='basic-addon3' th:placeholder='输入短信验证码'  autocomplete='off'/>");
    $("#codeDiv").append("<span class='input-group-btn'><button class='btn btn-lg btn-block btn-info' id='sendCodeBtn' type='button'>发送短信验证码</button></span>");

    //显示温馨提示
    $("#prompt").append("<small class='form-text text-muted'>温馨提示：<br />*使用短信验证码验证。<br /> </small>");

    /**
     * 发送短信验证码
     * @type {*|jQuery|HTMLElement}
     */
    // 定义按钮btn
    var btn = $("#sendCodeBtn");
    console.log(btn);

    // 定义发送时间间隔(s)
    var SEND_INTERVAL = 60;
    var timeLeft = SEND_INTERVAL;
    var timeout;

    //重新发送计时
    var timeCount = function() {
        btn.text(timeLeft + "s后重发");
        timeout = window.setTimeout(function() {
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

    //绑定btn按钮的监听事件
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
                url:"/h5/carrier/unicom/sendCode",
                data:$('#authform').serialize(),// 你的formid
                dataType : "json",
                beforeSend: function () {
                    btn.attr('disabled', 'disabled');
                    //开始倒计时
                    timeLeft = SEND_INTERVAL;
                    timeCount();

                },
                error: function(request) {
                    console.log("error");
                    // ** 重要：因为发送失败，所以要恢复发送按钮的监听 **
                    bindBtn();
                    btn.attr('disabled', false);
                    clearInterval(interval_send); //停止间隔一秒调用
                    //停止发送验证码倒计时
                    window.clearTimeout(timeout);
                    btn.text("发送短信验证码");
                },
                success: function(data) {
                    console.log(data);
                    //成功
                    interval_send();
                }
            })
        })
    };

    //点击发送短信验证码
    btn.click(bindBtn());

}

/**
 * 触发数据抓取接口
 */
function unicom(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/unicom/getAllData",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        error: function(request) {
            console.log("error");
            //$('#message').text('出错了...');
            clearInterval(interval_ver); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}