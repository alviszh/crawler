/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextSmsCode(); //触发爬虫
});

$('#smsCodeBtn').click( function() {
    nextSetCode(); //触发爬虫
});

//表单验证
function verifyForm() {
    var num = $('#num').val().trim();//姓名
    if (num == null || num == "") {
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