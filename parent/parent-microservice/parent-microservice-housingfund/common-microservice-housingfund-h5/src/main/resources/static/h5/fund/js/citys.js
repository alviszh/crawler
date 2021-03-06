$(function(){
    if (topHide != "none") {//显示顶部
        $('.container').css("padding-top", "70px");
    }
    showCities();
});

/*显示开发完成的城市*/
function showCities(){
    $.ajax({
        type: "POST",
        url:"/h5/fund/cityList",
        data:{
        },
        dataType : "json",
        error: function(request) {
            console.log("error");
            return;
        },
        success: function(data) {
            $("#search_text").keyup(function () {
                AutoComplete("sort_box", "search_text", data);
            });

            console.log(data[0].parentRegionName);
            // 城市列表
            $.each(data, function(i,item){
                var isFinished = item.isHousingFundFinished;
                if (isFinished == 1) {
                    $("#sort_box").append('<div class="sort_list"><a href="#" class="list-group-item" name="num_name" id="'+item.regionId+'">'
                        + item.regionName + '</a><b class="ion-ios-arrow-right"></b></div>');
                } else if (isFinished == 2){
                    $("#sort_box").append('<div class="sort_list"><a href="#" class="list-group-item disabled" style="background-color:#f5f5f5" name="num_name" id="'
                        + item.regionId+'">'+ item.regionName+'（正在维护中）</a></div>');
                } else if (isFinished == 0){
                    $("#sort_box").append('<div class="sort_list"><a href="#" class="list-group-item" name="num_name" id="'+item.regionId+'">'
                        + item.regionName + '（内测中）</a></div>');
                }
            });
            //城市列表排序
            initials();

            /*跳转登录页面*/
            $(document).on("click", "[name='num_name']:not(.disabled)", function(){
                var city = $(this).text();
                console.log(city);
                var taskId = $("#taskId").val();
                var idnum = $("#idnum").val();
                var username = $("#name").val();
                var isTopHide = false;
                if (topHide == "none") {
                    isTopHide = true;
                }
                if (city.indexOf("（内测中）") != -1) {
                    city = city.substr(0, city.indexOf("（内测中）"));
                    console.log(city);
                }

                $(this).attr("href", "/h5/fund/housingfund?themeColor="+themeColor+"&isTopHide="+isTopHide+"&city="+city+"&username="+ username + "&idnum=" + idnum);

            });
        }
    });
}


var old_value = "";
//自动完成
function AutoComplete(auto, search, mylist) {

    if ($("#" + search).val() != old_value || old_value == "") {
        var autoNode = $("#" + auto);   //缓存对象（弹出框）
        var carlist = new Array();
        var n = 0;
        old_value = $("#" + search).val();
        //console.log(old_value);
        for (i in mylist) {
            //console.log('mylist[i].regionName'+ mylist[i].regionName);
            if (mylist[i].regionName.indexOf(old_value) >= 0) {
                carlist[n++] = mylist[i];
            }
        }
        console.log(carlist);
        if (carlist.length == 0) {
            autoNode.hide();
            return;
        }

        $(".cur_city").hide();
        autoNode.empty();  //清空上次的记录

        for (i in carlist) {
            console.log(i);
            var isFinished = carlist[i].isHousingFundFinished;
            if (isFinished == 1) {
                console.log(carlist[i]);
                $("#sort_box").show().append('<div class="sort_list"><a href="#" class="list-group-item" name="num_name" id="' + carlist[i].regionId + '">'
                    + carlist[i].regionName + '</a><b class="ion-ios-arrow-right"></b></div>');
            } else if (isFinished == 2) {
                $("#sort_box").show().append('<div class="sort_list"><a href="#" class="list-group-item disabled" style="background-color:#f5f5f5" name="num_name" id="'
                    + carlist[i].regionId + '">' + carlist[i].regionName + '（正在维护中）</a></div>');
            } else if (isFinished == 0) {
                $("#sort_box").show().append('<div class="sort_list"><a href="#" class="list-group-item" name="num_name" id="' + carlist[i].regionId + '">'
                    + carlist[i].regionName + '（内测中）</a></div>');
            }
        }
        if ($("#search_text").val() == "") {
            $(".cur_city").show();
            initials();
        }
    }
}