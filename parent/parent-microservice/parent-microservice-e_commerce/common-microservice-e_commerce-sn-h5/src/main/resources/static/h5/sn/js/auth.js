
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    var idNum = $('#idNum').val();//身份证号码
    var idNumRegExp = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    if(!idNumRegExp.test(idNum)){
        $('#msg').modal('show');
        $('#alertMsg').text('身份证号码输入有误');
        return;
    }
    $("#authform").submit();
});
//表单验证
function verifyForm() {
    var userName = $('#username').val().trim();//姓名
    var idNum = $('#idNum').val().trim();//身份证号码
    if ((userName == null || userName == "") ||
        (idNum == null || idNum == "")) {
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