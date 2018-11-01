var chart;
var pieChart;
$(function(){

	chart = new Highcharts.Chart('container', {
        title: {
            text: '新增调用量',
//            x: -20
        },
        /*subtitle: {
         text: '数据来源: WorldClimate.com',
         x: -20
         },*/
        xAxis: {
            categories: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
        },
        yAxis: {
            title: {
                text: '调用量'
            },
            plotLines: [{
                value: 0,
                width: 1,
//                color: '#808080'
            }]
        },
        credits:{
            enabled:false
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: []
    });
	
	chart1 = new Highcharts.Chart('insurancePie', {
        title: {
            text: '社保城市调用量',
//            x: -20
        },
        /*subtitle: {
         text: '数据来源: WorldClimate.com',
         x: -20
         },*/
        xAxis: {
            categories: []
        },
        yAxis: {
            title: {
                text: '调用量'
            },
            plotLines: [{
                value: 0,
                width: 1,
//                color: '#808080'
            }]
        },
        credits:{
            enabled:false
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            borderWidth: 0
        },
        series: []
    });

	
    loadLineData();
    loadPieData();
});


function loadLineData(){
    $.ajax({
        url:'/webmanage/data/insurance/lineData',
        type:'GET',
        dataType:'json',
        success: function (data) {
            if (data["time"] != null && data["time"] != 'null') {
                chart.xAxis[0].categories = data["time"];
            }
            if (data["amount"] != null && data["amount"] != 'null') {
                var series = chart.addSeries({
                    name: '调用量',
                    data: data["amount"]
                }, true);
            }
        }
    });
}


function loadPieData(){
    $.ajax({
        url:'/webmanage/data/insurance/pieData',
        type:'GET',
        dataType:'json',
        success: function (data) {
        	console.log("data-----------"+data);
            if (data["city"] != null && data["city"] != 'null') {
                chart1.xAxis[0].categories = data["city"];
            }
            if (data["amount"] != null && data["amount"] != 'null') {
                var series = chart1.addSeries({
                    name: '调用量',
                    data: data["amount"]
                }, true);
            }
        }
    });
}