/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLoginD(); //触发爬虫
});

$('#loginName').bind('input propertychange', function() {
    var loginName = $('#loginName').val().trim();//姓名
    var reg1 = /^[1][3,4,5,7,8][0-9]{9}$/;

    if(!reg1.test(loginName)){
        $('#loginName').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
    else{
        $('#loginName').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
});

$('#password').bind('input propertychange', function() {
    var password = $('#password').val().trim();//密码
    var regP = /^\S{6,15}$/;
    if(!regP.test(password)){
        $('#password').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
    else{
        $('#password').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
});


//表单验证
function verifyForm() {
    if($(".log-wrong").length > 0 || $("form").find(".log-div").length > 0) {
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
    //verifyForm();
});

$('.back-pw').click( function() {
    window.location.href ="https://creditcard.ecitic.com/citiccard/ucweb/toValidatePhonePage.do"
});

$('.ion-ios-help-outline').click( function() {
    window.location.href  = '/h5/bank/help/CITIC-CREDIT.html';
});
