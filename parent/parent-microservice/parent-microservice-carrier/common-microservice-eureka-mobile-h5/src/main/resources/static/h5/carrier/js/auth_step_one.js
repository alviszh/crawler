
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if (!onLine) {
        $('#msg').modal('show');
        $('#alertMsg').text(errInfo);
        return;
    }
    /*var mobileOperator = $('input:radio:checked').val();
    var mobileOperator = $("input[name='mobileOperator']:checked").val();
    console.log("^^^"+mobileOperator);*/

    var username = $('#username').val().trim();//姓名
    var mobileNum = $('#mobileNum').val().trim();//手机号码
    var idNum = $('#idnum').val();//身份证号码
    //var mobileRegExp=/^1\d{10}$/;
    var mobileRegExp = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(166))+\d{8})$/;
    var idNumRegExp = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
    if(!mobileRegExp.test(mobileNum)){
        $('#msg').modal('show');
        $('#alertMsg').text('请输入有效的手机号码');
        return;
    }
    if(!idNumRegExp.test(idNum)){
        $('#msg').modal('show');
        $('#alertMsg').text('身份证号码输入有误');
        return;
    }
    if ((username == null || username == "") ||
        (mobileNum == null || mobileNum == "") ||
        (idNum == null || idNum == "")) {
        $('#msg').modal('show');
        $('#alertMsg').text('信息输入不完整！');
    }
    $("#auth_form").submit();
});
//表单验证
function verifyForm() {
    var username = $('#username').val().trim();//姓名
    var mobileNum = $('#mobileNum').val().trim();//手机号码
    var idNum = $('#idnum').val().trim();//身份证号码
    if ((username == null || username == "") ||
        (mobileNum == null || mobileNum == "") ||
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

/**
 * 手机号输入框，失去焦点事件
 */
$("input[id='mobileNum']").bind('blur', function(){
    var mobileNum = $('#mobileNum').val();//手机号码

    var mobileRegExp = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(14[0-9]{1})|(17[0-9]{1})|(166))+\d{8})$/;
//    var mobileRegExp = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(14[5,7]{1})|(17[5-8]{1}))+\d{8})$/;
    if(!mobileRegExp.test(mobileNum)){
        $('#msg').modal('show');
        $('#alertMsg').text('请输入有效的手机号码');
        return;
    }
    console.log(mobileNum);
    findMobileSegment(mobileNum);
    console.log(mobileNum);
});
