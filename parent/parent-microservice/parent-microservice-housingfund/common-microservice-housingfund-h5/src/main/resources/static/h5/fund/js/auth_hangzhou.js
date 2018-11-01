
$('#myTab').find('li').click( function() {
    if(!$(this).hasClass("active")&&$(this).index()==0){
        $('#logintype').val('CO_BRANDED_CARD');
        $('#account_num').find('input').attr("disabled","disabled");
        $('#mail').find('input').attr("disabled","disabled");
        $('#card').find('input').removeAttr("disabled");
    }
    if(!$(this).hasClass("active")&&$(this).index()==1){
        $('#logintype').val('ACCOUNT_NUM');
        $('#card').find('input').attr("disabled","disabled");
        $('#mail').find('input').attr("disabled","disabled");
        $('#account_num').find('input').removeAttr("disabled");
    }
    if(!$(this).hasClass("active")&&$(this).index()==2){
        $('#logintype').val('CITIZEN_EMAIL');
        $('#card').find('input').attr("disabled","disabled");
        $('#account_num').find('input').attr("disabled","disabled");
        $('#mail').find('input').removeAttr("disabled");
    }
});

/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextSubmit(); //触发爬虫
});

//表单验证
function verifyForm1() {
    var username1 = $('#username1').val().trim();//姓名
    var password1 = $('#password1').val().trim();//密码
    if ((username1 == null || username1 == "") ||
        (password1 == null || password1 == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm2() {
    var username2 = $('#username2').val().trim();//姓名
    var password2 = $('#password2').val().trim();//密码
    if ((username2 == null || username2 == "") ||
        (password2 == null || password2 == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm3() {
    var username3 = $('#username3').val().trim();//姓名
    var password3 = $('#password3').val().trim();//密码
    if ((username3 == null || username3 == "") ||
        (password3 == null || password3 == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    if($('#logintype').val()=='CO_BRANDED_CARD'){
        verifyForm1();
    }
    if($('#logintype').val()=='IDNUM'){
        verifyForm2();
    }
    if($('#logintype').val()=='IDNUM'){
        verifyForm3();
    }
});

$(function(){
    verifyForm1();
    $('#account_num').find('input').attr("disabled","disabled");
    $('#mail').find('input').attr("disabled","disabled");
});