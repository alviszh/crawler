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
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.haikou.HousingHaiKouPay;
import com.microservice.dao.entity.crawler.housing.haikou.HousingHaiKouUserinfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingHaiKouParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	public WebParam sendSMS(MessageLoginForHousing messageLoginForHousing) throws Exception{
		tracer.addTag("housing.haikou.parser.sendSMS.taskid", messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		String url = "http://bsdt.hngjj.net/hnwsyyt/platform/workflow/sendMessage.jsp?uuid=1522378679610&task=send&trancode=149364&type=socket&message=%3Cpubaccnum%3E"+messageLoginForHousing.getNum()+"%3C%2F%3E%3Clogintype%3E1%3C%2F%3E%3Cflag%3E1%3C%2F%3E%3Csmstmplcode%3E%3C%2F%3E";
		tracer.addTag("housing.haikou.parser.sendSMS.url", url);
		webParam.setUrl(url);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		tracer.addTag("housing.haikou.parser.sendSMS.page", page.getWebResponse().getContentAsString());
		webParam.setPage(page);
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
		
		JsonObject data = obj.get("data").getAsJsonObject();
		String RspCode = data.get("RspCode").getAsString();
		if(!RspCode.equals("000000")){	//短信发送不成功
			String message = obj.get("message").getAsString();
			tracer.addTag("housing.haikou.parser.sendSMS.fail.message", message);
			webParam.setHtml(message);
		}
		webParam.setWebClient(webClient);
		return webParam;
	}
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("housing.haikou.parser.login.taskid", messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		String url = "http://bsdt.hngjj.net/hnwsyyt/indexPerson.jsp";
		tracer.addTag("housing.haikou.parser.login.url", url);
		webParam.setUrl(url);
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage page = webClient.getPage(webRequest);
		tracer.addTag("housing.haikou.parser.login.page", "<xmp>"+page.asXml()+"</xmp>");
		webParam.setHtmlPage(page);
		if(page.getWebResponse().getStatusCode() == 200){
			HtmlTextInput certinum = page.getFirstByXPath("//input[@id='certinum']");
			HtmlTextInput accname = page.getFirstByXPath("//input[@id='accname']");
			HtmlPasswordInput perpwd = page.getFirstByXPath("//input[@name='perpwd']");
			HtmlTextInput pervcode = page.getFirstByXPath("//input[@name='pervcode']");
			HtmlButton submit = page.getFirstByXPath("//button[@type='submit']");
			
			certinum.setText(messageLoginForHousing.getNum());
			accname.setText(messageLoginForHousing.getUsername());
			perpwd.setText(messageLoginForHousing.getPassword());
			pervcode.setText(messageLoginForHousing.getSms_code());
			HtmlPage loginedpage = submit.click();
			tracer.addTag("housing.haikou.parser.logined.page", "<xmp>"+loginedpage.asXml()+"</xmp>");
			webParam.setHtmlPage(loginedpage);
			
			if(loginedpage.getWebResponse().getStatusCode() == 200){
				if(loginedpage.asText().trim().equals("")){
					tracer.addTag("housing.haikou.parser.login.success", "登陆成功！");
					webParam.setWebClient(webClient);
				}else{
					HtmlSpan error = loginedpage.getFirstByXPath("//span[@class='error']");
					if(null != error && !error.asText().trim().contains("")){
						tracer.addTag("housing.haikou.parser.login.fail", error.asText());
						webParam.setHtml(error.asText());
					}else{
						tracer.addTag("housing.haikou.parser.login.fail", "网络异常，请您稍后重试！");
						webParam.setHtml("网络异常，请您稍后重试！");
					}
				}
			}
		}
		return webParam;
	}

	public WebParam getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("housing.haikou.parser.getUserInfo.taskid", messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		String url = "http://bsdt.hngjj.net/hnwsyyt/command.summer?uuid=1522392338833";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "bsdt.hngjj.net");
		webRequest.setAdditionalHeader("Origin", "http://bsdt.hngjj.net");
		webRequest.setAdditionalHeader("Referer", "http://bsdt.hngjj.net/hnwsyyt/init.summer?_PROCID=30000001");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String reqBody = "%24page=%2Fydpx%2F30000001%2F300001_01.ydpx&_LOANCOUNT=0&_ACCNUM=&_IS=-1234567&_PAGEID=step1&_LOGIP=&isSamePer=false&_PROCID=30000001&_SENDOPERID="+messageLoginForHousing.getNum()+"&temp_.itemid%5B5%5D=99&temp_.itemid%5B3%5D=03&_BRANCHKIND=0&_SENDDATE="+getDate(0)+"&temp_.itemid%5B1%5D=01&temp_._xh%5B4%5D=4&CURRENT_SYSTEM_DATE="+getDate(0)+"&temp_.itemval%5B4%5D=%E5%A4%96%E5%9B%BD%E4%BA%BA%E6%B0%B8%E4%B9%85%E5%B1%85%E7%95%99%E8%AF%81&_ISCROP=0&_TYPE=init&temp_._xh%5B2%5D=2&_PORCNAME=%E4%B8%AA%E4%BA%BA%E2%80%94%E2%80%94%E4%B8%AA%E4%BA%BA%E5%88%86%E6%88%B7%E6%9F%A5%E8%AF%A2&temp_.itemval%5B2%5D=%E5%86%9B%E5%AE%98%E8%AF%81&temp__rownum=5&_RW=w&_UNITACCNAME=null&_ACCNAME=%E6%9D%8E%E5%A8%9C&_DEPUTYIDCARDNUM="+messageLoginForHousing.getNum()+"&temp_.itemid%5B4%5D=04&_SENDTIME="+getDate(0)+"&temp_.itemid%5B2%5D=02&temp_._xh%5B5%5D=5&temp_.itemval%5B5%5D=%E5%85%B6%E4%BB%96&temp_._xh%5B3%5D=3&temp_.itemval%5B3%5D=%E6%8A%A4%E7%85%A7&_WITHKEY=0&temp_._xh%5B1%5D=1&_sjhm=&temp_.itemval%5B1%5D=%E8%BA%AB%E4%BB%BD%E8%AF%81&xingming="+getURLEncoderString(messageLoginForHousing.getUsername())+"&zjhm="+messageLoginForHousing.getNum()+"&zjlx=01&sjhm=&grzhye=&usebal=&xingbie=&DealSeq=0&summarycode=&dwzh=&grzh=&onym=";
		webRequest.setRequestBody(reqBody);
		webParam.setUrl(url+"?"+reqBody);
		tracer.addTag("housing.haikou.parser.getUserInfo.url", url+"?"+reqBody);
		Page page = webClient.getPage(webRequest);
		if(page.getWebResponse().getStatusCode() == 200 && page.getWebResponse().getContentAsString().contains("data")){
			tracer.addTag("housing.haikou.parser.getUserInfo.page", page.getWebResponse().getContentAsString());
			webParam.setHtml(page.getWebResponse().getContentAsString());
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
			int returnCode = obj.get("returnCode").getAsInt();
			if(returnCode == 0){
				JsonObject data = obj.get("data").getAsJsonObject();
				String name = data.get("xingming").getAsString();
				String idNum = data.get("zjhm").getAsString();
				String idType = data.get("temp_.itemval[1]").getAsString();
				String phoneNum = data.get("sjhm").getAsString();
				String balance = data.get("grzhye").getAsString();
				
				List<HousingHaiKouUserinfo> userinfos = new ArrayList<HousingHaiKouUserinfo>();
				HousingHaiKouUserinfo userinfo = new HousingHaiKouUserinfo();
				userinfo.setTaskid(taskHousing.getTaskid());
				userinfo.setName(name);
				userinfo.setIdNum(idNum);
				userinfo.setIdType(idType);
				userinfo.setPhoneNum(phoneNum);
				userinfo.setBalance(balance);
				userinfos.add(userinfo);
				webParam.setList(userinfos);
			}
		}
		return webParam;
	}
	
	public WebParam getPayInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("housing.haikou.parser.getPayInfo.taskid", messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		String url = "http://bsdt.hngjj.net/hnwsyyt/init.summer?_PROCID=30000007";
		webParam.setUrl(url);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "bsdt.hngjj.net");
		webRequest.setAdditionalHeader("Origin", "http://bsdt.hngjj.net");
		webRequest.setAdditionalHeader("Referer", "http://bsdt.hngjj.net/hnwsyyt/init.summer?_PROCID=30000007");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		tracer.addTag("housing.haikou.parser.getPayInfo.url", url);
		HtmlPage page = webClient.getPage(webRequest);
		
		HtmlTextInput begdate = page.getFirstByXPath("//input[@id='begdate']");
		HtmlButton query = page.getFirstByXPath("//button[@id='b_query']");
		
		begdate.setText("2017-05-08");
		HtmlPage payInfoPage = query.click();
		webParam.setHtmlPage(payInfoPage);
		tracer.addTag("housing.haikou.parser.getPayInfo.payInfoPage", url);
		List<HousingHaiKouPay> pays = new ArrayList<HousingHaiKouPay>();
		
		pays = getPageNext(payInfoPage, pays, taskHousing.getTaskid(), 1);
		webParam.setList(pays);
		return webParam;
	}
	
	public List<HousingHaiKouPay> getPageNext(HtmlPage page, List<HousingHaiKouPay> pays, String taskid, int j) throws Exception{
		tracer.addTag("housing.haikou.parser.getPayInfo."+j+".taskid", taskid);
		tracer.addTag("housing.haikou.parser.getPayInfo."+j+".page", page.asXml());
		HtmlButton nextButton = page.getFirstByXPath("//button[@class='next ui-button ui-widget ui-state-default ui-button-text-icon-secondary']");
		
		Document document = Jsoup.parse(page.asXml());
		Element datalist1 = document.getElementById("datalist1");
		Elements trs = datalist1.select("tr");
		if(trs.size() > 1){
			for (int i = 1; i < trs.size(); i++) {
				Element tr = trs.get(i);
				Elements tds = tr.select("td");
				
				HousingHaiKouPay pay = new HousingHaiKouPay();
				pay.setTaskid(taskid);
				pay.setPayDate(tds.get(1).text());
				pay.setSerialNum(tds.get(2).text());
				pay.setIdNum(tds.get(3).text());
				pay.setName(tds.get(4).text());
				pay.setCompanyName(tds.get(5).text());
				pay.setMark(tds.get(6).text());
				pay.setStartDate(tds.get(7).text());
				pay.setEndDate(tds.get(8).text());
				pay.setChangeType(tds.get(9).text());
				pay.setChangeMoney(tds.get(10).text());
				pay.setBalance(tds.get(11).text());
				pay.setCheckUnit(tds.get(12).text());
				pay.setCheckStaff(tds.get(13).text());
				pays.add(pay);
			}
		}
		
		if(null != nextButton && nextButton.getAttribute("aria-disabled").equals("false")){
			HtmlPage nextPage = nextButton.click();
			pays = getPageNext(nextPage, pays, taskid, j++);
		}
		
		return pays;
	}
	
	public String getDate(int i){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -i);
		Date d = c.getTime();
		String day = format.format(d);
		return day;
	}
	
	public String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
