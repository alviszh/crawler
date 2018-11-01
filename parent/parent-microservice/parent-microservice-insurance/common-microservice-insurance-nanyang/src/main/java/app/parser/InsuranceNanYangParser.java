package app.parser;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangEndowment;
import com.microservice.dao.entity.crawler.insurance.nanyang.InsuranceNanYangUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
@Component
public class InsuranceNanYangParser {
	@Value("${zonecode}")    //区域码
	public String zonecode;
	@Value("${loginhost}")
	public String loginhost;
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
//	@Autowired
//	private TaskInsuranceRepository taskInsuranceRepository;
	
	
	public WebParam<InsuranceNanYangUserInfo> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://www.hnylbx.com:33002/siq/web/szpersonHome_seeInsuper.action?insuperType=3";
		
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		WebParam<InsuranceNanYangUserInfo> webParam = new WebParam<InsuranceNanYangUserInfo>();
		if(page.getWebResponse().getContentAsString().contains("个人信息"))
		{
			InsuranceNanYangUserInfo i = new InsuranceNanYangUserInfo();
//			Element elementsByClass = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(1).getElementsByTag("tbody").get(0).getElementsByTag("td").get(1);
//			<td width="180" align="left"> 用户名：410182199311021448 </td>
//			System.out.println(elementsByClass);
			Element elementsByClass1 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(1);
			if(elementsByClass1.getElementsByTag("td").get(0).text().contains("单位编号"))
			{
				i.setCompanyNum(elementsByClass1.getElementsByTag("td").get(0).text().substring(5));
			}
			else
			{
				i.setCompanyNum(elementsByClass1.getElementsByTag("td").get(0).text().substring(9));
			}
			i.setCompany(elementsByClass1.getElementsByTag("td").get(1).text().substring(5));
//			<td height="41" colspan="2" bgcolor="#ffffff" align="left" class="left5"> 单位编号:410499003579 </td> 
//			<td height="41" colspan="4" bgcolor="#ffffff" align="left" class="left5"> 单位名称:河南成就人力资源有限公司 </td>
			Elements elementsByClass2 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0).getElementsByTag("td");
			i.setName(elementsByClass2.get(3).text());
			i.setSex(elementsByClass2.get(5).text());
			Elements elementsByClass3 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td");
			i.setCardStatus(elementsByClass3.get(1).text());
			i.setIdNum(elementsByClass3.get(3).text());
			i.setNational(elementsByClass3.get(5).text());
			Elements elementsByClass4 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
			i.setBirthday(elementsByClass4.get(1).text());
			i.setJoinDate(elementsByClass4.get(3).text());
			i.setPersonalDate(elementsByClass4.get(5).text());
			Elements elementsByClass5 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(3).getElementsByTag("td");
			i.setSetDate(elementsByClass5.get(1).text());
			i.setStatus(elementsByClass5.get(3).text());
			i.setPayStatus(elementsByClass5.get(5).text());
			Elements elementsByClass6 = doc.getElementsByClass("white1000").get(0).getElementsByTag("tr").get(2).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(4).getElementsByTag("td");
			i.setBaseMoney(elementsByClass6.get(1).text());
			i.setTaskid(taskInsurance.getTaskid());
			System.out.println(i);
			webParam.setHtml(page.getWebResponse().getContentAsString());
			webParam.setInsuranceNanYangUserInfo(i);
			webParam.setUrl(url);
			return webParam;
		}
		return null;
	}
	
	public WebClient addcookie(WebClient webclient, TaskInsurance taskInsurance) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}
		return webclient;
	}

	
	//养老
	public WebParam<InsuranceNanYangEndowment> getEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) throws Exception {
		String url="http://www.hnylbx.com:33002/siq/web/szpersonHome_seeInsuper.action?insuperType=3";
		WebClient newWebClient = WebCrawler.getInstance().getNewWebClient();
		WebClient webClient = addcookie(newWebClient, taskInsurance);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String json = page.getWebResponse().getContentAsString();
		Document doc = Jsoup.parse(json);
		Elements elementsByClass = doc.getElementsByClass("content1");
		Element element2 = elementsByClass.get(1);
		Elements elementsByTag = element2.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(1).getElementsByTag("td");
		Elements elementsByTag1 = element2.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
		WebParam<InsuranceNanYangEndowment> webParam = new WebParam<InsuranceNanYangEndowment>();
		InsuranceNanYangEndowment j = null;
		List<InsuranceNanYangEndowment> list = new ArrayList<InsuranceNanYangEndowment>();
		for (int i = 0; i < elementsByTag.size()/2; i++) {
			System.out.println(elementsByTag.get(i).text().length());
			System.out.println(elementsByTag1.get(i).text().length());
			j = new InsuranceNanYangEndowment();
			if(elementsByTag.get(i).text().length()>4)
			{
				if(elementsByTag.get(i).text().contains("年"))
				{
					j.setYear(elementsByTag.get(i).text().substring(0, 4));
					
				}
				if(elementsByTag1.get(i).text().length()>5)
				{
					j.setBase(elementsByTag1.get(i).text().substring(0, 4));
				}
				j.setTaskid(taskInsurance.getTaskid());
				list.add(j);
			}
		}
		webParam.setList(list);
		webParam.setHtml(json);
		webParam.setUrl(url);
		return webParam;
	}

	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance)throws Exception {
			//先请求链接登录链接，获取图片验证码对象
			String url="http://"+loginhost+"/siq/indexsz.jsp?zoneCode="+zonecode.trim()+"";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			HtmlPage hpage = webClient.getPage(webRequest);
			HtmlImage image = hpage.getFirstByXPath("//img[@id='perVaidImg']");   //个人登录方式的图片验证码
			String code = chaoJiYingOcrService.getVerifycode(image, "1902");
			//需要先校验图片验证码，图片验证码的校验链接和登录账号密码的验证链接不一样
			url="http://"+loginhost+"/siq/pages/security/result.jsp?s=0.9832108861612767";
			String requestBody="fieldId=perVaidImgText&fieldValue="+code+"";
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webClient.getOptions().setJavaScriptEnabled(false);
			Page page=webClient.getPage(webRequest);
			WebParam webParam = new WebParam();
			if(page!=null){
				String html=page.getWebResponse().getContentAsString();
				System.out.println("获取的验证图片验证码的结果是："+html);
				if(html.contains("验证码错误")){
					System.out.println("图片验证码识别错误");
					webParam.setHtml(html);
//					if (captchaErrorCount<=2){
//						tracer.addTag("action.login.captchaErrorCount","解析图片验证码失败"+captchaErrorCount+"次，重新执行登录方法");
//						commonLogin(insuranceRequestParameters, taskInsurance);
//					} else {
//						captchaErrorCount=0;
//						insuranceService.changeLoginStatusCaptError(taskInsurance);
//						return taskInsurance;
//					}
				}else{
					url="http://"+loginhost+"/siq/web/szloginWeb.action";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					requestBody=""
							+ "user.userCode="+insuranceRequestParameters.getUsername().trim()+""
							+ "&user.psw="+insuranceRequestParameters.getPassword().trim()+""
							+ "&user.zoneCode="+zonecode.trim()+""
							+ "&user.userType=3";
					webRequest.setRequestBody(requestBody);
					page = webClient.getPage(webRequest);    
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
						System.out.println("校验登录信息结果页面html为："+html);
						tracer.addTag("校验登录信息结果页面html为", html);
						webParam.setHtml(html);
						webParam.setWebClient(webClient);
					}
				}
			}
			return webParam;
	   }

}
