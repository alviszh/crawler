var chart;
var pieChart;
$(function() {

	chart = new Highcharts.Chart('container', {
		title : {
			text : '新增调用量',
		},
		xAxis : {
			categories : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月',
					'九月', '十月', '十一月', '十二月' ]
		},
		yAxis : {
			title : {
				text : '调用量'
			},
			plotLines : [ {
				value : 0,
				width : 1,
			} ]
		},
		credits : {
			enabled : false
		},
		legend : {
			layout : 'vertical',
			align : 'right',
			verticalAlign : 'middle',
			borderWidth : 0
		},
		series : []
	});
//	chart.xAxis[0].categories = [ '1月', '2月', '3月', '四月', '五月', '六月', '七月',
//			'八月', '九月', '十月', '十一月', '十二月' ];
//	var series = chart.addSeries({
//		name : '纽约',
//		data : [ -0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6,
//				2.5 ]
//	}, true);

	pieChart = new Highcharts.chart(
			'mobilePie',
			{
				chart : {
					plotBackgroundColor : null,
					plotBorderWidth : null,
					plotShadow : false,
					type : 'pie'
				},
				title : {
					text : '网银任务数比例'
				},
				tooltip : {
					pointFormat : '{series.name}: <b>{point.percentage:.1f}%</b>'
				},
				plotOptions : {
					pie : {
						allowPointSelect : true,
						cursor : 'pointer',
						dataLabels : {
							enabled : true,
							format : '<b>{point.name}</b>: {point.percentage:.1f} %',
							style : {
								color : (Highcharts.theme && Highcharts.theme.contrastTextColor)
										|| 'black'
							}
						}
					}
				},
				 series: []
//				series : [ {
//					name : 'Brands',
//					colorByPoint : true,
//					data : [ {
//						name : '移动',
//						y : 56.33
//					}, {
//						name : '电信',
//						y : 24.03,
//						sliced : true,
//						selected : true
//					}, {
//						name : '联通',
//						y : 10.38
//					} ]
//				} ]
			});

	 loadLineData();
	 loadPieData();
});

function loadLineData() {
	$.ajax({
		url : '/webmanage/data/bank/lineData',
		type : 'GET',
		dataType : 'json',
		success : function(data) {
			if (data["time"] != null && data["time"] != 'null') {
				chart.xAxis[0].categories = data["time"];
			}
			if (data["amount"] != null && data["amount"] != 'null') {
				var series = chart.addSeries({
					name : '调用量',
					data : data["amount"]
				}, true);
			}
		}
	});
}

function loadPieData() {
	$.ajax({
		url : '/webmanage/data/bank/pieData',
		type : 'GET',
		dataType : 'json',
		success : function(data) {
			var series = pieChart.addSeries({
				name : '百分比',
				colorByPoint : true,
				data : data
			}, true);
		}
	});
}