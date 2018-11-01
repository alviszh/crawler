var themeColor = "";
var topHide = "";
var themeColorCss = "";
var onLine;//是否有网
var errInfo = "网络不给力，请重试";

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
    checkNetwork();//检查网络
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
    //$(this).css("border-radius","5px 0 0 5px");
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
    window.location.href="/h5/auth?themeColor="+themeColor+"&isTopHide="+isTopHide;
});

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