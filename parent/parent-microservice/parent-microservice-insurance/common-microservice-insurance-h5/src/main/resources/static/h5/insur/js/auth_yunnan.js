/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLogin(); //触发爬虫
});

//表单验证
function verifyForm() {
    var username = $('#username').val().trim();//姓名
    if ((username == null || username == "")) {
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