package app.service;

import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.liuan.HousingliuanBase;
import com.microservice.dao.entity.crawler.housing.liuan.HousingliuanHtml;
import com.microservice.dao.entity.crawler.housing.liuan.HousingliuanParameter;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.liuan.HousingliuanBaseRepository;
import com.microservice.dao.repository.crawler.housing.liuan.HousingliuanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.liuan.HousingliuanParameterRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.liuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.liuan")
public class HousingFundliuanService extends AbstractChaoJiYingHandler implements ICrawlerLogin {
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundliuanService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousingliuanHtmlRepository housingliuanHtmlRepository;
	@Autowired
	public HousingliuanBaseRepository housingliuanBaseRepository;
	@Autowired
	public HousingliuanParameterRepository housingliuanParameterRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	public HousingBasicService housingBasicService;
	WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	// 登录业务层
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			String url1 = "https://sso.ahzwfw.gov.cn/uccp-server/login";
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
			String url = "https://sso.ahzwfw.gov.cn/uccp-server/login";
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
//			webRequest.getRequestParameters().add(new NameValuePair("username", "shasha533"));
//			webRequest.getRequestParameters().add(new NameValuePair("password", "wss840825"));
			webRequest.getRequestParameters()
					.add(new NameValuePair("username", messageLoginForHousing.getNum().trim()));
			webRequest.getRequestParameters()
					.add(new NameValuePair("password", messageLoginForHousing.getPassword().trim()));
			webRequest.getRequestParameters().add(new NameValuePair("random", ""));
			Page html = webClient.getPage(webRequest);
			String contentAsString2 = html.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);
			if (contentAsString2.contains("<span>个人用户中心</span>")) {

				String[] split = contentAsString2.split("var USERID =");
				String[] split2 = split[1].split("\"");
				System.out.println(split2[1]);

				// 基本信息的入参存进数据库
				HousingliuanParameter housingliuanParameter = new HousingliuanParameter();
				housingliuanParameter.setTaskid(messageLoginForHousing.getTask_id());
				housingliuanParameter.setParameter(split2[1]);
				housingliuanParameterRepository.save(housingliuanParameter);

				System.out.println("登陆成功！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getError_code());
				taskHousingRepository.save(taskHousing);

			} else {
				System.out.println("登陆失败！异常错误！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getError_code());
				taskHousingRepository.save(taskHousing);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	// 爬取数据的业务层
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		// 从对应的表中查出基本信息的入参
		HousingliuanParameter housingliuanParameter = housingliuanParameterRepository
				.findByTaskid(messageLoginForHousing.getTask_id());
		String parameter = housingliuanParameter.getParameter();

		try {
			// 基本信息
			String jbxx11 = "https://sso.ahzwfw.gov.cn/uccp-user/nperson/info?userId=" + parameter
					+ "&_=S7RZBP4KEAO6477SJ7FG";
			WebRequest requestSettings11 = new WebRequest(new URL(jbxx11), HttpMethod.GET);
			Page page1 = webClient.getPage(requestSettings11);

			String contentAsString1 = page1.getWebResponse().getContentAsString();

			HousingliuanHtml housingliuanHtml = new HousingliuanHtml();
			housingliuanHtml.setHtml(contentAsString1);
			housingliuanHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housingliuanHtml.setType("基本信息");
			housingliuanHtml.setUrl(jbxx11);
			housingliuanHtmlRepository.save(housingliuanHtml);

			JSONObject object = JSONObject.fromObject(contentAsString1);
			String status = object.getString("status").trim();
			if ("true".equals(status)) {
				System.out.println("获取数据成功！");
				String data = object.getString("data").trim();
				JSONObject object2 = JSONObject.fromObject(data);
				// 用户名
				String yhm = object2.getString("account").trim();
				System.out.println("用户名：" + yhm);
				// 出生日期
				String csrq = object2.getString("birthDat").trim();
				System.out.println("出生日期：" + csrq);
				// 出生地
				String csd = object2.getString("birthAddress").trim();
				System.out.println("出生地：" + csd);
				// 居住地
				String jzd = object2.getString("liveAddress").trim();
				System.out.println("居住地：" + jzd);
				// 真实姓名
				String zsxm = object2.getString("name").trim();
				System.out.println("真实姓名：" + zsxm);
				// 证件类型
				String zjlx = object2.getString("credentTypeName").trim();
				System.out.println("证件类型：" + zjlx);
				// 证件号码
				String zjhm = object2.getString("credentNo").trim();
				System.out.println("证件号码：" + zjhm);
				// 性别
				String xb = object2.getString("sexName").trim();
				System.out.println("性别：" + xb);
				// 民族
				String mz = object2.getString("nationName").trim();
				System.out.println("民族：" + mz);
				HousingliuanBase housingliuanBase = new HousingliuanBase();
				housingliuanBase.setTaskid(messageLoginForHousing.getTask_id().trim());
				housingliuanBase.setMz(mz);
				housingliuanBase.setXb(xb);
				housingliuanBase.setZjhm(zjhm);
				housingliuanBase.setZjlx(zjlx);
				housingliuanBase.setZsxm(zsxm);
				housingliuanBase.setJzd(jzd);
				housingliuanBase.setCsd(csd);
				housingliuanBase.setCsrq(csrq);
				housingliuanBase.setYhm(yhm);
				housingliuanBaseRepository.save(housingliuanBase);

				taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());
				taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());
			} else {
				System.out.println("获取数据失败！");
			}
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