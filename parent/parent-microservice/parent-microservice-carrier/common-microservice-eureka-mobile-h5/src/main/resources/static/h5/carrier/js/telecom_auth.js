var count=0;
var errorCount = 0;

/**
 * 点击忘记密码
 */
$("#forgetPwdBtn").click(function(){
    $("#authform").attr("action","/h5/carrier/forgetpasswd");
    $("#authform").attr("method","POST");
    $("#authform").submit();
});

/**
 * 间隔一秒调用获取task状态的方法
 */
function interval() {
    count = 0;
    errorCount = 0;
    var interval = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var taskid = $("#task_id").val();
        var user_id = $("#id").val();
        var code = $("#password").val();
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
            console.log("count="+count)
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
                console.log(data.phase +","+data.phase_status);

                if(data.description != null && data.description != "") {
                    if (appActive == "prod" && (data.description.indexOf("采集中") > 0)){
                        $('#message').text("数据采集中，请耐心等待...");
                    } else {
                        $('#message').text(data.description); //反馈信息
                    }
                }
                var province = data.province;
                var key = $("#key").val();
                var redirectUrl = $("#redirectUrl").val();
                var owner = $("#owner").val();

                if (data.phase == "READY" && data.phase_status == "FAIL") {//开始准备
                    clearInterval(interval); //停止间隔一秒调用
                    $('#myModal').modal('hide');
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                }  else if (data.phase == "LOGIN" && data.phase_status == "SUCCESS") {//登录成功
                    console.log(data.province);
                    /*if (province == "海南" ) { //进行第二次登录
                        loginTwo("authform",province); //海南：调用第二次登录接口；

                    } else */if (province == "江西") { //江西进入第二次登录页面
                        $('#myModal').modal('hide');
                        clearInterval(interval); //停止间隔一秒调用
                        //themeColor = themeColor.substr(1, themeColor.length);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        //跳转页面
                        window.location.href = "/h5/carrier/telecom/authTwo?phoneNum=" + phoneNum + "&task_id=" + taskid + "&user_id=" + user_id
                            + "&code=" + code + "&province=" + province + "&owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                            + "&themeColor=" + themeColor + "&isTopHide=" + isTopHide;
                    } else if (province == "浙江" ) {
                        intermediate();

                    } else {
                        $('#myModal').modal('hide');
                        clearInterval(interval); //停止间隔一秒调用
                        themeColor = themeColor.substr(1,themeColor.length);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        window.location.href="/h5/carrier/telecom/verificodeOne?phoneNum=" + phoneNum  + "&task_id=" + taskid + "&user_id=" + user_id
                            + "&code=" + code+ "&province=" + province  + "&owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                            + "&themeColor="+themeColor+"&isTopHide="+isTopHide;
                        return;
                    }

                } else if (data.phase == "LOGIN" && data.phase_status == "FAIL") {//认证失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                } /*else if ((data.phase == "READY_CODE" && data.phase_status == "DONING") && (data.province == "山西")) { //山西实名认证成功之后跳转页面
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }

                    window.location.href="/h5/carrier/telecom/verificodeOne?phoneNum=" + phoneNum  + "&task_id=" + taskid + "&user_id=" + user_id
                        + "&code=" + code+ "&province=" + data.province + "&owner="+owner  + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                        + "&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    return;

                }*/// 安徽: VALIDATE,FAIL(抱歉您的号码没有进行查询的功能)，需要跳转到第一个页面重新登录
                else if ((data.phase == "VALIDATE" && data.phase_status == "FAIL") && (data.province == "山西" || data.province == "重庆" || data.province == "安徽")) {//认证失败！
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
                        //window.location.href = redirectUrl;
                        window.location.href="/h5/carrier/auth?owner="+owner + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                            +"&themeColor="+themeColor+"&isTopHide="+isTopHide;
                    });

                    return;

                } else if (data.phase == "LOGINTWO" && data.phase_status == "SUCCESS") { //第二次登录成功，跳转页面
                    /*if (province == "江苏") { //江苏第二次认证成功之后调用爬取接口
                        clearInterval(interval); //停止间隔一秒调用
                        telecom_getAllData("authform"); //调用数据采集接口
                        interval_crawler();
                    }
                    else {*/
                        $('#myModal').modal('hide');
                        clearInterval(interval); //停止间隔一秒调用
                        themeColor = themeColor.substr(1,themeColor.length);
                        var isTopHide = false;
                        if (topHide == "none") {
                            isTopHide = true;
                        }
                        window.location.href="/h5/carrier/telecom/verificodeOne?phoneNum=" + phoneNum  + "&task_id=" + taskid + "&user_id=" + user_id
                            + "&code=" + code+ "&province=" + data.province + "&owner="+owner   + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                            + "&themeColor="+themeColor+"&isTopHide="+isTopHide;

                    //}

                    return;
                } else if (data.phase == "LOGINTWO" && data.phase_status == "FAIL") {//第二次登录认证失败
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                } else if (data.phase == "INTERMEDIATE" && data.phase_status == "ERROR") {//页面加载失败（浙江）
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                } else if (data.phase == "INTERMEDIATE" && data.phase_status == "SUCCESS"){ //页面加载成功（浙江）
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    window.location.href="/h5/carrier/telecom/verificodeOne?phoneNum=" + phoneNum  + "&task_id=" + taskid + "&user_id=" + user_id
                        + "&code=" + code+ "&province=" + data.province + "&owner="+owner   + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                        + "&themeColor="+themeColor+"&isTopHide="+isTopHide;

                    return;
                }

            }
        });
    }, 1000);
}

/**
 * 间隔一秒调用获取task状态的方法（登录成功后直接爬取数据）
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
            if (count > 10) {
                $('#myModal').modal('hide');
                clearInterval(interval_crawler); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                $("#megBtn").click( function() {
                    window.history.go(-2);
                });
            }
            count ++;
            console.log("count="+count)
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
                    clearInterval(interval_crawler); //停止间隔一秒调用
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

                if (data.phase == "LOGIN" && data.phase_status == "FAIL") {//认证失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval_crawler); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;

                }else if (data.phase == "LOGIN" && data.phase_status == "SUCCESS") {// 登录成功
                    telecom_getAllData("authform"); //调用数据采集接口

                } else if (data.phase == "CRAWLER" && data.phase_status == "FAIL") {//数据采集失败！
                    $('#myModal').modal('hide');
                    clearInterval(interval_crawler); //停止间隔一秒调用
                    //显示错误信息
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                }else if ((data.phase == "PASSWORD" && data.phase_status == "FAIL") && (data.province == "甘肃" )) {
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
                        //window.location.href = redirectUrl;
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
 * 电信认证（触发爬虫认证接口）
 */
function nextSubmit(province){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/telecom/login",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#myModal').modal('show');
            $('#message').text('开始准备');
        },
        error: function(request) {
            console.log("error");
        },
        success: function(data) {
            console.log(data.message);
            console.log("province="+province);
            if (province == "北京" || province == "辽宁" || province == "陕西" || province == "甘肃" || province == "青海"
                || province == "天津" || province == "江苏" || province == "新疆") { //不需要短信验证码
                interval_crawler();
            } else {
                interval();
            }
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
    findTaskStatus();
    /*var province = $('#province').val();
    nextSubmit(province);*/
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
    verifyForm();
    var phonenum = $("#name").val();
    findMobileSegment(phonenum);
});

//获取task状态信息
function findTaskStatus(){
    var taskid = $("#task_id").val();
    var phoneNum = $("#name").val();
    var user_id = $("#id").val();
    var code = $("#password").val();
    $.ajax({
        type: "GET",
        url:"/h5/carrier/tasks/status",
        data:{
            taskid: taskid
        },
        dataType : "json",
        error: function(request) {
            console.log("error");
            return;
        },
        success: function(data) {
            console.log(data.description);
            console.log(data.phase +","+data.phase_status);

            var province = $('#province').val();
            var key = $("#key").val();
            var redirectUrl = $("#redirectUrl").val();
            var owner = $("#owner").val();

            if (data.phase == null || data.phase_status == null || data.phase == "LOGIN" || data.phase == "READY" ||
                ((data.phase == "VALIDATE" && data.phase_status == "FAIL") && (province == "山西" || province == "重庆")
                || (data.phase == "LOGINTWO" && data.phase_status == "FAIL"))//身份验证状态（正在验证、失败）
            ) { //调用登录接口
                console.log("========login");
                nextSubmit(province);
            } else if (province == "黑龙江" || province == "吉林" || province == "宁夏" || province == "山西" || province == "内蒙古"
                || province == "四川" || province == "山东" || province == "重庆" || province == "湖南" || province == "福建" || province == "贵州"
                || province == "云南" || province == "上海" || province == "广西" || province == "河南" || province == "江西" || province == "广东"
                || province == "湖北" || province == "海南" || province == "安徽" || province == "浙江" || province == "河北" ){ //需要短信验证码

                $('#myModal').modal('show');
                $('#message').text('开始准备');
                interval();
               /* if (province == "海南" ) {
                    loginTwo("authform", province);
                } else*/ if (province == "江西") { //江西进入第二次登录页面
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    //跳转页面
                    window.location.href="/h5/carrier/telecom/authTwo?phoneNum=" + phoneNum +"&task_id=" + taskid + "&user_id=" + user_id
                        + "&code=" + code+ "&province=" + province + "&owner="+owner   + "&key=" + key  + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                        + "&themeColor="+themeColor+"&isTopHide="+isTopHide;
                } else if (province == "浙江" ) {
                    intermediate();

                } else {
                    $('#myModal').modal('hide');
                    clearInterval(interval); //停止间隔一秒调用
                    themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    //跳转页面
                    window.location.href="/h5/carrier/telecom/verificodeOne?phoneNum=" + phoneNum  + "&task_id=" + taskid + "&user_id=" + user_id
                        + "&code=" + code+ "&province=" + province + "&owner="+owner   + "&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)
                        + "&themeColor="+themeColor+"&isTopHide="+isTopHide;
                }
                return;
            } /*else if (province == "江苏" ) { //调用第二次登录接口，登录成功后直接调用爬取接口
                $('#myModal').modal('show');
                $('#message').text('开始准备');
                interval();
                loginTwo("authform", province);

            }*/ else { //直接调用爬取接口
                $('#myModal').modal('show');
                $('#message').text('开始准备');
                console.log("========getData");
                telecom_getAllData("authform"); //调用数据采集接口
                interval_crawler();
            }
        }
    });
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
            //$('#message').text('请求超时，请重试！');
            //clearInterval(interval_crawler); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data);
            return;
        }
    });
}*/

/**
 * 浙江登录成功后需要处理的请求
 */
function intermediate(){
    $.ajax({
        type: "POST",
        url: "/h5/carrier/telecom/intermediate",
        data:$('#authform').serialize(),
        dataType: "json",
        error: function (request) {
            console.log("error");
            clearInterval(interval); //停止间隔一秒调用
            return;
        },
        success: function(data) {
            console.log(data);
            return;
        }
    });
}

/**
 * 第二次登录
 */
/*
function loginTwo(form,province){
    $.ajax({
        type: "POST",
        url: "/h5/carrier/telecom/loginTwo",
        data:$('#'+form).serialize(),
        dataType: "json",
        error: function (request) {
            console.log("error");
            clearInterval(interval); //停止间隔一秒调用
            return;
        },
        success: function(data) {
            console.log(data);
            return;
        }
    });
}*/
