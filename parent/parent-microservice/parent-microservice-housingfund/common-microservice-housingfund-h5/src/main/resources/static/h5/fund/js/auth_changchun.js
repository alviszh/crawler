var city = $('p.navbar-brand').find('span').text();
/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    if(city.indexOf("邢台市")!=-1||city.indexOf("济宁市")!=-1||city.indexOf("济南市")!=-1||city.indexOf("衡水市")!=-1||city.indexOf("南充市")!=-1||city.indexOf("濮阳市")!=-1||city.indexOf("凉山彝族自治州")!=-1||city.indexOf("长治市")!=-1||city.indexOf("丹东市")!=-1||city.indexOf("丽水市")!=-1||city.indexOf("昆明市")!=-1||city.indexOf("宜春市")!=-1||city.indexOf("柳州市")!=-1||city.indexOf("桂林市")!=-1||city.indexOf("赤峰市")!=-1||city.indexOf("吉林市")!=-1||city.indexOf("通化市")!=-1||city.indexOf("保定市")!=-1||city.indexOf("驻马店市")!=-1||city.indexOf("乐山市")!=-1||city.indexOf("焦作市")!=-1||city.indexOf("贵港市")!=-1||city.indexOf("义乌市")!=-1||city.indexOf("文山壮族苗族自治州公积金")!=-1||city.indexOf("泸州市")!=-1||city.indexOf("遂宁市")!=-1||city.indexOf("大连市")!=-1||city.indexOf("咸阳市")!=-1||city.indexOf("梧州市")!=-1||city.indexOf("湛江市")!=-1||city.indexOf("蚌埠市")!=-1){
        nextLogin(); //触发登录
    }
    else if(city.indexOf("潍坊市")!=-1){
        $('#sms_div').is(":visible") ? nextSetCode() : nextLogin();
    }
    else{
        nextSubmit(); //触发爬取
    }
});

$('#sendCodeBtn').click( function() {
        nextSmsCode()
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