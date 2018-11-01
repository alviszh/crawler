var city = $('p.navbar-brand').find('span').text();
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if(city.indexOf("德州市")!=-1||city.indexOf("呼和浩特市")!=-1||city.indexOf("沧州市")!=-1||city.indexOf("西安市")!=-1||city.indexOf("郑州市")!=-1){
        nextLogin();  //触发爬虫
    }
    else{
        nextSubmit(); //触发爬虫
    }
});

//表单验证
function verifyForm() {
    var username = $('#username').val().trim();//姓名
    var num = $('#num').val().trim();//姓名
    var password = $('#password').val().trim();//密码
    if ((num == null || num == "") ||(username == null || username == "")||
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