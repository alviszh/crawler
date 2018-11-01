package app.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhengzhou.InsuranceZhengzhouHtml;
import com.microservice.dao.entity.crawler.insurance.zhengzhou.InsuranceZhengzhouUserInfo;
import com.microservice.dao.repository.crawler.insurance.zhengzhou.InsuranceZhengzhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.zhengzhou.InsuranceZhengzhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.enums.InsuranceZhengzhouCrawlerResult;
import app.enums.InsuranceZhengzhouCrawlerType;
import app.htmlparser.ZhengzhouCrawler;

/**
 * 爬取基本信息Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.*"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.*"})
public class CrawlerBaseInfoService {
	
	@Autowired 
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private ZhengzhouCrawler zhengzhouCrawler;
	@Autowired
	private InsuranceZhengzhouHtmlRepository insuranceZhengzhouHtmlRepository;
	@Autowired
	private InsuranceZhengzhouUserInfoRepository insuranceZhengzhouUserInfoRepository;
	
	/**
	 * 爬取基本信息
	 * @param parameter
	 * @return
	 */
	public WebParam<String> crawlerBaseInfo(InsuranceRequestParameters parameter,TaskInsurance taskInsurance,Set<Cookie> cookies){
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取基本信息", parameter.toString());
		//更新Task表为 基本信息数据采集中
		insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS);
		//开始爬取
		String token = taskInsurance.getPid();
		WebParam<String> baseInfo = zhengzhouCrawler.crawlerBaseInfo(cookies,token);
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:爬取完成", baseInfo.toString());
		//Html入库
		if(InsuranceZhengzhouCrawlerResult.SUCCESS.getCode().equals(baseInfo.getCode()) &&
				baseInfo.getData() != null){
			String personInfoJson = baseInfo.getData();
			InsuranceZhengzhouHtml insuranceShenzhenHtml = new InsuranceZhengzhouHtml();
			insuranceShenzhenHtml.setHtml(personInfoJson);
			insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
			insuranceShenzhenHtml.setType(InsuranceZhengzhouCrawlerType.BASE_INFO.getCode());
			insuranceShenzhenHtml.setUrl(ZhengzhouCrawler.BASE_INFO);
			insuranceZhengzhouHtmlRepository.save(insuranceShenzhenHtml);
			// 解析json（郑州社保用户基本信息表）
			JSONArray jsonArray = JSON.parseArray(personInfoJson);
			InsuranceZhengzhouUserInfo userinfo = new InsuranceZhengzhouUserInfo();
			for(int i =0,j=jsonArray.size();i<j;i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				// TODO: 2017/9/21 这里涉及到家庭成员的社保基本信息获取，由于该账号家庭成员只有一个人，所以暂时不考虑List的情况
				userinfo.setTaskId(taskInsurance.getTaskid());
				userinfo.setCjbh(jsonObject.getString("cjbh"));
				userinfo.setIdNo(jsonObject.getString("idno"));
				userinfo.setName(jsonObject.getString("name"));
				userinfo.setPersonelNo(jsonObject.getString("personelno"));
				userinfo.setCardNo(jsonObject.getString("cardno"));
				userinfo.setAreaName(jsonObject.getString("areaname"));
				userinfo.setAreaNo(jsonObject.getString("areano"));
				userinfo.setCompanyId(jsonObject.getString("companyid"));
				userinfo.setCompanyName(jsonObject.getString("companyname"));
				userinfo.setInsureType(jsonObject.getInteger("insuretype"));
				userinfo.setStepFlag(jsonObject.getInteger("stepflag"));
				userinfo.setOldCardFlag(jsonObject.getInteger("oldcardflag"));
				userinfo.setPersonType(jsonObject.getInteger("persontype"));
				userinfo.setBalance(jsonObject.getString("balance"));
				userinfo.setPayBase(jsonObject.getString("payBase"));
				break;
			}
			insuranceZhengzhouUserInfoRepository.save(userinfo);
			//更新Task表为 基本信息数据采集成功
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS);
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS);

			// 将任务状态修改为爬取成功！
			insuranceService.changeCrawlerStatusSuccess(parameter.getTaskId());

		}else{
			//更新Task表为 基本信息数据采集失败
			insuranceService.changeTaskInsuranceStatus4Crawler(taskInsurance,InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE);
		}
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:爬取结束！", baseInfo.toString());
		return baseInfo;
	}

}
