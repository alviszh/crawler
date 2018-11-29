$(function(){
    if (topHide != "none") {//显示顶部
       // $('.container').css("padding-top", "46px");
    }
    showCities();
    $('#cardType').val('CREDIT_CARD');
});

$('#myTab').find('li').click( function() {
    if(!$(this).hasClass("active")&&$(this).index()==0){
        $('#cardType').val('CREDIT_CARD');

    }
    if(!$(this).hasClass("active")&&$(this).index()==1){
        $('#cardType').val('DEBIT_CARD');
    }
})

/*显示开发完成的城市*/
function showCities(){
    $.ajax({
        type: "POST",
        url:"/h5/bank/getBank",
        data:{
        },
        dataType : "json",
        error: function(request) {
            console.log("error");
            return;
        },
        success: function(data) {
            // 城市列表
            $.each(data, function(i,item){
                var isFlag = item.isFlag;var creFlag = item.creFlag;
                if (isFlag == 1) {
                    $("#deposits").append('<div class="sort_list"><a href="javascript:void(0)" class="list-group-item ' + item.bankShortnameEn + '"  name="num_name" id="' + item.bankId + '">'
                        + item.bankShortname + '</a><i></i></div>');
                }
                if (creFlag == 1) {
                    $("#visa").append('<div class="sort_list"><a href="javascript:void(0)" class="list-group-item ' + item.bankShortnameEn + '"  name="num_name" id="' + item.bankId + '">'
                        + item.bankShortname + '</a><i></i></div>');
                }
                if (isFlag == 0) {
                    $("#deposits").append('<div class="sort_list"><a style="filter:grayscale(0.9);color:#cdcdcd!important" href="javascript:void(0)" class="list-group-item disabled '+item.bankShortnameEn+'"  name="num_name" id="'+item.bankId+'">'
                        + item.bankShortname + ' (正在升级中)</a><i></i></div>');
                }
                if (creFlag == 0) {
                    $("#visa").append('<div class="sort_list"><a style="filter:grayscale(0.9);color:#cdcdcd!important" href="javascript:void(0)" class="list-group-item disabled '+item.bankShortnameEn+'"  name="num_name" id="'+item.bankId+'">'
                        + item.bankShortname + ' (正在升级中)</a><i></i></div>');
                }
            });

//            monitor();
            /*跳转登录页面*/
            //$("[name='num_name']:not(.disabled)").off("click");
            $(document).on("click", "[name='num_name']:not(.disabled)", function(){
                var city = $(this).text();
                var taskId = $("#taskId").val();
                var idNum = $("#idNum").val();
                var name = $("#name").val();
                var cardType = $("#cardType").val();
                var username = $("#username").val();
                var idNum = $("#idNum").val();

                var isTopHide = false;
                if (topHide == "none") {
                    isTopHide = true;
                }
                if (city.indexOf("（内测中）") != -1) {
                    city = city.substr(0, city.indexOf("（内测中）"));
                }

                $(this).attr("href", "/h5/bank/item?cardType=" + cardType + "&city="+city +"&themeColor="+themeColor+"&isTopHide="+isTopHide +"&username="+ username + "&idNum=" + idNum);

            });
        }
    });

}

function monitor() {
    $.ajax({
        type: "POST",
        url: "/h5/bank/monitor",
        data: {},
        dataType: "json",
        error: function (request) {
            console.log("error");
            return;
        },
        success: function (data) {
            var data=eval(data);
               var ct=$('#deposits');
               for (var i=0; i<data.length; i++){
                   if(data[i].usablestate!=0){
                       for (var j=0; j<ct.children().length; j++) {
                           if(data[i].banktype ==ct.children().eq(j).find('a').text()){
                               ct.children().eq(j).find('a').css({
                                   "filter":"grayscale(0.9)",
                                   "color":"#cdcdcd"
                               }).text(data[i].banktype+" (正在维护中)").addClass('disabled');
                           }
                       }
                   }
               }

               var ct1=$('#visa');
               for (var i=0; i<data.length; i++){
                   if(data[i].usablestate!=0){
                       for (var j=0; j<ct1.children().length; j++) {
                           if(data[i].banktype ==ct1.children().eq(j).find('a').text()){
                               ct1.children().eq(j).find('a').css({
                                   "filter":"grayscale(0.9)",
                                   "color":"#cdcdcd"
                               }).text(data[i].banktype+" (正在维护中)").addClass('disabled');
                           }
                       }
                   }
               }
        }
    })
}