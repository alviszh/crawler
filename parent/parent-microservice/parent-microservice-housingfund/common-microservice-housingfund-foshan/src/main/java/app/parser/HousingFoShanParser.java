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
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.foshan.HousingFoShanPay;
import com.microservice.dao.entity.crawler.housing.foshan.HousingFoShanUserinfo;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFoShanParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam login(WebClient webClient, MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.login.parser.login.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		
		String url = "http://www.fsgjj.gov.cn/webapply/login.do?fromFlag=wwgr";
		tracer.addTag("parser.login.parser.login.url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage loginPage1 = webClient.getPage(webRequest);
		tracer.addTag("parser.login.parser.login.page1", "<xmp>"+loginPage1.asXml()+"</xmp>");
		if(loginPage1.asXml().contains("登录")){
			HtmlImage cert1 = loginPage1.getFirstByXPath("//img[@id='cert']");
			HtmlPage loginPage = (HtmlPage) cert1.click();
			tracer.addTag("parser.login.parser.login.page2", "<xmp>"+loginPage.asXml()+"</xmp>");
			if(loginPage.asXml().contains("登录")){
				HtmlImage cert = loginPage.getFirstByXPath("//img[@id='cert']");
				HtmlTextInput idCard = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='idcard']");
				HtmlTextInput account = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='account']");
				HtmlTextInput validate = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='_cert_parameter']");
				HtmlImage loginImg = loginPage.getFirstByXPath("//img[@id='login_r17_c4']");
				
				idCard.setText(messageLoginForHousing.getNum());
				account.setText(messageLoginForHousing.getPassword());
				validate.setText(chaoJiYingOcrService.getVerifycode(cert, "1004"));
				HtmlPage loginedPage = (HtmlPage)loginImg.click();
				tracer.addTag("parser.login.parser.login.loginedPage", "<xmp>"+loginedPage.asXml()+"</xmp>");
				webParam.setHtmlPage(loginedPage);
				webParam.setWebClient(webClient);
			}
		}
		return webParam;
	}
	public WebParam getUseInfo(WebClient webClient, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.crawler.parser.getuserinfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		String url = "http://www.fsgjj.gov.cn/webapply/person/personQuery!personInfo.do?aId=&areaCode=";
		tracer.addTag("parser.crawler.parser.getuserinfo.url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.fsgjj.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.fsgjj.gov.cn/webapply/personapply/index.do");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		
		HtmlPage userinfoPage = webClient.getPage(webRequest);
		webParam.setHtmlPage(userinfoPage);
		webParam.setWebClient(webClient);
		tracer.addTag("parser.crawler.parser.getuserinfo.page", "<xmp>"+userinfoPage.asXml()+"</xmp>");
		if(null != userinfoPage && 200 == userinfoPage.getWebResponse().getStatusCode()){
			Document document = Jsoup.parse(userinfoPage.asXml());
			Element tree = document.getElementById("tree");
			if(null != tree){
				String approvalNumber = getNextLabelByKeyword(tree, "td", "审批单号");
				String name = getNextLabelByKeyword(tree, "td", "姓名");
				String idNum = getNextLabelByKeyword(tree, "td", "证件号码");
				String companyName = getNextLabelByKeyword(tree, "td", "开设单位");
				String personalNum = getNextLabelByKeyword(tree, "td", "个人明细编号");
				String setApprovalDate = getNextLabelByKeyword(tree, "td", "开设审批日期");
				String realSetDate = getNextLabelByKeyword(tree, "td", "实际开设日期");
				String personalStatus = getNextLabelByKeyword(tree, "td", "个人明细状态");
				String balance = getNextLabelByKeyword(tree, "td", "公积金余额");
				String payBase = getNextLabelByKeyword(tree, "td", "计缴公积金工资基数");
				String depositAmount = getNextLabelByKeyword(tree, "td", "缴存额");
				String companyPayPercent = getNextLabelByKeyword(tree, "td", "单位缴存比例");
				String personalPayPercent = getNextLabelByKeyword(tree, "td", "个人缴存比例");
				String isSubscribeSMS = getNextLabelByKeyword(tree, "td", "订阅短信服务");
				String subscribeTel = getNextLabelByKeyword(tree, "td", "订阅服务手机号码");
				
				HousingFoShanUserinfo userinfo = new HousingFoShanUserinfo();
				userinfo.setApprovalNumber(approvalNumber);
				userinfo.setName(name);
				userinfo.setIdNum(idNum);
				userinfo.setCompanyName(companyName);
				userinfo.setPersonalNum(personalNum);
				userinfo.setSetApprovalDate(setApprovalDate);
				userinfo.setRealSetDate(realSetDate);
				userinfo.setPersonalStatus(personalStatus);
				userinfo.setBalance(balance);
				userinfo.setPayBase(payBase);
				userinfo.setDepositAmount(depositAmount);
				userinfo.setCompanyPayPercent(companyPayPercent);
				userinfo.setPersonalPayPercent(personalPayPercent);
				userinfo.setIsSubscribeSMS(isSubscribeSMS);
				userinfo.setSubscribeTel(subscribeTel);
				if(null != userinfo){
					userinfo.setTaskid(taskHousing.getTaskid());
					List<HousingFoShanUserinfo> userinfos = new ArrayList<HousingFoShanUserinfo>();
					userinfos.add(userinfo);
					webParam.setList(userinfos);
				}
			}
		}
		return webParam;
	}
	
	public WebParam getPayInfo(WebClient webClient, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.crawler.parser.getPayInfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		String url = "http://www.fsgjj.gov.cn/webapply/person/personQuery!moneyseq.do?aId=&areaCode=";
		tracer.addTag("parser.crawler.parser.getPayInfo.url", url);
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "www.fsgjj.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.fsgjj.gov.cn/webapply/personapply/index.do");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
		
		HtmlPage payinfoPage = webClient.getPage(webRequest);
		webParam.setHtmlPage(payinfoPage);
		webParam.setWebClient(webClient);
		tracer.addTag("parser.crawler.parser.getPayInfo.page", "<xmp>"+payinfoPage.asXml()+"</xmp>");
		if(null != payinfoPage && 200 == payinfoPage.getWebResponse().getStatusCode()){
			Document document = Jsoup.parse(payinfoPage.asXml());
			Element tree = document.getElementById("tree");
			tracer.addTag("parser.crawler.parser.getPayInfo.tree", tree.toString());
			if(null != tree){
				Elements tbodys = tree.select("tbody");
				tracer.addTag("parser.crawler.parser.getPayInfo.tbodys", tbodys.toString());
				if(null != tbodys && tbodys.size() > 0){
					Element tbody = tbodys.first();
					if(null != tbody){
						Elements trs = tbody.select("tr");
						tracer.addTag("parser.crawler.parser.getPayInfo.trs", trs.toString());
						if(null != trs && trs.size() > 0){
							List<HousingFoShanPay> foShanPays = new ArrayList<HousingFoShanPay>();
							for (Element tr : trs) {
								List<String> txt = new ArrayList<String>();
								HousingFoShanPay housingFoShanPay = new HousingFoShanPay();
								Elements tds = tr.select("td");
								for (Element td : tds) {
									String text = td.text();
									txt.add(text);
								}
								housingFoShanPay.setOccurDate(txt.get(0));
								housingFoShanPay.setOccurMoney(txt.get(1));
								housingFoShanPay.setBalance(txt.get(2));
								housingFoShanPay.setMark(txt.get(3));
								if(null != housingFoShanPay){
									housingFoShanPay.setTaskid(taskHousing.getTaskid());
									foShanPays.add(housingFoShanPay);
								}
							}
							if(null != foShanPays && foShanPays.size() > 0){
								webParam.setList(foShanPays);
							}
						}
					}
				}
			}
		}
		return webParam;
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
