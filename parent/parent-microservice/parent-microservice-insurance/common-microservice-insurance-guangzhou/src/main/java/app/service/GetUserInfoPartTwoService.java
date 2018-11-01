/**
 * 
 */
package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.guangzhou.GuangzhouUserInfo;
import com.microservice.dao.entity.crawler.insurance.guangzhou.InsuranceGuangzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.GuangZhouUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.guangzhou.InsuranceGuangzhouHtmlRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceGuangzhouParser;

/**
 * @author 王培阳
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.guangzhou"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.guangzhou"})
public class GetUserInfoPartTwoService {

	@Autowired
	private InsuranceGuangzhouParser insuranceGuangzhouParser;
	@Autowired
	private GuangZhouUserInfoRepository guangZhouUserInfoRepository;
	@Autowired
	private GetMedicalInfoService getMedicalInfoService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGuangzhouHtmlRepository insuranceGuangzhouHtmlRepository;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 获取个人信息
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(TaskInsurance taskInsurance) {
		try {
			taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
			tracer.addTag("parser.crawler.getUserinfo个人信息", taskInsurance.toString());
			GuangzhouUserInfo userInfo = guangZhouUserInfoRepository.findByTaskid(taskInsurance.getTaskid()).get(0);
			
			WebParam webParam = insuranceGuangzhouParser.getUserInfoTwo(userInfo,taskInsurance);
			
			InsuranceGuangzhouHtml insuranceGuangzhouHtml = new InsuranceGuangzhouHtml();
			GuangzhouUserInfo guangzhouUserInfo = webParam.getGuangzhouUserInfo();
			
			if(null != guangzhouUserInfo){
				guangZhouUserInfoRepository.save(guangzhouUserInfo);
			}
			if(null != webParam.getPage()){
				insuranceGuangzhouHtml.setHtml(webParam.getPage().asXml());
				insuranceGuangzhouHtml.setTaskid(taskInsurance.getTaskid());
				insuranceGuangzhouHtml.setType("userinfo-人员非敏感资料变更");
				insuranceGuangzhouHtml.setPageCount(1);
				insuranceGuangzhouHtml.setUrl(webParam.getUrl());
				insuranceGuangzhouHtmlRepository.save(insuranceGuangzhouHtml);
			}
			taskInsurance.setUserInfoStatus(webParam.getCode());
			tracer.addTag("parser.crawler.getUserinfo.taskInsurance人员非敏感资料变更 ", taskInsurance.toString());
			taskInsuranceRepository.save(taskInsurance);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.getUserinfo.Exception",e.toString());
		} finally{
			getMedicalInfoService.getMedicalInfo(taskInsurance);
		}
		
	}
}
