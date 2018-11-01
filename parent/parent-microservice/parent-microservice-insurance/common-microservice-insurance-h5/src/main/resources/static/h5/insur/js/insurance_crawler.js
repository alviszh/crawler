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

/**
 * 触发数据抓取接口
 */
function crawler(){
    $.ajax({
        type: "POST",
        url:"/h5/insur/crawler",
        data:$('#authform').serialize(),
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_crawler();
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_crawler); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}

/**
 * 触发登录
 */
function nextLogin(){
    var url_insur;
    var city = $('p.navbar-brand').find('span').text();
    if(city.indexOf("四川省")!=-1||city.indexOf("新疆维吾尔自治区")!=-1){
         url_insur = '/h5/insur/crawler';
    }
    else{
         url_insur = '/h5/insur/login';
    }
    $.ajax({
        type: "POST",
        url: url_insur,
        data:$('#authform').serialize(),// 你的formid
        async: true,
        dataType : "json",
        beforeSend: function () {
            interval_login();
            $('#message').text('开始准备...');
            $('#myModal').modal('show');
        },
        error: function(request) {
            console.log("error");
            clearInterval(interval_login); //停止间隔一秒调用
        },
        success: function(data) {
            console.log(data.description);
            return;
        }
    });
}

/**
 * 间隔一秒调用获取task状态的方法（登录）
 */
function interval_login() {
    var interval_login = setInterval(function (){
        console.log("********** "+$("#taskId").val());
        var taskId = $("#taskId").val();
        $.ajax({
            type: "POST",
            url:"/h5/insur/tasks/status",
            data:{
                taskId: taskId
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_login); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息
                if ((data.phase == "LOGIN" && data.phase_status == "SUCCESS") && ((data.city != "宁波市" )||(data.city != "鞍山市" ))) {//认证成功！
                    clearInterval(interval_login);
                    crawler(); //调用数据采集接口

                }
                if ((data.phase == "LOGIN" && data.phase_status == "ERROR")  && (data.city != "烟台市")) {//认证失败！
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/insur"
                    });
                    return;
                }
                if ((data.phase == "LOGIN" && data.phase_status == "ERROR") && (data.city == "烟台市")) {//认证失败！(烟台市)
                    clearInterval(interval_login);
                    imgCode();
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/insur"
                    });
                    return;
                }
                if (data.phase == "AGENT" && data.phase_status == "ERROR") {
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/insur"
                    });
                    return;
                    return;
                }
                if (data.phase == "CRAWLER" && data.phase_status == "ERROR") {//爬取失败
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    $('#msg').find("button").bind("click",function(){
                        location.href = "/h5/insur"
                    });
                    return;
                }

                if (data.finished) { //数据采集成功
                    clearInterval(interval_login);
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    var city = $("#city").val();

                    result(taskId);
                    $('#success_modal').modal('show');
                    return;
                }

            }
        });
    }, 1000);
}

/**
 * 间隔一秒调用获取task状态的方法
 */
function interval_crawler() {
    var interval_crawler = setInterval(function (){
        console.log("********** "+$("#taskId").val());
        var taskId = $("#taskId").val();
        $.ajax({
            type: "POST",
            url:"/h5/insur/tasks/status",
            data:{
                taskId: taskId
            },
            dataType : "json",
            error: function(request) {
                console.log("error");
                clearInterval(interval_crawler); //停止间隔一秒调用
                $('#myModal').modal('hide');
                return;
            },
            success: function(data) {
                console.log(data.description);
                $('#message').text(data.description); //反馈信息
                if (data.phase == "CRAWLER" && data.phase_status == "ERROR") {//爬取失败
                    clearInterval(interval_crawler);
                    $('#myModal').modal('hide');
                    //弹出提示框
                    $('#msg').modal('show');
                    $('#alertMsg').text(data.description);
                    return;
                }

                if (data.finished) { //数据采集成功
                    clearInterval(interval_crawler);
                    $('#myModal').modal('hide');
                    //themeColor = themeColor.substr(1,themeColor.length);
                    var isTopHide = false;
                    if (topHide == "none") {
                        isTopHide = true;
                    }
                    var city = $("#city").val();

                    result(taskId);
                    $('#success_modal').modal('show');
                    return;
                }
            }
        });
    }, 1000);
}

