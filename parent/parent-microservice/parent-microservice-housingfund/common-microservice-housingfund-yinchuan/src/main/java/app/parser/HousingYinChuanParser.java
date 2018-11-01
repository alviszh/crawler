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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yinchuan.HousingYinChuanHtml;
import com.microservice.dao.entity.crawler.housing.yinchuan.HousingYinChuanUserinfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;

@Component
public class HousingYinChuanParser {
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	protected TaskHousingRepository taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	
	//登录（1） 首先根据身份证号来进行短信发送。
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.login.parser.taskid", taskHousing.getTaskid());
		taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		String url = "http://www.nxzfgjj.gov.cn/ycgjj/list_gjjcx.jsp?urltype=tree.TreeTempUrl&wbtreeid=1044";
		tracer.addTag("parser.login.parser.url", url);
		
		
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//先登陆首页
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		tracer.addTag("parser.login.parser.loginPage", page.asXml());
		//从页面获取需要的参数
		String html = page.getWebResponse().getContentAsString();
		int i = html.indexOf("var webOwner1 = ");
		int j = html.indexOf(";", i);
		String webOwner = html.substring(i+17, j-1);
		
		String smsUrl = "http://www.nxzfgjj.gov.cn/system/resource/dxSend1.jsp?sfzh="+messageLoginForHousing.getNum()+"&sjhm="+messageLoginForHousing.getPassword()+"&webOwner="+webOwner;
		tracer.addTag("parser.login.parser.smsUrl", smsUrl);
		webParam.setUrl(url+"|"+smsUrl);
		//然后请求发送短信
		webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);
		Page page2 = webClient.getPage(webRequest);
		if(null != page2 && page2.getWebResponse().getStatusCode() == 200){
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			tracer.addTag("parser.login.parser.sendSmsPage", page2.getWebResponse().getContentAsString());
		}
		webParam.setParam(webOwner);
		webParam.setWebClient(webClient);
		return webParam;
	}

	//登录（2） 登录网站，将收到的短信验证码发送以及用户密码进行加密
	public WebParam loginCombo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.loginCombo.parser.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = LoginAndGetCommon.addcookie(webClient, taskHousing);
		String valUrl = "http://www.nxzfgjj.gov.cn/system/resource/dxValidate.jsp?dxyzm="+messageLoginForHousing.getSms_code();
		WebRequest webRequest = new WebRequest(new URL(valUrl), HttpMethod.GET);
		Page valpage = webClient.getPage(webRequest);
		if(null != valpage && valpage.getWebResponse().getContentAsString().equals("Y")){
			tracer.addTag("parser.loginCombo.parser.valpage", valpage.getWebResponse().getContentAsString());
			String loginUrl = "http://www.nxzfgjj.gov.cn/ycgjj/gjjcx_result.jsp?urltype=tree.TreeTempUrl&wbtreeid=1052";
			webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			String requestBody = "webOwner=&sfzh="+messageLoginForHousing.getNum()+"&sjhm="+messageLoginForHousing.getPassword()+"&dxyzm="+messageLoginForHousing.getSms_code();
			webRequest.setRequestBody(requestBody);
			webParam.setUrl(loginUrl+"?"+requestBody);
			tracer.addTag("parser.loginCombo.parser.url", loginUrl+"?"+requestBody);
			Page page3 = webClient.getPage(webRequest);
			if(null != page3 && page3.getWebResponse().getStatusCode() == 200){
				tracer.addTag("parser.loginCombo.parser.loginedUrl", page3.getUrl()+"");
				tracer.addTag("parser.loginCombo.parser.page", "<xmp>"+page3.getWebResponse().getContentAsString()+"</xmp>");
				webParam.setHtml(page3.getUrl()+"");
				webParam.setPage(page3);
			}
		}else{
			tracer.addTag("parser.loginCombo.parser.valpage", valpage.getWebResponse().getContentAsString()+"短信验证码错误");
			webParam.setParam("短信验证码错误，请您重新登录");
		}
		webParam.setWebClient(webClient);
		return webParam;
	}
	
	public WebParam getUserInfo(HousingYinChuanHtml housingYinChuanHtml, TaskHousing taskHousing) throws Exception{
		WebParam webParam = new WebParam<>();
		tracer.addTag("parser.getUserInfo.parser.id", taskHousing.getTaskid());
		tracer.addTag("parser.getUserInfo.parser.page", "<xmp>"+housingYinChuanHtml.getHtml()+"</xmp>");
		String html = housingYinChuanHtml.getHtml();
		Document parse = Jsoup.parse(html);
		String name = getNextLabelByKeyword(parse, "td", "姓名");
		String companyName = getNextLabelByKeyword(parse, "td", "单位名称");
		String openDate = getNextLabelByKeyword(parse, "td", "开户日期");
		String idNum = getNextLabelByKeyword(parse, "td", "身份证号");
		String payDate = getNextLabelByKeyword(parse, "td", "缴至年月");
		String personMonthPay = getNextLabelByKeyword(parse, "td", "个人月缴存额");
		String monthPay = getNextLabelByKeyword(parse, "td", "月汇缴额");
		String balance = getNextLabelByKeyword(parse, "td", "公积金余额");
		String state = getNextLabelByKeyword(parse, "td", "缴存状态");
		
		HousingYinChuanUserinfo userinfo = new HousingYinChuanUserinfo();
		userinfo.setTaskid(taskHousing.getTaskid());
		userinfo.setName(name);
		userinfo.setCompanyName(companyName);
		userinfo.setOpenDate(openDate);
		userinfo.setIdNum(idNum);
		userinfo.setPayDate(payDate);
		userinfo.setPersonMonthPay(personMonthPay);
		userinfo.setMonthPay(monthPay);
		userinfo.setBalance(balance);
		userinfo.setState(state);
		
		List<HousingYinChuanUserinfo> userinfos = new ArrayList<HousingYinChuanUserinfo>();
		userinfos.add(userinfo);
		webParam.setList(userinfos);
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
