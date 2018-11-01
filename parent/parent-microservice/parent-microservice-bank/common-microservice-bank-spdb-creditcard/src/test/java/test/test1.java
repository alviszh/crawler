package test;

import java.util.Calendar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "340223199602250010";
		System.out.println(a.length());
//		String html = "{'res':true,'pntadj':'+0000000000','pntear':'+0000007985','ptnbon':'+0000002632','inteCustlv':'03','pntcum':'+0000118337','pntlas':'+0000107720','pntclm':'+0000000000','thisBillFlag':false}";
//		JsonParser parser = new JsonParser();
//		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
//		String type = object.get("pntcum").toString().replaceAll("\"", "").substring(1); 
//		if(type.equals("0000000000")){
//			type = "0";
//		}else{
//			int a = 0;
//			for(int i =1;i<=10;i++){
//				String s = String.valueOf(type.charAt(i));
//				if(!s.equals("0")){
//					a=i;
//					break;
//				}
//			}
//			type = type.substring(a);
//		}
//		String type1 = object.get("pntear").toString().replaceAll("\"", "").substring(1);  
//		if(type1.equals("0000000000")){
//			type1 = "0";
//		}else{
//			int a = 0;
//			for(int i =1;i<=10;i++){
//				String s = String.valueOf(type1.charAt(i));
//				if(!s.equals("0")){
//					a=i;
//					break;
//				}
//			}
//			type1 = type1.substring(a);
//		}
//		String type2 = object.get("pntadj").toString().replaceAll("\"", "").substring(1); 
//		if(type2.equals("0000000000")){
//			type2 = "0";
//		}else{
//			int a = 0;
//			for(int i =1;i<=10;i++){
//				String s = String.valueOf(type2.charAt(i));
//				if(!s.equals("0")){
//					a=i;
//					break;
//				}
//			}
//			type2 = type2.substring(a);
//		}
//		String type3 = object.get("pntclm").toString().replaceAll("\"", "").substring(1);
//		if(type3.equals("0000000000")){
//			type3 = "0";
//		}else{
//			int a = 0;
//			for(int i =1;i<=10;i++){
//				String s = String.valueOf(type3.charAt(i));
//				if(!s.equals("0")){
//					a=i;
//					break;
//				}
//			}
//			type3 = type3.substring(a);
//		}
		
		
		
//		String type4 = object.get("pntadj").toString().replaceAll("\"", "");
//		System.out.println(type);
//		System.out.println(type1);
//		System.out.println(type2);
//		System.out.println(type3);
//		String html ="{'payedU':'0.00','minBackExpenditure':'737.10','htClauseTitle':'账单分期付款业务条款与细则','cashLimit':'￥5,250.00','dueDate':'20180708','sign':'+','stmtAmtU':'0.00','loginFlag':0,'lastMoney':'2609.08','remark':'良好的分期习惯助您缓解还款压力，让您消费购物嗨翻天！','custlv':'03','htClauseText':'<p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:center\'><strong><span style=\'font-size:16px;font-family:宋体\'>账单分期付款业务条款与细则</span></strong></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp; 1</span><span style=\'font-size:16px;font-family: 宋体\'>、账单分期付款业务是上海浦东发展银行信用卡中心（下称“信用卡中心”） 为符合条件的浦发银行信用卡（除公务卡外）主卡持卡人提供的对其最近一期已出账单中的人民币消费本金欠款余额（实际可分期金额以浦发信用卡中心核定为准）分期偿还的服务。&nbsp;</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp; 2</span><span style=\'font-size:16px;font-family: 宋体\'>、持卡人可选择以下任一方式申请账单分期付款：</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（1） 致电信用卡中心24小时客户服务热线 400-820-8788、（021）3878-4988，申请账单分期付款业务。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（2）在线登录www.spdbccc.com.cn中&quot;在线账户服务&quot;，申请账单分期付款业务。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（3）发送短信ZDFQ（空格）卡号末四位（空格）期数至95528，申请账单分期付款业务。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（4）登录浦发手机银行客户端，申请账单分期付款业务。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（5）关注并绑定信用卡中心官方微信账号“浦发银行信用卡”，输入“我要分期”，办理账单分期付款业务。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（6）下载并登陆浦发银行信用卡客户端“浦大喜奔APP”，自助办理账单分期付款业务。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp; 3</span><span style=\'font-size:16px;font-family:宋体\'>、主卡持卡人本人可在最近一期已出账单的账单日次日至下期账单账单日间申请本业务。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp; 4</span><span style=\'font-size:16px;font-family:宋体\'>、主卡持卡人申请账单分期付款业务，可自由选择分期的期数为3期、6期、9期、12期、15期、18期或24期，每期为一个月。信用卡中心保留核准分期付款申请的权利。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp; 5</span><span style=\'font-size:16px;font-family:宋体\'>、每期还款本金按持卡人申请并经信用卡中心核准的分期本金总额和分期期数，按每月为一期，平均摊还，精确到分，尾数计入最后一期，每期还款本金逐月计入持卡人信用卡人民币账户。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp; 6</span><span style=\'font-size:16px;font-family:宋体\'>、账单分期手续费提供分期收取和一次性收取两种方式，且手续费一经收取，不予退还。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（1）分期收取：每期手续费=经信用卡中心核准的分期本金总额×对应期数的每期手续费率，按月收取，于成功申请分期后的第一个账单日开始分摊，在各期账单日逐期入账，余数计入最后一期分期。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（2）一次性收取：一次性手续费=经信用卡中心核准的分期本金总额×对应期数的一次性手续费率，一次性手续费总额在分期后第一个账单日一次性入账。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span><span style=\'font-size:16px;font-family:宋体\'>（3）账单分期手续费收费标准将在0-0.90%每单期的范围内根据持卡人的资信状况、用卡情况在下述标准手续费率的基础上进行调整，实际费率以浦发银行信用卡中心最终评定结果和出账账单为准，标准手续费费率表如下：</span></p><p style=\'margin-top:auto;margin-bottom: auto;text-align:left\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span></p><table><tbody><tr class=\'firstRow\'><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:宋体\'>分期期数</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:   16px;font-family:宋体\'>一次性收取手续费率</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:   16px;font-family:宋体\'>分期收取手续费率</span><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>/</span><span style=\'font-size:16px;font-family:宋体\'>期</span></p></td></tr><tr><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>3</span><span style=\'font-size:16px;font-family:   宋体\'>期</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>2.64%</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>0.90%</span></p></td></tr><tr><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>6</span><span style=\'font-size:16px;font-family:   宋体\'>期</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>4.44%</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>0.78%</span></p></td></tr><tr><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>9</span><span style=\'font-size:16px;font-family:   宋体\'>期</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>6.10%</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>0.73%</span></p></td></tr><tr><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>12</span><span style=\'font-size:16px;font-family:   宋体\'>期</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>8.16%</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>0.74%</span></p></td></tr><tr><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>15</span><span style=\'font-size:16px;font-family:   宋体\'>期</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>10.35%</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>0.75%</span></p></td></tr><tr><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>18</span><span style=\'font-size:16px;font-family:   宋体\'>期</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>12.60%</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>0.76%</span></p></td></tr><tr><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>24</span><span style=\'font-size:16px;font-family:   宋体\'>期</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>16.80%</span></p></td><td style=\'padding:5px 10px 5px 10px\'><p style=\'margin-top:auto;margin-bottom:   auto;text-align:left\'><span style=\'font-size:16px;font-family:&#39;Arial&#39;,&#39;sans-serif&#39;\'>0.76%</span></p></td></tr></tbody></table><p style=\'margin-top:auto;margin-bottom: auto;text-align:left\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp;</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>7</span><span style=\'font-size:16px;font-family:宋体\'>、通常情况下，持卡人申请账单分期付款的本金金额最低为人民币500元，最高不超过持卡人信用卡当期账单总金额的95%，最终以信用卡中心评定结果为准。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>8</span><span style=\'font-size:16px;font-family:宋体\'>、如持卡人仅对部分未偿付金额申请账单分期付款，则应在该期账单到期还款日前全额偿还剩余欠款，否则视为持卡人未对该期账单的欠款余额进行全额还款，则不可享受免息还款期待遇。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>9</span><span style=\'font-size:16px;font-family:宋体\'>、持卡人成功申请账单分期付款后，如该笔分期交易使用“倍富金”额度，则账单分期付款本金将全额计入已用“倍富金”额度，不占用可用信用额度；如该笔分期交易未使用“倍富金”额度，则账单分期付款本金将全额计入已用信用额度，可用信用额度根据持卡人每期还款本金等额恢复。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>10</span><span style=\'font-size:16px;font-family:宋体\'>、账单分期每期还款的分期本金及当期手续费将全额计入当期最低应还款总额。持卡人还款时, 本业务的还款优于一般消费先行偿付。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>11</span><span style=\'font-size:16px;font-family:宋体\'>、持卡人成功申请账单分期付款后，不能对已申请的分期期数、金额进行更改，且不可进行撤销。持卡人发生退货或撤销交易等情形时，仍须按已申请成功的账单分期付款业务偿付本金及手续费。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>12</span><span style=\'font-size:16px;font-family:宋体\'>、 下列交易金额不可申请账单分期付款：房产类相关交易、预借现金交 易、分期付款业务（包括商场分期业务、自由分期付业务、邮购分期业务、“万用金”现金分期业务以及信用卡中心不时推出的其他信用卡分期业务）及各项费用 （包括年费、入伙费、利息、滞纳金、超限费、手续费及其他各类依《上海浦东发展银行信用卡（个人卡）章程》及相关领用合约约定的费用）。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>13</span><span style=\'font-size:16px;font-family:宋体\'>、持卡人可通过信用卡中心24小时客户服务热线提前终止已成功申请的账单分期付款业务，但必须一次性支付剩余的本金及各期手续费；提前终止手续费及未偿还的本金金额将以固定百分比计入下期账单最低应缴金额，持卡人已支付的手续费不予退还。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>14</span><span style=\'font-size:16px;font-family:宋体\'>、 若持卡人卡片/账户被冻结、管制、注销，或持卡人卡片过期，或持卡人于分期付款期间内逾期还款，或发生信用卡中心认为有损持卡人信用的状况，所有剩余未偿 还的账单分期付款债务将于发生上述事项之时视为全部到期，持卡人应当一次性偿还全部剩余未偿还的账单分期付款本金及手续费。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>15</span><span style=\'font-size:16px;font-family:宋体\'>、账单分期每期还款的分期本金和手续费不参加信用卡中心积分计划。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>16</span><span style=\'font-size:16px;font-family:宋体\'>、账单分期的申请是否通过，以及是否使用倍富金额度，均以信用卡中心最终评定结果为准。</span></p><p style=\'margin-top:5px;margin-right:0;margin-bottom:5px;margin-left:0;text-align:left;text-indent:28px;line-height:24px\'><span style=\'font-size:16px;font-family:宋体\'>17</span><span style=\'font-size:16px;font-family:宋体\'>、信用卡中心保留依法修改本条款与细则、费率及各项费用、核准或拒绝分期付款的申请以及终止本业务的权利。</span></p><p style=\'margin-top:auto;margin-bottom: auto;text-align:left\'><span style=\'font-size:16px;font-family:宋体\'>&nbsp; 18</span><span style=\'font-size:16px;font-family:宋体\'>、本业务条款及细则及其未尽事宜，仍同时受《上海浦东发展银行信用卡（个人卡）章程》与相关领用合约等文件约束。</span></p><p><br/></p>','backMoney':'2609.08','cardNo':'4984 **** **** 2295','cycle':'18','minPay':'737.10','stmtFlagU':'','periodsValue':'3','localCurrencyDueAmount':'0.0','lastSignU':'','foreignCurrencyDueAmount':'0.0','billMonth':'201805','lastMoneyU':'0.00','backMoneyU':'0.0','lastBackDateU':'','marketOperation':null,'creditLimit':'￥10,500.00','creditLimitD':'￥10,500.00','cash':'4744.67','moClauseText':null,'payed':'2609.08','appChannelFlag':'0','nav':'index','cashU':'0.00','stmtFlag':'+','userName':'李世雄 先生','billYM':'201805','closeDate':'00000000','lastSign':'+','accountDay':'18','moClauseTitle':'  ','stmtAmt':'4744.67','minPayU':'0.00','lastBackDate':'20180607','minBackExpenditureU':'0.00','accDay':'18','signU':''}";
//		String html1 = html.substring(0,html.indexOf("htClauseText")-2);
//		String html2 = html.substring(html.indexOf("<br/></p>")+10);
//		html = html1+html2;
//		System.out.println(html);
//		JsonParser parser = new JsonParser();
//		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
//		String type = object.get("cashLimit").toString().replaceAll("\"", "");  
//		String type1 = object.get("creditLimit").toString().replaceAll("\"", "");  
//		String type2 = object.get("creditLimitD").toString().replaceAll("\"", ""); 
//		System.out.println(type);
//		System.out.println(type1);
//		System.out.println(type2);
		
//		String html = "{'accountInfoList':[{'closeCode':'SP','cardNbr':'4984511269872295','account':'0028119074','cardNum':'002','maFlag':'个人信用卡','cateGGory':'0001','cycleNbr':'18','eppQs':'00','revs':'','closyCode':'H1','busiName':'','pbFlag':'P','currCode2':'840','prodLe':'10','statchg':'0','odueFlag':'0','credLmt':'10500','mpLLmt':'10500','closeChdy':'00000000','closyChdy':'20180630','truenamYn':'1','calLimit':'0'}]}";
//		JsonParser parser = new JsonParser();
//		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对象
//		JsonArray accountCardList = object.get("accountInfoList").getAsJsonArray();
////		JsonObject object1 = object.get("fieldData").getAsJsonObject();
//		if (accountCardList.size()>0){
//			for (JsonElement acc : accountCardList) {
//				JsonObject account = acc.getAsJsonObject();
//				String type = account.get("cardNbr").toString().replaceAll("\"", "");  
//				String type1 = account.get("account").toString().replaceAll("\"", "");  
//				String type2 = account.get("maFlag").toString().replaceAll("\"", "");  
//				String type3 = account.get("cycleNbr").toString().replaceAll("\"", ""); 
//				int a = Integer.parseInt("03");
//				
//				int y,m,d,h,mi,s;    
//				Calendar cal=Calendar.getInstance();    
//				y=cal.get(Calendar.YEAR);    
//				m=cal.get(Calendar.MONTH);    
//				d=cal.get(Calendar.DATE);    
//				h=cal.get(Calendar.HOUR_OF_DAY);    
//				mi=cal.get(Calendar.MINUTE);    
//				s=cal.get(Calendar.SECOND);    
//				System.out.println(type);
//				System.out.println(type1);
//				System.out.println(type2);
//				System.out.println(type3);
//				System.out.println(a);
//				System.out.println(d);
//			}
//		}
		
	}

}
