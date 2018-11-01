package app.parser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.microservice.dao.entity.crawler.housing.weifang.HousingWeiFangHtml;
import com.microservice.dao.entity.crawler.housing.weifang.HousingWeiFangPay;
import com.microservice.dao.entity.crawler.housing.weifang.HousingWeiFangUserinfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.ReqParam;
import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.common.LoginAndGetCommon;
import sun.misc.BASE64Encoder;

@Component
public class HousingWeiFangParser {
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
		String url = "http://www.wfgjj.gov.cn/personal/login.jspx";
		String smsUrl = "http://www.wfgjj.gov.cn/personal/sendVercodeByIdNo.jspx?idNo="+messageLoginForHousing.getNum();
		tracer.addTag("parser.login.parser.url", url);
		tracer.addTag("parser.login.parser.smsUrl", smsUrl);
		
		WebParam webParam = new WebParam();
		webParam.setUrl(url+"|"+smsUrl);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//先登陆首页
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		tracer.addTag("parser.login.parser.loginPage", page.asXml());
		//然后请求发送短信
		webRequest = new WebRequest(new URL(smsUrl), HttpMethod.GET);
		Page page2 = webClient.getPage(webRequest);
		if(null != page2 && page2.getWebResponse().getStatusCode() == 200){
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			tracer.addTag("parser.login.parser.sendSmsPage", page2.getWebResponse().getContentAsString());
		}
		webParam.setWebClient(webClient);
		return webParam;
	}

	//登录（2） 登录网站，将收到的短信验证码发送以及用户密码进行加密
	public WebParam loginCombo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.loginCombo.parser.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = LoginAndGetCommon.addcookie(webClient, taskHousing);
		String loginUrl = "http://www.wfgjj.gov.cn/personal/personLogin_new.jspx";
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
		String requestBody = "key=&idno="+messageLoginForHousing.getNum()+"&pwd="+encode64PWD(messageLoginForHousing.getPassword())+"&captcha="+messageLoginForHousing.getSms_code();
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
		webParam.setWebClient(webClient);
		return webParam;
	}
	
	public String encode64PWD(String s) throws Exception{
        String encode = new BASE64Encoder().encode(s.getBytes());  
        return encode;
	}

	public WebParam getUserInfo(HousingWeiFangHtml loginedHtml, TaskHousing taskHousing) throws Exception{
		WebParam webParam = new WebParam<>();
		tracer.addTag("parser.getUserInfo.parser.id", taskHousing.getTaskid());
		tracer.addTag("parser.getUserInfo.parser.page", "<xmp>"+loginedHtml.getHtml()+"</xmp>");
		String html = loginedHtml.getHtml();
		Document parse = Jsoup.parse(html);
		String staffName = getNextLabelByKeyword(parse, "td", "职工姓名");
		String idNum = getNextLabelByKeyword(parse, "td", "身份证号码");
		String staffNum = getNextLabelByKeyword(parse, "td", "职工账号");
		String companyName = getNextLabelByKeyword(parse, "td", "单位名称");
		String payBase = getNextLabelByKeyword(parse, "td", "缴存基数");
		String personalPercent = getNextLabelByKeyword(parse, "td", "个人存缴比例");
		String companyPercent = getNextLabelByKeyword(parse, "td", "单位存缴比例");
		String sumPercent = getNextLabelByKeyword(parse, "td", "合计存缴比例");
		String monthPay = getNextLabelByKeyword(parse, "td", "月汇缴额");
		String balance = getNextLabelByKeyword(parse, "td", "余额");
		String lastPayDate = getNextLabelByKeyword(parse, "td", "最新汇缴年月");
		String state = getNextLabelByKeyword(parse, "td", "账号状态");
		String openDate = getNextLabelByKeyword(parse, "td", "开户日期");
		String department = getNextLabelByKeyword(parse, "td", "所在管理部");
		String phoneNum = getNextLabelByKeyword(parse, "td", "手机号码");
		
		HousingWeiFangUserinfo userinfo = new HousingWeiFangUserinfo();
		userinfo.setTaskid(taskHousing.getTaskid());
		userinfo.setStaffName(staffName);
		userinfo.setIdNum(idNum);
		userinfo.setStaffNum(staffNum);
		userinfo.setCompanyName(companyName);
		userinfo.setPayBase(payBase);
		userinfo.setPersonalPercent(personalPercent);
		userinfo.setCompanyPercent(companyPercent);
		userinfo.setSumPercent(sumPercent);
		userinfo.setMonthPay(monthPay);
		userinfo.setBalance(balance);
		userinfo.setLastPayDate(lastPayDate);
		userinfo.setState(state);
		userinfo.setOpenDate(openDate);
		userinfo.setDepartment(department);
		userinfo.setPhoneNum(phoneNum);
		
		List<HousingWeiFangUserinfo> userinfos = new ArrayList<HousingWeiFangUserinfo>();
		userinfos.add(userinfo);
		webParam.setList(userinfos);
		return webParam;
	}
	
	public WebParam getParams(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("crawler.getParams.parser.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = LoginAndGetCommon.addcookie(webClient, taskHousing);
		String paramUrl = "http://www.wfgjj.gov.cn/personal/personDetail.jspx";
		webParam.setUrl(paramUrl);
		tracer.addTag("crawler.getParams.parser.url", paramUrl);
		WebRequest webRequest = new WebRequest(new URL(paramUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		webParam.setHtml(page.getWebResponse().getContentAsString());
		tracer.addTag("crawler.getParams.parser.userPage", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		Document document = Jsoup.parse(page.getWebResponse().getContentAsString());
		Elements eles = document.getElementsByAttributeValue("name", "spcode");
		if(eles.size() > 0){
			Elements selects = eles.get(0).children();
			List<ReqParam> params = new ArrayList<ReqParam>();
			for (Element element : selects) {
				ReqParam reqParam = new ReqParam();
				reqParam.setCompanyNum(element.val());
				reqParam.setCompanyName(element.text());
				params.add(reqParam);
			}
			webParam.setList(params);
			webParam.setWebClient(webClient);
		}
		
		return webParam;
	}
	
	public WebParam getTrans(ReqParam reqParam, TaskHousing taskHousing, WebClient webClient, int i) throws Exception{
		tracer.addTag("crawler.getTrans.parser.taskid."+i, taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		
		String paramUrl = "http://www.wfgjj.gov.cn/personal/personDetail.jspx?year="+getDateBefore(i)+"&spcode="+reqParam.getCompanyNum();
		webParam.setUrl(paramUrl);
		tracer.addTag("crawler.getTrans.parser.url."+i, paramUrl);
		WebRequest webRequest = new WebRequest(new URL(paramUrl), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		if(null != page && page.getWebResponse().getStatusCode() == 200){
			String html = page.getWebResponse().getContentAsString();
			webParam.setHtml(html);
			tracer.addTag("crawler.getTrans.parser.userPage."+i, "<xmp>"+html+"</xmp>");
			Document document = Jsoup.parse(html);
			Elements contents = document.getElementsByClass("content");
			if(null != contents && contents.size() > 0){
				Elements tables = contents.first().select("table");
				if(null != tables && tables.size() > 0){
					Elements trs = tables.first().select("tr");
					if(null != trs && trs.size() > 1){
						List<HousingWeiFangPay> pays = new ArrayList<HousingWeiFangPay>();
						for (int j = 1; j < trs.size(); j++) {
							Element tr = trs.get(j);
							Elements tds = tr.select("td");
							
							String payDate = tds.get(1).text();
							String markDate = tds.get(2).text();
							String mark = tds.get(3).text();
							String inMoney = tds.get(4).text();
							String outMoney = tds.get(5).text();
							String balance = tds.get(6).text();
							
							HousingWeiFangPay pay = new HousingWeiFangPay();
							pay.setTaskid(taskHousing.getTaskid());
							pay.setPayDate(payDate);
							pay.setMarkDate(markDate);
							pay.setMark(mark);
							pay.setInMoney(inMoney);
							pay.setOutMoney(outMoney);
							pay.setBalance(balance);
							pay.setCompanyName(reqParam.getCompanyName());
							
							pays.add(pay);
						}
						webParam.setList(pays);
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
	
	//获取 距今i年前 的时间
	public String getDateBefore(int i) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -i);
        Date y = c.getTime();
        String year1 = format.format(y);
        return year1;
	}

}
