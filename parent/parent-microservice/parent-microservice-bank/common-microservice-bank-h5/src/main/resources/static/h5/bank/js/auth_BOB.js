/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLoginD(); //触发爬虫
});

$('#loginName').bind('input propertychange', function() {
    var loginName = $('#loginName').val().trim();//姓名

    bankCardAttribution(loginName);
});

$('#password').bind('input propertychange', function() {
    var password = $('#password').val().trim();//密码
    var regP = /^[0-9]{6}$/;
    if(!regP.test(password)){
        $('#password').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
    else{
        $('#password').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
});


//表单验证
function verifyForm() {
    if($(".log-wrong").length > 0 || $("form").find(".log-div").length >0) {
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
})

$('.ion-ios-help-outline').click( function() {
    window.location.href  = '/h5/bank/help/BOC.html';
});


