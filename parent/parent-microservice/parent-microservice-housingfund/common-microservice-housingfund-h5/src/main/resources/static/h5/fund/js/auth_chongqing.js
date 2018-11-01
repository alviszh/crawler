var city = $('p.navbar-brand').find('span').text();
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if(city.indexOf("通辽市")!=-1||city.indexOf("钦州市")!=-1||city.indexOf("武汉市")!=-1||city.indexOf("肇庆市")!=-1){
        nextLogin();
    }
    else{
        nextSubmit(); //触发爬取
    }
});

//表单验证
function verifyForm() {
    var num = $('#num').val().trim();//姓名
    var password = $('#password').val().trim();//密码
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