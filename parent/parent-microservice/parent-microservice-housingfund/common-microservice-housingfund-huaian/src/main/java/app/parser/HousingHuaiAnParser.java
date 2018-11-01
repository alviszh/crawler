package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huaian.HousingHuaiAnPay;
import com.microservice.dao.entity.crawler.housing.huaian.HousingHuaiAnUserinfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingHuaiAnParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing) throws Exception{
		// TODO Auto-generated method stub
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		tracer.addTag("login.parser.taskid", messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		
		String url = "http://www.hagjj.com/office/geren/login.asp";
		tracer.addTag("login.parser.url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		
		HtmlImage safecode1 = (HtmlImage)loginPage.getFirstByXPath("//img[@id='safecode']");
		loginPage = (HtmlPage) safecode1.click();
		tracer.addTag("login.parser.loginPage", "<xmp>"+loginPage.asXml()+"</xmp>");
		
		HtmlImage safecode = (HtmlImage)loginPage.getFirstByXPath("//img[@id='safecode']");
		if(null != safecode){
			String verifycode = chaoJiYingOcrService.getVerifycode(safecode, "1004");
			//登录地址
			String loginUrl = "http://www.hagjj.com/office/geren/slogin.asp";
			webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "text/html, application/xhtml+xml, */*");
			webRequest.setAdditionalHeader("Referer", "http://www.hagjj.com/office/geren/login.asp");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Host", "www.hagjj.com");
			webRequest.setAdditionalHeader("Connection", "Keep-Alive");
			webRequest.setAdditionalHeader("Cache-Control", "no-cache");
			
			webRequest.setRequestBody("pcardid="+messageLoginForHousing.getNum()+"&userpwd="+messageLoginForHousing.getPassword()+"&verifycode="+verifycode);
			HtmlPage loginedPage = webClient.getPage(webRequest);
			tracer.addTag("login.parser.loginedPage", "<xmp>"+loginedPage.asXml()+"</xmp>");
			
			//数据地址1
			String loginUrl2 = "http://www.hagjj.com/office/geren/grgjjcx.asp";
			webRequest = new WebRequest(new URL(loginUrl2), HttpMethod.GET);
			HtmlPage loginedPage2 = webClient.getPage(webRequest);
			//登陆成功
			if(loginedPage2.asXml().contains("账户状态")){
				webParam.setHtmlPage(loginedPage2);
				webParam.setWebClient(webClient);
				tracer.addTag("login.parser.success", "登录成功，可以抓取数据！");
			}else if(loginedPage2.asXml().contains("用户登录已经失效，请重新登录后再查询!")){
				tracer.addTag("login.parser.fail", "登录失败，账号或密码不正确");
				webParam.setHtml("账号或密码不正确，请您稍后重试！");
			}else{
				tracer.addTag("login.parser.fail2", "登录失败，其他原因");
				webParam.setHtml("网络异常，请您稍后重试！");
			}
		}else{
			webParam.setHtml("网络异常，请您稍后重试！");
		}
		return webParam;
	}

	public WebParam getUserinfo(TaskHousing taskHousing, WebClient webClient) throws Exception{
		tracer.addTag("login.parser.getUserinfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		
		String url = "http://www.hagjj.com/office/geren/grgjjcx.asp";
		webParam.setUrl(url);
		tracer.addTag("login.parser.getUserinfo.url", url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage html = webClient.getPage(webRequest);
		webParam.setHtmlPage(html);
		tracer.addTag("login.parser.getUserinfo.html", "<xmp>"+html.asXml()+"</xmp>");
		if(html.asXml().contains("账户状态")){
			Document document = Jsoup.parse(html.asXml());
			String status = getNextLabelByKeyword(document, "td", "账户状态");
			String companyName = getNextLabelByKeyword(document, "td", "单位名称");
			String companyNum = getNextLabelByKeyword(document, "td", "单位账号");
			String userName = getNextLabelByKeyword(document, "td", "姓    名：");
			String idNum = getNextLabelByKeyword(document, "td", "身份证号码");
			String personalNum = getNextLabelByKeyword(document, "td", "个人账号");
			
			List<HousingHuaiAnUserinfo> userinfos = new ArrayList<HousingHuaiAnUserinfo>();
			HousingHuaiAnUserinfo userinfo = new HousingHuaiAnUserinfo();
			
			userinfo.setTaskid(taskHousing.getTaskid());
			userinfo.setStatus(status);
			userinfo.setCompanyName(companyName);
			userinfo.setCompanyNum(companyNum);
			userinfo.setUserName(userName);
			userinfo.setIdNum(idNum);
			userinfo.setPersonalNum(personalNum);
			
			userinfos.add(userinfo);
			webParam.setList(userinfos);
		}
		return webParam;
	}

	public WebParam getPayinfo(TaskHousing taskHousing, WebClient webClient) throws Exception{
		tracer.addTag("login.parser.getUserinfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		List<HousingHuaiAnPay> pays = new ArrayList<HousingHuaiAnPay>();
		
		String url = "http://www.hagjj.com/office/geren/grgjjcx.asp";
		tracer.addTag("login.parser.getUserinfo.url", url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage html = webClient.getPage(webRequest);
		tracer.addTag("login.parser.getUserinfo.html", "<xmp>"+html.asXml()+"</xmp>");
		if(html.asXml().contains("账户状态")){
			pays = parserPays(pays, html.asXml(), taskHousing.getTaskid());
		}
		
		String url2 = "http://www.hagjj.com/office/geren/grgjjcx0.asp";
		webParam.setUrl(url2);
		tracer.addTag("login.parser.getUserinfo.url2", url2);
		webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
		HtmlPage html2 = webClient.getPage(webRequest);
		webParam.setHtmlPage(html2);
		tracer.addTag("login.parser.getUserinfo.html2", "<xmp>"+html2.asXml()+"</xmp>");
		if(html2.asXml().contains("账户状态")){
			pays = parserPays(pays, html2.asXml(), taskHousing.getTaskid());
		}
		webParam.setList(pays);
		return webParam;
	}
	
	public List<HousingHuaiAnPay> parserPays(List<HousingHuaiAnPay> pays, String html, String taskid) throws Exception{
		Document document = Jsoup.parse(html);
		Element table = document.getElementsByAttributeValue("border", "1").first();
		Elements trs = table.select("tr");
		if(trs.size() > 1){
			for (int i = 1; i < trs.size(); i++) {
				Element tr = trs.get(i);
				Elements tds = tr.select("td");
				String date = tds.get(0).text();
				String remark = tds.get(1).text();
				String increase = tds.get(2).text();
				String decrease = tds.get(3).text();
				String balance = tds.get(4).text();
				
				HousingHuaiAnPay pay = new HousingHuaiAnPay();
				pay.setTaskid(taskid);
				pay.setDate(date);
				pay.setRemark(remark);
				pay.setIncrease(increase);
				pay.setDecrease(decrease);
				pay.setBalance(balance);
				
				pays.add(pay);
			}
		}
		
		return pays;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}
}
