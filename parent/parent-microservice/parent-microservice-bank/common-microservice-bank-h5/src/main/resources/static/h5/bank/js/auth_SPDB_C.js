/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLoginD(); //触发爬虫
});

//登录方式
$("#loginmode").change(function(){
    var index=  $(this).get(0).selectedIndex;
    if(index==0){
        $('#loginType').val('IDNUM');
    }
    else if(index==1){
        $('#loginType').val('PASSPORT');
    }
    else if(index==2){
        $('#loginType').val('OFFICER_CARD');
    }
    else{
        $('#loginType').val('OTHER');
    }
});

$('#password').bind('input propertychange', function() {
    var password = $('#password').val().trim();//密码
    var regP1 = /^[0-9]{6}$/;
    if(!regP1.test(password)){
        $('#password').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
    else{
        $('#password').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
});

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    verifyForm();
});

//表单验证
function verifyForm() {
    if($(".log-wrong").length > 0 || $("form").find(".log-div").length >0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

//$('#loginName').bind('input propertychange', function() {
//    var loginName = $('#loginName').val().trim();//姓名
//
//    var reg1 = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
//
//    if(!reg1.test(loginName)){
//        $('#loginName').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
//    }
//    else{
//        $('#loginName').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
//    }
//});

$('.ion-ios-help-outline').click( function() {
    window.location.href  = '/h5/bank/help/SPD-CREDIT.html';
});
