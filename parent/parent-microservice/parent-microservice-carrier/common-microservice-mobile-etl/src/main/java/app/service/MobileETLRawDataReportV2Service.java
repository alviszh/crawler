package app.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileBillinfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileCallInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobilePayInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileServiceInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileSmsInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileBillinfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileCallInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobilePayInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileServiceInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileSmsInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileUserInfoRepository;

import app.bean.MobileEtlEnum;
import app.bean.RequestParam;
import app.bean.WebRawDataReportV2;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile.etl","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile.etl","com.microservice.dao.repository.crawler.mobile"})
public class MobileETLRawDataReportV2Service {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private ProMobileBillinfoRepository proMobileBillinfoRepository;
	@Autowired
	private ProMobileCallInfoRepository proMobileCallInfoRepository;
	@Autowired
	private ProMobileUserInfoRepository proMobileUserInfoRepository;
	@Autowired
	private ProMobilePayInfoRepository  proMobilePayInfoRepository;
	@Autowired
	private ProMobileServiceInfoRepository proMobileServiceInfoRepository;
	@Autowired
	private ProMobileSmsInfoRepository proMobileSmsInfoRepository;
	@Value("${spring.profiles.active}")
	String profile;
	public WebRawDataReportV2 getAllData(RequestParam requestParam){
		
		WebRawDataReportV2 webDataReport = new WebRawDataReportV2();
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getMobileNum())){
			
			//返回错误码
			webDataReport.setParam(requestParam);
			webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_NULL.getMessage());
			webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_NULL.getErrorCode());
			webDataReport.setProfile(profile);
			return webDataReport;
		}
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getMobileNum())){
			//通过查询找出对应电话号时间最大的那个
			TaskMobile taskMobile = taskMobileRepository.findTopByPhonenumAndFinishedAndDescriptionOrderByCreatetimeDesc(requestParam.getMobileNum(),true,"数据采集成功！");
			return getData(taskMobile,webDataReport,requestParam);				
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getMobileNum())){
			TaskMobile taskMobile = taskMobileRepository.findByTaskid(requestParam.getTaskid());
			if(null != taskMobile){
				if (null==taskMobile.getFinished() || null==taskMobile.getDescription()) {
					webDataReport.setParam(requestParam);
//					webDataReport.setMobileUserInfos(getUserInfos(taskMobile));
					webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getMessage());
					webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getErrorCode());
					webDataReport.setProfile(profile);
					return webDataReport;
				}else if(taskMobile.getFinished() && taskMobile.getDescription().equals("数据采集成功！")){
					return getData(taskMobile,webDataReport,requestParam);
				}else{
					webDataReport.setParam(requestParam);
//					webDataReport.setMobileUserInfos(getUserInfos(taskMobile));
					webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getMessage());
					webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getErrorCode());
					webDataReport.setProfile(profile);
					return webDataReport;
				}			
			}else{
				webDataReport.setParam(requestParam);
				webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getMessage());
				webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataReport.setProfile(profile);
				return webDataReport;
			}
		
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getMobileNum())){
			TaskMobile taskMobile = taskMobileRepository.findByTaskid(requestParam.getTaskid());
			if(null != taskMobile){
				if(taskMobile.getPhonenum().equals(requestParam.getMobileNum())){
					if (null==taskMobile.getFinished() || null==taskMobile.getDescription()) {
						webDataReport.setParam(requestParam);
//						webDataReport.setMobileUserInfos(getUserInfos(taskMobile));
						webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getMessage());
						webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getErrorCode());
						webDataReport.setProfile(profile);
						return webDataReport;
					}else if(taskMobile.getFinished() && taskMobile.getDescription().equals("数据采集成功！")){
						return getData(taskMobile,webDataReport,requestParam);
					}else{
						webDataReport.setParam(requestParam);
//						webData.setMobileUserInfos(getUserInfos(taskMobile));
						webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getMessage());
						webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getErrorCode());
						webDataReport.setProfile(profile);
						return webDataReport;
					}
				}else{
					webDataReport.setParam(requestParam);
					webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_ERROR.getMessage());
					webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_ERROR.getErrorCode());
					webDataReport.setProfile(profile);
					return webDataReport;
				}			
			}else{
				webDataReport.setParam(requestParam);
				webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getMessage());
				webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataReport.setProfile(profile);
				return webDataReport;
			}
		
		}
		
		return webDataReport;
	}
	
	public WebRawDataReportV2 getData(TaskMobile taskMobile,WebRawDataReportV2 webDataReport,RequestParam requestParam){
		
		if(null != taskMobile){
			if(null == taskMobile.getEtltime()){
				webDataReport.setParam(requestParam);
				webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_NOT_EXCUTE.getMessage());
				webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_NOT_EXCUTE.getErrorCode());
				webDataReport.setProfile(profile);
				return webDataReport;
			}
			List<ProMobileBillinfo> proMobileBillinfos = proMobileBillinfoRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileCallInfo> proMobileCallInfos = proMobileCallInfoRepository.findByTaskId(taskMobile.getTaskid());			
			List<ProMobileUserInfo> proMobileUserInfos = proMobileUserInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobilePayInfo>  proMobilePayInfos = proMobilePayInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileSmsInfo> proMobileSmsInfos = proMobileSmsInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileServiceInfo> proMobileServiceInfos = proMobileServiceInfoRepository.findByTaskId(taskMobile.getTaskid());
						
			webDataReport.setProMobileUserInfos(proMobileUserInfos);
			webDataReport.setProMobilePayInfos(proMobilePayInfos);
			webDataReport.setProMobileSmsInfos(proMobileSmsInfos);
			webDataReport.setProMobileServiceInfos(proMobileServiceInfos);			
			webDataReport.setProMobileCallInfos(proMobileCallInfos);
			webDataReport.setProMobileBillinfos(proMobileBillinfos);
			webDataReport.setParam(requestParam);
			webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_SUCCESS.getMessage());
			webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_SUCCESS.getErrorCode());
			webDataReport.setProfile(profile);
			return webDataReport;
			
		}else{
			webDataReport.setParam(requestParam);
			webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getMessage());
			webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_PARAMS_NO_RESULT.getErrorCode());
			webDataReport.setProfile(profile);
			return webDataReport;
		}
		
	}
	
}
