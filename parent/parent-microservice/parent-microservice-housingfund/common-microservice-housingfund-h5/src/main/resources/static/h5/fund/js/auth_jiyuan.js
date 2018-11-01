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
       $('#basic-addon1').html('账<zw style="visibility: hidden">占</zw>号：');
   }
   else if(index==1){
       $('#basic-addon1').html('姓<zw style="visibility: hidden">占</zw>名：');
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