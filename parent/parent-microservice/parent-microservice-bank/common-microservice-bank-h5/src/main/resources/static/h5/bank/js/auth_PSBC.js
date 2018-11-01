
$('#myTab').find('li').find("a").click( function() {
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==0){
        $('#loginType').val('ID_NUM');
        $('#idnum').find('input').removeAttr("disabled");
        $('#account,#phone,#card').find('input').attr("disabled","disabled");
        verifyForm1()
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==1){
        $('#loginType').val('ACCOUNT_NUM');

        $('#account').find('input').removeAttr("disabled");
        $('#idnum,#phone,#card').find('input').attr("disabled","disabled");
        verifyForm2()
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==2){
        $('#loginType').val('PHONE_NUM');

        $('#phone').find('input').removeAttr("disabled");
        $('#idnum,#account,#card').find('input').attr("disabled","disabled");
        verifyForm3()
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==3){
        $('#loginType').val('CARD_NUM');

        $('#card').find('input').removeAttr("disabled");
        $('#idnum,#account,#phone').find('input').attr("disabled","disabled");
        verifyForm4()
    }
});

/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLoginD(); //触发爬虫
});


$("input[name='loginName']").bind('input propertychange', function() {
    var loginName =$("input[name='loginName']").not(':disabled').val().trim();//姓名

    var reg;
    if($('#loginType').val()=='ID_NUM'){
        reg = /^[1-9]\d{7}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
        if(reg.test(loginName)){
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
        }
        else{
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
        }
    }
    else if($('#loginType').val()=='ACCOUNT_NUM'){
        reg = /^\S{6,14}$/;
        if(reg.test(loginName)){
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
        }
        else{
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
        }
    }
    else if($('#loginType').val()=='PHONE_NUM'){
        reg = /^[1][3,4,5,7,8][0-9]{9}$/;
        if(reg.test(loginName)){
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
        }
        else{
            $("input[name='loginName']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
        }
    }
    else{
        bankCardAttribution(loginName);
    }
});

$("input[name='password']").bind('input propertychange', function() {
    var regP;
    if($('#loginType').val() == 'ID_NUM'|| $('#loginType').val()=='ACCOUNT_NUM'){
        regP = /^\S{6,}$/;
    }
    else{
        regP = /^\S{6,20}$/;
    }

    var password = $("input[name='password']").not(':disabled').val().trim();//密码

    if(!regP.test(password)){
        $("input[name='password']").not(':disabled').parent().next('div').removeClass('log-right log-div').addClass('log-wrong');
    }
    else{
        $("input[name='password']").not(':disabled').parent().next('div').removeClass('log-wrong log-div').addClass('log-right');
    }
});


//表单验证
function verifyForm1() {
    if($("#idnum").find(".log-wrong").length > 0 || $("#idnum").find(".log-div").length > 0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm2() {
    if($("#account").find(".log-wrong").length > 0 || $("#account").find(".log-div").length > 0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm3() {
    if($("#phone").find(".log-wrong").length > 0 && $("#phone").find(".log-div").length ==0) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm4() {
    if($("#card").find(".log-wrong").length > 0 && $("#card").find(".log-div").length ==0) {
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
    if($('#loginType').val()=='ACCOUNT_NUM'){
        verifyForm2();
    }
    if($('#loginType').val()=='PHONE_NUM'){
        verifyForm3();
    }
    if($('#loginType').val()=='CARD_NUM'){
        verifyForm4();
    }
});

$('.ion-ios-help-outline').click( function() {
    window.location.href  = '/h5/bank/help/PSBC.html';
});
