/**
 * 表单控制
 */
//$('.tex_box').eq(0).click( function() {
//    $(this).prev('input').val("");
//});

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

/**
 * 登录爬取
 */
function nextLoginD(){
    $.ajax({
        type: "POST",
        url:"/h5/bank/loginD",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        beforeSend: function () {
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
            interval_loginD();
        },
        error: function(request) {
            console.log("error+login");
            clearInterval(interval_loginD); //停止间隔一秒调用

            //$('#myModal').modal('hide');
            return;
        },
        success: function(data) {
            console.log("成功+loginD");
            if($('#bankType').val()=="渤海银行"||($('#bankType').val()=="建设银行"&&$('#loginType').val()=="ACCOUNT_NUM"&&$('#cardType').val()=="DEBIT_CARD")){
                if((data.phase == "LOGIN" && data.phase_status == "SUCCESS_NEEDSMS")){
                var taskid = $("#taskid").val();
                var bankType = $("#bankType").val();
                var loginName = $("input[name='loginName']").not(':disabled').val();
                var cardType = $("#cardType").val();
                window.location.href = "/h5/bank/qacode?taskid=" + taskid + "&bankType=" + bankType
                + "&loginName=" + loginName + "&cardType=" + cardType + "&loginType=" + loginType + "&question=" + data.question
                }
            }
            return;
        }
    });
}


/**
 * 间隔一秒调用获取task状态的方法（登录）
 */
function interval_loginD() {
    var interval_loginD = setInterval(function (){
        console.log("********** "+$("#taskid").val());
        var taskid = $("#taskid").val();
        var loginType = $("#loginType").val();
        $.ajax({
            type: "POST",
            url:"/h5/bank/tasks/status",
            data:{
                taskid: taskid ,
                loginType:loginType
            },
            async: false,
            dataType : "json",
            error: function(request) {
                console.log("error+login");
                clearInterval(interval_loginD); //停止间隔一秒调用
                $('#myModal').modal('hide');
                $('#msg').modal('show');
                $('#alertMsg').text('网络超时');
                $('#msg').find("button").bind("click",function(){
                    location.href = "/h5/bank"
                });
                return;
            },
            success: function(data) {
                var isTopHide = false;
                if (topHide == "none") {
                    isTopHide = true;
                }
                var bankType = $("#bankType").val();
                data.description==null?$('#message').text("正在准备中..."):$('#message').text(data.description); //反馈信息

                if((data.phase == "LOGIN" && data.phase_status == "DOING")||(data.phase == null && data.phase_status == null)||(data.phase == "SEND_CODE" && data.phase_status == "DONING")||(data.phase == "VALIDATE" && data.phase_status == "DONING")){
                    console.log("LOGIN+DOING");
                }
                else if(data.phase == "LOGIN" && data.phase_status == "SUCCESS_NEXTSTEP"){
                    console.log("SUCCESS_NEXTSTEP");
                    if(bankType !="建设银行") {
                        clearInterval(interval_loginD);
                        crawlerD(); //调用数据采集接口
                        return;
                    }
                }
                else if((data.phase == "AGENT" && data.phase_status == "ERROR")||data.error_code == -1){
                    console.log("AGENT+ERROR");
                    clearInterval(interval_loginD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }

                else if ((data.phase == "LOGIN")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    console.log("login+error");
                    clearInterval(interval_loginD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }

                else if((data.phase == "LOGIN" && data.phase_status == "SUCCESS_NEEDSMS")){
                    clearInterval(interval_loginD);
                    var taskid = $("#taskid").val();var loginName = $("input[name='loginName']").not(':disabled').val();var cardType = $("#cardType").val();
                    var loginType = $("#loginType").val();
                    if(bankType =="渤海银行") {
                        //window.location.href = "/h5/bank/qacode?themeColor=" + themeColor + "&isTopHide=" + isTopHide + "&taskid=" + taskid + "&bankType=" + bankType
                            //+ "&loginName=" + loginName + "&cardType=" + cardType + "&loginType=" + loginType
                    }
                    else{
                        window.location.href = "/h5/bank/smscode?themeColor=" + themeColor + "&isTopHide=" + isTopHide + "&taskid=" + taskid + "&bankType=" + bankType
                         + "&loginName=" + loginName + "&cardType=" + cardType + "&loginType=" + loginType
                    }
                    return;
                }
                else if(data.phase == "SEND_CODE" && data.phase_status == "FAIL") {
                    console.log("5");
                    clearInterval(interval_loginD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }
                else if ((data.phase == "WAIT_CODE")&& (data.phase_status == "SUCCESS")) {
                    console.log("6");
                    clearInterval(interval_loginD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $("#sms_code").removeAttr("disabled");
                    $('#alertMsg').text(data.description);
                    return;
                }
                else{
                    console.log("7+else");
                    var bt= $("#bankType").val();
                    if(bt =="农业银行"|| bt =="工商银行"|| bt =="浦发银行"|| bt =="建设银行"){
                        console.log(1);
                        if ((data.phase == "CRAWLER")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//爬取失败
                            clearInterval(interval_loginD);
                            $('#myModal').modal('hide');
                            //弹出提示框
                            $('#msg').modal('show');
                            $('#alertMsg').text(data.description);
                            $('#msg').find("button").bind("click",function(){
                                location.href = "/h5/bank"
                            });
                            return;
                        }

                        if (data.finished && data.phase == "CRAWLER") { //数据采集成功
                            clearInterval(interval_loginD);
                            $('#myModal').modal('hide');
                            //themeColor = themeColor.substr(1,themeColor.length);
                            var isTopHide = false;
                            if (topHide == "none") {
                                isTopHide = true;
                            }
                            var taskid= $("#taskid").val();
                            result(taskid);
                            $('#success_modal').modal('show');
                            return;
                        }
                        if(data.finished && data.phase != "CRAWLER"){
                            clearInterval(interval_loginD);
                            $('#myModal').modal('hide');
                            //弹出提示框
                            $('#msg').modal('show');
                            $('#alertMsg').text("系统超时，请重新登录");
                            $('#msg').find("button").bind("click",function(){
                                location.href = "/h5/bank"
                            });
                            return;
                        }
                    }
                    else{
                        console.log(2);
                        clearInterval(interval_loginD);
                        crawlerD(); //调用数据采集接口
                        return;
                    }
                }

            }
        });
    }, 1000);
}

function nextSendSmsCodeD(){
    var ln;
    ($("#loginName").text()=="") ? ln=$("#loginName").val() :ln=$("#loginName").text();

    $.ajax({
        type: "POST",
        url:"/h5/bank/sendSmsCodeD",
        data:{
            taskid:$("#taskid").val(),
            bankType:$("#bankType").val(),
            loginName:ln,
            cardType:$("#cardType").val(),
            loginType:$("#loginType").val(),
            verification:$("#sms_code").val(),
            password:$("#password").val(),
        },// 你的formid
        async: true,
        beforeSend: function () {
            interval_sendSmsD();
            $('#message').text('正在验证中...');
            $('#myModal').modal('show');
            return;
        },
        error: function(request) {
            console.log("error+sendSmsCodeD");
            clearInterval(interval_sendSmsD); //停止间隔一秒调用
            return;
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}


function  interval_sendSmsD() {
    var interval_sendSmsD = setInterval(function (){
        console.log("********** "+$("#taskid").val());
        var taskid = $("#taskid").val();
        var loginType = $("#loginType").val();
        var bankType = $("#bankType").val();
        var cardType = $("#cardType").val();
        $.ajax({
            type: "POST",
            url:"/h5/bank/tasks/status",
            data:{
                taskid: taskid,
                loginType:loginType
            },
            async: false,
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_sendSmsD); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息
                if ((data.phase == "LOGIN")&& (data.phase_status == "ERROR")) {
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }
                if ((data.phase == "SEND_CODE")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//爬取失败
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }

                if ((data.phase == "WAIT_CODE")&& (data.phase_status == "SUCCESS")) {
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                }
                if (data.phase == "VALIDATE" && data.phase_status == "SUCCESS") {//验证成功！
                    console.log("验证成功");
                    // $('#myModal').modal('hide');
                    // $('#smsCode').modal('show');
                    if(bankType=="中信银行" && cardType =="CREDIT_CARD"){
                        console.log("中信crawler");
                        crawlerD(); //调用数据采集接口
                        clearInterval(interval_sendSmsD);
                    }
                    return;
                }
                if (data.phase == "VALIDATE" && (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }
                if ((data.phase == "CRAWLER")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//爬取失败
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }

                if (data.finished && data.phase == "CRAWLER") { //数据采集成功
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    result(taskid);
                    $('#success_modal').modal('show');
                    return;
                }
                if(data.finished && data.phase != "CRAWLER"){
                    clearInterval(interval_sendSmsD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text("系统超时，请重新登录");
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }
            }
        });
    }, 1000);

}


function smsVerfiyD(){
    $.ajax({
        type: "POST",
        url:"/h5/bank/smsverfiyD",
        data:{
            taskid:$("#taskid").val(),
            verification:$("#sms_code").val(),
            bankType:$("#bankType").val(),
            cardType:$("#cardType").val()
          },
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_smsVerfiyD();
            $('#message').text('正在验证...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_smsVerfiyD); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}

function interval_smsVerfiyD() {
    var interval_smsVerfiyD = setInterval(function (){
        console.log("********** "+$("#task_id").val());
        var taskid = $("#taskid").val();
        $.ajax({
            type: "POST",
            url:"/h5/bank/tasks/status",
            data:{
                taskid: taskid
            },
            async: false,
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_smsVerfiyD); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息

                if (data.phase == "VALIDATE" && data.phase_status == "SUCCESS") {//认证成功！
                    clearInterval(interval_smsVerfiyD);
                   // $('#myModal').modal('hide');
                   // $('#smsCode').modal('show');
                    crawlerD(); //调用数据采集接口
                    return;

                } else if (data.phase == "VALIDATE" && (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    clearInterval(interval_smsVerfiyD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }
                else if(data.finished && data.phase != "CRAWLER"){
                    clearInterval(interval_smsVerfiyD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text("系统超时，请重新登录");
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                }

            }
        });
    }, 1000);
}

function crawlerD(){
    $.ajax({
        type: "POST",
        url:"/h5/bank/crawlerD",
        data:$('#authform').serialize(),
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_crawlerD();
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_crawlerD); //停止间隔一秒调用
            return;
        },
        success: function(data) {
            console.log(data.error_message);
            return;
        }
    });
}


/**
 * 直接爬取
 */
function nextSubmitD(){
    $.ajax({
        type: "POST",
        url:"/h5/bank/loginD",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_crawlerD();
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
function interval_crawlerD() {
    var interval_crawlerD = setInterval(function (){
        console.log("********** "+$("#taskid").val());
        var taskid = $("#taskid").val();
        var loginType = $("#loginType").val();
        $.ajax({
            type: "POST",
            url:"/h5/bank/tasks/status",
            data:{
                taskid: taskid,
                loginType:loginType
            },
            async: false,
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_crawlerD); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                $('#message').text(data.description); //反馈信息
                if(interval_crawlerD==0){return}
                if ((data.phase == "LOGIN")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//认证失败！
                    console.log("login+error");
                    clearInterval(interval_crawlerD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                     console.log(1);
                    return;
                }
                if ((data.phase == "CRAWLER")&& (data.phase_status == "FAIL"||data.phase_status == "ERROR")) {//爬取失败
                    clearInterval(interval_crawlerD);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/bank"
                    });
                    return;
                    console.log(2);
                }

                if (data.finished) { //数据采集成功
                    console.log(3);
                    clearInterval(interval_crawlerD);
                    interval_crawlerD=0;
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    $('#success_modal').modal('show');
                    result(taskid);
                    console.log(4);
                    return;
                }
                //if(data.finished && data.phase != "CRAWLER"){
                //    clearInterval(interval_crawlerD);
                //    $('#myModal').modal('hide');
                //    //弹出提示框
                //    $('#msg').modal('show');
                //    $('#alertMsg').text("系统超时，请重新登录");
                //    $('#msg').find("button").bind("click",function(){
                //        location.href = "/h5/bank"
                //    });
                //}
            }
        });
    }, 1000);
}


/**
 * 登录爬取
 */
function nextFirstV(){
    $.ajax({
        type: "POST",
        url:"/h5/bank/firstV",
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            $('#message').text('正在验证...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error+nextFirstV");
            //$('#myModal').modal('hide');
            return;
        },
        success: function(data) {
            var taskid = $("#taskid").val();var bankType = $("#bankType").val();var loginName = $("input[name='loginName']").not(':disabled').val();var cardType = $("#cardType").val();
            if(data.phase == "VALIDATE" && data.phase_status == "SUCCESS"){
                 crawlerD();
            }
            else if(data.phase == "VALIDATE" && data.phase_status == "FAIL"){
                $('#myModal').modal('hide');
                //弹出提示框
                $('#msg').modal('show');
                $('#alertMsg').text(data.description);
                $('#msg').find("button").bind("click",function(){
                    location.href = "/h5/bank"
                });
                return;
            }
            else{
                $('#myModal').modal('hide');
                //themeColor = themeColor.substr(1,themeColor.length);
                var isTopHide = false;
                if (topHide == "none") {
                    isTopHide = true;
                }
                result(taskid);
                $('#success_modal').modal('show');
                return;
            }
            console.log("成功+nextFirstV");
            return;
        }
    });
}

//测试用的数据，这里可以用AJAX获取服务器数据
var test_list = ["小张", "小苏", "小杨", "老张", "老苏", "老杨", "老爷爷", "小妹妹", "老奶奶", "大鹏", "大明", "大鹏展翅", "你好", "hello", "hi"];
var old_value = "";
var highlightindex = -1;   //高亮
//自动完成
function AutoComplete(auto, search, mylist) {
    if ($("#" + search).val() != old_value || old_value == "") {
        var autoNode = $("#" + auto);   //缓存对象（弹出框）
        var carlist = new Array();
        var n = 0;
        old_value = $("#" + search).val();
        for (i in mylist) {
            if (mylist[i].indexOf(old_value) >= 0) {
                carlist[n++] = mylist[i];
            }
        }
        if (carlist.length == 0) {
            autoNode.hide();
            return;
        }
        autoNode.empty();  //清空上次的记录
        for (i in carlist) {
            var wordNode = carlist[i];   //弹出框里的每一条内容
            var newDivNode = $("<div>").attr("id", i);    //设置每个节点的id值
            newDivNode.attr("style", "font:14px/25px arial;height:25px;padding:0 8px;cursor: pointer;");
            newDivNode.html(wordNode).appendTo(autoNode);  //追加到弹出框
            //鼠标移入高亮，移开不高亮
            newDivNode.mouseover(function () {
                if (highlightindex != -1) {        //原来高亮的节点要取消高亮（是-1就不需要了）
                    autoNode.children("div").eq(highlightindex).css("background-color", "white");
                }
                //记录新的高亮节点索引
                highlightindex = $(this).attr("id");
                $(this).css("background-color", "#ebebeb");
            });
            newDivNode.mouseout(function () {
                $(this).css("background-color", "white");
            });
            //鼠标点击文字上屏
            newDivNode.click(function () {
                //取出高亮节点的文本内容
                var comText = autoNode.hide().children("div").eq(highlightindex).text();
                highlightindex = -1;
                //文本框中的内容变成高亮节点的内容
                $("#" + search).val(comText);
            })
            if (carlist.length > 0) {    //如果返回值有内容就显示出来
                autoNode.show();
            } else {               //服务器端无内容返回 那么隐藏弹出框
                autoNode.hide();
                //弹出框隐藏的同时，高亮节点索引值也变成-1
                highlightindex = -1;
            }
        }
    }
    //点击页面隐藏自动补全提示框
    document.onclick = function (e) {
        var e = e ? e : window.event;
        var tar = e.srcElement || e.target;
        if (tar.id != search) {
            if ($("#" + auto).is(":visible")) {
                $("#" + auto).css("display", "none")
            }
        }
    }
}

