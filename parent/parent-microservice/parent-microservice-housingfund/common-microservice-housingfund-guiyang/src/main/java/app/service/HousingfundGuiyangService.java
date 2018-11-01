package app.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.guiyang.HousingGuiyangDetailAccount;
import com.microservice.dao.entity.crawler.housing.guiyang.HousingGuiyangUserinfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.guiyang.HousingGuiyangDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.guiyang.HousingGuiyangUserinfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundGuiyangParser;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import app.service.common.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.guiyang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.guiyang"})
public class HousingfundGuiyangService extends HousingBasicService implements ICrawlerLogin {
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingFundGuiyangParser housingFundGuiyangParser;
	@Autowired
	private HousingGuiyangUserinfoRepository housingGuiyangUserinfoRepository;
	@Autowired
	private HousingGuiyangDetailAccountRepository housingGuiyangDetailAccountRepository;

	/**
	 * @Des 登录
	 * @param messageLoginForHousing
	 * @param taskHousing
	 */
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String loginUrl = "http://zxcx.gygjj.gov.cn";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(loginUrl,webClient);
			HtmlImage image = page.getFirstByXPath("//img[@id='rand']");
			if(null != image){
				String code = chaoJiYingOcrService.getVerifycode(image, "1902");
				//登录认证
				String admingUrl = "http://zxcx.gygjj.gov.cn/checklogin.do?method=login&aaxmlrequest=true&logintype=person&spcode=&fromtype=null&"
						+ "IsCheckVerifyCode=On&IdCard="+messageLoginForHousing.getNum()+"&PassWord="+messageLoginForHousing.getPassword()+"&Ed_Confirmation="+code+"&";
				Page adminPage = LoginAndGetCommon.getHtml(admingUrl,webClient);
				String html = adminPage.getWebResponse().getContentAsString();
				tracer.addTag("parser.login.page", "<xmp>"+html+"</xmp>");
				if(html.contains("您输入的验证码错误")){
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getDescription());
					taskHousingRepository.save(taskHousing);
				}else if(html.contains("密码错误")){
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
					taskHousingRepository.save(taskHousing);
				}else if(html.contains("身份证号不存在")){
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR_INEXISTENCE.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR_INEXISTENCE.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR_INEXISTENCE.getDescription());
					taskHousingRepository.save(taskHousing);
				}else{
					taskHousing.setCookies(CommonUnit.transcookieToJson(webClient));
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousingRepository.save(taskHousing);
				}
			}else{			
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getDescription());
				taskHousingRepository.save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.login.error", e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getDescription());
			taskHousingRepository.save(taskHousing);
		}
		return taskHousing;
		
	}

	/**
	 * @Des 获取用户信息
	 * @param messageLoginForHousing
	 * @param taskHousing
	 */
	@Async
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		
		tracer.addTag("crawler.guiyang.getuserinfo.start", messageLoginForHousing.getTask_id());
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		String userUrl = "http://zxcx.gygjj.gov.cn/PersonBaseInfo.do?method=view";
		
		try {
			HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(userUrl,webClient);
			tracer.addTag("crawler.guiyang.getuserinfo.page", "<xmp>"+page.asXml()+"</xmp>");
			HousingGuiyangUserinfo housingGuiyangUserinfo = housingFundGuiyangParser.parserUserInfo(page,taskHousing);
			housingGuiyangUserinfoRepository.save(housingGuiyangUserinfo);
			tracer.addTag("crawler.guiyang.getuserinfo.success", messageLoginForHousing.getTask_id());
			
			taskHousing.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskHousing.setUserinfoStatus(200);
			taskHousingRepository.save(taskHousing);
			updateTaskHousing(messageLoginForHousing.getTask_id());
			
		} catch (Exception e) {
			tracer.addTag("crawler.guiyang.getuserinfo.error", e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
			taskHousing.setUserinfoStatus(404);
			taskHousingRepository.save(taskHousing);
			updateTaskHousing(messageLoginForHousing.getTask_id());
		}
	}

	/**
	 * @Des 获取缴费信息
	 * @param messageLoginForHousing
	 * @param taskHousing
	 */
	@Async
	public void getPay(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("crawler.guiyang.getPay.start", messageLoginForHousing.getTask_id());
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String payUrl = "http://zxcx.gygjj.gov.cn/PersonAccountsList.do?method=list&aaxmlrequest=true&startTime_a=2000-01-01&startTime_b="+sdf.format(new Date())+"&";
		try {
			XmlPage page = (XmlPage) LoginAndGetCommon.getHtml(payUrl,webClient);
			tracer.addTag("crawler.guiyang.getPay.page", "<xmp>"+page.asXml()+"</xmp>");
			List<HousingGuiyangDetailAccount> accounts = housingFundGuiyangParser.parserAccount(page,taskHousing);
			if(null != accounts){
				housingGuiyangDetailAccountRepository.saveAll(accounts);
				tracer.addTag("crawler.guiyang.getPay.success", messageLoginForHousing.getTask_id());
				
				taskHousing.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ACCOUNT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(200);
				taskHousingRepository.save(taskHousing);
				updateTaskHousing(messageLoginForHousing.getTask_id());
			}else{
				tracer.addTag("crawler.guiyang.getPay.error", "获取页面数据为null");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_TIMEOUT.getDescription());
				taskHousing.setPaymentStatus(404);
				taskHousingRepository.save(taskHousing);
				updateTaskHousing(messageLoginForHousing.getTask_id());
			}
			
		} catch (Exception e) {
			tracer.addTag("crawler.guiyang.getPay.error", e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
			taskHousing.setPaymentStatus(404);
			taskHousingRepository.save(taskHousing);
			updateTaskHousing(messageLoginForHousing.getTask_id());
		}
	
	
	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		//用户信息
		try {
			getUserInfo(messageLoginForHousing,taskHousing);
		} catch (Exception e) {
			tracer.addTag("parser.guiyang.crawler.getuserinfo.error", e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		}
				
		//缴费信息
		try{
			getPay(messageLoginForHousing,taskHousing);			
		}catch(Exception e){
			tracer.addTag("parser.guiyang.crawler.getpay.error", e.getMessage());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		}
				
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
