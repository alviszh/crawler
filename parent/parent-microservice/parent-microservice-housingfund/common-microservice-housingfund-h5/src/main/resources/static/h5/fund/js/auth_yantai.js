var city = $('p.navbar-brand').find('span').text();
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if(city.indexOf("烟台市")!=-1){
        nextSubmit(); //触发爬虫
    }
    else{
        nextLogin(); //触发爬虫
    }
});

//表单验证
function verifyForm() {
    var username = $('#username').val().trim();//姓名
    var num = $('#num').val().trim();//姓名
    if ((num == null || num == "") ||(username == null || username == "")) {
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