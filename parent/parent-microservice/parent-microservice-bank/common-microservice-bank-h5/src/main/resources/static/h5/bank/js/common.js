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
    $(this).css("border","1px solid #ccc");
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
    window.location.href="/h5/bank?themeColor="+themeColor+"&isTopHide="+isTopHide;
});

/*爬取成功状态*/
function result(taskid){
    console.log("********** "+ taskid);
    //var taskId = $("#taskId").val();
    $.ajax({
        type: "POST",
        url:"/h5/bank/tasks/status",
        data:{
            taskid: taskid,
            loginType:$("#loginType").val()
        },
        dataType : "json",
        error: function(request) {
            console.log("error");
            return;
        },
        success: function(data) {

            if ($('#successMsg').children().length==0) {
            // 用户信息
                if (data.userinfoStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【用户信息】抓取已完成</p>");
                } else if (data.userinfoStatus == 201 || data.userinfoStatus == 160) {
                    $("#successMsg").append("<p class='bg-info'>【用户信息】抓取已完成，无数据</p>");
                } else {
                    $("#successMsg").append("<p class='bg-warning'>【用户信息】抓取未完成，状态码："+data.userinfoStatus+"</p>");
                }
            }

            if ($('#successMsg').children().length<=1) {
                // 缴存明细信息
                if (data.transflowStatus == 200) {
                    $("#successMsg").append("<p class='bg-success'>【交易流水】抓取已完成</p>");
                } else if (data.transflowStatus == 201|| data.userinfoStatus == 160) {
                    $("#successMsg").append("<p class='bg-info'>【交易流水】抓取已完成，无数据</p>");
                } else {
                    $("#successMsg").append("<p class='bg-warning'>【交易流水】抓取未完成，状态码："+data.transflowStatus+"</p>");
                }
            }
            if($('#successMsg').children().length<=2){
                $("#successMsg").append("<p>银行："+ $('#bankType').val() +"，taskId："+ taskid +"</p>");
            }

        }
    });
}
