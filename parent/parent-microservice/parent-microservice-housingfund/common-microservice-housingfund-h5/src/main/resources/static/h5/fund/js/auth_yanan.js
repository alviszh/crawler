var city = $('p.navbar-brand').find('span').text();
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if(city.indexOf("四平市")!=-1){
           nextLogin(); //触发登录
    }
    else{
           nextSubmit(); //触发爬取
    }
});

//表单验证
function verifyForm() {
    var num = $('#num').val().trim();//姓名
    var countNumber = $('#countNumber').val().trim();//姓名
    var password = $('#password').val().trim();//密码
    if ((num == null || num == "") ||(countNumber == null || password == "")||
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