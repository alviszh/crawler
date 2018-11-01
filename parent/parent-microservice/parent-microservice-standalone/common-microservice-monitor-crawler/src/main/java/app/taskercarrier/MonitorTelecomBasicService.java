package app.taskercarrier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.repository.crawler.monitor.MonitorTelecomTaskerRepository;

import app.client.CarrierClient;
import app.client.SmsClient;
import app.client.carrier.TaskClient;
import app.commontracerlog.TracerLog;
import app.exceptiondetail.EUtils;

//定义基本类，用于继承关系
@Component
public class MonitorTelecomBasicService {
	@Autowired
	public TracerLog tracer;
	@Autowired
	public EUtils eUtils;
	@Autowired
	public MonitorTelecomUtils executeTelecomHelper;
	@Autowired
	public MonitorTelecomTaskerRepository telecomTaskerRepository;
	@Autowired
	public TaskClient taskClient;   
	@Autowired
	public CarrierClient carrierClient;   //自己整理的client，用于调用运营商h5服务
	@Autowired
	public SmsClient smsClient;
}
