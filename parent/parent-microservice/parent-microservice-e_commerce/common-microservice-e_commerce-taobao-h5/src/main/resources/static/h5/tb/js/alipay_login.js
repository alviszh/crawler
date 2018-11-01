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
    nextSubmit(); //触发爬虫
});

$('#smsCodeBtn').click( function() {
    $.ajax({
        type: "POST",
        url:"/h5/tb/verfiySMS",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $("#smsCode").hide();
            interval_sms();
            $('#message').text('正在验证...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_login); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
});

//表单验证
function verifyForm() {
    var username = $('#username').val().trim();//姓名
    var passwd = $('#passwd').val().trim();//密码
    if ((username == null || username == "") ||
        (passwd == null || passwd == "")) {
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

/**
 * 触发数据抓取接口
 */
function crawler(){
    $.ajax({
        type: "POST",
        url:"/h5/tb/crawler",
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
            console.log(data.description);
            return;
        }
    });
}

/**
 * 触发认证
 */
function nextSubmit(){
    $.ajax({
        type: "POST",
        url:"/h5/tb/login",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_login();
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            $("#NoIdleInstance").text("1");
            $('#msg').modal('show');
            $('#alertMsg').text("系统忙 请稍后再试");
            $('#msg').find("button").bind("click",function(){
                location.href = "/h5/alipay"
            });
            return;
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
        if($("#NoIdleInstance").text()=="1"){
            clearInterval(interval_login);
        }
        else{
            console.log("********** "+$("#taskid").val());
            var taskid = $("#taskid").val();
            $.ajax({
                type: "POST",
                url:"/h5/tb/tasks/status",
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
                    if (data.phase == "LOGIN" && data.phase_status == "ERROR" && data.description!="此账号登录异常，安全起见 请使用扫码登录") {//认证失败！
                        clearInterval(interval_login);
                        $('#myModal').modal('hide');
                        //弹出提示框
                        $('#msg').modal('show');
                        $('#alertMsg').text(data.description);
                        $('#msg').find("button").bind("click",function(){
                            location.href = "/h5/alipay"
                        });
                        return;
                    }
                    if (data.phase == "LOGIN" && data.phase_status == "ERROR" && data.description =="此账号登录异常，安全起见 请使用扫码登录") {//认证失败！
                        clearInterval(interval_login);
                        $('#myModal').modal('hide');
                        //弹出提示框
                        $('#msg').modal('show');
                        $('#alertMsg').text(data.description);
                        $('#msg').find("button").bind("click",function(){
                            location.href = "/h5/alipay"
                        });
                        return;
                    }
                    if (data.phase == "AGENT" && data.phase_status == "ERROR") {
                        clearInterval(interval_login);
                        $('#myModal').modal('hide');
                        //弹出提示框
                        $('#msg').modal('show');
                        $('#alertMsg').text(data.description);
                        $('#msg').find("button").bind("click",function(){
                            location.href = "/h5/alipay"
                        });
                    }
                    if (data.phase == "SYSTEM" && data.phase_status == "QUIT") {
                        clearInterval(interval_login);
                        $('#myModal').modal('hide');
                        //弹出提示框
                        $('#msg').modal('show');
                        $('#alertMsg').text(data.description);
                        $('#msg').find("button").bind("click",function(){
                            location.href = "/h5/alipay"
                        });
                    }
                    if (data.phase == "LOGIN" && data.phase_status == "SUCCESS_NEEDSMS") {
                        clearInterval(interval_login);
                        $('#myModal').modal('hide');
                        $("#smsCode").modal('show');
                    }
                    if (data.phase == "LOGIN" && data.phase_status == "SUCCESS_NEXTSTEP") {
                        clearInterval(interval_login);
                        crawler(); //调用数据采集接口
                        $('#myModal').modal('hide');$('#waitinView').css("display","flex");
                    }
                    if (data.phase == "CRAWLER" && data.phase_status == "ERROR") {//爬取失败
                        clearInterval(interval_login);
                        $('#myModal').modal('hide');
                        //弹出提示框
                        $('#msg').modal('show');
                        $('#alertMsg').text(data.description);
                        $('#msg').find("button").bind("click",function(){
                            location.href = "/h5/alipay"
                        });
                        return;
                    }
                    if (data.finished) { //数据采集成功
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
        }
    }, 1000);
}

function interval_sms() {
    var interval_sms = setInterval(function () {
        console.log("********** " + $("#taskid").val());
        var taskid = $("#taskid").val();
        $.ajax({
            type: "POST",
            url: "/h5/tb/tasks/status",
            data: {
                taskid: taskid
            },
            dataType: "json",
            error: function (request) {
                console.log("error");
                clearInterval(interval_sms); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function (data) {
                $('#message').text(data.description); //反馈信息
                if (data.phase == "WEBDRIVER" && data.phase_status == "ERROR") {//爬取失败
                    clearInterval(interval_sms);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/alipay"
                    });
                    return;
                }
                if (data.phase == "WEBDRIVER" && data.phase_status == "ERROR") {//爬取失败
                    clearInterval(interval_sms);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/alipay"
                    });
                    return;
                }
                if (data.phase == "LOGIN" && data.phase_status == "SUCCESS_NEXTSTEP") {
                    clearInterval(interval_sms);
                    crawler(); //调用数据采集接口
                    $('#myModal').modal('hide');$('#waitinView').css("display","flex");
                    return;
                }
                if (data.finished) { //数据采集成功
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
        })
    }, 1000);
}

/**
 * 间隔一秒调用获取task状态的方法
 */
function interval_crawler() {
    var interval_crawler = setInterval(function (){
        console.log("********** "+$("#taskid").val());
        var taskid = $("#taskid").val();
        var logintype = $("#logintype").val();
        $.ajax({
            type: "POST",
            url:"/h5/tb/tasks/status",
            data:{
                taskid: taskid,
                logintype:logintype
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_crawler); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                //$('#message').text(data.description); //反馈信息
                if (data.phase == "NEED_SWEEP_QR_CODE"||data.phase_status == 'WAIT_SCAN') {}
                if (data.phase == "NEED_COMFIRM_QR_CODE"||data.phase_status == "WAIT_CONFIRM") {
                    $('#message').text(data.description);
                    $('#myModal').modal('show');
                }
                if (data.phase == "LOGIN" && data.phase_status == "SUCCESS_NEXTSTEP") {
                    clearInterval(interval_crawler);
                    $('#waitinView').css("display","flex");
                    crawler(); //调用数据采集接口
                    $('#is_qr').text('yes');
                }
                if ((data.phase == "LOGIN")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    console.log("login+error");
                    clearInterval(interval_crawler);
                    $('#waitinView').css("display","none");
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/alipay"
                    });
                    return;
                }
                if ((data.phase == "CRAWLER")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//爬取失败
                    clearInterval(interval_crawler);
                    $('#waitinView').css("display","none");
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/alipay"
                    });
                    return;
                }

                if (data.finished) { //数据采集成功

                    clearInterval(interval_crawler);
                    $('#waitinView').css("display","none");
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
                //if(data.finished && data.phase != "CRAWLER"){
                //    clearInterval(interval_crawler);
                //    $('#waitinView').css("display","none");
                //    //弹出提示框
                //    $('#msg').modal('show');
                //    $('#alertMsg').text("系统超时，请重新登录");
                //    $('#msg').find("button").bind("click",function(){
                //        location.href = "/h5/alipay"
                //    });
                //}
            }
        });
        var countdown = $("#countdown").text(); var is_qr = $('#is_qr').text();
        if (countdown == 0 && is_qr =="no") {
            clearInterval(interval_crawler);
        }
    }, 1000);

    $('#myTab').find('li').click( function() {
        if(!$(this).hasClass("active")&&$(this).index()==1){
            clearInterval(interval_crawler);
            console.log('clearInterval(interval_QRcode');
        }
    })
}

$(function(){
    themeColor = $("#themeColor").val();
    themeColorCss = "#" + themeColor;
    console.log("themeColor: "+themeColor);
    //themeColorCss = "#eda79a";
    //themeColorCss = "#b92226";

    topHide = $("#topHide").val();
    //topHide = "block";
    setTopHide(topHide);
    themecss(themeColorCss);
})

$("#myTab").delegate("li", "click", function () {
    if(!$(this).hasClass("active")&&$(this).index()==1){
        $('#logintype').val("alipay_name");
    }
    if(!$(this).hasClass("active")&&$(this).index()==0){
        $('#logintype').val("tb_qr");
    }
    if($('#qr_state').text()==1&&!$(this).hasClass("active")&&$(this).index()==0){
        console.log($('#qr_state').text()==1&&!$(this).hasClass("active")&&$(this).index()==0);
        $('#myTab li:eq(0) a').tab('show');
        nextQRcode();
        console.log(1);
        return false;
    }
    if($('#qr_state').text()==0&&!$(this).hasClass("active")&&$(this).index()==0) {
            setTimeout(interval_crawler, 1000);
    }
})

function nextQRcode(){
    var taskid = $("#taskid").val();
    var logintype = $("#logintype").val();
    $.ajax({
        type: "POST",
        url:"/h5/tb/base64Ap",
        async: true,
        data:{
            taskid: taskid,
            logintype: logintype
        },
        dataType : "json",
        beforeSend: function () {
            //interval_login();
            //$('#message').text('开始准备...');
            //$('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
            quit();
            $('#msg').modal('show');
            $('#alertMsg').text("系统忙 请稍后再试");
            $('#msg').find("button").bind("click",function(){
                location.href = "/h5/alipay"
            });
            return;
            //clearInterval(interval_login); //停止间隔一秒调用
        },
        success: function(data) {
            if (data.phase == "GET_LOGIN_QR_CODE_ERROR" && data.phase_status == "ERROR"){
                $('#msg').modal('show');
                $('#alertMsg').text("系统忙 请稍后再试");
                $('#msg').find("button").bind("click",function(){
                    location.href = "/h5/alipay"
                });
                return;
            }
            if(data.phase == "SYSTEM" && data.phase_status == "QUIT"){
                $('#msg').modal('show');
                $('#alertMsg').text(data.description);
                $('#msg').find("button").bind("click",function(){
                    location.href = "/h5/alipay"
                });
                return;
            }
            if(data.phase == "AGENT" && data.phase_status == "ERROR"){
                $('#msg').modal('show');
                $('#alertMsg').text(data.description);
                $('#msg').find("button").bind("click",function(){
                    location.href = "/h5/alipay"
                });
                return;
            }
            if(data.phase == "LOGIN" && data.phase_status == "WAIT_SCAN"){
                $('.qrcode-login-btn').show();
                $('.loader').hide();
                $(".qrcode-image").attr("src","data:image/png;base64,"+data.baseCode).show();
                $('#qr_state').text("0");
                $(".qrcode-title").text("扫码授权");
                console.log('checkQRcodeAp前');
                $('.qrcode-login-btn').bind("click",function(){
                   // confirm("郭杨和小代是好朋友吗？");

                    window.opener=null;
                    setTimeout(function() {
                        window.close();
                      window.history.back();
                    }, 500);
                    window.open(data.qrUrl+'&ReturnUrl=https://i.jd.com/user/info','_self')
                    //window.location.href = decodeURIComponent(data.qrUrl);
                    // $.getJSON(data.qrUrl, function(data) {});

                    //$.getJSON(data.qrUrl,function (json) {
                    //    alert(json);
                    //});

                    //$.ajax({
                    //    type : "get",
                    //    url : data.qrUrl,
                    //    dataType : "jsonp",//数据类型为jsonp
                    //    crossDomain: true,
                    //    success : function(data){
                    //       alert('suces')
                    //    },
                    //    error:function(){
                    //       alert('fail1');
                    //    }
                    //});
                });
                checkQRcodeAp();
                setTimeout(function(){
                    interval_crawler();
                },1000)
                console.log('checkQRcodeAp1后');
                return;
            }
            //var countdown = $("#countdown").text();
            //function Timer(){
            //    if (countdown == 0 && $('#is_qr').text()=="no") {
            //        quit();
            //        setTimeout(function(){
            //            $('#msg').modal('show');
            //            $('#alertMsg').text("二维码已过期");
            //            $('#msg').find("button").bind("click",function(){
            //                location.href = "/h5/alipay"
            //            });
            //        },100);
            //    } else {
            //        countdown -= 1;
            //        $("#countdown").text(countdown);
            //        setTimeout(Timer,1000);
            //    }
            //}
            //Timer();
        }
    });
}

//var iTime;
function checkQRcodeAp() {
    console.log('checkQRcodeAp方法内');
    var taskid = $("#taskid").val();
        $.ajax({
            type: "POST",
            url:"/h5/tb/checkQRcodeAp",
            data:{
                taskid: taskid
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                //clearInterval(interval_QRcode); //停止间隔一秒调用
                //$('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                //else{
                //    clearInterval(interval_QRcode);
                //    //弹出提示框
                //    $('#msg').modal('show');
                //    $('#alertMsg').text(data.description);
                //    $('#msg').find("button").bind("click",function(){
                //        location.href = "/h5/alipay"
                //    });
                //}
             }
            });
}

function quit(){
    var taskid = $("#taskid").val();
    $.ajax({
        type: "POST",
        url:"/h5/tb/quit",
        data:{
            taskid: taskid
        },
        async:false,
        dataType : "json",
        error: function(request) {
            console.log("errorquit");
            return;
        },
        success: function(data) {

        }
    });
}

function getQueryString() {
    var qs = location.search.substr(1), // 获取url中"?"符后的字串
        args = {}, // 保存参数数据的对象
        items = qs.length ? qs.split("&") : [], // 取得每一个参数项,
        item = null,
        len = items.length;

    for(var i = 0; i < len; i++) {
        item = items[i].split("=");
        var name = decodeURIComponent(item[0]),
            value = decodeURIComponent(item[1]);
        if(name) {
            args[name] = value;
        }
    }
    return args;
}

 if(getQueryString('source')=='alipayqr'){
     $("#myTab").find('li').eq(0).trigger("click");
 }

$('#jump_a').click( function() {
    quit();
    window.location.href ="/h5/tb/item"+location.search +"&source=tbqr"
});

