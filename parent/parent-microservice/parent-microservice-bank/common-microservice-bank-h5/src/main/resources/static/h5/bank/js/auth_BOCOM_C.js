
$('#myTab').find('li').find("a").click( function() {
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==0){
        $('#loginType').val('CARD_NUM');
        $('#username').find('input').attr("disabled","disabled");
        $('#idnum').find('input').removeAttr("disabled");
        verifyForm1()
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==1){
        $('#loginType').val('PHONE_NUM');

        $('#idnum').find('input').attr("disabled","disabled");
        $('#username').find('input').removeAttr("disabled");
        verifyForm2()
    }
});

/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLoginD(); //触发爬虫
});

$("input[name='loginName']").bind('input propertychange', function() {
    var loginName = $("input[name='loginName']").not(':disabled').val().trim();//姓名

    var reg;

    if($('#loginType').val()=='CARD_NUM'){
        bankCardAttribution(loginName);
    }
    else{
        reg = /^[1][3,4,5,7,8][0-9]{9}$/;
        if(reg.test(loginName)){
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
        }
        else{
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
        }
    }
});


$("input[name='password']").bind('input propertychange', function() {
    var password = $("input[name='password']").not(':disabled').val().trim();//密码
    var regP;
    if($('#loginType').val()=='CARD_NUM'){
        regP = /^\S{6}$/;
    }
    else{
        regP = /^\S{6,15}$/;
    }


    if(!regP.test(password)){
        $("input[name='password']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
    else{
        $("input[name='password']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
});


//表单验证
function verifyForm1() {
    if( $("#idnum").find(".log-wrong").length > 0 || $("#idnum").find(".log-div").length >0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm2() {
    if($("#username").find(".log-wrong").length > 0 || $("#username").find(".log-div").length >0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    if($('#loginType').val()=='CARD_NUM'){
        verifyForm1();
    }
    if($('#loginType').val()=='PHONE_NUM'){
        verifyForm2();
    }
});


$(function(){
    verifyForm2();
    $('#username').find('input').attr("disabled","disabled");
});

$('.back-pw').click( function() {
    window.location.href ="https://creditcardapp.bankcomm.com/sac/public/findpwd.html?service=https://creditcardapp.bankcomm.com/member/shiro-cas"
});

$('.ion-ios-help-outline').click( function() {
    window.location.href  = '/h5/bank/help/COMM-CREDIT.html';
});
