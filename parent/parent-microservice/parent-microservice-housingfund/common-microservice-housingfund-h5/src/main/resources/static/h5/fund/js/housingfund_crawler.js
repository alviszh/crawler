/**
 * 登录爬取
 */
function crawler(){
    $.ajax({
        type: "POST",
        url:"/h5/fund/crawler",
        data:$('#authform').serialize(),
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_crawler();
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_crawler); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.error_message);
            return;
        }
    });
}
function nextLogin(){
    $.ajax({
        type: "POST",
        url:"/h5/fund/login",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_login();
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_login); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.error_message);
            return;
        }
    });
}


/**
 * 间隔一秒调用获取task状态的方法（登录）
 */
function interval_login() {
    var interval_login = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var task_id = $("#task_id").val();
        $.ajax({
            type: "POST",
            url:"/h5/fund/tasks/status",
            data:{
                taskId: task_id
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_login); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息
                if ((data.phase == "LOGIN"||data.phase == "PASSWORD") && data.phase_status == "SUCCESS") {//认证成功！
                    clearInterval(interval_login);
                    crawler(); //调用数据采集接口
                }
                if ((data.phase == "LOGIN" || data.phase == "LOGINTWO"|| data.phase == "PASSWORD"|| data.phase == "CRAWLER")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/fund"
                    });
                    return;
                }

                if (data.phase == "SEND_CODE" && (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证成功！
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/fund"
                    });
                }
                if (data.phase == "VALIDATE" && data.phase_status == "FAIL") {
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/fund"
                    });
                }
                if (data.finished) { //数据采集成功
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    var city = $("#city").val();

                    result(task_id);
                    $('#success_modal').modal('show');
                    return;
                }
                if (data.phase == "WAIT_CODE" && data.phase_status == "SUCCESS") {//认证成功！
                    //$('#sms_div').show();
                    $('#myModal').modal('hide');
                    clearInterval(interval_login);
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        $('#msg').modal('hide');
                        $('#smsCode').modal('show');
                    });
                }
            }
        });
    }, 1000);
}

/**
 * 直接爬取
 */
function nextSubmit(){
    $.ajax({
        type: "POST",
        url:"/h5/fund/crawler",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_crawler();
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
            // clearInterval(interval_crawler); //停止间隔一秒调用
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
function interval_crawler() {
    var interval_crawler = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var task_id = $("#task_id").val();
        $.ajax({
            type: "POST",
            url:"/h5/fund/tasks/status",
            data:{
                taskId: task_id
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_crawler); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息
                if ((data.phase == "LOGIN" || data.phase == "LOGINTWO")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//爬取失败
                    clearInterval(interval_crawler);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/fund"
                    });
                    return;
                }

                if (data.finished) { //数据采集成功
                    clearInterval(interval_crawler);
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    var city = $("#city").val();

                    result(task_id);
                    $('#success_modal').modal('show');
                    return;
                }
            }
        });
    }, 1000);
}

function nextSmsCode(){
    $.ajax({
        type: "POST",
        url:"/h5/fund/getcode",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_getcode();
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_getcode); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}

function interval_getcode() {
    var interval_getcode = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var task_id = $("#task_id").val();
        $.ajax({
            type: "POST",
            url:"/h5/fund/tasks/status",
            data:{
                taskId: task_id
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_getcode); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息
                if (data.phase == "WAIT_CODE" && data.phase_status == "SUCCESS") {//认证成功！
                    clearInterval(interval_getcode);
                    $('#myModal').modal('hide');
                    $('#smsCode').modal('show');
                    //crawler(); //调用数据采集接口

                } else if (data.phase == "VALIDATE" && (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    clearInterval(interval_getcode);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/fund"
                    });
                    return;
                }

            }
        });
    }, 1000);
}

function nextSetCode(){
    $.ajax({
        type: "POST",
        url:"/h5/fund/setcode",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_setcode();
            $('#message').text('正在验证中...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_setcode); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}

function interval_setcode() {
    var interval_setcode = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var task_id = $("#task_id").val();
        $.ajax({
            type: "POST",
            url:"/h5/fund/tasks/status",
            data:{
                taskId: task_id
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_setcode); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息
                if (data.phase == "LOGIN" && data.phase_status == "SUCCESS") {//认证成功！
                    clearInterval(interval_setcode);
                    var city = $('p.navbar-brand').find('span').text();
                   // $('#myModal').modal('hide');
                    //$('#smsCode').modal('show');
                 if(city.indexOf("潍坊市")!=-1||city.indexOf("唐山市")!=-1||city.indexOf("乐山市")!=-1||city.indexOf("银川市")!=-1) crawler(); //调用数据采集接口

                } else if ((data.phase == "VALIDATE"||data.phase == "WAIT_CODE") && (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    clearInterval(interval_setcode);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/fund"
                    });
                    return;
                }
            }
        });
    }, 1000);
}

$('#smsCodeBtn').click( function() {
    $('#smsCode').hide(); nextSetCode();  //触发验证短信
});

