package app.amqp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.client.standalone.PhoneClient;
import app.commontracerlog.TracerLog;
import app.service.MobileETLReportService;
import app.service.amqp.inbound.Sink; 
 
@EnableBinding(Sink.class)
public class CarrierEventListener {
	
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private MobileETLReportService mobileETLReportService;
	
	@Autowired
    private PhoneClient phoneClient;
	
	
	@StreamListener(Sink.INPUT)
	public void messageSink(TaskMobile taskMobile) {
		tracer.addTag("ETL Received", taskMobile.toString()); 
		
		if (taskMobile.getFinished() !=null && taskMobile.getFinished() && taskMobile.getPhase().equals(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getPhase())
                && taskMobile.getPhase_status().equals(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getPhasestatus())) {
			tracer.qryKeyValue("Mobile数据采集完成触发存储过程",taskMobile.getTaskid());
			mobileETLReportService.mobileReport(taskMobile.getTaskid());
            tracer.qryKeyValue("Mobile数据采集完成触发存储过程end",taskMobile.getTaskid());
            
            tracer.addTag("Mobile数据采集完成触发存储过程end,开始查找数据",taskMobile.getTaskid());
            phoneClient.getInquireProMobile(taskMobile.getTaskid());//调用查询pro_mobile_call_info表接口   
        }
		
	}
}
