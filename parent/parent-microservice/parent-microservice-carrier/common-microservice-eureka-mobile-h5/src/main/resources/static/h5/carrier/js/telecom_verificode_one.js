var province;
var count = 0;
var errorCount = 0;

/**
 * 发送验证码每秒刷一次task状态（和点击下一步的查询task状态分开）
 */
function interval_send() {
    count = 0;
    errorCount = 0;
    var interval_send = setInterval(function (){
        console.log("****send****** "+$("#task_id").val());
        var taskid = $("#task_id").val();
        var key = $("#key").val();
        var redirectUrl = $("#redirectUrl").val();
        var owner = $("#owner").val();
        if (!onLine) {
            console.log("interval_send,count==" + count);
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval_send); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                $("#megBtn").click( function() {
                    if (province == "江西") {//前面多一个登录页面
                        window.history.go(-6);
                    } else {
                        window.history.go(-4);
                    }
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
                    clearInterval(interval_send); //停止间隔一秒调用
                    return;
                }  else if ((data.phase == "PASSWORD" && data.phase_status == "FAIL") && (data.province == "山东")) {
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
                        window.location.href="/h5/carrier/auth?owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                            +"&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });
                    return;

                } else if ((data.phase == "VALIDATE" && data.phase_status == "FAIL") && (data.province == "湖南" || data.province == "广西"
                    || data.province == "云南" || data.province == "重庆" || data.province == "山西")) {//实名认证（在发送验证接口中）
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
                        //实名验证失败，跳转到第一个页面
                        window.location.href="/h5/carrier/auth?owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                            + "&themeColor="+themeColor+"&isTopHide="+isTopHide;
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
function interval() {
    count = 0;
    errorCount = 0;
    var interval = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var taskid = $("#task_id").val();
        var key = $("#key").val();
        var redirectUrl = $("#redirectUrl").val();
        var owner = $("#owner").val();
        if (!onLine) {
            console.log("count==" + count);
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                $("#megBtn").click( function() {
                    if (province == "江西") {//前面多一个登录页面
                        window.history.go(-6);
                    } else {
                        window.history.go(-4);
                    }
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

                if(data.description != null && data.description != "") {
                    if (appActive == "prod" && (data.description.indexOf("采集中") > 0)){
                        $('#message').text("数据采集中，请耐心等待...");
                    } else {
                        $('#message').text(data.description); //反馈信息
                    }
                }
                console.log(data.description);
                console.log(data.phase +","+data.phase_status);

                if (data.phase == "PASSWORD" && data.phase_status == "SUCCESS" ) {//短信验证码验证成功， 数据采集中
                    if (data.province == "广西") {
                        var phoneNum = $("#name").val();
                        var user_id = $("#user_id").val();
                        var code = $("#password").val();
                        $('#myModal').modal('hide');
                        clearInterval(interval); //停止间隔一秒调用
                        themeColor = themeColor.substr(1,themeColor.length);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        //跳转到发送第二次验证码页面
                        window.location.href="/h5/carrier/telecom/verificodeTwo?themeColor="+themeColor+"&isTopHide="+isTopHide+"&phoneNum=" + phoneNum
                            + "&task_id=" + taskid + "&user_id=" + user_id + "&code=" + code+ "&province=" + province + "&owner="+owner
                            + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl);
                        return;
                    } else {
                        if (data.province != "黑龙江") { //黑龙江的爬取在验证短信接口里
                            telecom_getAllData("verCodeForm"); //调用数据采集接口
                        }
                    }

                } else if (data.phase == "CRAWLER" && data.phase_status == "FAIL") {//数据采集失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } //湖南、广西、云南、安徽实名验证失败时状态是VALIDATE FAIL，所以需要排除处理
                else if ((data.phase == "WAIT_CODE" && data.phase_status == "FAIL") ||
                    ((data.phase == "VALIDATE" && data.phase_status == "FAIL")
                    && (data.province != "湖南" && data.province != "广西" && data.province != "云南" && data.province != "安徽" && data.province != "重庆")) ) {//短信验证码验证失败!
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } else if ((data.phase == "PASSWORD" && data.phase_status == "FAIL" && data.province == "山东")
                    || (data.phase == "WAIT_CODE" && data.phase_status == "FAIL" && data.province == "江西")) {//需要跳转到第一个页面
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);

                    $("#megBtn").click( function() {
                        console.log("themeColor:" + themeColor);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        window.location.href="/h5/carrier/auth?owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                            + "&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });
                    return;

                } else if ((data.phase == "VALIDATE" && data.phase_status == "FAIL")
                    && (data.province == "湖南" || data.province == "广西" || data.province == "云南" || data.province == "安徽"
                    || data.province == "重庆" || data.province == "山西")) {//实名认证（在发送验证接口中）
                    clearInterval(interval); //停止间隔一秒调用
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
                        window.location.href="/h5/carrier/auth?owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+"&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });
                    return;
                }

                if (data.finished) { //数据采集成功
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    result(taskid);
                    $('#success_modal').modal('show');
                    return;
                }
            }
        });
    }, 1000);
}

/**
 * 间隔一秒调用获取task状态的方法（直接爬取数据）
 */
function interval_crawler() {
    count = 0;
    errorCount = 0;
    var interval_crawler = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var taskid = $("#task_id").val();
        var key = $("#key").val();
        var redirectUrl = $("#redirectUrl").val();
        var owner = $("#owner").val();
        if (!onLine) {
            console.log("interval_crawler,count===="+count);
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval_crawler); //停止间隔一秒调用
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
                console.log("interval_crawler.error, errorCount=" + errorCount);
                if (count > 10) {
                    $('#myModal').modal('hide');
                    clearInterval(interval_crawler); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text("网络超时请重试！");
                }
                count ++;
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

                if (data.phase == "CRAWLER" && data.phase_status == "FAIL") {//数据采集失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval_crawler); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                } else if (data.phase == "VALIDATE" && data.phase_status == "FAIL") {//实名认证失败
                    clearInterval(interval); //停止间隔一秒调用
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
                        window.location.href="/h5/carrier/auth?owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                            + "&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });
                    return;
                }

                if (data.finished) { //数据采集成功
                    $('#myModal').modal('hide');
                    clearInterval(interval_crawler); //停止间隔一秒调用
                    themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
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
        url:"/h5/carrier/telecom/verifiCode",
        data:$('#verCodeForm').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#myModal').modal('show');
            $('#message').text('开始准备');
        },
        error: function(request) {
            console.log("error");
            //$('#myModal').modal('hide');
            clearInterval(interval); //停止间隔一秒调用
            clearInterval(interval_crawler); //停止间隔一秒调用
            //$('#message').text('短信验证码验证失败');
        },
        success: function(data) {
            console.log(data.description);
            return;
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
 * 点击下一步（电信爬取接口）
 */
$('#nextBtn').click( function() {
    if (!onLine) {
        $('#msg').modal('show');
        $('#alertMsg').text(errInfo);
        return;
    }
    if (province == "吉林" //用户编辑短信发送验证码
            || province == "河北") //直接调用数据爬取接口
    {
        $('#myModal').modal('show');
        $('#message').text('开始准备');
        telecom_getAllData("verCodeForm"); //调用数据采集接口
        interval_crawler();
    } else {
        nextSubmit();
        interval();
    }
});

$(function(){
    province = $('#province').val();

    if (province == "江西") {
        //显示提示信息
        $('#msg').modal('show');
        $('#alertMsg').text("请进行第二次短信验证!");
    }

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
    if (province == "吉林") {
        //设置短信验证码输入框
        $("#codeDiv").append("<span class='input-group-addon' id='basic-addon3'>短信验证码：</span>");
        $("#codeDiv").append("<input type='text' class='form-control' id='sms_code1' name='sms_code' aria-describedby='basic-addon3' th:placeholder='输入短信验证码' />");

        //显示温馨提示
        $("#prompt").append("<small class='form-text text-muted'>温馨提示：<br />*短信验证码：请用本机发送CXXD至10001获取查询详单的验证码（发送免费）。<br /> </small>");
    } else if (province == "江西"){
        //设置短信验证码输入框
        $("#codeDiv").append("<input type='text' class='form-control' id='sms_code' name='sms_code' aria-describedby='basic-addon3' th:placeholder='输入短信验证码'/>");
        $("#codeDiv").append("<span class='input-group-btn'><button class='btn btn-lg btn-block btn-info' id='sendCodeBtn' type='button'>发送短信验证码</button></span>");

        //显示温馨提示<br />
        $("#prompt").append("<small class='form-text text-muted'>温馨提示：<br />*中国电信为保护客户隐私，需进行二次短信认证。<br />*中国电信短信验证码可能存在延迟或未发送情况，可以通过再次发送短信验证码重新获取<br /> </small>");

    }else {
        //设置短信验证码输入框
        $("#codeDiv").append("<input type='text' class='form-control' id='sms_code' name='sms_code' aria-describedby='basic-addon3' th:placeholder='输入短信验证码'/>");
        $("#codeDiv").append("<span class='input-group-btn'><button class='btn btn-lg btn-block btn-info' id='sendCodeBtn' type='button'>发送短信验证码</button></span>");

        //显示温馨提示
        $("#prompt").append("<small class='form-text text-muted'>温馨提示：<br />*中国电信短信验证码可能存在延迟或未发送情况，可以通过再次发送短信验证码重新获取<br /> </small>");

    }

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
                    url:"/h5/carrier/telecom/sendCode",
                    data:$('#verCodeForm').serialize(),// 你的formid
                    dataType : "json",
                    beforeSend: function () {
                        btn.attr('disabled', 'disabled');
                        //开始倒计时
                        timeLeft = SEND_INTERVAL;
                        timeCount();

                        interval_send();
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
                    }
                })
            })
        };

    //点击发送短信验证码
    btn.click(bindBtn());

}

/*触发爬虫爬取*/
/*function telecom_getAllData(form){
    $.ajax({
        type: "POST",
        url:"/carrier/telecom/getAllData",
        data:$('#'+form).serialize(),// 你的formid
        async: true,
        dataType : "json",
        error: function(request) {
            console.log("error");
            $('#message').text('请求超时，请重试！');
            clearInterval(interval); //停止间隔一秒调用
            clearInterval(interval_crawler); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data);
            return;
        }
    });
}*/
