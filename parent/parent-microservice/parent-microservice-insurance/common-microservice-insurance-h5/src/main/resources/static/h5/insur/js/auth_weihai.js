
$('#myTab').find('li').find("a").click( function() {
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==0){
        $('#loginType').val('IDNUM');
        $('#passport').find('input').attr("disabled","disabled");
        $('#idnum').find('input').removeAttr("disabled");
        verifyForm1()
    }
    if(!$(this).parent().hasClass("active")&&$(this).parent().index()==1){
        $('#loginType').val('PASSPORT');

        $('#idnum').find('input').attr("disabled","disabled");
        $('#passport').find('input').removeAttr("disabled");
        verifyForm2()
    }
});

/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLogin(); //触发爬虫
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


function verifyForm1() {
    if($("#idnum").find(".log-right").length <2) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm2() {
    if($("#passport").find(".log-right").length <2) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

$('input').bind('input propertychange', function() {
    if($('#loginType').val()=='IDNUM'){
        verifyForm1();
    }
    if($('#loginType').val()=='PASSPORT'){
        verifyForm2();
    }
});