var city = $('p.navbar-brand').find('span').text();
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if(city.indexOf("铁岭市")!=-1||city.indexOf("大同市")!=-1){
        nextSubmit(); //触发爬虫
    }
    else{
        nextLogin(); //触发爬虫
    }
});

//登录方式
$("#loginmode").change(function(){
   var index=  $(this).get(0).selectedIndex;
   if(index==0){
       $('#loginType').val('ACCOUNT_NUM');
   }
   else if(index==1){
       $('#loginType').val('IDNUM');
   }
   else{
       $('#loginType').val('CO_BRANDED_CARD');
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