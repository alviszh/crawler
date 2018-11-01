
/*触发爬虫爬取*/
function telecom_getAllData(form){
    $.ajax({
        type: "POST",
        url:"/h5/carrier/telecom/getAllData",
        data:$('#'+form).serialize(),// 你的formid
        async: true,
        dataType : "json",
        error: function(request) {
            console.log("error");
            //$('#message').text('请求超时，请重试！');
        },
        success: function(data) {
            console.log(data);
            return;
        }
    });
}