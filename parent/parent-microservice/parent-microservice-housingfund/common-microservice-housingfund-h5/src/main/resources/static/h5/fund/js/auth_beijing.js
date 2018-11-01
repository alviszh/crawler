/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextSubmit(); //触发爬虫
});

//登录方式
$("#loginmode").change(function(){
   var index=  $(this).get(0).selectedIndex;
   if(index==0){
       $('#loginType').val('CO_BRANDED_CARD');
   }
   else if(index==1){
       $('#loginType').val('ACCOUNT_NUM');
   }
   else if(index==2){
       $('#loginType').val('IDNUM');
   }
   else if(index==3){
       $('#loginType').val('OFFICER_CARD');
   }
   else{
       $('#loginType').val('PASSPORT');
   }
});

//表单验证
function verifyForm() {
    var num = $('#num').val().trim();//姓名
    var password = $('#password').val().trim();//姓名
    if ((num == null || num == "") ||
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