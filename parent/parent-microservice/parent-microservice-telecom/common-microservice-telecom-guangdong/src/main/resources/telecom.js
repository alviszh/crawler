/*
 **个人中心共用JS文件
 */

//创建html5元素 for IE6,7,8
var elems = ['section', 'article', 'nav', 'header', 'footer', 'aside', 'menu', 'figure', 'figcaption', 'time', 'mark', 'details', 'summary', 'hgroup', 'dialog'];
for (var i = 0, j = elems.length; i < j; i++) {
	document.createElement(elems[i]);
}

var _loginNumber = '';
var _loginNumBusiType = '';
var _payType = '';
var _custCode = '';
var _custType = '';
var _latn = '';
var _adsl = '';
var _adslType = '';
var msgNum = 0;
var _Now_loginNumber;
var _Now_loginNumber_type;;
$(function() {
	//右下角弹
	$('#rb_box').animate({
		bottom: '2px'
	}, 400);
	$('#rb_box .rbShut').click(function() {
		$('#rb_box').hide();
	})

	//锚点定位
	//window.location.hash = "#f_info"; 

	//验证登录
	$.get('/common/getIsLogin.jsp', function(json) {
		if (json && json != null) {
			if ($.trim(json) == 'loginInfo=0') {
				var noLogin = '';
				if ("undefined" != typeof withoutLogin) {
					noLogin = true;
				}
				if (noLogin == null || noLogin == '' || noLogin == undefined || noLogin == 'undefined') {
					reLogin();
					return false;
				} else {
					$('#f_info_login').hide();
					$('#nologin').show();
				}
			} else {
				_loginNumber = $.cookie('wt_acc_nbr');
				_loginNumBusiType = $.cookie('wt_serv_type');
				_latn = $.cookie('LATN_CODE_COOKIE');

				var _d_ = new Date();
				var _month_ = _d_.getMonth();
				var _date_ = _d_.getDate();
				var _h_ = _d_.getHours();
				var _minu_ = _d_.getMinutes();
				var _se_ = _d_.getSeconds();
				var _cur_ = _d_.getFullYear() + '-' + ((_month_ + 1) < 10 ? '0' + (_month_ + 1) : (_month_ + 1)) + '-' + (_date_ < 10 ? ('0' + _date_) : _date_);
				var last_login_time = _cur_ + '  ' + (_h_ < 10 ? ('0' + _h_) : _h_) + ':' + (_minu_ < 10 ? ('0' + _minu_) : _minu_) + ':' + (_se_ < 10 ? ('0' + _se_) : _se_);
				$('#last_login').text(last_login_time); //上次登录时间

				/*客户信息*/
				renderLocalStorage('custInfo', _latn, _loginNumber, {});
				//
				renderLocalStorage('iptvInfo', _latn, _loginNumber, {});
			}
		}
	});

	//退出登录
	$('#exitSystem').click(function() {
		if (confirm("你确定要退出登录吗？")) {
			window.location.href = "/common/exitSystem.jsp";
			return false;
		}
	});

	//==================================

	var loginUrl = 'http://gd.189.cn/common/login.jsp?loginOldUri=' + location.pathname + location.search;
	$('#toLoginUrl').attr("href", loginUrl);


	//问候语
	var today = new Date();
	var hour = today.getHours();
	var mon = today.getMonth() + 1;
	var dd = today.getDate();
	var text1, text2;
	//if(hour <4){text1 = "夜已深，注意休息";}
	//else if(hour <9){text1 = "早上好";}
	if (hour < 9) {
		text1 = "早上好";
	} else if (hour < 12) {
		text1 = "上午好";
	} else if (hour < 14) {
		text1 = "中午好";
	} else if (hour < 19) {
		text1 = "下午好";
	} else {
		text1 = "晚上好";
	}
	//else if(hour <22){text1 = "晚上好";}
	//else {text1 = "夜猫族，早点睡哦";}

	if (mon <= 5 && mon >= 3) {
		text2 = "气候多变，小心感冒哦";
	} //春季
	else if (mon <= 8 && mon >= 6) {
		text2 = "炎炎夏日，要多喝水哦";
	} //夏季
	else if (mon <= 11 && mon >= 9) {
		text2 = "秋风瑟瑟，别忘记添加衣物";
	} //秋季
	else {
		text2 = "天气寒冷，注意保暖啊";
	} //冬季

	$('#time').text(text1);
	$('#time_text').text(text2);

	$('#helloTime').text(text1);
	$('#tianqi').text(text2);

	// 未登录账单提示
	var zhangdan = "";
	if (dd >= 4) { // 4号出账
		zhangdan = (mon - 1) + "月账单可以查询了哦";
		$('#zhangdan').text(zhangdan);
		$('#zhangdan').show();
	} else {
		$('#zhangdan').hide();
		$('#wxtips').hide();
		$('#zhangdantips').hide();
	}


	//问号划过
	$('.why,.why>img').hover(function() {
		$(this).next('.whyBox').show();
	}, function() {
		$(this).next('.whyBox').hide();
	});

	//消息查看
	$('.userMsg .fold').bind('click', function() {
		$('.userMsg .fold').hide();
		$('.userMsg .open').show();
		$('.userMsg').animate({
			right: '0'
		}, 600);
	});
	$('.icoClose').bind('click', function() {
		$('.userMsg').animate({
			right: '-426px'
		}, 600, function() {
			$('.userMsg .fold').show();
			$('.userMsg .open').hide();
		})
	});

	//选择查询号码
	$('.currSel').hover(function() {
		$('.selList', $(this)).show();
	}, function() {
		$('.selList', $(this)).hide();
	});
	$('.selList li:not(.lst)').bind('click', function() {
		$('.selList').hide();
		$(this).parent().prev('.currItem').text($(this).text());
	});

	//勾选同意
	$('#together~label').click(function() { //alert(1)
		$(this).parent('.chkbox').toggleClass('chkyes');
	});

	//左边栏子菜单效果
	/*$('.sideMenu dt').click(function(){
		$('.sideMenu dd').not('.dd'+this.className.slice(-1)).slideUp(150);
		$(this).nextAll('.dd'+this.className.slice(-1)).slideDown(150);
		$(this).addClass('add_ico').siblings('dt').removeClass('add_ico');	
	});
	
	$('.sideMenu dd').hover(function(){
		$(this).addClass('hover').find('ul').show();
	},function(){
		$(this).removeClass('hover').find('ul').hide();
	});*/

	//关注的商品
	var sNum = 0; //滚动次数
	$('.recoBox .prev').click(function() {
		if (sNum > 0) {
			$('.recoList').animate({
				marginLeft: '+=220px'
			}, 200);
			sNum--;
		}
	});

	$('.recoBox .next').click(function() {
		if (sNum < ($('.recoList li').length - 4)) {
			$('.recoList').animate({
				marginLeft: '-=220px'
			}, 200);
			sNum++;
		}
	});

	//我的优惠- 查看详情
	$('.yhTb .seeDetail').toggle(function() {
		$(this).text('收　起 ↑');
		$(this).parent().parent().next('tr').show();
	}, function() {
		$(this).text('查看详情 ↓');
		$(this).parent().parent().next('tr').hide();
	});

	//我的账单-查询方式切换
	$('.checkType input:radio').click(function(e) {
		var i = $('.checkType input:radio').index(this);
		if (i == 1) {
			$('#sel_num').show();
		} else {
			$('#sel_num').hide();
		}
	});


	//我的服务登录切换
	$('.ty_tab li').click(function(e) {
		var pathname = location.pathname;
		if (pathname == '/malfunction/malFunction_index.html' || pathname.indexOf('malFunction_index.html') > -1) { //如果是修账页面，图片地址不一样
			var i = $('.ty_tab li').index(this);
			$(this).addClass('curr').siblings().removeClass('curr');
			if (i == 0) {
				$(this).children('img').attr('src', '/service/home/images/zjq_cur.png');
				$(this).siblings('li').children('img').attr('src', '/service/home/images/cpq.png');
			} else {
				$(this).children('img').attr('src', '/service/home/images/cpq_cur.png');
				$(this).siblings('li').children('img').attr('src', '/service/home/images/zjq.png');
			}
			$(this).parent('.ty_tab').siblings('.ty_tab_con').children('table').eq(i).show().siblings('table').hide();
		} else {
			var i = $('.ty_tab li').index(this);
			$(this).addClass('curr').siblings().removeClass('curr');
			if (i == 0) {
				$(this).children('img').attr('src', '../images/zjq_cur.png');
				$(this).siblings('li').children('img').attr('src', '../images/cpq.png');
			} else {
				$(this).children('img').attr('src', '../images/cpq_cur.png');
				$(this).siblings('li').children('img').attr('src', '../images/zjq.png');
			}
			$(this).parent('.ty_tab').siblings('.ty_tab_con').children('table').eq(i).show().siblings('table').hide();
		}
	});

	//我的189栏
	$('.my189Bar dl').hover(function() {
		$(this).addClass('hover').find('dd').show();
	}, function() {
		$(this).removeClass('hover').find('dd').hide();
	})

	//我的套餐-我的消费 切换
	$('.ttB .tabItem li').not('.fst').click(function() {
		$(this).addClass('curr').siblings('li').removeClass('curr');
		$(this).parent().parent().siblings('.tabCon').eq($(this).index() - 1).show().siblings('.tabCon').hide();
		//		if($(this).index()==2){
		//			$('.userSel').hide();
		//		}else{
		//			$('.userSel').show();
		//		}
	});
});



//====================================================================================


//是否存在指定变量 
function exitsVariable(variableName) {
	try {
		if ("undefined" == typeof(variableName)) {
			return false;
		} else {
			return true;
		}
	} catch (e) {}
	return false;
}



//设置导航菜单当前项
function setLtNav(m_id) {
	if (m_id) {
		$('.sideMenu #' + m_id + ' a').addClass('hovLv');
	}
}



//设置本地缓存sessionStorage
function setLocalStorage(name, value) {
	try {
		var storage = window.sessionStorage;
		if (storage) {
			storage.setItem(name, value);
		}
	} catch (e) {}
}

//删除本地缓存sessionStorage
function removeLocalStorage(name) {
	try {
		var storage = window.sessionStorage;
		if (storage) {
			storage.removeItem(name);
		}
	} catch (e) {}
}

//根据name值渲染页面的相应模块（优先从本地缓存获取相应信息）
function renderLocalStorage(name, latnId, num, params) {
	try {
		//var loginRedirect = $.cookie('loginRedirect');
		//if(loginRedirect){
		//	renderSessionStorage(name);
		//	return;
		//}

		var storage = window.sessionStorage;
		if (storage) {
			if (name == "custInfo" || name == "adslInfo" || name == "adslExpire" || name == "opticalInfo") {
				renderSessionStorage(name, latnId, num, params);
			} else {
				var json = JSON.parse(storage.getItem(latnId + '-' + num + '-' + name + '-TEST'));
				if (json != null && json != '' && json != 'undefined') {
					callback(name, JSON.parse(storage.getItem(latnId + '-' + num + '-' + name + '-TEST')));
				} else {
					renderSessionStorage(name, latnId, num, params);
				}
			}
		} else {
			renderSessionStorage(name, latnId, num, params);
		}
	} catch (e) {}
}

//根据name值渲染页面的相应模块（从后台获取相应信息）
function renderSessionStorage(name, latnId, num, params) {
	//var scure={'a.c':'0','a.u':'user','a.p':'pass','a.s':'ECSS'};
	params['a.c'] = '0';
	params['a.u'] = 'user';
	params['a.p'] = 'pass';
	params['a.s'] = 'ECSS';
	var actionUrl = getActionUrl(name);
	$.ajax({
		url: actionUrl,
		type: "post",
		data: params,
		dataType: "json",
		timeout: 30000,
		error: function() {
			return false;
		}, //错误处理
		complete: function(XMLHttpRequest, status) { //请求完成后最终执行参数
			　　　　
			if (status == 'timeout') { //加载15s超时,status还有success,error等值的情况
				return false;　　　　
			}
		},
		success: function(result) {

			if (name == 'balance' || name == 'realTimeFee' || name == 'arrearage' || name == 'integral') { //余额、实时话费、欠费、积分不做缓存
			} else if (name == 'consume') { //查询成功才做相应的本地缓存
				if (num) {
					if (result != null || result != '' || result != 'undefined') {
						var resultCode = result.resultCode;
						if (resultCode != null) {
							if (resultCode == '0') {
								setLocalStorage(latnId + '-' + num + '-' + name + '-TEST', JSON.stringify(result));
							}
						} else {
							var resultInfo = result.resultInfo;
							if (resultInfo != null) {
								if (resultInfo == '') {
									setLocalStorage(latnId + '-' + num + '-' + name + '-TEST', JSON.stringify(result));
								}
							}
						}
					}
				}
			} else {
				//				if(name == 'custInfo') {
				//					if(result == 'undefined' || result == null || result == '') {return false;}
				//					if(result.b == null || result.b == ''){ return false; }
				//					var c = result.b.c;
				//					var m = result.b.m;
				//					if(c == '01') {if( m == '未登录!' || m.indexOf('未登录') != -1) {reLogin();return false;}}
				//					
				//					if(result.r == null || result.r == ''){return false; }
				//					var code = result.r.code;
				//					var msg = result.r.msg;
				//					if( msg == '未登录!' || msg.indexOf('未登录') != -1){ reLogin(); return false;}//未登录
				//				}

				if (num) {
					if (result != null || result != '' || result != 'undefined') {
						if (result.b != null || result.b != '' || result.b != 'undefined') {
							var c = result.b.c;
							var m = result.b.m;
							if (c != null && c == '00') {
								if (result.r != null || result.r != '' || result.r != 'undefined') {
									var code = result.r.code;
									var msg = result.r.msg;
									if (code != null && code == '000') {
										setLocalStorage(latnId + '-' + num + '-' + name + '-TEST', JSON.stringify(result));
									}
								}
							}
							//else if(c == '01') {if( m == '未登录!' || m.indexOf('未登录') != -1) {reLogin();return false;}}
						}
					}
				}
			}

			callback(name, result); //回调渲染函数
		}
	});
}

//根据key值获取请求的url
function getActionUrl(key) {
	var url = '';
	switch (key) {
		case 'custInfo':
			url = '/J/J10036.j';
			break;
		case 'myProducts':
			url = '/J/J10037.j';
			break;
		case 'balance':
			url = '/J/J10038.j';
			break;
		case 'integral':
			url = '/J/J10032.j';
			break;
		case 'MyComboUsed':
			url = '/J/J10041.j';
			break;
		case 'adslInfo':
			url = '/J/J10043.j';
			break;
		case 'adslExpire':
			url = '/J/J10044.j';
			break;
		case 'unpayCrmOrder':
			url = '/J/J10040.j';
			break;
			//case 'arrearage':url='/query/json/qianfei.parser?queryType='+(_loginNumBusiType=='CUST'?'1':'0');break;
		case 'arrearage':
			url = '/J/J10060.j';
			break;
		case 'arrearageMsg':
			url = '/J/J10060.j';
			break;
		case 'consume':
			url = '/consume.do';
			break;
			//case 'realTimeFee':url='/query/json/realTimeFee.parser?queryType='+(_loginNumBusiType=='CUST'?'1':'0');break;
		case 'realTimeFee':
			url = '/J/J10059.j';
			break;
		case 'iptvInfo':
			url = '/J/J10045.j';
			break;
		case 'is4G_tj':
			url = '/J/J10012.j';
			break;
		case 'opticalInfo':
			url = '/J/J10097.j';
			break;
	}
	return url;
}

//根据key值，分别调用不同的渲染方法，渲染不同的模块
function callback(key, json) {
	switch (key) {
		case 'custInfo':
			formatCustInfo(json);
			break;
		case 'balance':
			formatBalance(json);
			break;
		case 'integral':
			formatIntegral(json);
			break;
		case 'realTimeFee':
			formatRealTimeFee(json);
			break;
		case 'arrearage':
			formatArrearage(json);
			break;
		case 'MyComboUsed':
			formatMyComboUsed(json);
			break;
		case 'myProducts':
			formatMyProducts(json);
			break;
		case 'adslInfo':
			formatAdslInfo(json);
			break;
		case 'adslExpire':
			formatAdslExpire(json);
			break;
		case 'unpayCrmOrder':
			formatUnpayCrmOrder(json);
			break;
		case 'arrearageMsg':
			formatArrearageMsg(json);
			break;
		case 'consume':
			formatConsume(json);
			break;
		case 'opticalInfo':
			formatOpticalInfo(json);
			break;
		case 'iptvInfo':
			formatIptvInfo(json);
			break;
		case 'is4G_tj':
			formatis4G_tj(json);
			break;
	}
}


/**
 * 光纤信息展示
 * @param json
 * @return
 */
function formatOpticalInfo(json) {
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				var r01 = json.r.r01;
				var r02 = json.r.r02;
				var r03 = json.r.r03;
				var r04 = json.r.r04;

				$('#opticalInfo').text('光纤（' + r02 + '）');
				$('#optical').show();
				$('#sbmInfo').text(r03);
				$('#sbm').show();
			}
		}
	}
}


function formatis4G_tj(json) {
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				if (json.r.r01) { //4G用户
					$('.Recommend_tj').hide();
					$('#res_is4g').show();
				} else { //非4G用户
					$('.Recommend_tj').hide();
					$('#res_not4g').show();
				}
			}
		}
	}
}
//初始化查询
function initMiddleQuery(_nbr, _nbrType) {
	if (_nbrType == 'CUST') {
		$('#usableBalance').text('--'); //余额查询
		$('#arrearage').html('--'); //欠费查询
		$('#realTimeFee').text('--'); //实时话费
	} else {
		if (_nbrType && _nbrType == 'CDMA') {
			renderLocalStorage('realTimeFee', _latn, _nbr, {}); //实时话费
		}
		renderLocalStorage('balance', _latn, _nbr, {
			'd.d01': _loginNumBusiType == 'CUST' ? '1' : '0'
		}); //余额查询
		if (_loginNumBusiType == 'CUST')
			$('#arrearage').html('--'); //欠费查询
		else
			renderLocalStorage('arrearage', _latn, _nbr, {});
	}
	renderLocalStorage('integral', _latn, _nbr, {}); /*我的积分*/
	renderLocalStorage('unpayCrmOrder', _latn, _nbr, {
		'd.d01': _latn,
		'd.d02': _custCode
	}); /*待支付订单提醒*/
	renderLocalStorage('arrearageMsg', _latn, _nbr, {}); /*欠费提醒*/
	renderLocalStorage('is4G_tj', _latn, _nbr, {}); /*4G标识*/
}

function initHomeQuery(_cnbr, _cnbrType) {
	if (isHomeUrl()) {
		/*我的套餐*/
		if (_cnbrType.indexOf('CDMA') > -1 || _cnbrType.indexOf('DH') > -1) {
			renderLocalStorage('MyComboUsed', _latn, _cnbr, {});
		} else {
			$('.modelTab dt').eq(1).addClass('curr').siblings('dt').removeClass();
			$('.modelTab dt').parent('dl').nextAll('.modelCon').eq(1).show().siblings('.modelCon').hide();
			$('.modelTab dt').nextAll('dd').eq(1).show().siblings('dd').hide();
			$('.load1,.load2,.load3,.load4').hide();
			makeIt('myCanvas1', 0, 0, 'M');
			makeIt('myCanvas2', 0, 0, '分钟');
			makeIt('myCanvas3', 0, 0, '条');
			//makeIt('myCanvas4',0,0,'分钟');
		}
		if (_payType == '001') {
			renderLocalStorage('consume', _latn, _loginNumber, {});
		} else {
			//$('#zstDiv').hide().siblings().show();
			$('#xfData').hide();
			$('#zstDiv').show();
		}
		/*我的产品*/
		renderLocalStorage('myProducts', _latn, _cnbr, {});
	}
}

//初始化客户信息
function formatCustInfo(json) {
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				var r13 = json.r.r13;

				//若取上次登录时间为空，则取当期时间
				if (r13 != null && r13 != '' && r13 != 'undefined') {
					$('#last_login').text(r13); //上次登录时间
				}

				//会员等级
				var vip_lev = json.r.r12;
				var vip_lev_show = '--'; //会员等级，默认显示“--”
				if (vip_lev) {
					if (vip_lev == '0') {
						vip_lev_show = '钻石';
					} else if (vip_lev == '1') {
						vip_lev_show = '金牌';
					} else if (vip_lev == '2') {
						vip_lev_show = '银牌';
					}
					$('#vip_lev').text(vip_lev_show); //会员等级
					$('#vip_lev_show').show(); //会员等级
				}

				$('#custName').text(json.r.r01);
				$('#custName').attr('title', json.r.r01);
				$('#loginNumber_n').text(json.r.r02);
				$('#loginNumber_n').attr('title', json.r.r02);
				$('#sel_num').text(json.r.r02).attr('title', json.r.r02);
				_loginNumber = json.r.r02;
				_loginNumBusiType = json.r.r04;
				_payType = json.r.r07;
				_custCode = json.r.r08;
				_custType = json.r.r10;
				var _currentNum = json.r.r03; //当前号码
				_Now_loginNumber = json.r.r03;
				_Now_loginNumber_type = json.r.r05;
				//var _currentNumType=json.r.r05;//当前号码类型
				var _currentNumType = 'CDMA'; //当前号码类型
				if (json.r.r10 == '01' && json.r.r11 == '01' && isHomeUrl()) {
					location.href = 'http://gd.189.cn/biz/service/';
				}

				var _qryNumber = '';
				var _qryNumberType = ''
				if (_loginNumBusiType == 'CUST' && json.r.r10 == '01') {
					if (_currentNum) {
						_qryNumber = _currentNum;
						_qryNumberType = _currentNumType;
					} else {
						_qryNumber = _loginNumber;
						_qryNumberType = _loginNumBusiType;
					}
				} else {
					_qryNumber = _loginNumber;
					_qryNumberType = _loginNumBusiType;
				}
				initMiddleQuery(_qryNumber, _qryNumberType);
				initHomeQuery(_currentNum, _currentNumType);
			}
		} else if (json.b && json.b.c == '01') {
			var message = json.b.m;
			if (message.indexOf("用户必须使用客户密码") > -1) {
				alert('尊敬的客户，为了不影响您使用网厅的功能，请使用客户密码登录系统，给您造成不便敬请谅解！');
				return false;
			}
		}
	}
}

//账户可用余额
function formatBalance(json) {
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				$('#usableBalance').text(json.r.r01);
				$('#dqye').text(json.r.r01);
			}
		}
	}
}

//可用积分
function formatIntegral(json) {
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				if (json.r.r01 == null || json.r.r01 == '') {
					$('#usableIntegral').text('--');
					$('#xd_jf').text('--'); //积分
				} else {
					$('#usableIntegral').text(json.r.r01);
					$('#xd_jf').text(json.r.r01); //积分
				}
			}
		}
		if (json.b) {
			if (window.location.pathname == "/service/home/") {
				if (json.b.v == 999 || json.b.v == "999") {
					if (_loginNumBusiType != 'CUST') {
						openBox("showBox_Real_name", "close");
					}
				}
			}
		}
	}
}

//欠费查询
function formatArrearage(json) {
	//  if (json.isLogin == "Y") { //登录情况下
	//		if (!json.nqBill || json.nqBill == null) { //系统异常
	//		} else {
	//			$('#arrearage').html(json.nqBill.totleFee);
	//		}
	//	}

	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				$('#arrearage').html(json.r.r02);
			}
		}
	}
}

//查询宽带速度
function formatAdslInfo(json) {
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				var r01 = json.r.r01;
				var r02 = json.r.r02;
				var _rate = (r02 == '' ? r01 : r02);
				/*
				var angle;
				switch(_rate){
					case '2M' : angle = -90; break;
					case '4M' : angle = -68; break;
					case '6M' : angle = -43; break;
					case '8M' : angle = -15; break;
					case '12M' : angle = 15; break;
					case '20M' : angle = 43; break;
					case '50M' : angle = 68; break;
					case '100M' : angle = 90; break;
					default : angle = 0;
				}
				$('#pointer').rotate(angle);
				*/
				$('#adsl_width').attr('src', '/service/home/images/adsl/' + _rate + '.png');

				$('#speed').text(_rate);
				$('#adslNum').text(_adsl);
				$('#adslNum').attr('title', _adsl);
				$('#myAsdlDiv').show();
			}
		}
	}
}
//查询是否安装iptv
var iptvFlag = '0';

function formatIptvInfo(json) {
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				if (!$.isEmptyObject(json.r.r04)) {
					$.each(json.r.r04, function(i, item) {
						var prodId = item.r0409;
						if (prodId == 1004 || prodId == 3816) {
							iptvFlag = '1';
						}
					});
				}
			}
		}
	}
}

//查询宽带到期时间
function formatAdslExpire(result) {
	if (result == null || result == '') {
		return false;
	}
	if (result.r == null) {
		return false;
	}
	var code = result.r.code;
	var msg = result.r.msg;
	if (code == '' || code == null) {
		return false;
	}
	if (code != '000' && code != '004') {
		return false;
	}
	if (code == '000') {
		var _str = result.r.r03;
		if (_str) { //包年缴费
			var _date1 = new Date(_str.substring(0, 4) + '/' + _str.substring(4, 6) + '/' + _str.substring(6, 8));
			var _date2 = new Date();
			var _d = _date1.getTime() - _date2.getTime();
			var _m = MouthDiff(_date2, _date1);
			var days = parseInt(Math.floor(_d / (24 * 3600 * 1000)));
			var dateStr;
			var expireDateHtml;

			if (_m > 13) {
				var expireDateHtml = '<li>您还可以修改<br/><a href="http://gd.189.cn/transaction/taocanapply1.jsp?operCode=PasswordModifyADSLI&in_cmpid=khzy-kd-kdmmxg" target="_blank"><em>宽带密码</em></a></li>';
			} else if (_m < 13 && _m > 3) { //三个月以上
				dateStr = _m + '个月';
				expireDateHtml = '<li>离宽带到期还剩<em id="expireDate">' + dateStr + '</em></li>';
			} else if (3 > _m && _m > 0) { //不足三个月
				dateStr = days + '天';
				expireDateHtml = '<li>离宽带到期还剩<em id="expireDate">' + dateStr + '</em></li>';
			} else {
				var dqsj = _str.substring(0, 4) + '年' + _str.substring(4, 6) + '月' + _str.substring(6, 8) + '日';
				//expireDateHtml = '<li>尊敬的客户，您的宽带<em id="expireDate">于'+dqsj+'已到期</em></li>';
				expireDateHtml = '<li>尊敬的客户，您的宽带<em id="expireDate">已到期</em></li>';
			}
			$('#adslData').prepend(expireDateHtml);
			//if(iptvFlag == '0') {//未办理
			var lihtml = '<li>您还可以查看<br/><a href="http://gd.189.cn/kd/" target="_blank"><em>宽带优惠</em></a></li>';
			$('#adslData').append(lihtml);
			//} 
		} else { //包月
			var expireDateHtml = '<li>您还可以修改<br/><a href="http://gd.189.cn/transaction/taocanapply1.jsp?operCode=PasswordModifyADSLI&in_cmpid=khzy-kd-kdmmxg" target="_blank"><em>宽带密码</em></a></li>';
			$('#adslData').prepend(expireDateHtml);

			//if(iptvFlag == '0') {//未办理
			var lihtml = '<li>您还可以查看<br/><a href="http://gd.189.cn/kd/" target="_blank"><em>宽带优惠</em></a></li>';
			$('#adslData').append(lihtml);
			//} 
		}
	}
}

//计算两个日期的月份差
function MouthDiff(beginDate, endDate) {
	var month = (parseInt(endDate.getFullYear()) * 12 + parseInt(endDate.getMonth())) - (parseInt(beginDate.getFullYear()) * 12 + parseInt(beginDate.getMonth()));
	return month;
}


//欠费信息提醒
function formatArrearageMsg(json) {
	//	if(json.resultCode == '0'){
	//		dealMsg(json,'arrearage');
	//	} 

	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				dealMsg(json, 'arrearage');
			}
		}
	}
}

//实时话费界面渲染
function formatRealTimeFee(json) {
	//if(json.resultCode == '0'){
	//	//$('#realTimeFee').text(json.row2[3]);
	//	$('#byhf').text(json.r.r04.r0404); 
	//}

	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				$('#byhf').text(json.r.r04.r0404);
			}
		}
	}
}

//待支付业务订单
function formatUnpayCrmOrder(json) {
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				dealMsg(json, 'unpayCrmOrder');
			}
		}
	}
}


//消息提醒处理
function dealMsg(json, type) {
	var _openFlag = false; //消息提醒开关
	if (msgNum < 3 && msgNum >= 0) {
		if (type && type == 'arrearage') {
			if (parseInt(json.r.r02) > 0) {
				$('#msg_news').append('<p>尊敬的客户，您的<a href="/service/home/query/xf_qf.html?in_cmpid=khzy-zcdh-fycx-wdxf-qfcx" target="_blank">账号已欠费</a>，请及时缴费，保证您的正常使用。</p>');
				msgNum++;
			}
		} else if (type && type == 'taocan') {
			if (json.remainll <= 0 && json.hasll) {
				$('#msg_news').append('<p>尊敬的客户，您当月套餐内流量已用完，马上升级<em class="hl"><a href="http://gd.189.cn/gd/vas/llb.html?in_cmpid=khzy-xxzx-ll" target="_blank">流量充值</a></em>。</p>');
				msgNum++;
			}
			if (msgNum < 3 && json.hasyy && json.remainyy <= 0) {
				$('#msg_news').append('<p>尊敬的客户，您当月套餐内通话时长已用完。</p>');
				msgNum++;
			}
			if (msgNum < 3 && json.hasdx && json.remaindx <= 0) {
				$('#msg_news').append('<p>尊敬的客户，您当月套餐内短信已用完。</p>');
				msgNum++;
			}
		} else if (type && type == 'unpayCrmOrder') {
			if (parseInt(json.r.r02) > 0) {
				$('#msg_news').append('<p>尊敬的客户，您有' + json.r.r02 + '张<a href="/oncePayCrmOrderQuery.do?in_cmpid=khzy-yhxx-xxts-dzfyw" target="_blank">待支付业务订单</a>。</p>');
				$('#numifo').text(json.r.r02);
				msgNum++;
			} else {
				$('#numifo').text('0');
				$('.linkOrder').hide();
				$('.textOrder').show();
			}
		}

		if (msgNum == 0) {
			$('#msg_news').empty();
			var str = '<p>亲，您的消费、套餐使用等紧急通知，及最新优惠消息，都能通过邮箱发送给您啦！立即登记邮箱~ <a href="http://gd.189.cn/transaction/operApply1.jsp?operCode=ChangeCustInfoNew&cmpid=gd04-push-ad-wtzy-djmail" target="_blank">点击登记</a></p>';
			$('#msg_news').append(str);
			msgNum++;
		} else if (msgNum > 0) {
			_openFlag = true;
			$('#no_msg').hide();
		}
		$('.msgMore').hide();
	} else if (msgNum > 3) {
		$('.msgMore').show();
		msgNum++;
	}

	if (_openFlag) {
		$('.userMsg .fold').click();
	}
	$('#msg_num').text(msgNum);
	$('.msgMore').html('更多消息(' + msgNum + ')');
}


//我拥有的产品
function formatMyProducts(json) {
	_adsl = '';
	_adslType = '';
	$('.adslBox').removeClass('none');
	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && json.r.code == '000') {
				if (_Now_loginNumber_type == "AD") {
					$.each(json.r.r01, function(i, e) {
						if (_adsl == '' && (e.r0101 == 'AD' || e.r0101 == 'ADp')) {
							if (_Now_loginNumber == e.r0102) {
								_adsl = e.r0102;
								_adslType = e.r0101;
								var p = {
									'd.d01': _adslType,
									'd.d02': _adsl
								};
								renderLocalStorage('adslInfo', _latn, _adsl, p);
								renderLocalStorage('adslExpire', _latn, _adsl, p);
								renderLocalStorage('opticalInfo', _latn, _adsl, p);
								return;
							}
						}

					});
				} else {
					$.each(json.r.r01, function(i, e) {
						if (_adsl == '' && (e.r0101 == 'AD' || e.r0101 == 'ADp')) {
							_adsl = e.r0102;
							_adslType = e.r0101;
							/*取第一个宽带账号,查询宽带速度*/
							var p = {
								'd.d01': _adslType,
								'd.d02': _adsl
							};
							renderLocalStorage('adslInfo', _latn, _adsl, p);
							renderLocalStorage('adslExpire', _latn, _adsl, p);
							renderLocalStorage('opticalInfo', _latn, _adsl, p);
							return;
						}
					});
				}

				if (_adsl == '') {
					$('.adslBox').addClass('none');
				}
			}
		}
	}
}

//超量预警与我的套餐信息
function formatMyComboUsed(json) {
	var ll = 0,
		yy = 0,
		wl = 0,
		dx = 0,
		llunlimit=0;
	var remainll = 0;
	remainyy = 0, remainwl = 0, remaindx = 0;
	var usedll = 0,
		usedyy = 0,
		usedwl = 0,
		useddx = 0;
	var Flow_junction = 0;
	var Flow_junction_type = "0";
	var Flow_junction_type_num = "0";
	var Flow_junction_type_not_g = "0";
	var hasll = false,
		hasdx = false,
		hasyy = false,
		haswl = false;
	var msgdata = {}

	var d = new Date();
	var month = d.getMonth();
	var date = d.getDate();
	var _cur = d.getFullYear() + '-' + ((month + 1) < 10 ? '0' + (month + 1) : (month + 1)) + '-' + (date < 10 ? ('0' + date) : date);
	var maxDay = getMaxDay(d.getFullYear(), month + 1);
	var lastDays = parseInt(maxDay) - parseInt(date);
	var isLimit = false; //是否不限量套餐
	$('#current').text(_cur);
	$('#lastDays').text(lastDays);

	if (json) {
		if (json.b && json.b.c == '00') {
			if (json.r && (json.r.code == '000' || json.r.code == '003' || json.r.code == '004')) {
				if (_payType == '001') { //后付费
					$.each(json.r.r01, function(i, e) {
						if (e.r0103 == '1') { //短信
							dx += parseInt(e.r0107);
							remaindx += parseInt(e.r0109);
							hasdx = true;
							msgdata.remaindx = remaindx;
							msgdata.hasdx = hasdx;
						} else if (e.r0103 == '0') { //语音
							yy += parseInt(e.r0107);
							remainyy += parseInt(e.r0109);
							hasyy = true;
							msgdata.remainyy = remainyy;
							msgdata.hasyy = hasyy;
						} else if (e.r0103 == '2') { //手机上网
							if (parseFloat(e.r0115) == "1") { //可正常结转
								Flow_junction_type_num = parseFloat(e.r0115);
							}
							if (parseFloat(e.r0115) == "2") { //可结转未修正
								Flow_junction_type_not_g = parseFloat(e.r0115);
							}
							Flow_junction_type = parseFloat(e.r0115);
							var Flow_junction_val = parseFloat(e.r0112);
							if (!Flow_junction_val) {
								Flow_junction_val = "0";
							}
							Flow_junction += parseFloat(Flow_junction_val);
							ll += parseFloat(e.r0107); //流量初值
							remainll += parseFloat(e.r0109); //剩余流量
							remainll = parseFloat(parseFloat(remainll).toFixed(2));
							hasll = true;
							msgdata.remainll = remainll;
							msgdata.hasll = hasll;
						} else if (e.r0103 == '6') { //WIFI
							wl += parseInt(e.r0107);
							remainwl += parseInt(e.r0109);
							haswl = true;
							msgdata.remainwl = remainwl;
							msgdata.haswl = haswl;
						}
						if (e.r0101 == '999999902' ) {
							$(".limitArea").text('省内');
							llunlimit= parseFloat(e.r0108); 
							isLimit = true;
							//usedll=e.r0108;
						} else if (e.r0101 == '999999904') {
							$(".limitArea").text('国内');
							llunlimit= parseFloat(e.r0108); 
							isLimit = true;
							//usedll=e.r0108;
						}
					});
				} else {
					var yyArr = ["9037", "9040", "9041", "9044", "9042", "9046", "9047", "9055", "9056", "9057", "9083", "9084", "9090", "9091", "9050", "9051", "9058", "9059", "9020", "9021", "9022", "9023", "9024", "9025", "9088"];
					var dxArr = ["9203", "9200", "9087"];
					var llArr = ["9502", "9504", "9220", "9500", "9600", "9230", "4221", "9002", "9001", "9003", "9508", "520", "8500", "558", "519"]; //已除去彩色流量
					var wlanArr = ["9214", "9225", "9234", "9235", "9226", "9227", "9228", "9229", "9210", "9315"];
					var Now_times_llArr_home = getNowFormatDate_home();
					var r0309_558 = false;  //国内不限量用户判断标准
					var r0309_519 = false;  //省内不限量用户判断标准
					var r0306_558 = "";      //国内不限量流量剩余
					var r0306_519 = "";      //省外不限量流量剩余
					$.each(json.r.r03, function(i, e) {
						if (Now_times_llArr_home <= e.r0308.substring(0, 8) || !e.r0308) {
							var r0309 = e.r0309;
							
							
							if ($.inArray(r0309, llArr) != -1) {
								if (parseFloat(e.r0313) == "1") {
									Flow_junction_type = parseFloat(e.r0313);

									var Flow_junction_val = e.r0312;
									if (!Flow_junction_val) {
										Flow_junction_val = "0";
									}
									Flow_junction += parseFloat(parseFloat(Flow_junction_val / 1024.00).toFixed(2));
								}
								if(r0309 == "558"){
								r0309_558 = true;
								r0306_558 = parseFloat(e.r0306 / 1024.00);
							    }else if(r0309 == "519"){
								r0309_519 = true;
								r0306_519 = parseFloat(e.r0306 / 1024.00);
							    }
								ll += parseFloat(parseFloat(e.r0305 / 1024.00).toFixed(2));
								//remainll += parseFloat(parseFloat(e.r0306/1024.00).toFixed(2));
								//ll += parseFloat(e.r0305/1024.00);
								remainll += parseFloat(e.r0306 / 1024.00);
								remainll = parseFloat(parseFloat(remainll).toFixed(2));
								hasll = true;
								msgdata.remainll = remainll;
								msgdata.hasll = hasll;
							} else if ($.inArray(r0309, yyArr) != -1) {
								yy += parseInt(e.r0305);
								remainyy += parseInt(e.r0306);
								hasyy = true;
								msgdata.remainyy = remainyy;
								msgdata.hasyy = hasyy;
							} else if ($.inArray(r0309, dxArr) != -1) {
								dx += parseInt(e.r0305);
								remaindx += parseInt(e.r0306);
								hasdx = true;
								msgdata.remaindx = remaindx;
								msgdata.hasdx = hasdx;
							}

							if (r0309_558) {
								if(r0309_519){
								$(".limitArea").text('省内');
								llunlimit= parseFloat(parseFloat(r0306_558 - r0306_519).toFixed(2));
								isLimit = true;
                                }else{
								$(".limitArea").text('国内');
								llunlimit= parseFloat(r0306_558.toFixed(2));
								isLimit = true;	
									}
							}
						}

					});
				}
				//
				dealMsg(msgdata, 'taocan');

				usedll = parseFloat((ll - remainll).toFixed(2)); //已用流量


				usedyy = parseFloat((yy - remainyy).toFixed(2));
				useddx = parseFloat((dx - remaindx).toFixed(2));
				usedwl = parseFloat((wl - remainwl).toFixed(2));

				if (hasll) {
					if (isLimit) {
						$("#bxlUsedFlow1").text(llunlimit);
						$("#bxlUsedFlow2").text(llunlimit+'M');
							$(".flowData").hide();
							$(".flowData").eq(1).show();
					} else {
						makeIt('myCanvas1', usedll, remainll, 'M'); //流量使用情况
					}
				//	makeIt('myCanvas1', usedll, remainll, 'M');
					$('.load1').hide();
				} else {
					$('.load1').hide();
					makeIt('myCanvas1', 0, 0, 'M');
				}
				if (Flow_junction_type == "0") {
					if (Flow_junction_type_num == "1") {
						$('#toTalLL').text(parseFloat(ll.toFixed(2)) + 'M');
						$("#toDaTa").text(Flow_junction + 'M');
						$("#toDaTa_show").show();
						$(".flowTool_p").show();
					} else if (Flow_junction_type_not_g == "2") {
						$(".flowData").hide();
						$(".flowDlayTip").show();
					} else {
						$('#toTalLL').text(parseFloat(ll.toFixed(2)) + 'M');
						$("#toDaTa_show").hide();
					}
				} else if (Flow_junction_type == "1") {
					$('#toTalLL').text(parseFloat(ll.toFixed(2)) + 'M');
					$("#toDaTa").text(parseFloat(Flow_junction.toFixed(2)) + 'M');
					$("#toDaTa_show").show();
					$(".flowTool_p").show();
				} else if (Flow_junction_type == "2") {
					$(".flowData").hide();
					$(".flowDlayTip").show();
				}

				//话费短信等信息
				$('#syth').text(remainyy);
				$('#sydx').text(remaindx);
			} else {
				$('.load1,.load2,.load3,.load4').hide();
				makeIt('myCanvas1', 0, 0, 'M');

				$('#combo').hide();
				$('#tab_wdtc').hide();
				$('#tab_wdxf').addClass("curr");
				$('#tab_wdxf').click();

				//				makeIt('myCanvas2',0,0,'分钟');
				//				makeIt('myCanvas3',0,0,'条');
				//				//makeIt('myCanvas4',0,0,'分钟');
			}
		}
	} else {
		$('.load1,.load2,.load3,.load4').hide();
		makeIt('myCanvas1', 0, 0, 'M');

		$('#combo').hide();
		$('#tab_wdtc').hide();
		$('#tab_wdxf').addClass("curr");
		$('#tab_wdxf').click();


		//		$('.load1,.load2,.load3,.load4').hide();
		//		makeIt('myCanvas1',0,0,'M');
		//		makeIt('myCanvas2',0,0,'分钟');
		//		makeIt('myCanvas3',0,0,'条');
		//makeIt('myCanvas4',0,0,'分钟');


	}
}

//消费走势图
function formatConsume(json) {
	if (json.resultCode == '0') {
		var consume = json.consume;
		var _data = [consume.last5, consume.last4, consume.last3, consume.last2, consume.last1, consume.current];
		var _categories = [convertMonth(consume.last5Month),
			convertMonth(consume.last4Month),
			convertMonth(consume.last3Month),
			convertMonth(consume.last2Month),
			convertMonth(consume.last1Month),
			convertMonth(consume.currentMonth)
		];

		//求平均数
		var Calc = {
			array: _data,
			avg: function() {
				var sum = 0;
				for (var i = 0; i < Calc.array.length; i++) {
					sum += Calc.array[i];
				}
				var avv = sum / Calc.array.length;
				return avv;
			}
		};

		$('#costLastMonth').text(consume.current);
		$('#averageCost').text((Calc.avg()).toFixed(2));
		$('#xfData').show();

		//消费走势图
		$('#zstDiv').empty();
		$('#zstDiv').highcharts({
			chart: {
				type: 'line',
				height: '307',
				width: '550'
			},
			//走线颜色
			colors: ['#848484'],
			title: {
				text: ''
			},
			//X坐标
			xAxis: {
				categories: _categories,
				lineColor: '#828282',
				lineWidth: '1'
			},
			//Y坐标
			yAxis: {
				title: {
					text: ''
				},
				labels: {
					formatter: function() {
						return this.value + '元';
					}
				},
				gridLineColor: '#FFFFFF',
				lineColor: '#aaa',
				lineWidth: '1'
			},
			//配置数据点提示框
			tooltip: {
				formatter: function() {
					return '消费总额：' + this.y + '元';
				}
			},
			//表底标题
			legend: {
				enabled: false
			},
			//点
			series: [{
				data: _data,
				marker: {
					radius: 7,
					lineWidth: 4,
					lineColor: "#FF8202",
					fillColor: "#fff"
				}
			}],
			//线条
			plotOptions: {
				line: {
					dataLabels: {
						enabled: true,
						color: "#FF8202",
						padding: 10
					},
					enableMouseTracking: true
				}
			},
			//水印网址
			credits: {
				enabled: false
			},
			exporting: { //网址
				enabled: false
			}
		});
	}
}

function convertMonth(month) {
	month = month.substring(4, month.length);
	if (month[0] == '0') {
		month = month.substring(1, month.length);
	}
	return month + '月';
}


//圆环进度条通用
var paper = null;
//console.log(1);
function makeIt(canId, yy, sy, unit) { //console.log(canId+','+yy+','+sy+','+unit);
	$('#' + canId).show();
	var browser = navigator.appName;
	if (browser == "Microsoft Internet Explorer") {
		var b_version = navigator.appVersion;
		var version = b_version.split(";");
		var trim_Version = version[1].replace(/[ ]/g, "");
	}
	if (browser == "Microsoft Internet Explorer" && (trim_Version == "MSIE6.0" || trim_Version == "MSIE7.0" || trim_Version == "MSIE8.0")) {
		//初始化Raphael画布 
		this.paper = Raphael(canId, 188, 188);
		//把底图先画上去 
		this.paper.image("/service/home/images/progress_bg2.png", 2, 2, 186, 186);
	} else {
		//初始化Raphael画布 
		this.paper = Raphael(canId, 186, 186);
		//把底图先画上去 
		this.paper.image("/service/home/images/progress_bg2.png", 0, 0, 186, 186);
	}
	//进度比例，0到1，在本例中我们画65% 
	//需要注意，下面的算法不支持画100%，要按99.99%来画
	var percent;
	if (sy == 0 && yy == 0) { //alert(1)
		percent = 0.0001;
	} else if (sy != 0 && yy == 0) {
		percent = 0.0001;
	} else {
		percent = (yy / (yy + sy)).toFixed(2);
	}
	//console.log(percent);
	var drawPercent = percent >= 1 ? 0.9999 : percent;
	//console.log(drawPercent); 	
	//开始计算各点的位置，见后图 
	//r1是内圆半径，r2是外圆半径 
	var r1 = 86,
		r2 = 93,
		PI = Math.PI,
		p1 = {
			x: 93,
			y: 0
		};
	/*ie678判断
	var browser=navigator.appName ;
	if(browser=="Microsoft Internet Explorer"){
		var b_version=navigator.appVersion 
		var version=b_version.split(";"); 
		var trim_Version=version[1].replace(/[ ]/g,""); 
		if(trim_Version=="MSIE6.0" || trim_Version=="MSIE7.0" || trim_Version=="MSIE8.0"){ 
			p1 = { 
				x:63,  
				y:-2
			}
		}
	};
	*/
	var p4 = {
			x: p1.x,
			y: r2 - r1
		},
		p2 = {
			x: r2 + r2 * Math.sin(2 * PI / 360 * (360 * drawPercent)),
			y: r2 - r2 * Math.cos(2 * PI / 360 * (360 * drawPercent))
		},
		p3 = {
			x: r2 + r1 * Math.sin(2 * PI / 360 * (360 * drawPercent)),
			y: r2 - r1 * Math.cos(2 * PI / 360 * (360 * drawPercent))
		},
		path = [
			'M', p1.x, ' ', p1.y,
			'A', r2, ' ', r2, ' 0 ', percent < 0.5 ? 0 : 1, ' 1 ', p2.x, ' ', p2.y,
			'L', p3.x, ' ', p3.y,
			'A', r1, ' ', r1, ' 0 ', percent < 0.5 ? 0 : 1, ' 0 ', p4.x, ' ', p4.y,
			'Z'
		].join('');
	//用path方法画图形，由两段圆弧和两条直线组成，画弧线的算法见后 
	var c; //进度环颜色
	if (percent >= 1) {
		c = '#E9514E';
		//c='#96cd18';
	} else if (percent >= 0.9) {
		c = '#FF651E';
		//c='#96cd18';
	} else {
		c = '#ff8202';
	}
	this.paper.path(path)
		//填充渐变色，从#3f0b3f到#ff66ff 
		.attr({
			"stroke-width": 0,
			"stroke": "#2a70a6",
			"fill": "90-" + c
		});
	//显示进度文字 
	//$(t).text(Math.round(percent * 100) + "%"); 
	var text;
	if (yy != 0) {
		text = '已用流量</br><em>' + yy + unit + '</em></br>' + '剩余流量</br><em>' + sy + unit + '</em>';
	} else {
		text = '</br></br>未使用';
	}
	$('#' + canId).next('p').remove();
	$('#' + canId).after('<p>' + text + '</p>');
}

function isHomeUrl() {
	if (location.pathname == '/service/home/' ||
		location.pathname == '/service/home/index.html' ||
		location.pathname == '/service/home/index_2015.html' ||
		location.pathname == '/for_Group/myhome.html'
	) {
		return true;
	}
	return false;
}

/**
 * 统一登录失效跳转
 * @return
 */
function reLogin() {
	window.location.href = '/common/login.jsp?loginOldUri=' + location.pathname + location.search;
	return false;
}

/**
 * 获取最大天数
 * @param year
 * @param month
 * @return
 */
function getMaxDay(year, month) {
	return new Date(year, month, 0).getDate();
}
//毫秒格式化日期
function getNowFormatDate_home() {
	var date = new Date();
	var seperator1 = "-";
	var seperator2 = ":";
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = String(date.getFullYear()) + String(month) + String(strDate)
	return currentdate;
}