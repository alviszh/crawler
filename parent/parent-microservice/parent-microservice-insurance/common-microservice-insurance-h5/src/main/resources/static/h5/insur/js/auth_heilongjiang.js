
$('#myTab').find('li').click( function() {
    if(!$(this).hasClass("active")&&$(this).index()==0){
        $('#loginType').val('INSURANCE_CARD');
        $('#card').find('input').removeAttr("disabled");
        $('#user,#email,#phone').find('input').attr("disabled","disabled");
        verifyForm1()
    }
    if(!$(this).hasClass("active")&&$(this).index()==1){
        $('#loginType').val('ACCOUNT_NUM');
        $('#user').find('input').removeAttr("disabled","disabled");
        $('#card,#email,#phone').find('input').attr("disabled");
        verifyForm2()
    }
    if(!$(this).hasClass("active")&&$(this).index()==2){
        $('#loginType').val('CITIZEN_EMAIL');
        $('#email').find('input').removeAttr("disabled","disabled");
        $('#card,#user,#phone').find('input').attr("disabled");
        verifyForm3()
    }
    if(!$(this).hasClass("active")&&$(this).index()==3){
        $('#loginType').val('PHONE_NUM');
        $('#phone').find('input').removeAttr("disabled","disabled");
        $('#card,#user,#email').find('input').attr("disabled");
        verifyForm4()
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
    var username = $('#card').find("input[name='username']").val().trim();//姓名
    var password = $('#card').find("input[name='password']").val().trim();//密码
    if ((username == null || username == "") ||
        (password == null || password == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm2() {
    var username = $('#user').find("input[name='username']").val().trim();//姓名
    var password = $('#user').find("input[name='password']").val().trim();//密码
    if ((username == null || username == "") ||
        (password == null || password == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm3() {
    var username = $('#email').find("input[name='username']").val().trim();//姓名
    var password = $('#email').find("input[name='password']").val().trim();//密码
    if ((username == null || username == "") ||
        (password == null || password == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

function verifyForm4() {
    var username = $('#phone').find("input[name='username']").val().trim();//姓名
    var password = $('#phone').find("input[name='password']").val().trim();//密码
    if ((username == null || username == "") ||
        (password == null || password == "")) {
        $("#nextBtn").addClass("disabled");
    }else{
        $("#nextBtn").removeClass("disabled");
    }
}

/**
 * 表单改变事件
 */
$('input').bind('input propertychange', function() {
    if($('#loginType').val()=='INSURANCE_CARD'){
        verifyForm1();
    }
    if($('#loginType').val()=='ACCOUNT_NUM'){
        verifyForm2();
    }
    if($('#loginType').val()=='CITIZEN_EMAIL'){
        verifyForm3();
    }
    if($('#loginType').val()=='PHONE_NUM'){
        verifyForm4();
    }
});

$(function(){

});