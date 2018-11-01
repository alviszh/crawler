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

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanMedicalCare;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionAccount;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionDetail;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;

@Component
public class InsuranceZhongshanParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		tracer.addTag("InsuranceZhongshanParser.login", insuranceRequestParameters.getTaskId());
		// 登录日志
		tracer.addTag("parser.login", insuranceRequestParameters.getTaskId());
		String loginUrl = "http://www.gdzs.lss.gov.cn:8030/main/myprofile/index.action?did=1";
		
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		
		HtmlPage searchPage = webClient.getPage(webRequest);
//		webClient.waitForBackgroundJavaScript(30000);
		int status = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("InsuranceZhongshanParser.login.status",
				insuranceRequestParameters.getTaskId() + "---status:" + status);
		if (200 == status) {
			HtmlImage image = searchPage.getFirstByXPath("//*[@id=\"codeimg\"]");
			// 超级鹰解析验证码
			String code = "";
			try {
				code = chaoJiYingOcrService.getVerifycode(image, "1902");
			} catch (Exception e) {
				tracer.addTag("ERROR:InsuranceZhongshanParser.login.code",
						insuranceRequestParameters.getTaskId() + "-----ERROR:" + e);
				e.printStackTrace();
			}
			tracer.addTag("InsuranceZhongshanParser.login.code",
					insuranceRequestParameters.getTaskId() + "---超级鹰解析code:" + code);
			 String loginUrl4 = "http://www.gdzs.lss.gov.cn:8030/ajax/login.action";
	         
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
			String requestBody="cardid="+insuranceRequestParameters.getUserIDNum()+"&taxid=&sbkid="+insuranceRequestParameters.getName()+"&password="+insuranceRequestParameters.getPassword()+"&code="+code+"&type=1";
			webRequest4.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest4.setAdditionalHeader("Host", "www.gdzs.lss.gov.cn:8030");
			webRequest4.setAdditionalHeader("Origin", "ttp://www.gdzs.lss.gov.cn:8030");
			webRequest4.setAdditionalHeader("Referer", "http://www.gdzs.lss.gov.cn:8030/main/");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestBody(requestBody);
			HtmlPage page4 = webClient.getPage(webRequest4);
			String html = page4.getWebResponse().getContentAsString();
			webParam.setCode(searchPage.getWebResponse().getStatusCode());
			webParam.setPage(page4);
			return webParam;
		}
		return null;
	}

	/**
	 * 通过url获取 HtmlPage
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public HtmlPage getHtmlPage(TaskInsurance taskInsurance, String cookies, String url, HttpMethod type)
			throws Exception {
		tracer.addTag("InsuranceZhongshanParser.getHtmlPage---url:" + url + " ", taskInsurance.getTaskid());
		WebClient webClient = taskInsurance.getClient(cookies);
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("InsuranceZhongshanParser.getHtmlPage---url:" + url + "---taskid:" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
			if (html.contains("没有查询出相关数据")) {
				return null;
			}
			return searchPage;
		}

		return null;
	}

	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("th:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	/**
	 * 获取养老账户信息
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceZhongShanPensionAccount> getPensionaccount(TaskInsurance taskInsurance, String cookies) {
		String urlData = "http://www.gdzs.lss.gov.cn:8030/myprofile/index.action?appcode=A1_01_01";
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("InsuranceZhongshanParser.getPensionaccount 社保养老账户信息" + taskInsurance.getTaskid(),
						"<xmp>" + html + "</xmp>");
				WebParam<InsuranceZhongShanPensionAccount> webParam1 = parserSummary(html, taskInsurance);
				webParam1.setUrl(urlData);
				webParam1.setCode(page.getWebResponse().getStatusCode());
				return webParam1;
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceZhongshanParser.getPensionaccount---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}
		return null;

	}
	
	
	/**
	 * 获取养老缴费情况明细信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	
	public WebParam<InsuranceZhongShanPensionDetail> getPensionDetail(TaskInsurance taskInsurance, String cookies) {
		String urlData="http://www.gdzs.lss.gov.cn:8030/myprofile/index.action?appcode=C1_02_01";
		WebParam webParam = new WebParam();
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			if (null != page) {
			String html = page.getWebResponse().getContentAsString();
			tracer.addTag("InsuranceZhongshanParser.getMedicalcare 社保养老账户信息" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
			List<InsuranceZhongShanPensionDetail> infoList = new ArrayList<InsuranceZhongShanPensionDetail>();
			infoList = htmlParserDetail(html,taskInsurance,infoList);
			if(null !=infoList){
    			webParam.setList(infoList);
    			webParam.setUrl(urlData);
    			webParam.setHtml(html);
    			return webParam;
    		}
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceZhongshanParser.getPensionDetail---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 解析养老账户明细信息
	 * @param html
	 * @param taskInsurance
	 * @param infoList
	 * @return
	 */
	private List<InsuranceZhongShanPensionDetail> htmlParserDetail(String html, TaskInsurance taskInsurance,List<InsuranceZhongShanPensionDetail> infoList) {
		Document doc = Jsoup.parse(html);
		Elements baseInfo = doc.getElementsByClass("tablediv normal_table").select("tr");
		for(int i =2;i<baseInfo.size()-2;i++){
			Elements tds = baseInfo.get(i).select("td");
			List<String> lists= new ArrayList<>();
			for (Element element : tds) {
				lists.add(element.text().trim());
			}
			if(null != lists){
				InsuranceZhongShanPensionDetail detail = new InsuranceZhongShanPensionDetail();
				detail.setStartYear(lists.get(0));
				detail.setEndYear(lists.get(1));
				detail.setPayMonth(lists.get(2));
				detail.setPayWages(lists.get(3));
				detail.setConmpanyPay(lists.get(4));
				detail.setUnitDelimit(lists.get(5));
				detail.setPersonalPayment(lists.get(6));
				detail.setInsuranceType(lists.get(7));
				detail.setTaskid(taskInsurance.getTaskid());
				infoList.add(detail);
			}
		}
		return infoList;
	}

	/**
	 * 获取医疗待遇信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 */
	public WebParam<InsuranceZhongShanMedicalCare> getMedicalcare(TaskInsurance taskInsurance, String cookies) {
		String urlData = "http://www.gdzs.lss.gov.cn:8030/myprofile/index.action?appcode=A1_05_01";
		WebParam webParam = new WebParam();
		try {
			HtmlPage page = getHtmlPage(taskInsurance, cookies, urlData, null);
			String html = page.getWebResponse().getContentAsString();
			tracer.addTag("InsuranceZhongshanParser.getMedicalcare 社保养老账户信息" + taskInsurance.getTaskid(),
					"<xmp>" + html + "</xmp>");
			InsuranceZhongShanMedicalCare medical = parserMedicalcare(html,taskInsurance);
			webParam.setInsuranceZhongShanMedicalCare(medical);
			webParam.setHtml(html);
			webParam.setUrl(urlData);
			webParam.setCode(page.getWebResponse().getStatusCode());
			return webParam;
		} catch (Exception e) {
			tracer.addTag("InsuranceZhongshanParser.getMedicalcare---ERROR:" , taskInsurance.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 医疗待遇查询解析
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private InsuranceZhongShanMedicalCare parserMedicalcare(String html, TaskInsurance taskInsurance) {
		Document documentHtml = Jsoup.parse(html);
		Elements baseInfo  = documentHtml.getElementsByClass("tablediv normal_table");
		String ybgzzje = getNextLabelByKeyword(baseInfo,"医保个帐总金额","td");
		String knye =getNextLabelByKeyword(baseInfo,"卡内余额","td");
		String wqcje=getNextLabelByKeyword(baseInfo,"未圈存金额","td");
		String bndtcxe =getNextLabelByKeyword(baseInfo,"本年度统筹限额","td");
		String bndtcye = getNextLabelByKeyword(baseInfo,"本年度统筹余额","td");
		String yxdsqwsz = getNextLabelByKeyword(baseInfo,"已选定社区卫生站","td");
		Elements trs = baseInfo.select("tr");
		
		
		Elements tds9 =trs.get(9).select("td");
		Elements tds11=trs.get(11).select("td");
		Elements tds13=trs.get(13).select("td");
		Elements tds15=trs.get(15).select("td");
		
		InsuranceZhongShanMedicalCare medical = new InsuranceZhongShanMedicalCare();
		medical.setAccountTotal(ybgzzje);
		medical.setCardBalance(knye);
		medical.setNoQuancunAmount(wqcje);
		medical.setOverallBalanceCommunity(bndtcxe);
		medical.setPlanningBalanceCommunity(bndtcye);
		medical.setCommunityHealthStation(yxdsqwsz);
		medical.setOverallBalanceBasic(tds9.get(0).text());
		medical.setPlanningBalanceBasic(tds9.get(1).text());
		medical.setOverallBalanceSupplement(tds11.get(0).text());
		medical.setEspeciallyOverallBalance(tds11.get(1).text());
		medical.setPlanningBalanceSupplement(tds13.get(0).text());
		medical.setEspeciallyPlanningBalance(tds13.get(1).text());
		medical.setHospitalizationTotal(tds15.get(0).text());
		medical.setHospitalizationEspeciallyTotal(tds15.get(1).text());
		medical.setTaskid(taskInsurance.getTaskid());
		return medical;
	}
	
	
	
	public static String getNextLabelByKeyword(Elements baseInfo, String keyword, String tag){ 
		Elements es = baseInfo.select(tag+":contains("+keyword+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.text(); 
			} 
		} 
		return null; 
	}
	/**
	 * 解析社保总信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private WebParam<InsuranceZhongShanPensionAccount> parserSummary(String html, TaskInsurance taskInsurance) {
		WebParam webParam = new WebParam();
		Document doc = Jsoup.parse(html);
		
		String xm = getByText(doc, "姓名：");
		xm = xm.substring(xm.indexOf("姓名：")+3);
		
		String sfz = getByText(doc, "身份证号码：");
		sfz = sfz.substring(sfz.indexOf("身份证号码：")+6);
		
		String jbjfys = getByText(doc, "基本养老保险缴费月数：");
		jbjfys = jbjfys.substring(jbjfys.indexOf("基本养老保险缴费月数：")+11);
		
		String ncjfys = getByText(doc, "农村养老保险缴费月数：");
		ncjfys = ncjfys.substring(ncjfys.indexOf("农村养老保险缴费月数：")+11);
		
		String jmjfys = getByText(doc, "居民养老保险缴费月数：");
		jmjfys = jmjfys.substring(jmjfys.indexOf("居民养老保险缴费月数：")+11);
		
		Elements link1 = doc.getElementsByTag("td");
		String dwhrgz = link1.get(1).text();
		String grjn = link1.get(2).text();
		String hj = link1.get(3).text();
		
		InsuranceZhongShanPensionAccount  account = new InsuranceZhongShanPensionAccount();
		account.setName(xm);
		account.setCardId(sfz);
		account.setBasicEndowmentMonth(jbjfys);
		account.setCountrysideEndowmentMonth(ncjfys);
		account.setResidentEndowmentMonth(jmjfys);
		account.setUnitDelimit(dwhrgz);
		account.setPersonalPayment(grjn);
		account.setTotal(hj);
		account.setTaskid(taskInsurance.getTaskid());
		webParam.setInsuranceZhongShanPensionAccount(account);
		webParam.setHtml(html);
		return webParam;
	}

	
	public String getByText(Document doc, String keyword){
		Elements es = doc.select("th:contains("+keyword+")");
		if(null != es && es.size() > 0){
			String text = es.first().text();
			return text;
		}
		return null;
	}


	

	

}
