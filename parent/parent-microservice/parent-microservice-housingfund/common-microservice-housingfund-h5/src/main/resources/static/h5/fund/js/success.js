function show(){
    console.log("********** "+$("#taskId").val());
    var taskId = $("#taskId").val();
    $.ajax({
        type: "POST",
        url:"/h5/fund/tasks/status",
        data:{
            taskId: taskId
        },
        dataType : "json",
        error: function(request) {
            console.log("error");
            return;
        },
        success: function(data) {
            console.log(data.phase+","+data.phase_status);
            console.log(data.description);

            // 用户信息
            if (data.userInfoStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【用户信息】抓取已完成</p>");
            } else if (data.userInfoStatus == 201 || data.userInfoStatus == 160) {
                $("#successMsg").append("<p class='bg-info'>【用户信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【用户信息】抓取未完成，状态码："+data.userInfoStatus+"</p>");
            }
            // 养老保险信息
            if (data.yanglaoStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【养老保险信息】抓取已完成</p>");
            } else if (data.yanglaoStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【养老保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【养老保险信息】抓取未完成，状态码："+data.yanglaoStatus+"</p>");
            }

            // 失业保险信息
            if (data.shiyeStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【失业保险信息】抓取已完成</p>");
            } else if (data.shiyeStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【失业保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【失业保险信息】抓取未完成，状态码："+data.shiyeStatus+"</p>");
            }

            // 工伤保险信息
            if (data.gongshangStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【工伤保险信息】抓取已完成</p>");
            } else if (data.gongshangStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【工伤保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【工伤保险信息】抓取未完成，状态码："+data.gongshangStatus+"</p>");
            }

            // 生育保险信息
            if (data.shengyuStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【生育保险信息】抓取已完成</p>");
            } else if (data.shengyuStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【生育保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【生育保险信息】抓取未完成，状态码："+data.shengyuStatus+"</p>");
            }

            // 医疗保险信息
            if (data.yiliaoStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【医疗保险信息】抓取已完成</p>");
            } else if (data.yiliaoStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【医疗保险信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【医疗保险信息】抓取未完成，状态码："+data.yiliaoStatus+"</p>");
            }
            $("#successMsg").append("<p>社保城市："+ data.city +"，taskId："+ taskId +"</p>");

        }
    });
}

$(function(){
    show();
});