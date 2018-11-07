package app.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportBaseinfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallDetailStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallSumStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallTimeDetailStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCarrierConsume;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportConsumeinfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportContactsTop;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportLocationTop;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportPeriodStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportSocialAnalysisSummary;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportStability;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportVitality;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportVitalityAnalysisSummary;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportBaseinfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportCallDetailStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportCallSumStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportCallTimeDetailStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportCarrierConsumeRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportConsumeinfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportContactsTopRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportLocationTopRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportPeriodStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportSocialAnalysisSummaryRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportStabilityRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportVitalityAnalysisSummaryRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportVitalityRepository;

import app.bean.MobileEtlEnum;
import app.bean.RequestParam;
import app.bean.WebDataReportV2;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile.etl","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile.etl","com.microservice.dao.repository.crawler.mobile"})
public class MobileETLReportV2Service {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private ProMobileReportBaseinfoRepository proMobileReportBaseinfoRepository;
	@Autowired
	private ProMobileReportSocialAnalysisSummaryRepository proMobileReportSocialAnalysisSummaryRepository;
	@Autowired
	private ProMobileReportContactsTopRepository proMobileReportContactsTopRepository;
	@Autowired
	private ProMobileReportLocationTopRepository proMobileReportLocationTopRepository;
	@Autowired
	private ProMobileReportVitalityAnalysisSummaryRepository proMobileReportVitalityAnalysisSummaryRepository;
	@Autowired
	private ProMobileReportConsumeinfoRepository proMobileReportConsumeinfoRepository;
	@Autowired
	private ProMobileReportCarrierConsumeRepository proMobileReportCarrierConsumeRepository;
	@Autowired
	private ProMobileReportCallSumStatisticsRepository proMobileReportCallSumStatisticsRepository;
	@Autowired
	private ProMobileReportCallDetailStatisticsRepository proMobileReportCallDetailStatisticsRepository;
	@Autowired
	private ProMobileReportPeriodStatisticsRepository proMobileReportPeriodStatisticsRepository;
	@Autowired
	private ProMobileReportCallTimeDetailStatisticsRepository proMobileReportCallTimeDetailStatisticsRepository;
	@Autowired
	private ProMobileReportStabilityRepository proMobileReportStabilityRepository;
	@Autowired
	private ProMobileReportVitalityRepository proMobileReportVitalityRepository;
	
	@Value("${spring.profiles.active}")
	String profile;
	public WebDataReportV2 getAllData(RequestParam requestParam){
		
		WebDataReportV2 webDataReport = new WebDataReportV2();
		
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
				if (null==taskMobile.getFinished() || null==taskMobile.getReportTime()) {
					webDataReport.setParam(requestParam);
//					webDataReport.setMobileUserInfos(getUserInfos(taskMobile));
					webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getMessage());
					webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_CRAWLER_ERROR.getErrorCode());
					webDataReport.setProfile(profile);
					return webDataReport;
				}else if(taskMobile.getFinished()  && taskMobile.getDescription().equals("数据采集成功！")){
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
					if (null==taskMobile.getFinished() || null==taskMobile.getReportTime()) {
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
	
	public WebDataReportV2 getData(TaskMobile taskMobile,WebDataReportV2 webDataReport,RequestParam requestParam){
		
		if(null != taskMobile){
			if(null == taskMobile.getReportTime()){
				webDataReport.setParam(requestParam);
				webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_NOT_EXCUTE.getMessage());
				webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_NOT_EXCUTE.getErrorCode());
				webDataReport.setProfile(profile);
				return webDataReport;
			}
			List<ProMobileReportBaseinfo> baseInfos = proMobileReportBaseinfoRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportSocialAnalysisSummary> socialAnalysisSummarys = proMobileReportSocialAnalysisSummaryRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportContactsTop> contactsTops = proMobileReportContactsTopRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportLocationTop> locationTops = proMobileReportLocationTopRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportVitalityAnalysisSummary> vitalityAnalysisSummarys = proMobileReportVitalityAnalysisSummaryRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportConsumeinfo> consumeinfos = proMobileReportConsumeinfoRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportCarrierConsume> carrierConsumes = proMobileReportCarrierConsumeRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportCallSumStatistics> callSumStatisticss = proMobileReportCallSumStatisticsRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportCallDetailStatistics> callDetailStatistics3ms = proMobileReportCallDetailStatisticsRepository.findByTaskIdAndDataTypeOrderByCommunicateDurationDesc(taskMobile.getTaskid(),"三月");
			List<ProMobileReportCallDetailStatistics> callDetailStatistics6ms = proMobileReportCallDetailStatisticsRepository.findByTaskIdAndDataTypeOrderByCommunicateDurationDesc(taskMobile.getTaskid(),"六月");			
			List<ProMobileReportPeriodStatistics> periodStatisticss = proMobileReportPeriodStatisticsRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportCallTimeDetailStatistics> callTimeDetailStatisticss = proMobileReportCallTimeDetailStatisticsRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportStability> stabilitys = proMobileReportStabilityRepository.findByTaskId(taskMobile.getTaskid());
			List<ProMobileReportVitality> vitalitys = proMobileReportVitalityRepository.findByTaskId(taskMobile.getTaskid());

			webDataReport.setProMobileReportBaseinfos(baseInfos);
			webDataReport.setProMobileReportSocialAnalysisSummarys(socialAnalysisSummarys);
			webDataReport.setProMobileReportContactsTops(contactsTops);
			webDataReport.setProMobileReportLocationTops(locationTops);
			webDataReport.setProMobileReportVitalityAnalysisSummarys(vitalityAnalysisSummarys);
			webDataReport.setProMobileReportConsumeinfos(consumeinfos);
			webDataReport.setProMobileReportCarrierConsumes(carrierConsumes);
			webDataReport.setProMobileReportCallSumStatisticss(callSumStatisticss);
			
			webDataReport.setProMobileReportCallDetailStatistics3ms(callDetailStatistics3ms);
			webDataReport.setProMobileReportCallDetailStatistics6ms(callDetailStatistics6ms);
			webDataReport.setProMobileReportPeriodStatisticss(periodStatisticss);
			webDataReport.setProMobileReportCallTimeDetailStatisticss(callTimeDetailStatisticss);
			webDataReport.setProMobileReportStabilitys(stabilitys);
			webDataReport.setProMobileReportVitalitys(vitalitys);
			
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
