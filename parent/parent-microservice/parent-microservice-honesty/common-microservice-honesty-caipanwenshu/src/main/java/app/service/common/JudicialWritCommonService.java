package app.service.common;

import java.net.URL;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritList;

import net.sf.json.JSONObject;

@Component
public class JudicialWritCommonService {

	public JudicialWritList getcontent(JudicialWritList judicialWritList, WebClient webClient,HttpProxyBean httpProxyBean) throws Exception{

		String url = "http://wenshu.court.gov.cn/CreateContentJS/CreateContentJS.aspx?"
				+ "DocID="+judicialWritList.getWritid();
		Page page = getHtml(url, webClient, httpProxyBean);
		String html = page.getWebResponse().getContentAsString();
		if(html.indexOf("var caseinfo=JSON.stringify")!=-1){
			String[] split = html.split("var caseinfo=JSON.stringify");
			String[] split2 = split[1].split("}");
			String substring = split2[0].substring(1, split2[0].length());
			substring = substring + "}";
			JSONObject object = JSONObject.fromObject(substring);
			System.out.println("文书信息json>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(object);
			judicialWritList.setCourtid(object.getString("法院ID"));
			judicialWritList.setCasebasic(object.getString("案件基本情况段原文"));
			judicialWritList.setFujia(object.getString("附加原文"));
			judicialWritList.setPrefectural(object.getString("法院地市"));
			judicialWritList.setProvince(object.getString("法院省份"));
			judicialWritList.setPreludetext(object.getString("文本首部段落原文"));
			judicialWritList.setCourtdistrict(object.getString("法院区域"));
			judicialWritList.setCounty(object.getString("法院区县"));
			judicialWritList.setAmendment(object.getString("补正文书"));
			judicialWritList.setFulltype(object.getString("文书全文类型"));
			judicialWritList.setLawsuitrecord(object.getString("诉讼记录段原文"));
			judicialWritList.setResult_of_judgment(object.getString("判决结果段原文"));
			judicialWritList.setCasename(object.getString("案件名称"));
		}

		if(html.indexOf("var jsonHtmlData = ")!=-1||html.indexOf("varjsonHtmlData=")!=-1){
			String[] split3 = null;
			if(html.indexOf("var jsonHtmlData = ")!=-1){
				split3 = html.split("var jsonHtmlData = ");
			}else{
				split3 = html.split("varjsonHtmlData=");
			}
			String[] split4 = null;
			if(split3[1].indexOf("var jsonData = eval")!=-1){
				split4 = split3[1].split("var jsonData = eval");
			}else{
				split4 = split3[1].split("varjsonData=eval");
			}

			String html2 = split4[0].substring(1, split4[0].length()-8);
			String htmltext = html2.replace("\\", "");
			System.out.println("文书内容json>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(htmltext);
			String string = JSONObject.fromObject(htmltext).getString("Html");
			judicialWritList.setHtmltext(string);
			String text = Jsoup.parse(string).text();
			judicialWritList.setText(text);
		}
		return judicialWritList;
	}

	public static Page getHtml(String url, WebClient webClient,HttpProxyBean httpProxyBean) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		if (httpProxyBean!=null) {
			webRequest.setProxyHost(httpProxyBean.getIp());
			webRequest.setProxyPort(Integer.parseInt(httpProxyBean.getPort()));
		}
		//		webClient.setJavaScriptTimeout(50000); 
		//		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}


}
