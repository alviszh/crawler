package app.service.tasker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.etl.ETLInsuranceDetailRepository;

import app.bean.MonitorInsuranceTaskerBean;
import app.commontracerlog.TracerLog;

@Component
public class MonitorInsuranceTaskerResultService {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired 
	private ETLInsuranceDetailRepository etlDetailRepository;
	//============================================================
	//获取近24小时所有执行过的任务
	public List<MonitorInsuranceTaskerBean> InsuranceEtlResultForOneDay() {
		List<MonitorInsuranceTaskerBean> todayList=new ArrayList<MonitorInsuranceTaskerBean>();
		Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //获取24小时之前
        date = calendar.getTime();  
        //=============================================
        MonitorInsuranceTaskerBean monitorInsuranceTaskerBean=null;
        int etlTreatCount = 0;   //etl处理的通话记录总数
		List<TaskInsurance> taskResultList = taskInsuranceRepository.findTaskResultForEtlByData(date);
		for (TaskInsurance taskInsurance : taskResultList) {
			String taskid = taskInsurance.getTaskid().trim();
			try {
				//根据taskid获取etl处理的记录总数(流水记录)
				etlTreatCount = etlDetailRepository.countEltTreatResultByTaskId(taskid);
				monitorInsuranceTaskerBean=new MonitorInsuranceTaskerBean(taskInsurance, etlTreatCount);
				todayList.add(monitorInsuranceTaskerBean);
			} catch (Exception e) {
				tracer.addTag("获取社保爬取任务："+taskid+" 对应相关信息时出现异常：",e.toString());
			}
		}
		return todayList;
	}
}
