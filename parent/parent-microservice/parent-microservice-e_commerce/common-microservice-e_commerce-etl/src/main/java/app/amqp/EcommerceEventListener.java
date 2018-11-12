package app.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;

import app.client.standalone.PhoneClient;
import app.commontracerlog.TracerLog;
import app.service.EcommerceETLReportService;
import app.service.amqp.inbound.Sink; 

@EnableBinding(Sink.class)
public class EcommerceEventListener {
	@Autowired
	private TracerLog tracer;
	
	/*@Autowired
	private EcommerceETLReportService ecommercemobileETLReportService;*/
	
	@Autowired
    private PhoneClient phoneClient;
	@Autowired
	private EcommerceETLReportService ecommerceETLReportService;
	
	@StreamListener(Sink.INPUT)
	public void messageSink(E_CommerceTask e_commerceTask) {
		tracer.addTag("ETL Received", e_commerceTask.toString()); 
		
		if (e_commerceTask.getFinished() !=null && e_commerceTask.getFinished() && e_commerceTask.getPhase().equals(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getPhase())
                && e_commerceTask.getPhase_status().equals(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhasestatus())) {
			tracer.qryKeyValue("Mobile数据采集完成触发存储过程",e_commerceTask.getTaskid());
			ecommerceETLReportService.ecommerceReport(e_commerceTask.getTaskid());
            tracer.qryKeyValue("Mobile数据采集完成触发存储过程end",e_commerceTask.getTaskid());
            
            /*tracer.addTag("Mobile数据采集完成触发存储过程end,开始查找数据",e_commerceTask.getTaskid());
            phoneClient.getInquireProMobile(e_commerceTask.getTaskid());//调用查询pro_mobile_call_info表接口   
*/        }
		
	}
}
