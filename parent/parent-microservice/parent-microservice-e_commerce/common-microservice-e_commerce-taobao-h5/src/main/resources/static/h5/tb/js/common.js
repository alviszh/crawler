var themeColor = "";
var topHide = "";
var themeColorCss = "";
/*设置主题色*/
function themecss(themeColorCss){
    $('#nextBtn').css("background-color", themeColorCss);
    $('a.btn').css("border-color", themeColorCss);
    $('nav').css("background-color", themeColorCss);
    $('nav').css("border-color", themeColorCss);
    $('nav span').css("color","white");
    $('button').css("background-color", themeColorCss);
    $('button').css("border-color", themeColorCss);

    $("div[class='panel-heading']").css("background-color", themeColorCss);
    $("div[class='panel-heading']").css("border-color", themeColorCss);
    $("div[class='panel panel-info']").css("border-color", themeColorCss);
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
    themeColor = $("#themeColor").val();
    themeColorCss = "#" + themeColor;
    console.log("themeColor: "+themeColor);
    //themeColorCss = "#eda79a";
    //themeColorCss = "#b92226";

    topHide = $("#topHide").val();
    //topHide = "block";
    setTopHide(topHide);
    themecss(themeColorCss);
});

//input获取焦点
$('input').bind('focus', function(){
    $(this).css("border-color",themeColorCss);
    $(this).css("outline","none");
    $(this).css("box-shadow","0 0 5px " + themeColorCss);
    $(this).css("border-radius","0 5px 5px 0");
});
//input失去焦点
$('input').bind('blur', function(){
    $(this).css("border-color","none");
    $(this).css("outline","none");
    $(this).css("box-shadow","none");
});
//input-group span在右边
$("input[name='mobileMsg']").bind('focus', function(){
    $(this).css("border-color",themeColorCss);
    $(this).css("outline","none");
    $(this).css("box-shadow","0 0 5px " + themeColorCss);
    $(this).css("border-radius","5px 0 0 5px");
});
//input-group span在右边
$("input[name='sms_code']").bind('focus', function(){
    $(this).css("border-color",themeColorCss);
    $(this).css("outline","none");
    $(this).css("box-shadow","0 0 5px " + themeColorCss);
    $(this).css("border-radius","5px 0 0 5px");
});

//button按下事件
$('button').bind('mousedown', function(){
    //$(this).css("box-shadow","0 0 5px " + themeColorCss);
    $(this).css("outline","none");
});

/*跳转认证首页面*/
$('#modal_btn').click( function() {
    console.log("themeColor:" + themeColor);
    var isTopHide = false;
    if (topHide == "none") {
        isTopHide = true;
    }
    window.location.href="/h5/tb?themeColor="+themeColor+"&isTopHide="+isTopHide;
});

/*爬取成功状态*/
function resultTest(taskId){
    console.log("********** "+taskId);
    //var taskId = $("#taskId").val();
    $.ajax({
        type: "POST",
        url:"/h5/tb/tasks/status",
        data:{
            taskid: taskId
        },
        dataType : "json",
        error: function(request) {
            console.log("error");
            return;
        },
        success: function(data) {
            console.log(data.phase+","+data.phase_status);
            console.log(data.description);
            // 用户信息
            if($('#successMsg').children().length==0){
                if (data.userinfoStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【用户信息】抓取已完成</p>");
                } else if (data.userinfoStatus == 201 || data.userinfoStatus == 160) {
                    $("#successMsg").append("<p class='bg-info'>【用户信息】已抓完，暂未获取数据</p>");
                } else {
                    $("#successMsg").append("<p class='bg-warning'>【用户信息】抓取未完成，状态码："+data.userinfoStatus+"</p>");
                }
            }
            if($('#successMsg').children().length<=1){
                // 订单信息
                if (data.orderInfoStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【订单信息】抓取已完成</p>");
                } else if (data.orderInfoStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【订单信息】已抓完，暂未获取数据</p>");
                } else {
                    $("#successMsg").append("<p class='bg-warning'>【订单信息】抓取未完成，状态码："+data.orderInfoStatus+"</p>");
                }
            }

            if ($('#successMsg').children().length<=2) {
                // 地址信息
                if (data.addressInfoStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【地址信息】抓取已完成</p>");
                } else if (data.addressInfoStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【地址信息】已抓完，暂未获取数据</p>");
                } else {
                    $("#successMsg").append("<p class='bg-warning'>【地址信息】抓取未完成，状态码："+data.addressInfoStatus+"</p>");
                }
            }

            if ($('#successMsg').children().length<=3) {
                // 银行卡信息
                if (data.bankCardInfoStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【银行卡信息】抓取已完成</p>");
                } else if (data.bankCardInfoStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【银行卡信息】已抓完，暂未获取数据</p>");
                } else {
                    $("#successMsg").append("<p class='bg-warning'>【银行卡信息】抓取未完成，状态码："+data.bankCardInfoStatus+"</p>");
                }
            }

            if ($('#successMsg').children().length<=4) {
                //支付宝基本信息
                if (data.alipayInfoStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【支付宝基本信息】抓取已完成</p>");
                } else if (data.alipayInfoStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【支付宝基本信息】已抓完，暂未获取数据</p>");
                } else {
                    $("#successMsg").append("<p class='bg-warning'>【支付宝基本信息】抓取未完成，状态码："+data.alipayInfoStatus+"</p>");
                }
            }

            if ($('#successMsg').children().length<=5) {
            //支付宝支付信息
                if (data.alipayPaymentInfoStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【支付宝支付信息】抓取已完成</p>");
                } else if (data.alipayPaymentInfoStatus == 201) {
                    $("#successMsg").append("<p class='bg-info'>【支付宝支付信息】已抓完，暂未获取数据</p>");
                } else {
                    $("#successMsg").append("<p class='bg-warning'>【支付宝支付信息】抓取未完成，状态码："+data.alipayPaymentInfoStatus+"</p>");
                }
            }

            if($('#successMsg').children().length<=6) {
                $("#successMsg").append("<p>淘宝"+"，taskId："+ taskId +"</p>");
            }

        }
    });
}

/*爬取成功状态*/
function resultProd(taskId) {
    console.log("********** " + taskId);
    //var taskId = $("#taskId").val();
    $.ajax({
        type: "POST",
        url: "/h5/tb/tasks/status",
        data: {
            taskid: taskId
        },
        dataType: "json",
        error: function (request) {
            console.log("error");
            return;
        },
        success: function (data) {
            $('#alertMsg').text(data.description);
            $('#msg').find("button").bind("click",function(){
                location.href = "/h5/tb"
            });
        }
    })
}


