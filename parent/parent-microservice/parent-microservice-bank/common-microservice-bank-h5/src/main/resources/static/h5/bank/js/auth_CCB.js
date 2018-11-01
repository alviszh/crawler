
$('#myTab').find('li').find("a").click( function() {
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==0){
        $('#loginType').val('CARD_NUM');
        $('#idnum,#username').find('input').attr("disabled","disabled");
        $('#card_num').find('input').removeAttr("disabled");
        verifyForm1();
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==1){
        $('#loginType').val('ACCOUNT_NUM');

        $('#idnum,#card_num').find('input').attr("disabled","disabled");
        $('#username').find('input').removeAttr("disabled");
        verifyForm2();
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==2){
        $('#loginType').val('ID_NUM');

        $('#username,#card_num').find('input').attr("disabled","disabled");
        $('#idnum').find('input').removeAttr("disabled");
        verifyForm3();
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


    if($('#loginType').val()=='ID_NUM'){
        var reg = /^[1-9]\d{7}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
    }
    else if($('#loginType').val()=='ACCOUNT_NUM'){
        var reg = /^\S{3,}$/;
    }
    else{
        bankCardAttribution(loginName);
    }

    if(reg.test(loginName)){
        $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
    else{
        $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
});

$("input[name='password']").bind('input propertychange', function() {
    var password = $("input[name='password']").not(':disabled').val().trim();//密码
    var regP = /^\S{6,12}$/;
    if(!regP.test(password)){
        $("input[name='password']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
    else{
        $("input[name='password']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
});

//表单验证
function verifyForm1() {
    if($("#card_num").find(".log-wrong").length > 0 || $("#card_num").find(".log-div").length >0) {
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

function verifyForm3() {
    if($("#idnum").find(".log-wrong").length > 0 || $("#idnum").find(".log-div").length >0) {
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
    if($('#loginType').val()=='ACCOUNT_NUM'){
        verifyForm2();
    }
    if($('#loginType').val()=='ID_NUM'){
        verifyForm3();
    }
});

$(function(){
    //verifyForm2();
    $('#username').find('input').attr("disabled","disabled");
});

$('.back-pw').click( function() {
    window.location.href ="https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_13?SERVLET_NAME=B2CMainPlat_13&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=O10100"
});

$('.back-user').click( function() {
    window.location.href ="https://ibsbjstar.ccb.com.cn/CCBIS/B2CMainPlat_13?SERVLET_NAME=B2CMainPlat_13&CCB_IBSVersion=V6&PT_STYLE=1&CUSTYPE=0&TXCODE=O10110"
});

$('.ion-ios-help-outline').click( function() {
    window.location.href  = '/h5/bank/help/CCB-CARDNO.html';
});

