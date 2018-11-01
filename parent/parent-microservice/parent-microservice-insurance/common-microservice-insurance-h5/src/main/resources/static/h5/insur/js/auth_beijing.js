/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    var idNum = $('#username').val();//身份证号码
    var idNumRegExp = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    if(!idNumRegExp.test(idNum)){
        $('#msg').modal('show');
        $('#alertMsg').text('身份证号码输入有误');
        return;
    }
    var password = $('#password').val().trim();//密码
    var passwordRegExp = /^.{8,}$/;
    if(!passwordRegExp.test(password)){
        $('#msg').modal('show');
        $('#alertMsg').text('请输入不少于8位的合法登录密码');
        return;
    }
    nextLogin(); //触发爬虫
});

//表单验证
function verifyForm() {
    var username = $('#username').val().trim();//身份证号码
    var password = $('#password').val().trim();//密码
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
    verifyForm();
});

$(function(){
    verifyForm();
});