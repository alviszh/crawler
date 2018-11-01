package app.service.tasker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.cmcc.CmccUserCallResultRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileCallRecordDetailRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileCallInfoRepository;
import com.microservice.dao.repository.crawler.telecom.anhui.TelecomAnhuiRepositoryCall;
import com.microservice.dao.repository.crawler.telecom.beijing.TelecomBeijingCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.chongqing.TelecomChongqingCallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.fujian.TelecomFujianCallHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.gansu.TelecomGansuCallRepository;
import com.microservice.dao.repository.crawler.telecom.guangdong.TelecomGuangDongCallThremRepository;
import com.microservice.dao.repository.crawler.telecom.guangxi.TelecomGuangxiRepositoryCall;
import com.microservice.dao.repository.crawler.telecom.guizhou.TelecomGuizhouCallrecordRepository;
import com.microservice.dao.repository.crawler.telecom.hainan.TelecomHaiNanCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.hebei.TelecomHebeiCallRecRepository;
import com.microservice.dao.repository.crawler.telecom.heilongjiang.TelecomResultRepository;
import com.microservice.dao.repository.crawler.telecom.henan.TelecomHenanCallDetailRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiCallrecordsRepository;
import com.microservice.dao.repository.crawler.telecom.hunan.TelecomHunanCallHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.jiangsu.TelecomJiangsuCallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.jiangxi.TelecomJiangxiCallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinCallDetailsRepository;
import com.microservice.dao.repository.crawler.telecom.liaoning.TelecomLiaoNingCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.neimenggu.TelecomNeimengguCallHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.ningxia.TelecomNingxiaCallHistoryRepository;
import com.microservice.dao.repository.crawler.telecom.qinghai.TelecomQingHaiCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongCallDetailRepository;
import com.microservice.dao.repository.crawler.telecom.shanghai.TelecomShanghaiCallRecRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi1.TelecomShanxi1RecordRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3CallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.sichuan.TelecomSiChuanCallThremRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinCallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangVoiceRecordRepository;
import com.microservice.dao.repository.crawler.telecom.yunnan.TelecomYunNanCallThremResultRepository;
import com.microservice.dao.repository.crawler.telecom.zhejiang.TelecomZhejiangCallRecRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomCallThemResultRepository;

import app.bean.MonitorCarrierTempBean;
import app.commontracerlog.TracerLog;

/**
 * @description:运营商定时任务执行结果监控
 * @author: sln 
 * @date: 2018年4月8日 下午4:26:10 
 */
@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.mobile","com.microservice.dao.entity.crawler.cmcc","com.microservice.dao.entity.crawler.unicom","com.microservice.dao.entity.crawler.telecom"}) 
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.mobile","com.microservice.dao.repository.crawler.cmcc","com.microservice.dao.repository.crawler.unicom","com.microservice.dao.repository.crawler.telecom"})
public class MonitorCarrierTaskerResultService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private MobileCallRecordDetailRepository mobileCallRecordDetailRepository;
	@Autowired
	private ProMobileCallInfoRepository proMobileCallInfoRepository;
	//============================================================
	@Autowired
	private TelecomShanxi3CallRecordRepository telecomShanxi3CallRecordRepository;
	@Autowired
	private TelecomJiangxiCallRecordRepository telecomJiangxiCallRecordRepository;
	@Autowired
	private TelecomAnhuiRepositoryCall telecomAnhuiRepositoryCall;
	@Autowired
	private TelecomZhejiangCallRecRepository telecomZhejiangCallRecRepository;
	@Autowired
	private TelecomYunNanCallThremResultRepository telecomYunNanCallThremResultRepository;
	@Autowired
	private TelecomXinjiangVoiceRecordRepository telecomXinjiangVoiceRecordRepository;
	@Autowired
	private TelecomTianjinCallRecordRepository telecomTianjinCallRecordRepository;
	@Autowired
	private TelecomSiChuanCallThremRepository telecomSiChuanCallThremRepository;
	@Autowired
	private TelecomShanxi1RecordRepository telecomShanxi1RecordRepository;
	@Autowired
	private TelecomShanghaiCallRecRepository telecomShanghaiCallRecRepository;
	@Autowired
	private TelecomShandongCallDetailRepository telecomShandongCallDetailRepository;
	@Autowired	
	private TelecomQingHaiCallThremResultRepository telecomQingHaiCallThremResultRepository;
	@Autowired
	private TelecomNingxiaCallHistoryRepository telecomNingxiaCallHistoryRepository;
	@Autowired
	private TelecomNeimengguCallHistoryRepository telecomNeimengguCallHistoryRepository;
	@Autowired
	private TelecomLiaoNingCallThremResultRepository telecomLiaoNingCallThremResultRepository;
	@Autowired
	private TelecomJilinCallDetailsRepository telecomJilinCallDetailsRepository;
	@Autowired
	private TelecomJiangsuCallRecordRepository telecomJiangsuCallRecordRepository;
	@Autowired
	private TelecomHunanCallHistoryRepository telecomHunanCallHistoryRepository;
	@Autowired
	private TelecomHubeiCallrecordsRepository telecomHubeiCallrecordsRepository;
	@Autowired
	private TelecomResultRepository telecomResultRepository;
	@Autowired
	private TelecomHebeiCallRecRepository telecomHebeiCallRecRepository;
	@Autowired
	private UnicomCallThemResultRepository unicomCallThemResultRepository;
	@Autowired
	private TelecomHaiNanCallThremResultRepository telecomHaiNanCallThremResultRepository;
	@Autowired
	private TelecomGuizhouCallrecordRepository telecomGuizhouCallrecordRepository;
	@Autowired
	private TelecomGuangxiRepositoryCall telecomGuangxiRepositoryCall;
	@Autowired
	private TelecomGansuCallRepository telecomGansuCallRepository;
	@Autowired
	private TelecomGuangDongCallThremRepository telecomGuangDongCallThremRepository;
	@Autowired
	private TelecomFujianCallHistoryRepository telecomFujianCallHistoryRepository;
	@Autowired
	private TelecomBeijingCallThremResultRepository telecomBeijingCallThremResultRepository;
	@Autowired
	private TelecomChongqingCallRecordRepository telecomChongqingCallRecordRepository;
	@Autowired
	private CmccUserCallResultRepository cmccUserCallResultRepository;
	@Autowired
	private TelecomHenanCallDetailRepository telecomHenanCallDetailRepository;
	@Autowired
	private TracerLog tracer;
	//============================================================
	//获取近24小时所有执行过的运营商任务
	public List<MonitorCarrierTempBean> carrierEtlResultForOneDay(){
		List<MonitorCarrierTempBean> todayList=new ArrayList<MonitorCarrierTempBean>();
		List<TaskMobile> carrierTaskResultList = taskMobileRepository.findTaskResultForEtl();
		for (TaskMobile taskMobile : carrierTaskResultList) {
			String carrier = taskMobile.getCarrier().trim();  //获取运营商
			String taskid = taskMobile.getTaskid().trim();
			String province ="";
			try {
				province = taskMobile.getProvince().trim();  //获取省份
			} catch (Exception e) {
				province="null";
				tracer.addTag("获取的province------e------", e.toString());
			}
			if(!"null".equals(province)){
				todayList.add(getMonitorWebResult(carrier,province,taskid));
			}
		}
		return todayList;
	}
	//获取近10天tasker的执行结果
	public List<MonitorCarrierTempBean> carrierEtlResultForMoreDay() {
		List<MonitorCarrierTempBean> taskerList=new ArrayList<MonitorCarrierTempBean>();
		List<TaskMobile> taskerTenDaysResultList = taskMobileRepository.findTenDaysTaskResultOwnerIsTasker();
		for (TaskMobile taskMobile : taskerTenDaysResultList) {
			String carrier = taskMobile.getCarrier().trim();  //获取运营商
			String taskid = taskMobile.getTaskid().trim();
			String province ="";
			try {
				province = taskMobile.getProvince().trim();  //获取省份
			} catch (Exception e) {
				province="null";
				tracer.addTag("获取的province------e------", e.toString());
			}
			if(!"null".equals(province)){
				taskerList.add(getMonitorWebResult(carrier,province,taskid));
			}
		}
		return taskerList;
	}
	public MonitorCarrierTempBean getMonitorWebResult(String carrier,String province,String taskid){
		try {
			int taskCallCount = 0 ;   //运营商中爬取的通话记录总数
			int etlTreatCallCount = 0 ;  //etl处理的通话记录总数
			int proTreatCallCount =0 ;  //产品化处理的通话记录总数
			String developer = "";    //网站负责人
			String isNeedSms = "是1";
			TaskMobile taskMobile =null;
			MonitorCarrierTempBean monitorCarrierTempBean=null;
			if(carrier.equals("CMCC")){  //移动
				isNeedSms = "是2";
				developer="张振";
				taskCallCount=cmccUserCallResultRepository.countByTaskId(taskid);
			}else if(carrier.equals("UNICOM")){  //联通
				isNeedSms = "是2";
				developer="韩译兴";
				taskCallCount = unicomCallThemResultRepository.countByTaskid(taskid);
			}else{   //电信
				if (province.equals("黑龙江")) {    //根据不同的省份去不同的表中查询
					developer="韩译兴";
					taskCallCount=telecomResultRepository.countByTaskid(taskid);
		        } else if (province.equals("山西")) {
		        	developer="唐振";
		        	taskCallCount=telecomShanxi1RecordRepository.countByTaskid(taskid);
		        } else if (province.equals("宁夏")) {
		        	developer="齐忠斌";
		        	taskCallCount=telecomNingxiaCallHistoryRepository.countByTaskid(taskid);
		        } else if (province.equals("新疆")) {
		        	isNeedSms = "否";
		            developer="赵春香";
		            taskCallCount=telecomXinjiangVoiceRecordRepository.countByTaskid(taskid);
		        } else if (province.contains("内蒙古")) {
		        	developer="齐忠斌";
		        	taskCallCount=telecomNeimengguCallHistoryRepository.countByTaskid(taskid);
		        } else if (province.equals("四川")) {
		        	developer="刘芷豪";
		        	taskCallCount=telecomSiChuanCallThremRepository.countByTaskid(taskid);
			    } else if (province.equals("山东")) {
			    	developer="王培阳";
			    	taskCallCount=telecomShandongCallDetailRepository.countByTaskid(taskid);
		        } else if (province.equals("重庆")) {
		        	developer="唐振";
		        	taskCallCount=telecomChongqingCallRecordRepository.countByTaskid(taskid);
		        } else if (province.equals("湖南")) {
		        	developer="齐忠斌";
		        	taskCallCount=telecomHunanCallHistoryRepository.countByTaskid(taskid);
		        } else if (province.equals("贵州")) {
		        	developer="赵春香";
		        	taskCallCount=telecomGuizhouCallrecordRepository.countByTaskid(taskid);
		        } else if (province.equals("福建")) {
		        	developer="齐忠斌";
		        	taskCallCount=telecomFujianCallHistoryRepository.countByTaskid(taskid);
		        } else if (province.equals("云南")) {
		        	developer="韩译兴";
		        	taskCallCount=telecomYunNanCallThremResultRepository.countByTaskid(taskid);
		        } else if (province.equals("上海")) {
		        	developer="张振";
		        	taskCallCount=telecomShanghaiCallRecRepository.countByTaskid(taskid);
		        } else if (province.equals("广西")) {
		        	isNeedSms = "是2";
		        	developer="杨磊";
		        	taskCallCount=telecomGuangxiRepositoryCall.countByTaskid(taskid);
		        } else if (province.equals("河南")) {
		        	developer="王培阳";
		        	taskCallCount=telecomHenanCallDetailRepository.countByTaskid(taskid);
		        } else if (province.equals("江西")) {
		        	isNeedSms = "是2";
		            developer="孙利楠";
		            taskCallCount = telecomJiangxiCallRecordRepository.countByTaskid(taskid); 
		        } else if (province.equals("广东")) {
		        	developer="刘芷豪";
		        	taskCallCount=telecomGuangDongCallThremRepository.countByTaskid(taskid);
		        } else if (province.equals("湖北")) {
		        	developer="赵春香";
		        	taskCallCount=telecomHubeiCallrecordsRepository.countByTaskid(taskid);
		        } else if (province.equals("海南")) {
		        	developer="韩译兴";
		        	taskCallCount=telecomHaiNanCallThremResultRepository.countByTaskid(taskid);
		        } else if (province.equals("安徽")) {
		        	developer="杨磊";
		        	taskCallCount = telecomAnhuiRepositoryCall.countByTaskid(taskid);
		        } else if (province.equals("浙江")) {
		        	developer="张振";
		        	taskCallCount=telecomZhejiangCallRecRepository.countByTaskid(taskid);
		        } else if (province.equals("河北")) {
		        	developer="张振";
		        	taskCallCount=telecomHebeiCallRecRepository.countByTaskid(taskid);
		        } else if(province.equals("天津")){
		        	isNeedSms = "否";
		        	developer="孙利楠";
		        	taskCallCount=telecomTianjinCallRecordRepository.countByTaskid(taskid);
		        } else if(province.equals("青海")){
		        	isNeedSms = "否";
		        	developer="韩译兴";
		        	taskCallCount=telecomQingHaiCallThremResultRepository.countByTaskid(taskid);
		        } else if(province.equals("吉林")){
		        	developer="王培阳";
		        	taskCallCount=telecomJilinCallDetailsRepository.countByTaskid(taskid);
		        } else if(province.equals("辽宁")){
		        	isNeedSms = "否";
		        	developer="刘芷豪";
		        	taskCallCount=telecomLiaoNingCallThremResultRepository.countByTaskid(taskid);
		        } else if(province.equals("北京")){
		        	isNeedSms = "否";
		        	developer="韩译兴";
		        	taskCallCount=telecomBeijingCallThremResultRepository.countByTaskid(taskid);
		        } else if(province.equals("江苏")){
		        	isNeedSms = "否";
		        	developer="唐振";
		        	taskCallCount=telecomJiangsuCallRecordRepository.countByTaskid(taskid);
		        } else if(province.equals("陕西")){
		        	isNeedSms = "否";
		        	developer="孙利楠";
		        	taskCallCount=telecomShanxi3CallRecordRepository.countByTaskid(taskid);
		        } else if(province.equals("甘肃")){
		        	isNeedSms = "否";
		        	developer="杨磊";
		        	taskCallCount=telecomGansuCallRepository.countByTaskid(taskid);
		        }else{
		        	tracer.addTag("出现测试的数据没有存储运营商省份", "监测邮件忽视该数据");
		        }
			}
			//根据taskid获取etl处理的通话记录的总数
			etlTreatCallCount = mobileCallRecordDetailRepository.countEltTreatResultByTaskId(taskid);
			//etl产品化处理的通话记录总数
			proTreatCallCount=proMobileCallInfoRepository.countByTaskId(taskid);
			//根据taskid查询该任务下的taskmobile信息
			taskMobile = taskMobileRepository.findByTaskid(taskid);
	    	monitorCarrierTempBean=new  MonitorCarrierTempBean(taskMobile, taskCallCount, etlTreatCallCount, developer,isNeedSms,proTreatCallCount);
	    	return monitorCarrierTempBean;
		} catch (Exception e) {
			tracer.addTag("获取"+carrier+"   "+province+"   "+taskid+"   "+"相关信息时出现异常：",e.toString());
		}
		return null;  //出现异常
	}
}
