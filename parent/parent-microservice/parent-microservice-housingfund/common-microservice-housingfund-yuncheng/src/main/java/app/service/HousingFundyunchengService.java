package app.service;

import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yuncheng.HousingyunchengBase;
import com.microservice.dao.entity.crawler.housing.yuncheng.HousingyunchengHtml;
import com.microservice.dao.entity.crawler.housing.yuncheng.HousingyunchengPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.yuncheng.HousingyunchengBaseRepository;
import com.microservice.dao.repository.crawler.housing.yuncheng.HousingyunchengHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yuncheng.HousingyunchengPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yuncheng")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yuncheng")
public class HousingFundyunchengService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundyunchengService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingyunchengHtmlRepository housingyunchengHtmlRepository;
	@Autowired
	public HousingyunchengBaseRepository housingyunchengBaseRepository;
	@Autowired
	public HousingyunchengPayRepository housingyunchengPayRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	public HousingBasicService housingBasicService;

	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://www.sxycgjj.gov.cn/login.jspx";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage html = webClient.getPage(webRequest);

			// 用户名
			HtmlTextInput IDcard = (HtmlTextInput) html.getFirstByXPath("//input[@id='username']");
			// 密码
			HtmlPasswordInput password = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='password']");
			// 验证码输入框
			HtmlTextInput captcha = (HtmlTextInput) html.getFirstByXPath("//input[@id='captcha']");
			// 图片
			HtmlImage randImage = (HtmlImage) html.getFirstByXPath("//img[@id='captchaImg']");
			// 查询按钮
			HtmlSubmitInput button = (HtmlSubmitInput) html
					.getFirstByXPath("//*[@id=\"jvForm\"]/table[2]/tbody/tr/td[2]/div/table/tbody/tr[6]/td/input");

			String code = chaoJiYingOcrService.getVerifycode(randImage, "1006");

			IDcard.setText("zhanfengbo");
			password.setText("790623");
			captcha.setText(code);

			HtmlPage htmlpage = button.click();
			String contentAsString = htmlpage.getWebResponse().getContentAsString();
			System.out.println(contentAsString);

			if (contentAsString.contains("您现在正在浏览") && contentAsString.contains("退出登录")) {
				System.out.println("登陆成功！");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setCookies(cookies);
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
				taskHousingRepository.save(taskHousing);
			} else {
				System.out.println("登陆失败！异常错误！");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getError_code());
				taskHousingRepository.save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		// 基本信息和流水信息
		String jbxx = "http://www.sxycgjj.gov.cn/fundsearch/index.jhtml?locale=zh_CN";
		try {
			WebRequest requestSettings = new WebRequest(new URL(jbxx), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();
			System.out.println("基本信息和流水信息----" + contentAsString);
			HousingyunchengHtml housingyunchengHtml = new HousingyunchengHtml();
			housingyunchengHtml.setHtml(contentAsString + "");
			housingyunchengHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingyunchengHtml.setType("基本信息和流水信息");
			housingyunchengHtml.setUrl(jbxx);
			housingyunchengHtmlRepository.save(housingyunchengHtml);

			Document doc = Jsoup.parse(contentAsString);
			Elements dls = doc.getElementsByTag("dl");

			Element dl1 = dls.get(1);
			Elements dd0 = dl1.getElementsByTag("dd");
			// 姓名
			String xm0 = dd0.get(0).text();
			System.out.println("姓名：" + xm0);
			// 身份证号
			String sfzh0 = dd0.get(1).text();
			System.out.println("身份证号：" + sfzh0);
			// 工作单位
			String gzdw0 = dd0.get(2).text();
			System.out.println("工作单位：" + gzdw0);
			HousingyunchengBase housingyunchengBase0 = new HousingyunchengBase();
			housingyunchengBase0.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingyunchengBase0.setXm(xm0);
			housingyunchengBase0.setSfzh(sfzh0);
			housingyunchengBase0.setGzdw(gzdw0);
			housingyunchengBaseRepository.save(housingyunchengBase0);

			Element dl2 = dls.get(2);
			Elements dd = dl2.getElementsByTag("dd");
			// 姓名
			String xm = dd.get(0).text();
			System.out.println("姓名：" + xm);
			// 身份证号
			String sfzh = dd.get(1).text();
			System.out.println("身份证号：" + sfzh);
			// 工作单位
			String gzdw = dd.get(2).text();
			System.out.println("工作单位：" + gzdw);

			HousingyunchengBase housingyunchengBase = new HousingyunchengBase();
			housingyunchengBase.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingyunchengBase.setXm(xm);
			housingyunchengBase.setSfzh(sfzh);
			housingyunchengBase.setGzdw(gzdw);
			housingyunchengBaseRepository.save(housingyunchengBase);

			taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());

			/////////////////////////// 流水信息//////////////////////////////
			Elements elementsByClass = doc.getElementsByClass("fund-search-result");
			for (int k = 0; k < elementsByClass.size(); k++) {
				Elements trs = elementsByClass.get(k).getElementsByTag("tr");
				for (int i = 0; i < trs.size() - 1; i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					for (int j = 0; j < tds.size(); j += 5) {
						// 日期
						String rq = tds.get(j).text().trim();
						System.out.println("日期：" + rq);
						// 摘要
						String zy = tds.get(j + 1).text().trim();
						System.out.println("摘要：" + zy);
						// 借方
						String jf = tds.get(j + 2).text().trim();
						System.out.println("借方：" + jf);
						// 贷方
						String df = tds.get(j + 3).text().trim();
						System.out.println("贷方：" + df);
						// 余额
						String ye = tds.get(j + 4).text().trim();
						System.out.println("余额：" + ye);
						System.out.println("=================");
						HousingyunchengPay housingyunchengPay = new HousingyunchengPay();
						housingyunchengPay.setTaskid(taskHousing.getTaskid());
						housingyunchengPay.setRq(rq);
						housingyunchengPay.setZy(zy);
						housingyunchengPay.setJf(jf);
						housingyunchengPay.setDf(df);
						housingyunchengPay.setYe(ye);
						housingyunchengPayRepository.save(housingyunchengPay);
					}
				}
			}
			taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());

			// 更新最后的状态
			taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}