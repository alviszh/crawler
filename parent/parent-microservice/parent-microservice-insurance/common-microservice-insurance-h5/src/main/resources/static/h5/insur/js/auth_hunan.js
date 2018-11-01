/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLogin(); //触发爬虫
});

//点击错误提示的按钮跳回首页
$('.modal-footer').click( function() {
    window.location.href="/h5"
})


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