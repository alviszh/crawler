var themeColor = "";
var topHide = "";
var owner = "";
var appActive = "";//打包环境
var onLine;//是否有网
var errInfo = "网络不给力，请重试";

/*设置主题色*/
function themecss(themeColor){
    $('#nextBtn').css("background-color", themeColor);
    $('a').css("border-color", themeColor);
    $('nav').css("background-color", themeColor);
    $('nav').css("border-color", themeColor);
    $('nav span').css("color","white");
    $('button').css("background-color", themeColor);
    $('button').css("border-color", themeColor);

    $("div[class='panel-heading']").css("background-color", themeColor);
    $("div[class='panel-heading']").css("border-color", themeColor);
    $("div[class='panel panel-info']").css("border-color", themeColor);
}

/*设置顶部隐藏*/
function setTopHide(topHide){
    console.log("display:"+topHide);
    $('nav').css("display", topHide);
    if (topHide == "none") {
        $('#br').append("<br/>"); //加换行符
    }
}

$(function(){
    checkNetwork();//检查网络
    appActive = $("#appActive").val();
    themeColor = $("#themeColor").val();
    console.log("appActive:"+appActive);
    console.log("themeColor: "+themeColor);
    //themeColor = "#eda79a";
    //themeColor = "#b92226";

    topHide = $("#topHide").val();
    //topHide = "block";
    setTopHide(topHide);
    themecss(themeColor);
});

//input获取焦点
$('input').bind('focus', function(){
    $(this).css("border-color",themeColor);
    $(this).css("outline","none");
    $(this).css("box-shadow","0 0 5px " + themeColor);
    $(this).css("border-radius","0 5px 5px 0");
});
//input失去焦点
$('input').bind('blur', function(){
    $(this).css("border-color","none");
    $(this).css("outline","none");
    $(this).css("box-shadow","none");
    $(this).css("border","1px solid #ccc");
});
//input-group span在右边
$("input[name='mobileMsg']").bind('focus', function(){
    $(this).css("border-color",themeColor);
    $(this).css("outline","none");
    $(this).css("box-shadow","0 0 5px " + themeColor);
    $(this).css("border-radius","5px 0 0 5px");
});
//input-group span在右边
$("input[id='sms_code']").bind('focus', function(){
    $(this).css("border-color",themeColor);
    $(this).css("outline","none");
    $(this).css("box-shadow","0 0 5px " + themeColor);
    $(this).css("border-radius","5px 0 0 5px");
});

//button按下事件
$('button').bind('mousedown', function(){
    //$(this).css("box-shadow","0 0 5px " + themeColor);
    $(this).css("outline","none");
});

/*跳转认证首页面*/
$('#modal_btn').click( function() {
    console.log("themeColor:" + themeColor);
    var isTopHide = false;
    if (topHide == "none") {
        isTopHide = true;
    }
    var task_id = $("#task_id").val();
    var key = $("#key").val();
    //sendResult(task_id, key); //调用回调接口

    //跳转页面（回调地址）
    var redirectUrl = $('#redirectUrl').val();
    if (redirectUrl == "") {
        window.location.href="/h5/carrier/auth?owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+
            "&themeColor="+themeColor+"&isTopHide="+isTopHide;
    } else {
        window.location.href = redirectUrl;
    }
});

/*爬取成功状态*/
function result(){
    console.log("********** "+$("#task_id").val());
    var taskid = $("#task_id").val();
    $.ajax({
        type: "GET",
        url:"/h5/carrier/tasks/status",
        data:{
            taskid: taskid
        },
        dataType : "json",
        beforeSend: function () {
            $("#successMsg").empty();
        },
        error: function(request) {
            console.log("error");
            return;
        },
        success: function(data) {
            console.log(data.phase+","+data.phase_status);
            console.log(data.description);

            owner = data.owner; //保存数据所属人
            console.log("数据所属人="+owner);
            $("#successMsg").empty();

            var carrier = "";
            if (data.carrier == "CMCC") {
                carrier = "中国移动";
            } else if (data.carrier == "UNICOM") {
                carrier = "中国联通";
            } else if (data.carrier == "CHINA_TELECOM") {
                carrier = "中国电信";
            } else if (data.carrier == "VNO") {
                carrier = "虚拟运营商";
            }
            if (appActive == "prod") { //生成环境

                $("#successMsg").append("<p class='bg-success'>手机号码："+ data.phonenum + "</p>");
                $("#successMsg").append("<p class='bg-success'>运营商："+ carrier + "</p>");
                $("#successMsg").append("<p class='bg-success'>归属地：" + data.province + "</p>");
                $("#successMsg").append("<p class='bg-success'>编号：" + taskid +"</p>");

            } else { //测试、本地环境
                // 用户信息
                if (data.userMsgStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【用户信息】采集已完成</p>");
                } else if (data.userMsgStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【用户信息】采集已完成，无数据</p>");
                } else if (data.smsRecordStatus == 202 && data.carrier == "CHINA_TELECOM") {
                    $("#successMsg").append("<p class='bg-info'>【用户信息】页面异常</p>");
                } else {
                    $("#successMsg").append("<p class='bg-success'>【用户信息】采集已完成，状态码："+data.userMsgStatus+"</p>");
                }
                // 账户信息
                if (data.accountMsgStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【账户信息】采集已完成</p>");
                } else if (data.accountMsgStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【账户信息】采集已完成，无数据</p>");
                } else if (data.smsRecordStatus == 202 && data.carrier == "CHINA_TELECOM") {
                    $("#successMsg").append("<p class='bg-info'>【账户信息】页面异常</p>");
                } else {
                    $("#successMsg").append("<p class='bg-success'>【账户信息】采集已完成，状态码："+data.accountMsgStatus+"</p>");
                }
                // 通话详单
                if (data.callRecordStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【通话详单】采集已完成</p>");
                } else if (data.callRecordStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【通话详单】采集已完成，无数据</p>");
                } else if (data.smsRecordStatus == 202 && data.carrier == "CHINA_TELECOM") {
                    $("#successMsg").append("<p class='bg-info'>【通话详单】页面异常</p>");
                } else {
                    $("#successMsg").append("<p class='bg-success'>【通话详单】采集已完成，状态码："+data.callRecordStatus+"</p>");
                }
                // 短信记录
                if (data.smsRecordStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【短信记录】采集已完成</p>");
                } else if (data.smsRecordStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【短信记录】采集已完成，无数据</p>");
                } else if (data.smsRecordStatus == 202 && data.carrier == "CHINA_TELECOM") {
                    $("#successMsg").append("<p class='bg-info'>【短信记录】页面异常</p>");
                } else {
                    $("#successMsg").append("<p class='bg-success'>【短信记录】采集已完成，状态码："+data.smsRecordStatus+"</p>");
                }
                // 业务信息
                if (data.businessMsgStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【业务信息】采集已完成</p>");
                } else if (data.businessMsgStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【业务信息】采集已完成，无数据</p>");
                } else if (data.smsRecordStatus == 202 && data.carrier == "CHINA_TELECOM") {
                    $("#successMsg").append("<p class='bg-info'>【业务信息】页面异常</p>");
                } else {
                    $("#successMsg").append("<p class='bg-success'>【业务信息】采集已完成，状态码："+data.businessMsgStatus+"</p>");
                }
                // 缴费信息
                if (data.payMsgStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【缴费信息】采集已完成</p>");
                } else if (data.payMsgStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【缴费信息】采集已完成，无数据</p>");
                } else if (data.smsRecordStatus == 202 && data.carrier == "CHINA_TELECOM") {
                    $("#successMsg").append("<p class='bg-info'>【缴费信息】页面异常</p>");
                } else {
                    $("#successMsg").append("<p class='bg-success'>【缴费信息】采集已完成，状态码："+data.payMsgStatus+"</p>");
                }
                // 积分信息
                if (data.integralMsgStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【积分信息】采集已完成</p>");
                } else if (data.integralMsgStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【积分信息】采集已完成，无数据</p>");
                } else if (data.smsRecordStatus == 202 && data.carrier == "CHINA_TELECOM") {
                    $("#successMsg").append("<p class='bg-info'>【积分信息】页面异常</p>");
                } else {
                    $("#successMsg").append("<p class='bg-success'>【积分信息】采集已完成，状态码："+data.integralMsgStatus+"</p>");
                }

                $("#successMsg").append("<p>手机号码："+ data.phonenum +"，运营商："+ carrier +"，归属地：" + data.province + "，taskid："+ taskid +"</p>");

            }
        }
    });
}

/**
 * 查询所属运营商
 */
function findMobileSegment(phonenum){
    $.ajax({
        type: "GET",
        url: "/h5/carrier/findMobileSegment",
        data: {
            phonenum: phonenum
        },
        dataType: "json",
        error: function (request) {
            console.log("error");
            return;
        },
        success: function(data) {
            $("#catnameMsg").empty();
            var dirMobileSegment = data.data;
            if (dirMobileSegment == null) {
                $("#catnameMsg").append(data.message);
                $("#catnameMsgDiv").css("display","none");
                return;
            }
            var catname = dirMobileSegment.catname; //所属运营商
            $("#catnameMsg").append('您的手机号运营商是<font style="color: red;">'+catname+'</font' +
                '>，如果不是<a href="#" id="catnameClick" style="font-weight:bold;">请点击此处</a>切换。');
            //显示div
            $("#catnameMsgDiv").css("display","");
            $("#catnameMsgDiv").css("visibility","visible");

            //点击显示运营商
            $('#catnameClick').click( function() {
                $("#catnameDiv").css("display","flex");
                $("#catnameDiv").css("visibility","visible");
            });

            if (catname == "中国电信") {
                $("#catname1").attr("checked", false);
                $("#catnameLab1").removeClass("active");
                $("#catname2").attr("checked", true);
                $("#catnameLab2").addClass("active");
                $("#catname3").attr("checked", false);
                $("#catnameLab3").removeClass("active");
            } else if (catname == "中国移动") {
                $("#catname1").attr("checked", false);
                $("#catnameLab1").removeClass("active");
                $("#catname2").attr("checked", false);
                $("#catnameLab2").removeClass("active");
                $("#catname3").attr("checked", true);
                $("#catnameLab3").addClass("active");
            } else if (catname == "中国联通") {
                $("#catname1").attr("checked", true);
                $("#catnameLab1").addClass("active");
                $("#catname2").attr("checked", false);
                $("#catnameLab2").removeClass("active");
                $("#catname3").attr("checked", false);
                $("#catnameLab3").removeClass("active");
            } /*else if (catname == "虚拟运营商") {
             $("#catname4").attr("checked", true);
             $("#catnameLab4").addClass("active");
             } else { //未知的运营商
             $("#catname5").attr("checked", true);
             $("#catnameLab5").addClass("active");
             }*/
            var province = dirMobileSegment.province;
            console.log(province);
            $('#province').val(province);//省（保存在隐藏域中）

            //显示温馨提示
            if (province == "甘肃" && catname == "中国电信") {
                $("#prompt").append("<small class='form-text text-muted'>温馨提示：<br />*甘肃省电信：由于甘肃省电信网站不让频繁点击访问，所以爬取时间比较长，请耐心等待！<br /> </small>");
            }
            return;
        }
    });
}
//调用回调接口
/*function sendResult(taskId, key){
    var isTopHide = false;
    var owner = $("#owner").val();
    if (topHide == "none") {
        isTopHide = true;
    }
    var key = $("#key").val();
    var redirectUrl = $('#redirectUrl').val();

    $.ajax({
        type: "POST",
        url: "/h5/carrier/mobile/sendResult",
        data: {
            taskId: taskId,
            key: key,
            owner: owner
        },
        dataType: "json",
        error: function (request) {
            console.log("调用回调接口失败！");
            if (redirectUrl == "") {
                window.location.href="/h5/carrier/auth?owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+
                    "&themeColor="+themeColor+"&isTopHide="+isTopHide;
            } else {
                window.location.href = redirectUrl;
            }
            return;
        },
        success: function(data) {
            console.log("调用回调接口成功！redirectURL=" + redirectUrl);
            console.log(data);
            if (redirectUrl == "") {
                window.location.href="/h5/carrier/auth?owner="+owner+"&key=" + key + "&redirectUrl=" +encodeURIComponent(redirectUrl)+
                    "&themeColor="+themeColor+"&isTopHide="+isTopHide;
            } else {
                window.location.href = redirectUrl;
            }
        }
    });
}*/

function checkNetwork(){
    onLine = navigator.onLine;
    if(onLine){
        //$$("status").innerHTML="第一次加载时在线";
        console.log("第一次加载时在线");
    }else{
        console.log("第一次加载时离线");
    }
    window.addEventListener("online",online,false);
    function online(){
        onLine = true;
        console.log("on");
    }

    window.addEventListener("offline",offline,false);
    function offline(){
        onLine = false;
        console.log("off");
    }
}

