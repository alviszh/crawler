
var count = 0;
var errorCount = 0;

function isMobile() {
    var userAgentInfo = navigator.userAgent;

    var mobileAgents = [ "Android", "iPhone", "SymbianOS", "Windows Phone", "iPad","iPod"];

    var mobile_flag = false;

    //根据userAgent判断是否是手机
    for (var v = 0; v < mobileAgents.length; v++) {
        if (userAgentInfo.indexOf(mobileAgents[v]) > 0) {
            mobile_flag = true;
            break;
        }
    }

     var screen_width = window.screen.width;
     var screen_height = window.screen.height;

     //根据屏幕分辨率判断是否是手机
     if(screen_width < 500 && screen_height < 800){
         mobile_flag = true;
     }

     return mobile_flag;
}

function showLogin(){
   isMobile() ? $("#tishi0").css("display","inline") : $("#tishi0").css("display","grid"); $("#loginNameInfo").hide()
}

function showPw(){
    isMobile() ? $("#tishi").css("display","inline") : $("#tishi").css("display","grid");$("#passwordInfo").hide()
}

//页面元素用户登录名格式检验
/*  要求：登录名由6-16位数字、字母、“_”、“-”、“/”组成    */
function checkLoginName(){
    var loginName = $("#username").val();
    var reg = new RegExp("^[a-zA-Z0-9\-_/]*$");
    if(loginName.length < 1 ||loginName.indexOf(" ")>=0){
		isMobile() ? $("#loginNameInfo").css("display","inline").text("登录名不能为空或空格") : $("#loginNameInfo").css("display","grid").text("登录名不能为空或空格");
        $("#loginNameInfo").removeClass("yes").addClass("no");
        $("#tishi0").hide()
        return false;
    }if(loginName.length < 6   ){
		isMobile() ? $("#loginNameInfo").css("display","inline").text("登录名不能小于6位字符") : $("#loginNameInfo").css("display","grid").text("登录名不能小于6位字符");
        $("#loginNameInfo").removeClass("yes").addClass("no")
        $("#tishi0").hide()
        return false;
    }else if(loginName.length > 16){
		isMobile() ? $("#loginNameInfo").css("display","inline").text("登录名不能大于16位字符") : $("#loginNameInfo").css("display","grid").text("登录名不能大于16位字符");
        $("#loginNameInfo").removeClass("yes").addClass("no");
        $("#tishi0").hide()
        return false;
    }else if(!reg.test(loginName)){
		isMobile() ? $("#loginNameInfo").css("display","inline").text("登录名只能包含字母、数字、_、-、/") : $("#loginNameInfo").css("display","grid").text("登录名只能包含字母、数字、_、-、/");
        $("#loginNameInfo").removeClass("yes").addClass("no");
        $("#tishi0").hide()
        return false;
    }else{
		$("#tishi0").hide();
		isMobile() ? $("#loginNameInfo").css("display","inline").text(" ") : $("#loginNameInfo").css("display","grid").text(" ");
        $("#loginNameInfo").removeClass("no").addClass("yes");
        return true;
    }
}

//页面元素用户登陆密码格式检验
/*要求：密码长度在6-20个字符，包含数字、小写字母和大写字母,至少包含两种  */
function checkPassword(){
    var password = $("#password").val();//获得密码长度
    var reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]+$/;
    if(password.length == 0 || length == undefined ){
		isMobile() ? $("#passwordInfo").css("display","inline").text("密码不能为空") : $("#passwordInfo").css("display","grid").text("密码不能为空");
        $("#passwordInfo").removeClass("yes").addClass("no");
        $("#tishi").hide()
        return false;
    }else if(password.length < 6){
		isMobile() ? $("#passwordInfo").css("display","inline").text("密码不能小于6位字符") : $("#passwordInfo").css("display","grid").text("密码不能小于6位字符");

        $("#passwordInfo").removeClass("yes").addClass("no");
        $("#tishi").hide()
        return false;
    }else if(password.length > 20){
        $("#passwordInfo").show().text("密码不能超过20位字符");
        $("#passwordInfo").removeClass("yes").addClass("no");
        $("#tishi").hide()
        return false;
    }/*else if(!reg.test(password)){
     $("#passwordInfo").text("密码只能使用数字和字母，且必须同时包含数字和字母");
     $("#passwordInfo").removeClass("yes").addClass("no");
     return false;
     }*/
	 else{
		$("#tishi").hide();
		$("#passwordInfo").text("").show();
		$("#passwordInfo").removeClass("no").addClass("yes");
		return true;
	 }
}


/**
 * 点击下一步
 */
function GetQueryString(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return unescape(r[2]);
    return null;
}

$('#excute_btn').click( function() {
    loginv1();
});



//表单验证
function verifyForm() {
    var username = $('#username').val();
    var password = $('#password').val();
    var loginNameInfo = $('#loginNameInfo');
    var passwordInfo = $('#passwordInfo');

  //  var verifycode = $('#verifycode').val();
    var tradecode = $('#tradecode').val();
    if ((username == null || username == "") ||
        (password == null || password == "") ||
        //(verifycode == null || verifycode == "") ||
        (tradecode == null || tradecode == "") ||
        (loginNameInfo.hasClass("yes")==false)||
        (passwordInfo.hasClass("yes")==false)
    ) {
        $("#excute_btn").addClass("disabled");
    }else{
        $("#excute_btn").removeClass("disabled");
    }
}

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    verifyForm();
});

function loginv1(){
    $.ajax({
        type:"POST",
        url:"/h5/pbccrc/v2/login",
        data:{
            "username": $("#username").val(),
            "tradecode": $("#tradecode").val(),
            "password": $("#password").val(),
            "reportCategory": "json",
            "key": $("#key").val(),
            "owner": $("#owner").val()
        },
        dataType : "json",
        beforeSend: function () {
            $('#message').text('开始准备...');
            $('#modalReady').modal('show');
        },
        error: function(request) {
            console.log("error");
            $('#modalReady').modal('hide');
            $('.modal-body').html("服务正在升级，请稍后重试");
            $('#msg').modal('show');
            $('#result').click( function() {
                location.reload(true);
            });
            return;
        },
        success: function(data) {
            console.log(data.taskid);
            interval(data.taskid);
            /*$('#modalReady').modal('hide');
            if(data.message=="查询成功"){
                $('.modal-body').html("报告采集成功");
                $('#result').click( function() {
                    if($.trim($('#redirectUrl').val())==""){
                        location.reload(true);
                    }
                    else{
                        var date_obj = new Date();
                        window.location.href = $('#redirectUrl').val();
                    }

                });
            }
            else{
                $('.modal-body').html(data.message);
                $('#result').click( function() {
                    location.reload(true);
                });
            }
            $('#msg').modal('show');*/
        }
    });
}

$(function(){
    var loginpageCookiesStr = "";
    verifyForm();
    //getloginpage();
});

/**
 * 间隔一秒调用获取task状态的方法
 */
function interval(taskid) {
    count = 0;
    errorCount = 0;
    var interval = setInterval(function (){
        console.log("********** " + taskid);
        if (!onLine) {
            if (count > 10) {
                $('#modalReady').modal('hide');
                clearInterval(interval); //停止间隔一秒调用
                //显示错误信息
                $('#msg').modal('show');
                $('#alertMsg').text(errInfo);

                //跳转到第一个页面
                $("#megBtn").click( function() {
                    location.reload(true);
                });
            }
            count ++;
        }
        $.ajax({
            type: "GET",
            url:"/h5/pbccrc/v2/standalone/tasks/status",
            data:{
                taskid: taskid
            },
            dataType : "json",
            error: function(request) {
                console.log("interval.error, online=" + onLine+", errorCount=" + errorCount);
                if (errorCount > 10) {
                    $('#modalReady').modal('hide');
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
                    $('#message').text(data.description);
                }


                if (data.phase == "LOGIN" && data.phase_status == "ERROR") {//登录失败！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#modalReady').modal('hide');
                    //弹出提示框
                    $('.modal-body').html(data.description);
                    $('#msg').modal('show');

                } else if (data.phase == "CRAWLER" && data.phase_status == "ERROR") {//数据获取失败！
                    clearInterval(interval); //停止间隔一秒调用
                    $('#modalReady').modal('hide');
                    //弹出提示框
                    $('.modal-body').html(data.description);
                    $('#msg').modal('show');

                } else if (data.phase == "PASSWORD" && data.phase_status == "ERROR") {//用户名或密码错误
                    clearInterval(interval); //停止间隔一秒调用
                    $('#modalReady').modal('hide');
                    //弹出提示框
                    $('.modal-body').html(data.description);
                    $('#msg').modal('show');

                } else if (data.phase == "TRADECODE" && data.phase_status == "ERROR") {//授权码错误、征信报告未生成或授权码已过期
                    clearInterval(interval); //停止间隔一秒调用
                    $('#modalReady').modal('hide');
                    //弹出提示框
                    $('.modal-body').html(data.description);
                    $('#msg').modal('show');

                } else if (data.phase == "AGENT" && data.phase_status == "ERROR") {//系统繁忙，请稍后再试（没有闲置实例）
                    clearInterval(interval); //停止间隔一秒调用
                    $('#modalReady').modal('hide');
                    //弹出提示框
                    $('.modal-body').html(data.description);
                    $('#msg').modal('show');
                }

                if (data.finished && (data.phase == "CRAWLER" && data.phase_status == "SUCCESS")) { //数据采集成功
                    clearInterval(interval); //停止间隔一秒调用
                    $('#modalReady').modal('hide');
                    themeColor = themeColor.substr(1,themeColor.length);
                    //弹出提示框
                    $('.modal-body').html(data.description);
                    $('#msg').modal('show');

                    $('#result').click( function() {
                        if($.trim($('#redirectUrl').val())==""){
                            location.reload(true);
                        }
                        else{
                            var date_obj = new Date();
                            window.location.href = $('#redirectUrl').val();
                        }

                    });
                } else {
                    $('#result').click( function() {
                        location.reload(true);
                    });
                }

            }
        });
    }, 1000);
}

$('.tex_box').click( function() {
    if($(this).find('span').hasClass('closed')){
        $(this).prev('input').attr("type","text");
        $(this).find('span').removeClass('closed').addClass('opend');
    }
    else{
        $(this).prev('input').attr("type","password");
        $(this).find('span').removeClass('opend').addClass('closed');
    }
});






