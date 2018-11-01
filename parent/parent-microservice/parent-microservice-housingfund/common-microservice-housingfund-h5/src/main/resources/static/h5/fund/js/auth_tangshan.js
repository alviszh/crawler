var city = $('p.navbar-brand').find('span').text();
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
        nextLogin();  //触发爬虫
});

//表单验证
function verifyForm() {
    var num = $('#num').val().trim();//身份证号
    var username = $('#hosingFundNumber').val().trim();//联名卡号
    var password = $('#password').val().trim();//密码
    if ((num == null || num == "") ||(username == null || username == "")||
        (password == null || password == "")) {
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