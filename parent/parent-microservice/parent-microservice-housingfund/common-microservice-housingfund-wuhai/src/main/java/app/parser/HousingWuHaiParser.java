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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuhai.HousingWuHaiUserinfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingWuHaiParser {
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	protected TaskHousingRepository taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("parser.login.parser.taskid", taskHousing.getTaskid());
		taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		String loginUrl = "http://www.whgjj.net/";
		tracer.addTag("parser.login.parser.url", loginUrl);
		
		WebParam webParam = new WebParam();
		webParam.setUrl(loginUrl);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		tracer.addTag("parser.login.parser.loginPage", "<xmp>"+loginPage.asXml()+"</xmp>");
		if(loginPage.getWebResponse().getStatusCode() == 200){
			HtmlTextInput nameInput =(HtmlTextInput) loginPage.getFirstByXPath("//input[@name='xm']");
			HtmlTextInput idInput =(HtmlTextInput) loginPage.getFirstByXPath("//input[@name='sfzid']");
			HtmlSubmitInput submit =(HtmlSubmitInput) loginPage.getFirstByXPath("//input[@type='submit']");
			if(null != submit){
				nameInput.setText(messageLoginForHousing.getUsername());
				idInput.setText(messageLoginForHousing.getNum());
				HtmlPage click = submit.click();
				tracer.addTag("parser.login.parser.infoPage", "<xmp>"+click.asXml()+"</xmp>");
				if(click.asXml().contains("本年缴存")){
					webParam.setHtml(click.asXml());
				}
			}
		}
		return webParam;
	}

	public WebParam crawler(String html, TaskHousing taskHousing) throws Exception{
		WebParam webParam = new WebParam();
		
		Document document = Jsoup.parse(html);
		String name = getNextLabelByKeyword(document, "td", "姓   名");
		String idNum = getNextLabelByKeyword(document, "td", "身份证号");
		String company = getNextLabelByKeyword(document, "td", "工作单位");
		String lastYearBalance = getNextLabelByKeyword(document, "td", "上年结转");
		String payDate = getNextLabelByKeyword(document, "td", "缴至年月");
		String nowPay = getNextLabelByKeyword(document, "td", "本年缴存");
		String nowOut = getNextLabelByKeyword(document, "td", "本年提取");
		String nowInterest = getNextLabelByKeyword(document, "td", "本年结息");
		String balance = getNextLabelByKeyword(document, "td", "余   额");
		
		List<HousingWuHaiUserinfo> lists = new ArrayList<HousingWuHaiUserinfo>();
		HousingWuHaiUserinfo userinfo = new HousingWuHaiUserinfo();
		userinfo.setTaskid(taskHousing.getTaskid());
		userinfo.setName(name);
		userinfo.setIdNum(idNum);
		userinfo.setCompany(company);
		userinfo.setLastYearBalance(lastYearBalance);
		userinfo.setPayDate(payDate);
		userinfo.setNowPay(nowPay);
		userinfo.setNowOut(nowOut);
		userinfo.setNowInterest(nowInterest);
		userinfo.setBalance(balance);
		lists.add(userinfo);
		webParam.setList(lists);
		
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
