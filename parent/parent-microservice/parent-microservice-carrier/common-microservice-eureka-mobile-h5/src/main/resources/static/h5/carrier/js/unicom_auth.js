/*主题色*/
var themeColor = $("#themeColor").val();
var count = 0;
var errorCount = 0;
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
            clearInterval(interval); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
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
        var taskid = $("#task_id").val();
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
                console.log(data.phase+","+data.phase_status);
                console.log(data.description);
                /*if (data.description == null) {
                    clearInterval(interval); //停止间隔一秒调用
                    return;
                }*/
                if(data.description != null && data.description != "") {
                    if (appActive == "prod" && (data.description.indexOf("采集中") > 0)){
                        $('#message').text("数据采集中，请耐心等待...");
                    } else {
                        $('#message').text(data.description); //反馈信息
                    }
                }
                /*if (data.phase == "READY" && data.phase_status == "DOING") {//开始准备
                    $('#message').text('开始准备...');

                } else if (data.phase == "LOGIN" && data.phase_status == "DOING") {//正在认证，请耐心等待...
                    $('#message').text('正在认证，请耐心等待...');

                } else*/
                if (data.phase == "LOGIN" && data.phase_status == "SUCCESS") {//认证成功！
                    unicom(); //调用数据采集接口

                } else if (data.phase == "LOGIN" && data.phase_status == "FAIL") {//认证失败！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);
                    return;

                }/*else if (data.phase == "CRAWLER" && data.phase_status == "SUCCESS") { //数据采集成功
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    window.location.href="/h5/success?themeColor="+themeColor+"&isTopHide="+isTopHide+"&taskid=" + taskid;
                    return;

                }*/ else if (data.phase == "CRAWLER" && data.phase_status == "FAIL") {//数据采集失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.error_message);
                    return;
                } if (data.phase == "PASSWORD" && data.phase_status == "FAIL") {//账号验证失败！24小时内发送5次信息！、账号验证失败！（后台图片验证码验证）
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } else if (data.phase == "PASSWORD" && data.phase_status == "SUCCESS") {//短信验证码准备发送 （忘记密码的图片验证码验证成功）
                    clearInterval(interval); //停止初始调用的间隔一秒获取task状态
                    $('#myModal').modal('hide');
                    //跳转忘记密码页面
                    $("#authform").attr("action","/h5/carrier/forgetpasswd");
                    $("#authform").attr("method","POST");
                    $("#authform").submit();
                }else if (data.phase == "READY_CODE" && data.phase_status == "DONING") { //登录失败（短信验证码不正确），需要发送短信验证码
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    var password = $("#password").val();
                    var user_id = $("#id").val();
                    var key = $("#key").val();
                    var owner = $("#owner").val();
                    var redirectUrl = $("#redirectUrl").val();
                    console.log("redirectUrl="+redirectUrl);
                    console.log("************");
                    window.location.href="/h5/carrier/unicom/authTwo?phoneNum=" + data.phonenum + "&owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+"&themeColor="+themeColor
                        +"&isTopHide="+isTopHide+"&task_id=" + taskid + "&user_id=" + user_id +  "&password=" + password;
                    return;
                }

                if (data.finished && (data.phase == "CRAWLER" && data.phase_status == "SUCCESS")) { //数据采集成功
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
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
 * 触发认证
 */
function nextSubmit(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/unicom/login",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
        },
        success: function(data) {
            //$('#myModal').modal('hide');
            //window.location.href="success.html";
            console.log(data.description);
            interval();
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
    nextSubmit(); //触发爬虫
});

/**
 * 修改密码第一步（图片验证码登录）
 */
function imageCodeLogin(){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/unicom/password/imageCode",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
            interval();
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
 * 点击忘记密码
 */
$("#forgetPwdBtn").click(function(){
    if (!onLine) {
        $('#msg').modal('show');
        $('#alertMsg').text(errInfo);
        return;
    }
    //触发修改密码第一步（图片验证码登录）
    imageCodeLogin();
});

//表单验证
function verifyForm() {
    var password = $('#password').val().trim(); //服务密码
    if ((password == null || password == "")) {
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
    var val = $('#mobileNum').val();
    var text = $('#mobileNum').text();
    console.log("mobileNum:"+val);
    console.log("text:"+text);
    $('#password').val("");
    verifyForm();
});