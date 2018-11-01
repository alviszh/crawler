/**获取图片验证码**/
function imgCode(){
    $.ajax({
        type: "POST",
        url:"/insurance/imgCode",
        data:$('#authform').serialize(),
        dataType : "json",
        error: function(request) {
            console.log("error");
        },
        success: function(data) {
            //$("#imgCode").empty();
            //$("#imgCode").append('<span style="font-weight:400;text-align:center;width: 30%;">验<zw style="visibility: hidden">z</zw>证<zw style="visibility: hidden">z</zw>码：</span>');
            $.each(data, function(i,item){
                var imageCurrent = item.base64;
                //console.log(imageCurrent);
                //$("#imgCode").append('<img src="'+item.base64+'"/>');
                $("#codeImage"+i).attr("src", item.base64);
            });
            //$("#imgCode").append('<input type="text" style="width: 20%;text-align:center;" id="verification" name="verification" />');
            //$('#imgCode').css("padding-bottom", "8px");
            return;
        }
    });
}

/**
 * 点击下一步
 */
$('#nextBtn').click( function() {
    nextLogin(); //触发爬虫
});

//表单验证
function verifyForm() {
    var username = $('#username').val();//姓名
    var password = $('#password').val();//密码
    var verification = $('#verification').val();//图片验证码
    if ((username == null || username == "") ||
        (password == null || password == "") ||
        (verification == null || verification == "")) {
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
    imgCode();
});
