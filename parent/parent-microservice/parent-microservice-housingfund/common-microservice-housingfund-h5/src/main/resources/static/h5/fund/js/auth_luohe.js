var city = $('p.navbar-brand').find('span').text();
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
        nextLogin();  //触发爬虫
});

//表单验证
function verifyForm() {
    var username = $('#username').val().trim();//姓名
    var num = $('#num').val().trim();//姓名
    var hfNumber = $('#hosingFundNumber').val().trim();//密码
    if ((num == ""&& username == "")||(num == ""&& hfNumber == "")||
        ( username == ""&& hfNumber == "")||(num == ""&& hfNumber == "" && username == "")) {
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

$(function(){
    verifyForm();
});