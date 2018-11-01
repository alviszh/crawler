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

//$("#authform").validate({
//    rules: {
//        loginName:{
//            minlength:3,
//            creditcard:true,
//            isFigure:true
//        }
//
//    },
//    messages: {
//        loginName: {
//            minlength:"店铺名称不能少于4个字符！",
//            creditcard:"请输入合法的信用卡好",
//            isFigure:"请输入合法的信用卡号"
//        }
//    }
//})
//
//
//jQuery.validator.addMethod("isFigure",function(value,element,param){
//    // 正则不能写错。
//    var pattern = /^([0-9]*)+$/;
//    return this.optional(element) || (!pattern.test(value));
//},"店铺名称不能为纯数字。");