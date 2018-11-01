package test;

import java.io.IOException;
import java.net.URL;
import java.util.List;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class sz_zhejianglogin {

	public static void main(String[] args) throws Exception {


		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://puser.zjzwfw.gov.cn/sso/usp.do?action=logout&servicecode=njdh";
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput loginname = (HtmlTextInput) page.getElementById("loginname");//身份证
		HtmlPasswordInput loginpwd = (HtmlPasswordInput) page.getElementById("loginpwd");//密码
		HtmlButtonInput log  = page.getFirstByXPath("//input[@id='submit']");//登录
		String city = "丽水市";
		loginname.setText("332501198902260222");//18857002671
		loginpwd.setText("");//xuwang888888
		//衢州市 18857002671 xuwang888888
		//丽水市 332501198902260222 pisces226
		Page page3 = log.click();

		String html3 = page3.getWebResponse().getContentAsString();
		System.out.println(html3);

		if(html3.indexOf("社保查询")!=-1){
			System.out.println("成功");
			if(city.equals("衢州市")){
				try {
					//个人信息
					String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_grjbxx";
					String url3 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_ylzh";
					getHtml(url2, webClient);
					Page page4 = getHtml(url2, webClient);
					String html2 = page4.getWebResponse().getContentAsString();
					System.out.println(html2);
					JSONObject object = JSONObject.fromObject(html2);
					JSONObject obj = object.getJSONArray("data").getJSONObject(0);
					String string = obj.getString("aac003");//姓名
					String string2 = obj.getString("aac031");//参保状态
					String string3 = obj.getString("aab004");//公司名称
					String string4 = obj.getString("aac002");//社会保障号
					Page page2 = getHtml(url3, webClient);
					String html4 = page2.getWebResponse().getContentAsString();
					System.out.println(html4);
					JSONObject object2 = JSONObject.fromObject(html4);
					JSONObject obj2 = object2.getJSONArray("data").getJSONObject(0);
					String string5 = obj2.getString("eic058");//至上年末实际缴费月数
					String string6 = obj2.getString("aae001");//年度
					String string7 = obj2.getString("aic072");//上年末记账金额
					String string8 = obj2.getString("aic073");//上年末记账利息
					String string9 = obj2.getString("eic001");//截至上年末个人账户累计储存额
					String string10 = obj2.getString("aic111");//本年末个人账户累计存储额
					String string11 = obj2.getString("aic112");//本年末记账金额

					System.out.println("衢州市-个人信息如下:\r姓名:"+string+"\r参保状态:"+string2+"\r公司名称:"+string3+
							"\r社会保障号:"+string4+"\r至上年末实际缴费月数:"+string5+"\r年度:"+string6+"\r上年末记账金额:"+string7
							+"\r上年末记账利息:"+string8+"\r截至上年末个人账户累计储存额:"+string9+"\r本年末个人账户累计存储额:"+string10+"\r本年末记账金额:"+string11);
					//养老  http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_yljf&limit=5&pageNo=1&pageSize=5
				/*	String url4 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?"
							+ "dataset=sbcx_sheng$sbcx_yljf"
							+ "&limit=500"
							+ "&pageNo=1"
							+ "&pageSize=500";
					Page page5 = getHtml(url4, webClient);
					String html5 = page5.getWebResponse().getContentAsString();
					System.out.println(html5);
					JSONObject object5 = JSONObject.fromObject(html5);
					JSONArray obArray = object5.getJSONArray("data");
					for (int i = 0; i < obArray.size(); i++) {
						String string12 = obArray.getJSONObject(i).getString("aae002");//缴费年月
						String string13 = obArray.getJSONObject(i).getString("jfjs");//缴费基数
						String string14 = obArray.getJSONObject(i).getString("gryj");//个人缴费金额
						String string15 = obArray.getJSONObject(i).getString("aab004");//公司名称
						String string16 = obArray.getJSONObject(i).getString("aae143");//缴费类型
						String string17 = obArray.getJSONObject(i).getString("aae111");//缴纳状态
						System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
								string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
					}
					//医疗  http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_ybjf&limit=5&pageNo=1&pageSize=50
					String url6 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?"
							+ "dataset=sbcx_sheng$sbcx_ybjf"
							+ "&limit=500"
							+ "&pageNo=1"
							+ "&pageSize=500";
					Page page6 = getHtml(url6, webClient);
					String html6 = page6.getWebResponse().getContentAsString();
					System.out.println(html6);
					JSONObject object6 = JSONObject.fromObject(html6);
					JSONArray obArray6 = object6.getJSONArray("data");
					for (int i = 0; i < obArray6.size(); i++) {
						String string12 = obArray6.getJSONObject(i).getString("aae002");//缴费年月
						String string13 = obArray6.getJSONObject(i).getString("jfjs");//缴费基数
						String string14 = obArray6.getJSONObject(i).getString("gryj");//个人缴费金额
						String string15 = obArray6.getJSONObject(i).getString("aab004");//公司名称
						String string16 = obArray6.getJSONObject(i).getString("aae143");//缴费类型
						String string17 = obArray6.getJSONObject(i).getString("aae111");//缴纳状态
						System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
								string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
					}
					//工伤
					String url5 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?"
							+ "dataset=sbcx_sheng$sbcx_gssyxx"
							+ "&limit=500"
							+ "&pageNo=1"
							+ "&pageSize=500";
					Page page7 = getHtml(url5, webClient);
					String html7 = page7.getWebResponse().getContentAsString();
					System.out.println(html7);
					JSONObject object7 = JSONObject.fromObject(html7);
					JSONArray obArray7 = object7.getJSONArray("data");
					for (int i = 0; i < obArray7.size(); i++) {
						String string12 = obArray7.getJSONObject(i).getString("aae002");//缴费年月
						String string13 = obArray7.getJSONObject(i).getString("jfjs");//缴费基数
						String string14 = obArray7.getJSONObject(i).getString("gryj");//个人缴费金额
						String string15 = obArray7.getJSONObject(i).getString("aab004");//公司名称
						String string16 = obArray7.getJSONObject(i).getString("aae143");//缴费类型
						String string17 = obArray7.getJSONObject(i).getString("aae111");//缴纳状态
						System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
								string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
					}*/
					
					//失业
					String url5 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?"
							+ "dataset=sbcx_sheng$sbcx_syjbxx"
							+ "&limit=500"
							+ "&pageNo=1"
							+ "&pageSize=500";
					Page page7 = getHtml(url5, webClient);
					String html7 = page7.getWebResponse().getContentAsString();
					System.out.println(html7);
					JSONObject object7 = JSONObject.fromObject(html7);
					JSONArray obArray7 = object7.getJSONArray("data");
					for (int i = 0; i < obArray7.size(); i++) {
						if(obArray7.getJSONObject(i).has("aae002")){
						String string12 = obArray7.getJSONObject(i).getString("aae002");//缴费年月
						String string13 = obArray7.getJSONObject(i).getString("jfjs");//缴费基数
						String string14 = obArray7.getJSONObject(i).getString("gryj");//个人缴费金额
						String string15 = obArray7.getJSONObject(i).getString("aab004");//公司名称
						String string16 = obArray7.getJSONObject(i).getString("aae143");//缴费类型
						String string17 = obArray7.getJSONObject(i).getString("aae111");//缴纳状态
						System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
								string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
						}else{
							System.out.println("无缴纳记录");
						}
					}
					
					//生育
					String url6 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?"
							+ "dataset=sbcx_sheng$sbcx_shengyxx"
							+ "&limit=500"
							+ "&pageNo=1"
							+ "&pageSize=500";
					Page page8 = getHtml(url6, webClient);
					String html8 = page8.getWebResponse().getContentAsString();
					System.out.println(html8);
					JSONObject object8 = JSONObject.fromObject(html8);
					JSONArray obArray = object8.getJSONArray("data");
					for (int i = 0; i < obArray.size(); i++) {
						if(obArray7.getJSONObject(i).has("aae002")){
						String string12 = obArray.getJSONObject(i).getString("aae002");//缴费年月
						String string13 = obArray.getJSONObject(i).getString("jfjs");//缴费基数
						String string14 = obArray.getJSONObject(i).getString("gryj");//个人缴费金额
						String string15 = obArray.getJSONObject(i).getString("aab004");//公司名称
						String string16 = obArray.getJSONObject(i).getString("aae143");//缴费类型
						String string17 = obArray.getJSONObject(i).getString("aae111");//缴纳状态
						System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
								string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
						}else{
							System.out.println("无缴纳记录");
						}
					}
					
				} catch (Exception e) {
					System.out.println("所选参保地未查询到相关信息");
				}
			}
			else if(city.equals("丽水市")){
				//个人信息
				String urll = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?dataset=sbcx_sheng$sbcx_grjbxx";
				getHtml(urll, webClient);
				Page page2 = getHtml(urll, webClient);
				String htmll = page2.getWebResponse().getContentAsString();
				System.out.println(htmll);
				JSONObject object = JSONObject.fromObject(htmll);
				JSONObject obj = object.getJSONArray("data").getJSONObject(0);
				String string = obj.getString("aac003");//姓名
				if(string.equals("")){
					System.out.println("所选城市查询不到该帐号信息");
					System.exit(0);
				}
				String string2 = obj.getString("aac031");//参保状态
				String string3 = obj.getString("aab004");//公司名称
				String string4 = obj.getString("aac002");//社会保障号
				System.out.println("丽水市-个人信息如下:\r姓名:"+string+"\r参保状态:"+string2+"\r公司名称:"+string3+
						"\r社会保障号:"+string4);


				//养老
				String urll2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?"
						+ "dataset=sbcx_sheng$sbcx_yljf"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page4 = getHtml(urll2, webClient);
				String html4 = page4.getWebResponse().getContentAsString();
				System.out.println(html4);
				JSONObject object5 = JSONObject.fromObject(html4);
				JSONArray obArray = object5.getJSONArray("data");
				if(obArray.size()>0){
					for (int i = 0; i < obArray.size(); i++) {
						String string12 = obArray.getJSONObject(i).getString("aae002");//缴费年月
						String string13 = obArray.getJSONObject(i).getString("jfjs");//缴费基数
						String string14 = obArray.getJSONObject(i).getString("gryj");//个人缴费金额
						String string15 = obArray.getJSONObject(i).getString("aab004");//公司名称
						String string16 = obArray.getJSONObject(i).getString("aae143");//缴费类型
						String string17 = obArray.getJSONObject(i).getString("aae111");//缴纳状态
						System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
								string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
					}
				}else{
					System.out.println("无数据");
				}
				//医疗
				String url6 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?"
						+ "dataset=sbcx_sheng$sbcx_ybzh"
						+ "&limit=5"
						+ "&pageNo=1"
						+ "&pageSize=5";
				Page page6 = getHtml(url6, webClient);
				String html6 = page6.getWebResponse().getContentAsString();
				System.out.println(html6);
				JSONObject object6 = JSONObject.fromObject(html6);
				JSONArray obArray6 = object6.getJSONArray("data");
				if(obArray6.size()>0){
					for (int i = 0; i < obArray6.size(); i++) {
						String string12 = obArray6.getJSONObject(i).getString("aae035");//变动日期
						String string13 = obArray6.getJSONObject(i).getString("aae013");//备考
						String string14 = obArray6.getJSONObject(i).getString("aad088");//支出金额
						String string15 = obArray6.getJSONObject(i).getString("aad089");//收入金额
						String string16 = obArray6.getJSONObject(i).getString("aaa097");//收支类型
						System.out.println("变动日期:"+string12+"\r备考:"+string13+"\r支出金额:"+string14+"\r收入金额:"+
								string15+"\r收支类型:"+string16);
					}
				}else{
					System.out.println("无数据");
				}
				
				//工伤
				String urll1 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?"
						+ "dataset=sbcx_sheng$sbcx_ybjzxx"
						+ "&limit=5"
						+ "&pageNo=1"
						+ "&pageSize=5";
				Page page5 = getHtml(urll1, webClient);
				String htmll1 = page5.getWebResponse().getContentAsString();
				System.out.println(htmll1);
				JSONObject objectl1 = JSONObject.fromObject(htmll1);
				JSONArray obArrayl1 = objectl1.getJSONArray("data");
				if(obArrayl1.equals("")!=true){
					for (int i = 0; i < obArray.size(); i++) {
						String string12 = obArray.getJSONObject(i).getString("aae002");//缴费年月
						String string13 = obArray.getJSONObject(i).getString("jfjs");//缴费基数
						String string14 = obArray.getJSONObject(i).getString("gryj");//个人缴费金额
						String string15 = obArray.getJSONObject(i).getString("aab004");//公司名称
						String string16 = obArray.getJSONObject(i).getString("aae143");//缴费类型
						String string17 = obArray.getJSONObject(i).getString("aae111");//缴纳状态
						System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
								string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
					}
				}else{
					System.out.println("无数据");
				}
				
				
				//生育
				String url8 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?"
						+ "dataset=sbcx_sheng$sbcx_shengyxx"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page8 = getHtml(url8, webClient);
				String html8 = page8.getWebResponse().getContentAsString();
				System.out.println(html8);
				JSONObject object8 = JSONObject.fromObject(html8);
				JSONArray obArray8 = object8.getJSONArray("data");
				for (int i = 0; i < obArray8.size(); i++) {
					if(obArray8.getJSONObject(i).has("aae002")){
					String string12 = obArray8.getJSONObject(i).getString("aae002");//缴费年月
					String string13 = obArray8.getJSONObject(i).getString("jfjs");//缴费基数
					String string14 = obArray8.getJSONObject(i).getString("gryj");//个人缴费金额
					String string15 = obArray8.getJSONObject(i).getString("aab004");//公司名称
					String string16 = obArray8.getJSONObject(i).getString("aae143");//缴费类型
					String string17 = obArray8.getJSONObject(i).getString("aae111");//缴纳状态
					System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
							string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
					}else{
						System.out.println("无缴纳记录");
					}
				}
				
				//失业
				String urll3 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?"
						+ "dataset=sbcx_sheng$sbcx_syjf"
						+ "&limit=5"
						+ "&pageNo=1"
						+ "&pageSize=5";
				Page page7 = getHtml(urll3, webClient);
				String htmll3 = page7.getWebResponse().getContentAsString();
				System.out.println(htmll3);
				JSONObject object9 = JSONObject.fromObject(htmll3);
				JSONArray obArray9 = object9.getJSONArray("data");
				for (int i = 0; i < obArray9.size(); i++) {
					if(obArray9.getJSONObject(i).has("aae002")){
					String string12 = obArray9.getJSONObject(i).getString("aae002");//缴费年月
					String string13 = obArray9.getJSONObject(i).getString("jfjs");//缴费基数
					String string14 = obArray9.getJSONObject(i).getString("gryj");//个人缴费金额
					String string15 = obArray9.getJSONObject(i).getString("aab004");//公司名称
					String string16 = obArray9.getJSONObject(i).getString("aae143");//缴费类型
					String string17 = obArray9.getJSONObject(i).getString("aae111");//缴纳状态
					System.out.println("缴费年月:"+string12+"\r缴费基数:"+string13+"\r个人缴费金额:"+string14+"\r公司名称:"+
							string15+"\r缴费类型:"+string16+"\r缴纳状态:"+string17);
					}else{
						System.out.println("无缴纳记录");
					}
				}
				
			}else if(city.equals("台州市")){


			}else if(city.indexOf("金华市")!=-1){
				

			}
		}else{
			Document doc = Jsoup.parse(html3);
			Elements element = doc.getElementsByClass("login-tip");
			String error = element.text();
			System.out.println("失败"+error);
		}
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
