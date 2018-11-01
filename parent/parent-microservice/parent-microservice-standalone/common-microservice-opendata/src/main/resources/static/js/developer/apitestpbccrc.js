$(function(){
    $("#reportBtn").click(function(){
        console.log("getReport");
        report();
    });
});
function report(){
    var taskId = $("#taskId").val();
    console.log("taskId:"+taskId);
    $.ajax({
        url: "/opendata/developer/app/pbccrc/v2/report",
        type:"POST",
        async:true,
        dataType:"JSON",
        data:{ taskId : taskId },
        success:function(data){
            console.log(data);
            console.log(data.url);
            //console.log(data.data);
            $("#urlPre").text(data.url);
            $("#reportPre").text(data.data);
            return;
        }
    });

}
