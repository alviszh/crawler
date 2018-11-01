/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLoginD(); //触发爬虫
});

$('#sendCodeBtn').click( function() {
    nextLoginD(); //触发爬虫
});

/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextSendSmsCodeD(); //触发爬虫
});


/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    verifyForm();
});

$("input[name='loginName']").bind('input propertychange', function() {
    var loginName = $("input[name='loginName']").val().trim();//姓名

    var reg = /^[1-9]\d{7}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;

    if(reg.test(loginName)){
        $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
    else{
        $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
});

$("input[name='password']").bind('input propertychange', function() {
    var password = $("input[name='password']").not(':disabled').val().trim();//密码
    var regP = /^[0-9A-Za-z]{6,}$/;
    if(!regP.test(password)){
        $("input[name='password']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
    else{
        $("input[name='password']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
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
});

$('.ion-ios-help-outline').click( function() {
    window.location.href  = '/h5/bank/help/HXB.html';
});
