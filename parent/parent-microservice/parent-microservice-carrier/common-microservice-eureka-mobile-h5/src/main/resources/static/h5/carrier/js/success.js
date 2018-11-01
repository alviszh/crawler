function show(){
    console.log("********** "+$("#task_id").val());
    var taskid = $("#task_id").val();
    $.ajax({
        type: "GET",
        url:"/h5/carrier/tasks/status",
        data:{
            taskid: taskid
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
            if (data.userMsgStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【用户信息】抓取已完成</p>");
            } else if (data.userMsgStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【用户信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【用户信息】抓取未完成，状态码："+data.userMsgStatus+"</p>");
            }
            // 账户信息
            if (data.accountMsgStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【账户信息】抓取已完成</p>");
            } else if (data.accountMsgStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【账户信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【账户信息】抓取未完成，状态码："+data.accountMsgStatus+"</p>");
            }
            // 通话详单
            if (data.callRecordStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【通话详单】抓取已完成</p>");
            } else if (data.callRecordStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【通话详单】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【通话详单】抓取未完成，状态码："+data.callRecordStatus+"</p>");
            }
            // 短信记录
            if (data.smsRecordStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【短信记录】抓取已完成</p>");
            } else if (data.smsRecordStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【短信记录】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【短信记录】抓取未完成，状态码："+data.smsRecordStatus+"</p>");
            }
            // 业务信息
            if (data.businessMsgStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【业务信息】抓取已完成</p>");
            } else if (data.businessMsgStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【业务信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【业务信息】抓取未完成，状态码："+data.businessMsgStatus+"</p>");
            }
            // 缴费信息
            if (data.payMsgStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【缴费信息】抓取已完成</p>");
            } else if (data.payMsgStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【缴费信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【缴费信息】抓取未完成，状态码："+data.payMsgStatus+"</p>");
            }
            // 积分信息
            if (data.integralMsgStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【积分信息】抓取已完成</p>");
            } else if (data.integralMsgStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【积分信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【积分信息】抓取未完成，状态码："+data.integralMsgStatus+"</p>");
            }
            // 亲情号信息
            /*if (data.familyMsgStatus == 200) {
                $("#successMsg").append("<p class='bg-success'>【亲情号信息】抓取已完成</p>");
            } else if (data.familyMsgStatus == 201) {
                $("#successMsg").append("<p class='bg-info'>【亲情号信息】抓取已完成，无数据</p>");
            } else {
                $("#successMsg").append("<p class='bg-warning'>【亲情号信息】抓取未完成，状态码："+data.familyMsgStatus+"</p>");
            }*/
            var carrier = "";
            if (data.carrier == "CMCC") {
                carrier = "中国移动";
            } else if (data.carrier == "UNICOM") {
                carrier = "中国联通";
            } else if (data.carrier == "CHINA_TELECOM") {
                carrier = "中国电信";
            } else if (data.carrier == "VNO") {
                carrier = "虚拟运营商";
            }

            $("#successMsg").append("<p>手机号码："+ data.phonenum +"，运营商："+ carrier +"，taskid："+ taskid +"</p>");

        }
    });
}

$(function(){
    show();
});