$('#myTab').find('li').find("a").click( function() {
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==0){
        $('#loginType').val('ID_NUM');
        $('#card_num,#p_num').find('input').attr("disabled","disabled");
        $('#ac_num').find('input').removeAttr("disabled");
        verifyForm1();
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==1){
        $('#loginType').val('CARD_NUM');

        $('#ac_num,#p_num').find('input').attr("disabled","disabled");
        $('#card_num').find('input').removeAttr("disabled");
        verifyForm2();
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==2){
        $('#loginType').val('PHONE_NUM');

        $('#card_num,#ac_num').find('input').attr("disabled","disabled");
        $('#p_num').find('input').removeAttr("disabled");
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

    var reg;
    if($("#loginType").val()=='ID_NUM'){
        reg =/^[1-9]\d{7}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
        if(reg.test(loginName)){
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
        }
        else{
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
        }
    }
    if($("#loginType").val()=='PHONE_NUM'){
        reg =/^[1][3,4,5,7,8][0-9]{9}$/;
        if(reg.test(loginName)){
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
        }
        else{
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
        }
    }
    else if($("#loginType").val()=='CARD_NUM'){
        bankCardAttribution(loginName);
    }
});

$("input[name='password']").bind('input propertychange', function() {
    var password = $("input[name='password']").not(':disabled').val().trim();//密码
    var regP;
    if($("#loginType").val()!='PHONE_NUM'){
       regP=/^\d{6}$/;
    }
    else{
        regP=/^\S{6,}$/;
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
    if($("#ac_num").find(".log-wrong").length > 0 || $("#ac_num").find(".log-div").length >0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm2() {
    if($("#card_num").find(".log-wrong").length > 0 || $("#card_num").find(".log-div").length >0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm3() {
    if($("#p_num").find(".log-wrong").length > 0 || $("#p_num").find(".log-div").length >0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    if($('#loginType').val()=='ID_NUM'){
        verifyForm1();
    }
    if($('#loginType').val()=='CARD_NUM'){
        verifyForm2();
    }
    if($('#loginType').val()=='PHONE_NUM'){
        verifyForm3();
    }
});

$('.back-pw').click( function() {
    window.location.href ="https://html.m.cmbchina.com/MobileHtml/CreditCard/M_CustomerService/ResetPwd/RP_CheckCustomerInfo.aspx?Type=H"
});

$('.ion-ios-help-outline').click( function() {
    window.location.href  = '/h5/bank/help/CMB-CREDIT.html';
});
