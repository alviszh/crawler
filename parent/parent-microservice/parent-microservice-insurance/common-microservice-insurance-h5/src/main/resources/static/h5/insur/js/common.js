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
    window.location.href="/h5/insur/auth?themeColor="+themeColor+"&isTopHide="+isTopHide;
    return false;
});

/*爬取成功状态*/
function result(taskId){
    console.log("********** "+taskId);
    //var taskId = $("#taskId").val();
    $.ajax({
        type: "POST",
        url:"/h5/insur/tasks/status",
        data:{
            taskId: taskId
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
            if (data.userInfoStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【用户信息】抓取已完成</p>");
            } else if (data.userInfoStatus == 201 || data.userInfoStatus == 160) {
                $("#successMsg").append("<p class='bg-info'>【用户信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【用户信息】抓取未完成，状态码："+data.userInfoStatus+"</p>");
            }
            // 养老保险信息
            if (data.yanglaoStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【养老保险信息】抓取已完成</p>");
            } else if (data.yanglaoStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【养老保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【养老保险信息】抓取未完成，状态码："+data.yanglaoStatus+"</p>");
            }

            // 失业保险信息
            if (data.shiyeStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【失业保险信息】抓取已完成</p>");
            } else if (data.shiyeStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【失业保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【失业保险信息】抓取未完成，状态码："+data.shiyeStatus+"</p>");
            }

            // 工伤保险信息
            if (data.gongshangStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【工伤保险信息】抓取已完成</p>");
            } else if (data.gongshangStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【工伤保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【工伤保险信息】抓取未完成，状态码："+data.gongshangStatus+"</p>");
            }

            // 生育保险信息
            if (data.shengyuStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【生育保险信息】抓取已完成</p>");
            } else if (data.shengyuStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【生育保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【生育保险信息】抓取未完成，状态码："+data.shengyuStatus+"</p>");
            }

            // 医疗保险信息
            if (data.yiliaoStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【医疗保险信息】抓取已完成</p>");
            } else if (data.yiliaoStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【医疗保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【医疗保险信息】抓取未完成，状态码："+data.yiliaoStatus+"</p>");
            }
            $("#successMsg").append("<p>社保城市："+ $('#city').val() +"，taskId："+ taskId +"</p>");

        }
    });
}
