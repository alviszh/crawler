/**
 * 表单控制
 */
$('.tex_box').eq(0).click( function() {
    $(this).prev('input').val("");
});

$('.tex_box').eq(1).click( function() {
    if($(this).find('span').hasClass('closed')){
        $(this).prev('input').attr("type","text");
        $(this).find('span').removeClass('closed').addClass('opend');
    }
    else{
        $(this).prev('input').attr("type","password");
        $(this).find('span').removeClass('opend').addClass('closed');
    }
});


$("#myTab").delegate("li", "click", function () {
    if(!$(this).hasClass("active")&&$(this).index()==1){
        $('#logintype').val("sn_phone");
        $("#username").attr("disabled","disabled");
        $("#username1").removeAttr("disabled");
    }
    if(!$(this).hasClass("active")&&$(this).index()==0){
        $('#logintype').val("sn_num");
        $("#username1").attr("disabled","disabled");
        $("#username").removeAttr("disabled");
    }
});
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    var username =  $("input[name='username']").val();//用户名
    var passwd =  $("input[name='passwd']").val();//密码
    if((username == null || username == "")||(passwd == null || passwd == "")){
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
    nextLogin(); //触发爬虫
});

//表单验证
function verifyForm1() {
    var username = $('#username').val().trim();//姓名
    var passwd = $('#passwd').val().trim();//密码
    if ((username == null || username == "") ||
        (passwd == null || passwd == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm2() {
    var username1 = $('#username1').val().trim();//姓名
    var verfiySMS = $('#verfiySMS').val().trim();//密码
    if ((username1 == null || username1 == "") ||
        (verfiySMS == null || verfiySMS == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
    var phone = document.getElementById('username1').value;
    if(!(/^1[34578]\d{9}$/.test(phone))||phone==""){
        console.log(phone);
        console.log(1);
        $("#sendCodeBtn").addClass("disabled");
    }
    else{
        $("#sendCodeBtn").removeClass("disabled");
    }
}

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    if($('#logintype').val()=='sn_num'){
        verifyForm1();
    }
    if($('#logintype').val()=='sn_phone'){
        verifyForm2();
    }
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
        var phone = document.getElementById('username1').value;
        // 需要先禁用按钮（为防止用户重复点击）
        if(!(/^1[34578]\d{9}$/.test(phone))||phone==""){
            console.log(phone);
            console.log(1);
        }
        else{
            console.log(2);
                $.ajax({
                    type: "POST",
                    url:"/h5/sn/sendSMS",
                    data:$('#authform').serialize(),// 你的formid
                    async: true,
                    dataType : "json",
                    beforeSend: function () {
                        btn.attr('disabled', 'disabled');
                        interval_sendSms();
                        $('#message').text('正在发送中...');
                        $('#myModal').modal('show');
                    },
                    error: function(request) {
                        console.log("error");
                        // ** 重要：因为发送失败，所以要恢复发送按钮的监听 **
                        bindBtn();
                        btn.attr('disabled', false);

                        $('#msg').modal('show');
                        $('#alertMsg').text('短信验证码发送超时，请尝试重新发送！');
                        clearInterval(interval_sendSms); //停止间隔一秒调用
                        $('#msg').find("button").bind("click",function(){
                            location.href = "/h5/sn"
                        });
                    },
                    success: function(data) {
                        console.log(data.description);
                        return;
                    }
                });
            }
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
 * 触发认证
 */
function nextLogin(){
    $.ajax({
        type: "POST",
        url:"/h5/sn/login",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_login();
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error+login");
            clearInterval(interval_loginD); //停止间隔一秒调用
            $('#myModal').modal('hide');
            $('#msg').modal('show');
            $('#alertMsg').text('网络超时');
            $('#msg').find("button").bind("click",function(){
                location.href = "/h5/sn"
            });
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}

/**
 * 间隔一秒调用获取task状态的方法（登录）
 */
function interval_login() {
    var interval_login = setInterval(function (){
        console.log("********** "+$("#taskid").val());
        var taskid = $("#taskid").val();
        $.ajax({
            type: "POST",
            url:"/h5/sn/tasks/status",
            data:{
                taskid: taskid
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
                if ((data.phase == "LOGIN" && data.phase_status == "ERROR")) {//认证失败！
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    if(data.description == "异常错误!"||data.description == "为了你的账户安全，请拖动滑块完成验证。"){
                        $('#alertMsg').text("网络超时,请稍后再试");
                        $('#msg').find("button").bind("click",function(){
                            location.href = "/h5/sn"
                        });
                    }
                    else if(data.description == "请输入6-20位密码"){
                        $('#alertMsg').text("请输入正确的密码");
                    }
                    else{
                        $('#alertMsg').text(data.description);
                        $('#msg').find("button").bind("click",function(){
                            location.href = "/h5/sn"
                        });
                    }
                    return;
                }
                if (data.finished && data.phase == "AGENT" && data.phase_status == "ERROR") {
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/sn"
                    });
                    return;
                }
                if (data.finished && data.phase != "AGENT") { //数据采集成功
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    if($("#appActive").text()=="prod"){
                        $('#msg').modal('show');
                        resultProd(taskid)
                    }

                    if($("#appActive").text()=="test"){
                        $('#success_modal').modal('show');
                        resultTest(taskid)
                    }
                    return;
                }
            }
        });
    }, 1000);
}



/**
 * 间隔一秒调用获取task状态的方法（登录）
 */

function interval_sendSms() {
    var interval_sendSms = setInterval(function (){
        console.log("********** "+$("#taskid").val());
        var taskid = $("#taskid").val();
        $.ajax({
            type: "POST",
            url:"/h5/sn/tasks/status",
            data:{
                taskid: taskid
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_sendSms); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息
                if ((data.phase == "SEND_CODE" && data.phase_status == "ERROR"||data.phase_status == "FAIL")) {//发送失败！
                    clearInterval(interval_sendSms);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/sn"
                    });
                    return;
                }
                if (data.phase == "WAIT_CODE" && data.phase_status == "SUCCESS") {//发送成功
                    clearInterval(interval_sendSms);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                }

                if (data.finished) { //数据采集成功
                    clearInterval(interval_sendSms);
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    if($("#appActive").text()=="prod"){
                        $('#msg').modal('show');
                        resultProd(taskid)
                    }

                    if($("#appActive").text()=="test"){
                        $('#success_modal').modal('show');
                        resultTest(taskid)
                    }
                    return;
                }
            }
        });
    }, 1000);
}


$(function(){
    $("#username1").attr("disabled","disabled");
});
