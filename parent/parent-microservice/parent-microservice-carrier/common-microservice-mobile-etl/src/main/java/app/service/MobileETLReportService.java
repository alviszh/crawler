package app.service;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportBaseInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatisticsSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatisticsThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumption;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumptionSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumptionThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportContactsStatisticsSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportContactsStatisticsThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportFamilyDetail;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportFamilyInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportLocationStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportPayment;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportSMS;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportBaseInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportCallStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportCallStatisticsSixMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportCallStatisticsThreeMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportConsumptionRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportConsumptionSixMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportConsumptionThreeMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportContactsStatisticsSixMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportContactsStatisticsThreeMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportFamilyDetailRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportFamilyInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportLocationStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportPaymentRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportSMSRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportRepository;

import app.bean.MobileEtlEnum;
import app.bean.RequestParam;
import app.bean.WebDataReport;
import app.commontracerlog.TracerLog;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile.etl","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile.etl","com.microservice.dao.repository.crawler.mobile"})
public class MobileETLReportService {

	@Autowired 
	private TracerLog tracer;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private MobileReportBaseInfoRepository mobileReportBaseInfoRepository;
	@Autowired
	private MobileReportCallStatisticsRepository mobileReportCallStatisticsRepository;
	@Autowired
	private MobileReportCallStatisticsSixMonthRepository mobileReportCallStatisticsSixMonthRepository;
	@Autowired
	private MobileReportCallStatisticsThreeMonthRepository mobileReportCallStatisticsThreeMonthRepository;
	@Autowired
	private MobileReportConsumptionRepository mobileReportConsumptionRepository;
	@Autowired
	private MobileReportConsumptionSixMonthRepository mobileReportConsumptionSixMonthRepository;
	@Autowired
	private MobileReportConsumptionThreeMonthRepository mobileReportConsumptionThreeMonthRepository;
	@Autowired
	private MobileReportContactsStatisticsSixMonthRepository mobileReportContractsStatisticsSixMonthRepository;
	@Autowired
	private MobileReportContactsStatisticsThreeMonthRepository mobileReportContractsStatisticsThreeMonthRepository;
	@Autowired
	private MobileReportFamilyDetailRepository mobileReportFamilyDetailRepository;
	@Autowired
	private MobileReportFamilyInfoRepository mobileReportFamilyInfoRepository;
	@Autowired
	private MobileReportLocationStatisticsRepository mobileReportLocationStatisticsRepository;
	@Autowired
	private MobileReportPaymentRepository mobileReportPaymentRepository;
	@Autowired
	private MobileReportSMSRepository mobileReportSMSRepository;
	@Autowired
	private ProMobileReportRepository mobileReportRepository;
	
	
	@Value("${spring.profiles.active}")
	String profile;
	
	public WebDataReport getAllData(RequestParam requestParam){
		
		WebDataReport webDataReport = new WebDataReport();
		
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
				if (null==taskMobile.getFinished() || null==taskMobile.getReportTime()){
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
					if (null==taskMobile.getFinished() || null==taskMobile.getReportTime()){
						webDataReport.setParam(requestParam);
//						webData.setMobileUserInfos(getUserInfos(taskMobile));
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
	
	public WebDataReport getData(TaskMobile taskMobile,WebDataReport webDataReport,RequestParam requestParam){
		
		if(null != taskMobile){
			if(null == taskMobile.getReportTime()){
				webDataReport.setParam(requestParam);
				webDataReport.setMessage(MobileEtlEnum.MOBILE_ETL_NOT_EXCUTE.getMessage());
				webDataReport.setErrorCode(MobileEtlEnum.MOBILE_ETL_NOT_EXCUTE.getErrorCode());
				webDataReport.setProfile(profile);
				return webDataReport;
			}
			List<MobileReportBaseInfo> baseInfos = mobileReportBaseInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportCallStatistics> callStatistics = mobileReportCallStatisticsRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportCallStatisticsSixMonth> callStatisticsSixMonth = mobileReportCallStatisticsSixMonthRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportCallStatisticsThreeMonth> callStatisticsThreeMonth = mobileReportCallStatisticsThreeMonthRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportConsumption> consumption = mobileReportConsumptionRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportConsumptionSixMonth> consumptionSixMonth = mobileReportConsumptionSixMonthRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportConsumptionThreeMonth> consumptionThreeMonth = mobileReportConsumptionThreeMonthRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportContactsStatisticsSixMonth> contractsStatisticsSixMonth = mobileReportContractsStatisticsSixMonthRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportContactsStatisticsThreeMonth> contractsStatisticsThreeMonth = mobileReportContractsStatisticsThreeMonthRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportFamilyDetail> familyDetail = mobileReportFamilyDetailRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportFamilyInfo> familyInfo = mobileReportFamilyInfoRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportLocationStatistics> locationStatistics = mobileReportLocationStatisticsRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportPayment> payment = mobileReportPaymentRepository.findByTaskId(taskMobile.getTaskid());
			List<MobileReportSMS> sms = mobileReportSMSRepository.findByTaskId(taskMobile.getTaskid());
			
			webDataReport.setMobileReportBaseInfos(baseInfos);
			webDataReport.setMobileReportCallStatisticss(callStatistics);
			webDataReport.setMobileReportCallStatisticsSixMonths(callStatisticsSixMonth);
			webDataReport.setMobileReportCallStatisticsThreeMonths(callStatisticsThreeMonth);
			webDataReport.setMobileReportConsumptions(consumption);
			webDataReport.setMobileReportConsumptionSixMonths(consumptionSixMonth);
			webDataReport.setMobileReportConsumptionThreeMonths(consumptionThreeMonth);
			webDataReport.setMobileReportContractsStatisticsSixMonths(contractsStatisticsSixMonth);
			webDataReport.setMobileReportContractsStatisticsThreeMonths(contractsStatisticsThreeMonth);
			webDataReport.setMobileReportFamilyDetails(familyDetail);
			webDataReport.setMobileReportFamilyInfos(familyInfo);
			webDataReport.setMobileReportLocationStatisticss(locationStatistics);
			webDataReport.setMobileReportPayments(payment);
			webDataReport.setMobileReportSMSs(sms);
			
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
	
	
	@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	public void mobileReport(String taskid) {
		tracer.addTag("ETL mobileReport", taskid);
		TaskMobile taskMobilen = taskMobileRepository.findByTaskid(taskid);
		if (taskMobilen != null && null != taskMobilen.getCarrier() && null != taskMobilen.getProvince()) {
			tracer.addTag("ETL mobileReport:taskMobilen---" + taskid, taskMobilen.toString());
			String etlStatus = "";
			if (taskMobilen.getCarrier().equals("CMCC")) {
				etlStatus = mobileReportRepository.cmccMobileEtl(taskid);
			} else if (taskMobilen.getCarrier().equals("UNICOM")) {
				etlStatus = mobileReportRepository.unicomMobileEtl(taskid);
			} else if (taskMobilen.getCarrier().equals("CHINA_TELECOM")) {
				switch (taskMobilen.getProvince()) {
				case "江苏":
					etlStatus = mobileReportRepository.telecomJiangsuEtl(taskid);
					break;
				case "重庆":
					etlStatus = mobileReportRepository.telecomChongqingEtl(taskid);
					break;
				case "山西":
					etlStatus = mobileReportRepository.telecomShanxi1Etl(taskid);
					break;
				case "广东":
					etlStatus = mobileReportRepository.telecomGuangdongEtl(taskid);
					break;
				case "天津":
					etlStatus = mobileReportRepository.telecomTianjinEtl(taskid);
					break;
				case "湖南":
					etlStatus = mobileReportRepository.telecomHunanEtl(taskid);
					break;
				case "河北":
					etlStatus = mobileReportRepository.telecomHebeiEtl(taskid);
					break;
				case "安徽":
					etlStatus = mobileReportRepository.telecomAnhuiEtl(taskid);
					break;
				case "海南":
					etlStatus = mobileReportRepository.telecomHainanEtl(taskid);
					break;
				case "青海":
					etlStatus = mobileReportRepository.telecomQinghaiEtl(taskid);
					break;
				case "新疆":
					etlStatus = mobileReportRepository.telecomXinjiangEtl(taskid);
					break;
				case "福建":
					etlStatus = mobileReportRepository.telecomFujianEtl(taskid);
					break;
				case "吉林":
					etlStatus = mobileReportRepository.telecomJilinEtl(taskid);
					break;
				case "上海":
					etlStatus = mobileReportRepository.telecomShanghaiEtl(taskid);
					break;
				case "河南":
					etlStatus = mobileReportRepository.telecomHenanEtl(taskid);
					break;
				case "宁夏":
					etlStatus = mobileReportRepository.telecomNingxiaEtl(taskid);
					break;
				case "四川":
					etlStatus = mobileReportRepository.telecomSichuanEtl(taskid);
					break;
				case "浙江":
					etlStatus = mobileReportRepository.telecomZhejiangEtl(taskid);
					break;
				case "山东":
					etlStatus = mobileReportRepository.telecomShandongEtl(taskid);
					break;
				case "江西":
					etlStatus = mobileReportRepository.telecomJiangxiEtl(taskid);
					break;
				case "贵州":
					etlStatus = mobileReportRepository.telecomGuizhouEtl(taskid);
					break;
				case "辽宁":
					etlStatus = mobileReportRepository.telecomLiaoningEtl(taskid);
					break;
				case "北京":
					etlStatus = mobileReportRepository.telecomBeijingEtl(taskid);
					break;
				case "云南":
					etlStatus = mobileReportRepository.telecomYunnanEtl(taskid);
					break;
				case "陕西":
					etlStatus = mobileReportRepository.telecomShanxi3Etl(taskid);
					break;
				case "内蒙古":
					etlStatus = mobileReportRepository.telecomNeimengguEtl(taskid);
					break;
				case "甘肃":
					etlStatus = mobileReportRepository.telecomGansuEtl(taskid);
					break;
				case "湖北":
					etlStatus = mobileReportRepository.telecomHubeiEtl(taskid);
					break;
				case "广西":
					etlStatus = mobileReportRepository.telecomGuangxiEtl(taskid);
					break;
				case "黑龙江":
					etlStatus = mobileReportRepository.telecomHeilongjiangEtl(taskid);
					break;
				default:
					etlStatus = "未知城市";
					break;
				}
			}
			tracer.addTag("ETL mobileReport:etlStatus---" + etlStatus, taskid);
			String reportStatus = mobileReportRepository.proMobileReport(taskid);
			taskMobilen.setEtlStatus(etlStatus);
			taskMobilen.setReportTime(new Date());
			taskMobilen.setReportStatus(reportStatus);
			taskMobileRepository.save(taskMobilen);
			tracer.addTag("ETL mobileReport:reportStatus---" + reportStatus, taskid);
		}
		tracer.addTag("ETL mobileReport---end", taskid);
	}
	
}
