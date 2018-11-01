$('.tex_box').click( function() {
    if($(this).find('span').hasClass('closed')){
        $(this).prev('input').attr("type","text");
        $(this).find('span').removeClass('closed').addClass('opend');
    }
    else{
        $(this).prev('input').attr("type","password");
        $(this).find('span').removeClass('opend').addClass('closed');
    }
});

//表单验证
function verifyForm() {
    var username = $('#username').val().trim();
    var password = $('#password').val().trim();
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


//背景图样式开始
function tobesize(){
    $("#ad-pos").css("left",-(1922-$(window).width())/2).css("top",0);
}

$(function(){
    verifyForm();
    tobesize();
});


$(window).on("resize",function(){
    tobesize();
})


