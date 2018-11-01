package app.service;

import java.net.URL;
import java.util.ArrayList;
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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xuancheng.HousingxuanchengBase;
import com.microservice.dao.entity.crawler.housing.xuancheng.HousingxuanchengHtml;
import com.microservice.dao.entity.crawler.housing.xuancheng.HousingxuanchengPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.xuancheng.HousingxuanchengBaseRepository;
import com.microservice.dao.repository.crawler.housing.xuancheng.HousingxuanchengHtmlRepository;
import com.microservice.dao.repository.crawler.housing.xuancheng.HousingxuanchengPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xuancheng")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xuancheng")
public class HousingFundxuanchengService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundxuanchengService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingxuanchengHtmlRepository housingxuanchengHtmlRepository;
	@Autowired
	public HousingxuanchengBaseRepository housingxuanchengBaseRepository;
	@Autowired
	public HousingxuanchengPayRepository housingxuanchengPayRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	public HousingBasicService housingBasicService;

	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url1 = "https://sso.ahzwfw.gov.cn/uccp-server/login?service=http%3a%2f%2fwww.xcgjj.cn%3a8081%2fSzCasLogin%2ftSingle.aspx&appCode=8bda7b91ebd640f38f8a3bb86b26a36c";
			// 调用下面的getHtml方法
			WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
			HtmlPage html1 = webClient.getPage(webRequest1);
			String asXml = html1.asXml();
			Document doc = Jsoup.parse(asXml);
			Element elementById = doc.getElementById("legpsdFm");
			String lt = elementById.getElementsByAttributeValue("name", "lt").val();
			System.out.println(lt);
			String execution = elementById.getElementsByAttributeValue("name", "execution").val();
			System.out.println(execution);
			String _eventId = elementById.getElementsByAttributeValue("name", "_eventId").val();
			System.out.println(_eventId);
			String platform = elementById.getElementsByAttributeValue("name", "platform").val();
			System.out.println(platform);
			String loginType = elementById.getElementsByAttributeValue("name", "loginType").val();
			System.out.println(loginType);
			String url = "https://sso.ahzwfw.gov.cn/uccp-server/login?service=http%3a%2f%2fwww.xcgjj.cn%3a8081%2fSzCasLogin%2ftSingle.aspx&appCode=8bda7b91ebd640f38f8a3bb86b26a36c";
			// 调用下面的getHtml方法
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest.getRequestParameters().add(new NameValuePair("lt", lt));
			webRequest.getRequestParameters().add(new NameValuePair("execution", execution));
			webRequest.getRequestParameters().add(new NameValuePair("_eventId", _eventId));
			webRequest.getRequestParameters().add(new NameValuePair("platform", platform));
			webRequest.getRequestParameters().add(new NameValuePair("loginType", loginType));
			webRequest.getRequestParameters().add(new NameValuePair("credentialType", "PASSWORD"));
			webRequest.getRequestParameters().add(new NameValuePair("userType", "0"));
			webRequest.getRequestParameters().add(new NameValuePair("username", "342524198712140824"));
			webRequest.getRequestParameters().add(new NameValuePair("password", "huanghuan1987"));
			webRequest.getRequestParameters().add(new NameValuePair("random", ""));
			Page html = webClient.getPage(webRequest);
			String contentAsString2 = html.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);

			String[] split = contentAsString2.split("&sfzhm=");
			String[] split2 = split[1].split("\"");
			String rucan = split2[0].trim();
			System.out.println(rucan);

			//入參需要存进数据库
			
			if (contentAsString2.contains("images/main_b01")) {
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
			System.out.println("登陆失败！异常错误！");
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getError_code());
			taskHousingRepository.save(taskHousing);
		}
		return taskHousing;
	}

	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cook = taskHousing.getCookies();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cook);
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		//从数据库中取出来，登录时候存进去的
		String rucan = "";
		
		try {
			String urltemp = "http://www.xcgjj.cn:8081/gjjcx/cx_fromsfzhm_sz.aspx?cx=20180319&sfzhm=" + rucan
					+ "";
			WebRequest requestSettings = new WebRequest(new URL(urltemp), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);
			String contentAsString = page.getWebResponse().getContentAsString();
			System.out.println("前提页面响应的内容是：" + contentAsString);

			HousingxuanchengHtml housingxuanchengHtml = new HousingxuanchengHtml();
			housingxuanchengHtml.setHtml(contentAsString + "");
			housingxuanchengHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingxuanchengHtml.setType("基本信息和流水信息");
			housingxuanchengHtml.setUrl(urltemp);
			housingxuanchengHtmlRepository.save(housingxuanchengHtml);

			if (contentAsString.contains("公积金在线查询结果")) {
				System.out.println("基本信息和流水信息获取成功！");
				Document doc4 = Jsoup.parse(contentAsString);
				// 单位代码
				String dwdm = doc4.getElementById("dwzhLabel").text();
				System.out.println("单位代码:" + dwdm);
				// 个人代码
				String grdm = doc4.getElementById("grzhLabel").text();
				System.out.println("个人代码:" + grdm);
				// 单位名称
				String dwmc = doc4.getElementById("dwmcLabel").text();
				System.out.println("单位名称:" + dwmc);
				// 个人姓名
				String grxm = doc4.getElementById("grxmLabel").text();
				System.out.println("个人姓名:" + grxm);
				// 单位银行账号
				String dwyhzh = doc4.getElementById("dwyhzhLabel").text();
				System.out.println("单位银行账号:" + dwyhzh);
				// 个人银行账号
				String gryhzh = doc4.getElementById("SGrYhZh").text();
				System.out.println("个人银行账号:" + gryhzh);
				// 身份证号
				String sfzh = doc4.getElementById("sfzhmLabel").text();
				System.out.println("身份证号:" + sfzh);
				// 余额
				String ye = doc4.getElementById("ljyeLabel").text();
				System.out.println("余额:" + ye);
				// 起缴月份
				String qjyf = doc4.getElementById("qjrqLabel").text();
				System.out.println("起缴月份:" + qjyf);
				// 缴至月份
				String jzyf = doc4.getElementById("DtJzrqLabel").text();
				System.out.println("缴至月份:" + jzyf);
				// 月应缴额
				String yyje = doc4.getElementById("dc_yjjeLabel").text();
				System.out.println("月应缴额:" + yyje);
				// 账户状态
				String zhzt = doc4.getElementById("IFcbjLabel").text();
				System.out.println("账户状态:" + zhzt);
				// 单位缴存比例
				String dwjcbl = doc4.getElementById("DwblLabel").text();
				System.out.println("单位缴存比例:" + dwjcbl);
				// 个人缴存比例
				String grjcbl = doc4.getElementById("GrblLabel").text();
				System.out.println("个人缴存比例:" + grjcbl);
				// 委托银行
				String wtyh = doc4.getElementById("SYhTypeLabel").text();
				System.out.println("委托银行:" + wtyh);

				HousingxuanchengBase housingxuanchengBase = new HousingxuanchengBase();
				housingxuanchengBase.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingxuanchengBase.setWtyh(wtyh);
				housingxuanchengBase.setGrjcbl(grjcbl);
				housingxuanchengBase.setDwjcbl(dwjcbl);
				housingxuanchengBase.setZhzt(zhzt);
				housingxuanchengBase.setYyje(yyje);
				housingxuanchengBase.setJzyf(jzyf);
				housingxuanchengBase.setQjyf(qjyf);
				housingxuanchengBase.setYe(ye);
				housingxuanchengBase.setSfzh(sfzh);
				housingxuanchengBase.setGryhzh(gryhzh);
				housingxuanchengBase.setDwyhzh(dwyhzh);
				housingxuanchengBase.setGrxm(grxm);
				housingxuanchengBase.setDwmc(dwmc);
				housingxuanchengBase.setGrdm(grdm);
				housingxuanchengBase.setDwdm(dwdm);
				housingxuanchengBaseRepository.save(housingxuanchengBase);
				taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200,
						taskHousing.getTaskid());
				////////////////////////////////////// 流水信息////////////////////////////////////////
				Element elementById4 = doc4.getElementById("GridViewZm");
				Elements trs = elementById4.getElementsByTag("tr");
				for (int i = 0; i < trs.size(); i++) {
					Elements tds = trs.get(i).getElementsByTag("td");
					for (int j = 0; j < tds.size(); j += 5) {
						// 日期
						String rq = tds.get(j).text().trim();
						System.out.println("日期：" + rq);
						// 摘要
						String zy = tds.get(j + 1).text().trim();
						System.out.println("摘要：" + zy);
						// 借方金额
						String jfje = tds.get(j + 2).text().trim();
						System.out.println("借方金额：" + jfje);
						// 贷方金额
						String dfje = tds.get(j + 3).text().trim();
						System.out.println("贷方金额：" + dfje);
						// 金额
						String je = tds.get(j + 4).text().trim();
						System.out.println("金额：" + je);
						System.out.println("=====================");
						HousingxuanchengPay housingxuanchengPay = new HousingxuanchengPay();
						housingxuanchengPay.setTaskid(taskHousing.getTaskid());
						housingxuanchengPay.setRq(rq);
						housingxuanchengPay.setZy(zy);
						housingxuanchengPay.setJfje(jfje);
						housingxuanchengPay.setDfje(dfje);
						housingxuanchengPay.setJe(je);
						housingxuanchengPayRepository.save(housingxuanchengPay);
					}
				}
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());

			} else {
				System.out.println("基本信息和流水信息获取失败！");
			}

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