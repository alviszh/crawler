package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangHtml;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangUserInfo;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangUserList;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.nanchang.InsuranceNanchangHtmlPepository;
import com.microservice.dao.repository.crawler.insurance.nanchang.InsuranceNanchangMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.nanchang.InsuranceNanchangPensionRepository;
import com.microservice.dao.repository.crawler.insurance.nanchang.InsuranceNanchangUserInfoListRepository;
import com.microservice.dao.repository.crawler.insurance.nanchang.InsuranceNanchangUserInfoRepository;
import com.module.htmlunit.WebCrawler;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.exceptiondetail.EUtils;
import app.parser.InsuranceNanchangParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.nanchang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.nanchang" })
public class InsuranceNanchangService implements InsuranceLogin{

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceNanchangParser insuranceNanchangParser;

	@Autowired
	private InsuranceNanchangUserInfoListRepository insuranceNanchangUserInfoListRepository;
	@Autowired
	private InsuranceNanchangUserInfoRepository insuranceNanchangUserInfoRepository;
	@Autowired
	private InsuranceNanchangHtmlPepository insuranceNanchangHtmlPepository;


	@Autowired
	private InsuranceNanchangPensionRepository insuranceNanchangPensionRepository;

	@Autowired
	private InsuranceNanchangMedicalRepository insuranceNanchangMedicalRepository;

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private EUtils eutils;
	
	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return
	 * @throws Exception
	 */
	public TaskInsurance loginRetry(InsuranceRequestParameters insuranceRequestParameters,int count)
			throws Exception {

		tracer.addTag("InsuranceNanchangService.login", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		if (null != taskInsurance) {

			WebParam webParam = insuranceNanchangParser.login(insuranceRequestParameters);

			if (null == webParam) {
				tracer.addTag("InsuranceNanchangService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);

				return taskInsurance;
			} else {
				String html = webParam.getPage().getWebResponse().getContentAsString();

				tracer.addTag("InsuranceChengduService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				if (html.contains("个人编号")) {

					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage(),webParam.getUrl());

					return taskInsurance;
				} else {
					tracer.addTag("InsuranceNanchangService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败次数" + ++count);
					if (count < 3) {
						loginRetry(insuranceRequestParameters,count);
					}else{
						taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
						return taskInsurance;
					}
				}

			}

		}

		return null;
	}

	/**
	 * 获取存储个人社保信息列表
	 *
	 * @param insuranceRequestParameters
	 *
	 * @return
	 */

	public List<String> getList(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceNanchangService.getTaskId", insuranceRequestParameters.getTaskId());

		List<String> list=new ArrayList<>();
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		//工伤 失业 生育 不存在

		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);

//		insuranceService.changeCrawlerStatus(
//				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
//				InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),200,taskInsurance);
//		insuranceService.changeCrawlerStatus(
//				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
//				InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),200,taskInsurance);
//		insuranceService.changeCrawlerStatus(
//				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
//				InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),200,taskInsurance);
//		//医疗养老初始化
//		insuranceService.changeCrawlerStatus(
//				InsuranceStatusCode.INSURANCE_CRAWLER_AGED_DOING.getDescription(),
//				InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),InsuranceStatusCode.INSURANCE_CRAWLER_AGED_DOING.getError_code(),taskInsurance);
//		insuranceService.changeCrawlerStatus(
//				InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_DOING.getDescription(),
//				InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_DOING.getError_code(),taskInsurance);
//		//更新Task表为 基本信息列表数据采集中
//		insuranceService.changeCrawlerStatus(
//				InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_DOING.getDescription(),
//				InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(),InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_DOING.getError_code(),taskInsurance);

		WebParam<InsuranceNanchangUserList> webParam = insuranceNanchangParser.getUserList(taskInsurance,
				taskInsurance.getPid());
		tracer.addTag("InsurancisteNanchangServer.getList 爬取初始个人信息列表页", taskInsurance.getPid());


		if (null != webParam) {
			List<InsuranceNanchangUserList> userLists=webParam.getList();

			insuranceNanchangUserInfoListRepository.saveAll(userLists);
			//更新Task表为 基本信息列表数据采集成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS);
			InsuranceNanchangHtml insuranceNanchangHtml=new InsuranceNanchangHtml(insuranceRequestParameters.getTaskId(),"InsuranceNanchangUserList" , 1,taskInsurance.getPid(), webParam.getHtml());

			insuranceNanchangHtmlPepository.save(insuranceNanchangHtml);

			tracer.addTag("InsurancisteNanchangServer.getList 个人信息列表页", "个人信息列表已入库!");

			tracer.addTag("InsurancisteNanchangServer.getList:SUCCESS", insuranceRequestParameters.getTaskId());

			for(InsuranceNanchangUserList userurl : userLists){
				list.add(userurl.getUrlinfor());
			}
			getUserInfo(insuranceRequestParameters, list);

			return list;
		}else {
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance);
			tracer.addTag("InsurancisteNanchangServer.getList 爬取初始个人信息列表页失败!", taskInsurance.getPid());
		return null;
		}
	}

	/**
	 * 采集并存储个人详细信息
	 * @param insuranceRequestParameters
	 * @param lists
	 * @return
	 */

	public List<String> getUserInfo(InsuranceRequestParameters insuranceRequestParameters,List<String> lists)  {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		List<String> listurl= new ArrayList<>();
		WebParam webParam=insuranceNanchangParser.getUserdetinfo(taskInsurance,lists);
		if (webParam!=null){
			//更新Task表为 基本信息数据解析成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
			List<InsuranceNanchangUserInfo> listdet=webParam.getList();
			insuranceNanchangUserInfoRepository.saveAll(listdet);
			tracer.addTag("InsurancisteNanchangServer.getUserInfo 个人详细信息", "个人详细信息已入库!");
			insuranceNanchangHtmlPepository.saveAll(webParam.getListhtml());

			for (InsuranceNanchangUserInfo insuranceNanchangUserInfo1 : listdet){
				listurl.add(insuranceNanchangUserInfo1.getUrldetinfo());
			}
			getallinfodata(insuranceRequestParameters,listurl);
			return listurl;
		}else {
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE);
			tracer.addTag("InsurancisteNanchangServer.getUserInfo 个人详细信息 webParam is null", insuranceRequestParameters.getTaskId());
		return null;}

	}

	/**
	 * 采集并存储医疗与养老明细信息
	 * @param insuranceRequestParameters
	 * @param urllist
	 */
	public void getallinfodata(InsuranceRequestParameters insuranceRequestParameters,List<String> urllist)  {
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		for (String list: urllist){
			WebParam webParamPension=insuranceNanchangParser.getwebparaPension(taskInsurance,list);
			if (webParamPension !=null ){

				insuranceNanchangPensionRepository.saveAll(webParamPension.getList());
				insuranceNanchangHtmlPepository.saveAll(webParamPension.getListhtml());
				//更新Task表为 养老保险数据采集成功
//				insuranceService.changeCrawlerStatus(
//						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
//						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),200,taskInsurance);
				insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
				tracer.addTag("InsurancisteNanchangServer.getallinfodata 养老明细信息", "养老明细信息已入库!");
			}else {
				tracer.addTag("InsurancisteNanchangServer.getallinfodata 养老明细信息 webParam is null", insuranceRequestParameters.getTaskId());
			}
			List<String> list1=insuranceNanchangParser.getlisturl(taskInsurance,list);
			for (String url : list1){
				if(url.contains("medical")){
					WebParam webParamMedical=insuranceNanchangParser.getwebparaMedical(taskInsurance,url);
					if (webParamMedical !=null ){

						//更新Task表为 医疗保险数据采集成功
						insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
						insuranceNanchangMedicalRepository.saveAll(webParamMedical.getList());
						insuranceNanchangHtmlPepository.saveAll(webParamMedical.getListhtml());
//						insuranceService.changeCrawlerStatus(
//								InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
//								InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),200,taskInsurance);
						tracer.addTag("InsurancisteNanchangServer.getallinfodata 医疗明细信息", "医疗明细信息已入库!");
					}else {
						tracer.addTag("InsurancisteNanchangServer.getallinfodata 医疗明细信息 webParam is null", insuranceRequestParameters.getTaskId());
					}
				}
			}
		}

		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS);
		insuranceService.changeCrawlerStatusSuccess(taskInsurance);

	}

	@HystrixCommand(fallbackMethod = "fallback")
	public String hystrix() {
		tracer.addTag("InsuranceNanchangService hystrix", "start");
		String url = "http://hrss.nc.gov.cn/insure/query.jsp?locale=zh_CN";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest);
			int status = page.getWebResponse().getStatusCode();
			tracer.addTag("hystrix 南昌社保登录页状态码", String.valueOf(status));
			if (200 == status) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("hystrix 南昌社保登录页", html);
				return "SUCCESS";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = null;
		try {
			taskInsurance  = loginRetry(insuranceRequestParameters, 0);
		} catch (Exception e) {
			tracer.addTag("InsuranceNanchangService.login---Taskid--",
					insuranceRequestParameters.getTaskId() + eutils.getEDetail(e));
		}
		return taskInsurance;
	}


}
