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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shiyan.InsuranceShiYanMedical;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanMedicalCare;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionDetail;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;

@Component
public class InsuranceFuShiYanParser {

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

	
	public String getByText(Document doc, String keyword){
		Elements es = doc.select("th:contains("+keyword+")");
		if(null != es && es.size() > 0){
			String text = es.first().text();
			return text;
		}
		return null;
	}

	public WebParam<InsuranceShiYanMedical> crawler(InsuranceRequestParameters insuranceRequestParameters)throws Exception  {
		String url = "http://219.138.76.215/socs/index.jsp";
		
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		int status = page.getWebResponse().getStatusCode();
		HtmlTextInput userCard = (HtmlTextInput)page.getFirstByXPath("//input[@id='userCard']");
		HtmlTextInput userNo = page.getFirstByXPath("//input[@id='userNo']");
		userCard.setText(insuranceRequestParameters.getUserIDNum());
		userNo.setText(insuranceRequestParameters.getUsername());
		HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@onclick='loginClick();']");
		HtmlPage searchPage = button.click();
			
			String html =searchPage.getWebResponse().getContentAsString(); 
			System.out.println(html);
			
			if(html.contains("医保个人账户基本信息")){
				InsuranceShiYanMedical medical = parserAccount(html,insuranceRequestParameters);
				tracer.addTag("InsuranceFuZhou3Parser.crawler 账户信息" + insuranceRequestParameters.getTaskId(),
						"<xmp>" + html + "</xmp>");
				if(null!=medical){
					webParam.setInsuranceShiYanMedical(medical);
					webParam.setHtml(html);
					webParam.setUrl(url);
					return webParam;
				}
				return null;
			}
		return null;
	}

	private InsuranceShiYanMedical parserAccount(String html,InsuranceRequestParameters insuranceRequestParameters) {
		WebParam webParam = new WebParam();
		Document doc = Jsoup.parse(html);
		String xm = getNextLabelByKeyword(doc,"姓名","td");
		String sfzh = getNextLabelByKeyword(doc,"（身份证号）","td");
		String yybkh = getNextLabelByKeyword(doc,"原医保卡号","td");
		String ndjzye = getNextLabelByKeyword(doc,"年度年终结转余额","td");
		String zhye =getNextLabelByKeyword(doc,"本周一账户余额","td");
		
		InsuranceShiYanMedical medical = new InsuranceShiYanMedical();
		medical.setName(xm);
		medical.setNum(sfzh);
		medical.setMedicalCardNum(yybkh);
		medical.setYearEndKnotsBalance(ndjzye);
		medical.setThisWeekAccountBalance(zhye);
		medical.setTaskid(insuranceRequestParameters.getTaskId());
		return medical;
		
	}


public static String getNextLabelByKeyword(Element document, String keyword, String tag){ 
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
