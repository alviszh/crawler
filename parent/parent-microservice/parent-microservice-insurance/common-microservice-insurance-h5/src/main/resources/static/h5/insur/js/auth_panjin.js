
$('#myTab').find('li').click( function() {
    if(!$(this).hasClass("active")&&$(this).index()==0){
        $('#loginType').val('ACCOUNT_NUM');
        $('#idnum').find('input').attr("disabled","disabled");
        $('#card').find('input').removeAttr("disabled");
        verifyForm1()
    }
    if(!$(this).hasClass("active")&&$(this).index()==1){
        $('#loginType').val('IDNUM');
        $('#card').find('input').attr("disabled","disabled");
        $('#idnum').find('input').removeAttr("disabled");
        verifyForm2()
    }
});

$("input[name='username']").bind('input propertychange', function() {
    var username = $("input[name='username']").not(':disabled').val().trim();//姓名

    var reg;

    if($('#loginType').val()=='IDNUM'){
        reg = /^[1-9]\d{7}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0[1-9])|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
    }
    else{
        reg = /\S/;
    }

    if(reg.test(username)){
        $("input[name='username']").not(':disabled').removeClass('log-wrong').addClass('log-right');
    }
    else{
        $("input[name='username']").not(':disabled').removeClass('log-right').addClass('log-wrong');
    }
});


$("input[name='password']").bind('input propertychange', function() {
    var password = $("input[name='password']").not(':disabled').val().trim();//密码
    var regP = /\S/;

    if(regP.test(password)){
        $("input[name='password']").not(':disabled').removeClass('log-wrong').addClass('log-right');
    }
    else{
        $("input[name='password']").not(':disabled').removeClass('log-right').addClass('log-wrong');
    }
});


/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLogin(); //触发爬虫
});

//表单验证
function verifyForm1() {
    if($("#card").find(".log-right").length <2) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm2() {
    if($("#idnum").find(".log-right").length <2) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    if($('#loginType').val()=='ACCOUNT_NUM'){
        verifyForm1();
    }
    if($('#loginType').val()=='IDNUM'){
        verifyForm2();
    }
});

$(function(){
});