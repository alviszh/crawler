package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.beijing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.beijing")
public class HousingBeiJingFutureService extends HousingBasicService implements ICrawlerLogin {

	public static final Logger log = LoggerFactory.getLogger(HousingBeiJingFutureService.class);

	@Autowired
	private LoginAndGetService loginAndGetService;

	@Autowired
	private HousingBeiJingUnitService housingBeiJingUnitService;

	private HtmlPage htmlpage = null;

	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "https://old.bjgjj.gov.cn/wsyw/wscx/gjjcx-login.jsp";
		tracer.addTag("parser.crawler.taskid", taskHousing.getTaskid());
		tracer.addTag("parser.crawler.auth", messageLoginForHousing.getNum());

		try {

			if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getIDNUM())) {
				// 身份证登录
				System.out.println("======s身份证登录===========");
				htmlpage = loginAndGetService.loginByIDNUM(webClient, url, messageLoginForHousing.getNum().trim(),
						messageLoginForHousing.getPassword().trim());
			} else if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getACCOUNT_NUM())) {
				// 根据个人登记号登录
				htmlpage = loginAndGetService.loginByACCOUNT_NUM(webClient, url, messageLoginForHousing.getNum().trim(),
						messageLoginForHousing.getPassword().trim());
			} else if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getOFFICER_CARD())) {
				// 根据军官证号登录
				htmlpage = loginAndGetService.loginByOFFICER_CARD(webClient, url,
						messageLoginForHousing.getNum().trim(), messageLoginForHousing.getPassword().trim());
			} else if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getPASSPORT())) {
				// 根据护照号登录
				htmlpage = loginAndGetService.loginByPASSPORT(webClient, url, messageLoginForHousing.getNum().trim(),
						messageLoginForHousing.getPassword().trim());
			} else if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getCO_BRANDED_CARD())) {
				// 根据联名卡号登录
				htmlpage = loginAndGetService.loginByCO_BRANDED_CARD(webClient, url,
						messageLoginForHousing.getNum().trim(), messageLoginForHousing.getPassword().trim());
			} else {
				System.out.println("==============未匹配登录方式 =================");
				taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
				taskHousing.setDescription("未匹配登录方式");

				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getError_code());
				taskHousing.setError_message("未匹配登录方式");
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
				return taskHousing;
			}
			System.out.println("=======登录结果url===========" + htmlpage.getUrl().toString());
			if (htmlpage.getUrl().toString().indexOf("https://old.bjgjj.gov.cn/wsyw/wscx/gjjcx-login.jsp") != -1) {
				taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
				
			
				Document doc = Jsoup.parse(htmlpage.toString());
				
				System.out.println(doc.toString());
				
				Element ele = doc.select("script").last();
				
				System.out.println("========"+ele);

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
				taskHousing.setDescription("登陆失败，请检查账号密码是否匹配");

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setError_message("登陆失败，请检查账号密码是否匹配");
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				tracer.addTag("parser.login.auth", htmlpage.getUrl().toString());
				save(taskHousing);
				return taskHousing;
			}

			if (htmlpage.getUrl().toString()
					.indexOf("https://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-logineoor.jsp?id=3") != -1) {
				taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
				taskHousing.setDescription("登陆失败，请检查账号密码是否匹配");

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setDescription("登陆失败，请检查账号密码是否匹配");
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				tracer.addTag("parser.login.auth", htmlpage.getUrl().toString());
				save(taskHousing);
				return taskHousing;
			}
			if (htmlpage.getUrl().toString().indexOf("https://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-logineoor.jsp") != -1) {
				taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
				taskHousing.setDescription("登陆失败，账号不存在");

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setDescription("登陆失败，账号不存在");
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				tracer.addTag("parser.login.auth", htmlpage.getUrl().toString());
				save(taskHousing);
				return taskHousing;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getDescription());

			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			tracer.addTag("parser.login.auth", e.getMessage());
			save(taskHousing);
			return taskHousing;
		}
		taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		if (htmlpage.getUrl().toString().indexOf("https://www.bjgjj.gov.cn/wsyw/wscx/gjjcx-choice.jsp") == -1) {

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getDescription());

			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
			return taskHousing;
		}
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

		taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
		taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
		taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
		save(taskHousing);

		return taskHousing;
	}


	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());

		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getError_code());
		taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
		save(taskHousing);
		List<String> list = housingBeiJingUnitService.getURLforUserInfo(htmlpage.asXml());
		System.out.println(list.toString());
		List<Future<String>> listfuture = new ArrayList<>();
		for (String url_result : list) {
			Future<String> future = housingBeiJingUnitService.getResult(messageLoginForHousing, taskHousing, url_result,
					htmlpage.getWebClient());

			listfuture.add(future);
		}

		boolean istrue = true;
		int i = 0;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			for (Future<String> future : listfuture) {
				if (i >= 1) {
					istrue = false;
				}
				if (future.isDone()) { // 判断是否执行完毕
					System.out.println("Result " + future.toString() + ":::" + future.isDone());
					i++;
					istrue = false;
					break;
				}
			}
		}

		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}