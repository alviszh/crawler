/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLogin(); //触发爬虫
});

//登录方式
$("#loginmode").change(function(){
   var index=  $(this).get(0).selectedIndex;
   if(index==0){
       $('#loginType').val('ACCOUNT_NUM');
   }
   else{
       $('#loginType').val('CITIZEN_EMAIL');
   }
});

//表单验证
function verifyForm() {
    var username = $('#username').val().trim();//姓名
    var password = $('#password').val().trim();//姓名
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