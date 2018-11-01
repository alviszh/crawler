/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if(city.indexOf("深圳")!=-1){
        nextSubmit(); //触发爬虫
    }
    else{
        nextLogin(); //触发爬虫
    }
});

//表单验证
function verifyForm() {
    var num = $('#num').val().trim();//姓名
    var hosingFundNumber = $('#hosingFundNumber').val().trim();//密码
    if ((num == null || num == "") ||
        (hosingFundNumber == null || hosingFundNumber == "")) {
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