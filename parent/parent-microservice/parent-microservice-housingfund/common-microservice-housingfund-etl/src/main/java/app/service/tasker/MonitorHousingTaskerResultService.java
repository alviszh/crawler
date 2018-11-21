package app.service.tasker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.etl.HousingDetailRepository;

import app.bean.MonitorHousingTaskerBean;
import app.commontracerlog.TracerLog;

@Component
public class MonitorHousingTaskerResultService {
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingDetailRepository housingDetailRepository;
	//============================================================
	//获取近24小时所有执行过的任务
	public List<MonitorHousingTaskerBean> housingEtlResultForOneDay() {
		List<MonitorHousingTaskerBean> todayList=new ArrayList<MonitorHousingTaskerBean>();
		Date date=new Date();  
        Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //获取24小时之前
        date = calendar.getTime();  
        //=============================================
        MonitorHousingTaskerBean monitorHousingTaskerBean=null;
        int etlTreatCount = 0;   //etl处理的通话记录总数
		List<TaskHousing> taskResultList = taskHousingRepository.findTaskResultForEtlByData(date);
		for (TaskHousing taskHousing : taskResultList) {
			String taskid = taskHousing.getTaskid().trim();
			try {
				//根据taskid获取etl处理的记录总数(流水记录)
				etlTreatCount = housingDetailRepository.countEltTreatResultByTaskId(taskid);
				monitorHousingTaskerBean=new MonitorHousingTaskerBean(taskHousing, etlTreatCount);
				todayList.add(monitorHousingTaskerBean);
			} catch (Exception e) {
				tracer.addTag("获取公积金爬取任务："+taskid+" 对应相关信息时出现异常：",e.toString());
			}
		}
		return todayList;
	}
}
