package app.service;

import java.net.URL;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangGongShang;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangShengYu;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangShiYe;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangUserInfo;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangYangLao;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangYiLiao;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.xiangyang.InsuranceXiangYangGongShangRepository;
import com.microservice.dao.repository.crawler.insurance.xiangyang.InsuranceXiangYangShengYuRepository;
import com.microservice.dao.repository.crawler.insurance.xiangyang.InsuranceXiangYangShiYeRepository;
import com.microservice.dao.repository.crawler.insurance.xiangyang.InsuranceXiangYangUserinfoRepository;
import com.microservice.dao.repository.crawler.insurance.xiangyang.InsuranceXiangYangYangLaoRepository;
import com.microservice.dao.repository.crawler.insurance.xiangyang.InsuranceXiangYangYiLiaoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceXiangYangParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.xiangyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.xiangyang" })
public class InsuranceXiangYangService extends InsuranceService{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceXiangYangParser insuranceXiangYangParser;
	@Autowired
	private InsuranceXiangYangUserinfoRepository insuranceXiangYangUserinfoRepository;
	@Autowired
	private InsuranceXiangYangYangLaoRepository insuranceXiangYangYangLaoRepository;
	@Autowired
	private InsuranceXiangYangYiLiaoRepository insuranceXiangYangYiLiaoRepository;
	@Autowired
	private InsuranceXiangYangGongShangRepository insuranceXiangYangGongShangRepository;
	@Autowired
	private InsuranceXiangYangShengYuRepository insuranceXiangYangShengYuRepository;
	@Autowired
	private InsuranceXiangYangShiYeRepository insuranceXiangYangShiYeRepository;
	public void login(InsuranceRequestParameters parameter, TaskInsurance taskInsurance){
		try {

			tracer.addTag("action.xiangyang.login", parameter.getTaskId());
			String url = "http://www.xf12333.cn/xywzweb/html/fwdt/xxcx/shbxjfcx/index.shtml";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			HtmlTextInput username = page.getElementByName("sfzh");
			HtmlPasswordInput pass = page.getElementByName("scbh");
			HtmlSubmitInput sub = page.getElementByName("cmdok");

			username.setText(parameter.getUserIDNum());
			pass.setText(parameter.getPassword());

			sub.click();
			String alertMsg = WebCrawler.getAlertMsg();
			//参数 dt
			int date = new Date().getDate();
			System.out.println(date);
			int mydate = date + 99;
			System.out.println(mydate);

			String url2 = "http://www.xf12333.cn/hbwz/qtpage/fwdt/shbxjfcx_result.jsp?"
					+ "scbh="+parameter.getPassword()
					+ "&sfzh="+parameter.getUserIDNum()
					+ "&dt="+mydate;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();

			System.out.println(html2);

			if(html2.indexOf("查询结果")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance.setCookies(cookies);
				taskInsurance.setTesthtml("Username:"+parameter.getUserIDNum()+";password:"+parameter.getPassword());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsuranceRepository.save(taskInsurance);

			}else{
				System.out.println("登录失败："+alertMsg);
				tracer.addTag("action.xiangyang.login", "登录失败:"+alertMsg);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance.setDescription(alertMsg);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.xiangyang.login", "登录失败:"+e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
			taskInsuranceRepository.save(taskInsurance);
		}
	}

	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	/***
	 * crawler
	 * @param parameter
	 * @param taskInsurance
	 */
	public void getcrawler(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			int date = new Date().getDate();
			System.out.println(date);
			int mydate = date + 99;
			System.out.println(mydate);

			String url2 = "http://www.xf12333.cn/hbwz/qtpage/fwdt/shbxjfcx_result.jsp?"
					+ "scbh="+parameter.getPassword()
					+ "&sfzh="+parameter.getUserIDNum()
					+ "&dt="+mydate;
			Page page3 = getHtml(url2, webClient);
			String html2 = page3.getWebResponse().getContentAsString();

			System.out.println(html2);

			if(html2.indexOf("查询结果")!=-1){
				//个人信息
				getUserinfo(html2,parameter,taskInsurance);
				//养老
				getyanglaopay(html2,parameter,taskInsurance);
				//医疗
				getyiliaopay(html2,parameter,taskInsurance);
				//失业
				getshiyepay(html2,parameter,taskInsurance);
				//工伤
				getgongshangpay(html2,parameter,taskInsurance);
				//生育
				getshengyupay(html2,parameter,taskInsurance);
				
				changeCrawlerStatusSuccess(parameter.getTaskId());
				
			}else{
				System.out.println("网络延迟，请重新登录");
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhasestatus());
				taskInsurance.setDescription("网络延迟，请重新登录");
				taskInsuranceRepository.save(taskInsurance);
			}

		} catch (Exception e) {
			taskInsurance.setFinished(true);
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getDescription());
			taskInsurance.setError_message("网站有变动");
			taskInsuranceRepository.save(taskInsurance);
		}
	}
	private void getshengyupay(String html2, InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		List<InsuranceXiangYangShengYu> list = insuranceXiangYangParser.getshengyupay(html2,parameter.getTaskId());
		if(list!=null){
			insuranceXiangYangShengYuRepository.saveAll(list);
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
			taskInsurance.setShengyuStatus(200);
			taskInsuranceRepository.save(taskInsurance);
		}else{
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
			taskInsurance.setShengyuStatus(201);
			taskInsuranceRepository.save(taskInsurance);
		}
		
	}

	/**
	 * getgongshangpay
	 * @param html2
	 * @param parameter
	 * @param taskInsurance
	 */
	private void getgongshangpay(String html2, InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		List<InsuranceXiangYangGongShang> list = insuranceXiangYangParser.getgongshangpay(html2,parameter.getTaskId());
		if(list!=null){
			insuranceXiangYangGongShangRepository.saveAll(list);
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
			taskInsurance.setGongshangStatus(200);
			taskInsuranceRepository.save(taskInsurance);
		}else{
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
			taskInsurance.setGongshangStatus(201);
			taskInsuranceRepository.save(taskInsurance);
		}
	}

	/**
	 * getshiyepay
	 * @param html2
	 * @param parameter
	 * @param taskInsurance
	 */
	private void getshiyepay(String html2, InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		List<InsuranceXiangYangShiYe> list = insuranceXiangYangParser.getshiyepay(html2,parameter.getTaskId());
		if(list!=null){
			insuranceXiangYangShiYeRepository.saveAll(list);
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
			taskInsurance.setShiyeStatus(200);
			taskInsuranceRepository.save(taskInsurance);
		}else{
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
			taskInsurance.setShiyeStatus(201);
			taskInsuranceRepository.save(taskInsurance);
		}
	}

	/**
	 * getyiliaopay
	 * @param html2
	 * @param parameter
	 * @param taskInsurance
	 */
	private void getyiliaopay(String html2, InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		List<InsuranceXiangYangYiLiao> list = insuranceXiangYangParser.getyiliaopay(html2,parameter.getTaskId());
		if(list!=null){
			insuranceXiangYangYiLiaoRepository.saveAll(list);
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYiliaoStatus(200);
			taskInsuranceRepository.save(taskInsurance);
		}else{
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYiliaoStatus(201);
			taskInsuranceRepository.save(taskInsurance);
		}
	}

	/**
	 * getyanglaopay
	 * @param html2
	 * @param parameter
	 * @param taskInsurance
	 */
	private void getyanglaopay(String html2, InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		List<InsuranceXiangYangYangLao> list = insuranceXiangYangParser.getyanglaopay(html2,parameter.getTaskId());

		if(list!=null){
			insuranceXiangYangYangLaoRepository.saveAll(list);
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYanglaoStatus(200);
			taskInsuranceRepository.save(taskInsurance);
		}else{
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYanglaoStatus(201);
			taskInsuranceRepository.save(taskInsurance);
		}
	}

	/**
	 * getuserinfo
	 * @param html
	 * @param parameter 
	 * @param taskInsurance 
	 */
	public void getUserinfo(String html, InsuranceRequestParameters parameter, TaskInsurance taskInsurance){

		InsuranceXiangYangUserInfo user = insuranceXiangYangParser.getUserinfo(html,parameter.getTaskId());

		if(user!=null){
			insuranceXiangYangUserinfoRepository.save(user);
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskInsurance.setUserInfoStatus(200);
			taskInsuranceRepository.save(taskInsurance);
		}else{
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskInsurance.setUserInfoStatus(201);
			taskInsuranceRepository.save(taskInsurance);
		}
	}
}
