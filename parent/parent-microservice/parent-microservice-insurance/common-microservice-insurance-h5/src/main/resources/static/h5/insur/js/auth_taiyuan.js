/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    var password = $('#password').val();//身份证号码
    var passwordRegExp = /^\d{6,10}$/;
    if(!passwordRegExp.test(password)){
        $('#msg').modal('show');
        $('#alertMsg').text('只能是数字，长度6-10位');
        return;
    }
    nextLogin(); //触发爬虫
});

//表单验证
function verifyForm() {
    var name = $('#name').val().trim();//姓名
    var username = $('#username').val().trim();//身份证号
    var password = $('#password').val().trim();//密码
    if ((name == null || name == "") ||(username == null || username == "") ||
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