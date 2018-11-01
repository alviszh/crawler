package app.crawler.telecom.htmlparse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanBalanceResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanBillResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanBusinessResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanCallThremResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanIntegraChangeResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanPayResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.hainan.TelecomHaiNanUserInfoResult;

import app.bean.TeleComHaiNanPayRoot;
import app.bean.TelecomHaiNanBalanceRoot;
import app.bean.TelecomHaiNanBillChongzis;
import app.bean.TelecomHaiNanBillRoot;
import app.bean.TelecomHaiNanUserIdBean;
import app.bean.WebParamTelecom;

public class TelecomParseHaiNan {
	private static Gson gs = new Gson();

	public static WebParamTelecom<TelecomHaiNanUserInfoResult> userinfo_parse(String html) {
		WebParamTelecom<TelecomHaiNanUserInfoResult> webParam = new WebParamTelecom<TelecomHaiNanUserInfoResult>();
		List<TelecomHaiNanUserInfoResult> list = new ArrayList<>();

		try {
			Document doc = Jsoup.parse(html);
			String username = doc.select("span:contains(" + "客户信息" + ")").text().split("：")[1];

			String usertype = doc.select("span:contains(" + "证件类型" + ")").text().split("：")[1];

			String useridcard = doc.select("span:contains(" + "证件号码" + ")").text().split("：")[1];

			String address = doc.select("span:contains(" + "客户地址" + ")").select("input").attr("value");
			TelecomHaiNanUserInfoResult telecomHaiNanUserInfoResult = new TelecomHaiNanUserInfoResult();
			telecomHaiNanUserInfoResult.setUsername(username);
			telecomHaiNanUserInfoResult.setUsertype(usertype);
			telecomHaiNanUserInfoResult.setUseridcard(useridcard);
			telecomHaiNanUserInfoResult.setAddress(address);
			list.add(telecomHaiNanUserInfoResult);
		} catch (Exception e) {
			webParam.setErrormessage(e.getMessage());
			webParam.setHtml(html);
		}

		webParam.setList(list);
		return webParam;

	}

	public static WebParamTelecom<TelecomHaiNanBillResult> bill_parse(String html) {

		// html =
		// "{'maps':{'zhouqi':'20170901-20170930','chongzis':[{'charge':'9.00元','details':[{'charge':'9.00元','showlevel':'2','chargeName':'套餐包月费'}],'showlevel':'1','chargeName':'套餐包月费'},{'charge':'0.10元','details':[{'charge':'0.10元','showlevel':'2','chargeName':'短信费'}],'showlevel':'1','chargeName':'短信费'},{},{}],'searchtime':'2017-09-26
		// 11:51','current':'9.1','resultcode':'0'}}";
		WebParamTelecom<TelecomHaiNanBillResult> webParam = new WebParamTelecom<TelecomHaiNanBillResult>();
		List<TelecomHaiNanBillResult> list = new ArrayList<>();
		TelecomHaiNanBillRoot jsonObject = gs.fromJson(html, TelecomHaiNanBillRoot.class);
		List<TelecomHaiNanBillChongzis> lists = jsonObject.getMaps().getChongzis();
		for (TelecomHaiNanBillChongzis result : lists) {
			if (result.getDetails() != null) {
				list.addAll(result.getDetails());
			}

		}
		webParam.setList(list);
		return webParam;

	}

	public static WebParamTelecom<TelecomHaiNanPayResult> payResult_parse(String html) {

		// html =
		// "{'maps':{'zhouqi':'20170901-20170930','chongzis':[{'userrage':'指定号码和业务','liushui':'980425860','feeway':'现金','ruzhangtime':'2017-09-20
		// 14:46:20','channel':'营业厅','ruzhangmon':'10.00元'}],'searchtime':'2017-09-26
		// 09:45','resultcode':'0'}}";
		WebParamTelecom<TelecomHaiNanPayResult> webParam = new WebParamTelecom<TelecomHaiNanPayResult>();

		TeleComHaiNanPayRoot jsonObject = gs.fromJson(html, TeleComHaiNanPayRoot.class);

		List<TelecomHaiNanPayResult> list = null;
		if (jsonObject.getMaps().getChongzis() != null) {
			list = jsonObject.getMaps().getChongzis();
		}
		webParam.setList(list);
		return webParam;
	}

	public static WebParamTelecom<TelecomHaiNanBalanceResult> balance_parse(String html) {

		// html =
		// "{'maps':{'zhouqi':'20170901-20170930','keyongs':[{'benqizhi':'0.00元','name':'专用余额','benqi':'10.00元','benqimo':'10.00元','shangqi':'0.00元'}],'searchtime':'2017-09-26
		// 10:20','resultcode':'0'}}";
		WebParamTelecom<TelecomHaiNanBalanceResult> webParam = new WebParamTelecom<TelecomHaiNanBalanceResult>();

		TelecomHaiNanBalanceRoot jsonObject = gs.fromJson(html, TelecomHaiNanBalanceRoot.class);

		List<TelecomHaiNanBalanceResult> list = null;
		if (jsonObject.getMaps().getKeyongs() != null) {
			list = jsonObject.getMaps().getKeyongs();
		}
		webParam.setList(list);
		return webParam;
	}

	public static WebParamTelecom<TelecomHaiNanIntegraChangeResult> integraChangeResult_parse(String html) {
		/*
		 * html =
		 * "<buffalo-reply><string>&lt;table width=&apos;99%&apos; border=&apos;0&apos; cellspacing=&apos;0&apos; cellpadding=&apos;0&apos;"
		 * + " style=&apos;margin-left:4px;&apos;&gt;&lt;tr&gt;" +
		 * "&lt;td&gt;序号&lt;/td&gt;" + "&lt;td&gt;积分日期&lt;/td&gt;" +
		 * "&lt;td&gt;当月新增积分&lt;/td&gt;" + "&lt;td&gt;当月消费积分&lt;/td&gt;" +
		 * "&lt;td&gt;当月奖励积分&lt;/td&gt;" + "&lt;td&gt;当前可用积分&lt;/td&gt;" +
		 * "&lt;/tr&gt;" +
		 * "&lt;tr&gt;&lt;td colspan=&quot;6&quot;&gt;对不起，无查询记录&lt;/td&gt;&lt;/tr&gt;&lt;/table&gt;</string></buffalo-reply>"
		 * ;
		 */
		WebParamTelecom<TelecomHaiNanIntegraChangeResult> webParam = new WebParamTelecom<TelecomHaiNanIntegraChangeResult>();
		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html), "utf-8");

		Elements tableeles = doc.select("tbody");
		if (tableeles.text().indexOf("对不起，无查询记录") != -1) {
			webParam.setHtml(html);
			webParam.setErrormessage("==========无数据==========");
			return webParam;
		}
		try {
			Elements trEles = tableeles.select("tr");
			List<TelecomHaiNanIntegraChangeResult> result = new ArrayList<>();
			int i = 0;
			for (Element trEle : trEles) {
				i++;
				if (i == 1) {
					continue;
				}
				try {
					TelecomHaiNanIntegraChangeResult telecomHaiNanIntegraChangeResult = new TelecomHaiNanIntegraChangeResult();

					telecomHaiNanIntegraChangeResult.setXuhao(trEle.select("td").get(0).text());
					telecomHaiNanIntegraChangeResult.setDate(trEle.select("td").get(1).text());
					telecomHaiNanIntegraChangeResult.setNewintegra(trEle.select("td").get(2).text());
					telecomHaiNanIntegraChangeResult.setConsumptionintegra(trEle.select("td").get(3).text());
					telecomHaiNanIntegraChangeResult.setBonusintegra(trEle.select("td").get(4).text());
					telecomHaiNanIntegraChangeResult.setUsableintegra(trEle.select("td").get(5).text());

					result.add(telecomHaiNanIntegraChangeResult);
				} catch (Exception e) {
					e.printStackTrace();
					webParam.setErrormessage(e.getMessage());
					webParam.setHtml(html);
				}

			}
			webParam.setList(result);
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setHtml(html);
			webParam.setErrormessage(e.getMessage());
		}

		return webParam;
	}

	public static WebParamTelecom<TelecomHaiNanCallThremResult> callThrem_parse(String html) {

		/*
		 * html =
		 * "<buffalo-reply><string>&lt;p class=&quot;m22 lh6 h15 p4&quot; align=&quot;right&quot; style=&quot;margin-right:20px&quot;&gt;	&lt;img src=&apos;/service/bill/images/downbtnbg.png&apos; width=&apos;80&apos; height=&apos;28&apos; alt=&apos;下载&apos; onclick=&apos;expDetailBill();&apos; /&gt;&amp;nbsp;&amp;nbsp;	&lt;img src=&apos;/service/bill/images/downbtnbg2.png&apos; width=&apos;80&apos; height=&apos;28&apos; alt=&apos;打印&apos; onclick=&apos;printDetailBill();&apos; /&gt;&lt;/p&gt;"
		 * +
		 * "&lt;table width=&quot;100%&quot; border=&quot;1&quot; cellspacing=&quot;0&quot; cellpadding=&quot;0&quot; style=&quot;border-collapse: collapse&quot; bordercolor=&quot;#DCDDD7&quot;&gt;"
		 * +
		 * "&lt;tr&gt;&lt;td clspan=&apos;2&apos; align=&quot;center&quot;&gt;累计费用（元）:0.00 &amp;nbsp;&amp;nbsp;累计时长（分）:1 &amp;nbsp;&amp;nbsp;累计时长（秒）:8&lt;/td&gt;&lt;/tr&gt;"
		 * +
		 * "&lt;table width=&qot;100%&quot; border=&quot;1&quot; cellspacing=&quot;0&quot; cellpadding=&quot;0&quot; style=&quot;border-collapse: collapse&quot; bordercolor=&quot;#DCDDD7&quot;&gt;"
		 * + "&lt;tr&gt;" +
		 * "&lt;td height=&quo;25&quot; align=&quot;center&quot;&gt;序号&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;呼叫类型&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;通话类型&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;通话地点&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;对方号码&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;开始时间&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;通话时长（分）&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;通话时长（秒）&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;费用（元）&lt;/td&gt;"
		 * + "&lt;/tr&gt;" + "&lt;tr&gt;" +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;1&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;被叫&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;国内通话&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;北京&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;13520800817&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;2017-09-21 11:55:24&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;1&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;8&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;0.00&amp;nbsp;&lt;/td&gt;"
		 * + "&lt;/tr&gt;" + "&lt;/table&gt;" + "&lt;/table&gt;" +
		 * "</string></buffalo-reply>";
		 */
		WebParamTelecom<TelecomHaiNanCallThremResult> webParam = new WebParamTelecom<TelecomHaiNanCallThremResult>();
		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html).replaceAll("&nbsp;", ""), "utf-8");
		if (doc.text() == null || doc.text().isEmpty()) {
			webParam.setHtml(html);
			webParam.setErrormessage("无话单记录");
			return webParam;
		}
		List<TelecomHaiNanCallThremResult> result = new ArrayList<>();
		try {
			Elements trEles = doc.select("tbody:contains(呼叫类型)").select("tr");
			for (Element trEle : trEles) {

				if (trEle.text().indexOf("呼叫类型") != -1) {
					continue;
				}
				try {
					TelecomHaiNanCallThremResult telecomHaiNanCallThremResult = new TelecomHaiNanCallThremResult();

					telecomHaiNanCallThremResult.setXuhao(trEle.select("td").get(0).text());

					telecomHaiNanCallThremResult.setType(trEle.select("td").get(1).text());

					telecomHaiNanCallThremResult.setCalltype(trEle.select("td").get(2).text());

					telecomHaiNanCallThremResult.setCalllocation(trEle.select("td").get(3).text());

					telecomHaiNanCallThremResult.setCallphoneother(trEle.select("td").get(4).text());

					telecomHaiNanCallThremResult.setDate(trEle.select("td").get(5).text());

					telecomHaiNanCallThremResult.setCalltimeminute(trEle.select("td").get(6).text());

					telecomHaiNanCallThremResult.setCalltimesecond(trEle.select("td").get(7).text());

					telecomHaiNanCallThremResult.setCallcosts(trEle.select("td").get(8).text());

					result.add(telecomHaiNanCallThremResult);
				} catch (Exception e) {
					e.printStackTrace();
					webParam.setErrormessage(e.getMessage());
					webParam.setHtml(html);
				}

			}
			webParam.setList(result);
		} catch (Exception e) {
			webParam.setErrormessage(e.getMessage());
			webParam.setHtml(html);
		}

		return webParam;

	}

	public static WebParamTelecom<TelecomHaiNanSMSThremResult> SMSThrem_parse(String html) {
		WebParamTelecom<TelecomHaiNanSMSThremResult> webParam = new WebParamTelecom<TelecomHaiNanSMSThremResult>();
		/*
		 * html =
		 * "<buffalo-reply><string>&lt;p class=&quot;m22 lh6 h15 p4&quot; align=&quot;right&quot; style=&quot;margin-right:20px&quot;&gt;	&lt;img src=&apos;/service/bill/images/downbtnbg.png&apos; width=&apos;80&apos; height=&apos;28&apos; alt=&apos;下载&apos; onclick=&apos;expDetailBill();&apos; /&gt;&amp;nbsp;&amp;nbsp;	&lt;img src=&apos;/service/bill/images/downbtnbg2.png&apos; width=&apos;80&apos; height=&apos;28&apos; alt=&apos;打印&apos; onclick=&apos;printDetailBill();&apos; /&gt;&lt;/p&gt;"
		 * +
		 * "&lt;table width=&quot;100%&quot; border=&quot;1&quot; cellspacing=&quot;0&quot; cellpadding=&quot;0&quot; style=&quot;border-collapse: collapse&quot; bordercolor=&quot;#DCDDD7&quot;&gt;"
		 * +
		 * "&lt;tr&gt;&lt;td colspan=&apos;2&apos; align=&quot;center&quot;&gt;累计费用（元）:0.10 &amp;nbsp;&amp;nbsp;累计条数（条）:1&lt;/td&gt;&lt;/tr&gt;"
		 * +
		 * "&lt;table width=&quot;100%&quot; border=&quot;1&quot; cellspacing=&quot;0&quot; cellpadding=&quot;0&quot; style=&quot;border-collapse: collapse&quot; bordercolor=&quot;#DCDDD7&quot;&gt;"
		 * + "&lt;tr&gt;" +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;序号&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;业务类型&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;收发类型&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;对方号码&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;发送时间&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;费用（元）&lt;/td&gt;"
		 * + "&lt;/tr&gt;" + "&lt;tr&gt;" +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;1&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;点对点短信&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;国内发&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;8617388977090&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;2017-09-21 11:38:30&amp;nbsp;&lt;/td&gt;"
		 * +
		 * "&lt;td height=&quot;25&quot; align=&quot;center&quot;&gt;0.10&amp;nbsp;&lt;/td&gt;"
		 * + "&lt;/tr&gt;" + "&lt;/table&gt;" + "&lt;/table&gt;" +
		 * "</string></buffalo-reply>";
		 */
		Document doc = Jsoup.parse(StringEscapeUtils.unescapeHtml(html).replaceAll("&nbsp;", ""), "utf-8");
		
		if (doc.text() == null || doc.text().isEmpty()) {
			webParam.setHtml(html);
			webParam.setErrormessage("无话单记录");
			return webParam;
		}
		List<TelecomHaiNanSMSThremResult> result = new ArrayList<>();
		try {
			Elements trEles = doc.select("tbody:contains(业务类型)").select("tr");
			for (Element trEle : trEles) {

				if (trEle.text().indexOf("业务类型") != -1) {
					continue;
				}

				try {
					TelecomHaiNanSMSThremResult telecomHaiNanSMSThremResult = new TelecomHaiNanSMSThremResult();

					telecomHaiNanSMSThremResult.setXuhao(trEle.select("td").get(0).text());

					telecomHaiNanSMSThremResult.setBusinesstype(trEle.select("td").get(1).text());

					telecomHaiNanSMSThremResult.setSmstype(trEle.select("td").get(2).text());

					telecomHaiNanSMSThremResult.setSmsothercall(trEle.select("td").get(3).text());

					telecomHaiNanSMSThremResult.setDate(trEle.select("td").get(4).text());

					telecomHaiNanSMSThremResult.setSmscost(trEle.select("td").get(5).text());

					result.add(telecomHaiNanSMSThremResult);
				} catch (Exception e) {
					e.printStackTrace();
					webParam.setErrormessage(e.getMessage());
					webParam.setHtml(html);
				}

			}
			webParam.setList(result);
		} catch (Exception e) {
			webParam.setErrormessage(e.getMessage());
			webParam.setHtml(html);
		}

		return webParam;

	}

	public static WebParamTelecom<TelecomHaiNanBusinessResult> business_parse(String html) {
		// html =
		// "{'NWKSTARTDATE':'2017-09-21','NWKCODE':'920172107','NWKENDDATE':'2047-08-31','NWKNAME':'红包卡（定向流量）','FEE':'--'}";
		WebParamTelecom<TelecomHaiNanBusinessResult> webParam = new WebParamTelecom<TelecomHaiNanBusinessResult>();
		List<TelecomHaiNanBusinessResult> list = new ArrayList<>();
		TelecomHaiNanBusinessResult jsonObject = gs.fromJson(html, TelecomHaiNanBusinessResult.class);
		list.add(jsonObject);
		webParam.setList(list);
		return webParam;
	}

	public static TelecomHaiNanUserIdBean readyforUserId(String html) {
		TelecomHaiNanUserIdBean telecomHaiNanUserIdBean = new TelecomHaiNanUserIdBean();

		Document doc = Jsoup.parse(html);

		try{
			String value = doc.select("select#sel_usernum").select("option").attr("value");
			telecomHaiNanUserIdBean.setPhonetype(value.split("\\|")[0]);
			telecomHaiNanUserIdBean.setPhone(value.split("\\|")[1]);
			telecomHaiNanUserIdBean.setWeizhiphonetype(value.split("\\|")[2]);
			telecomHaiNanUserIdBean.setWeizhi2(value.split("\\|")[3]);
			telecomHaiNanUserIdBean.setWeizhi3(value.split("\\|")[4]);
			telecomHaiNanUserIdBean.setCanshu(value.split("\\|")[5]);

			Pattern pt = Pattern.compile("userid=.*?;");
			Matcher match = pt.matcher(doc.toString());
			while (match.find()) {
				telecomHaiNanUserIdBean.setUserid(match.group().replaceAll("[^0-9]", "").trim());
				break;
			}
//			System.out.println(telecomHaiNanUserIdBean.toString());
			return telecomHaiNanUserIdBean;
		}catch(Exception e){
			return null;
		}
		
	}

	public static void main(String[] args) {
		try {
			SMSThrem_parse(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
