package app.webchange;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import app.commontracerlog.TracerLog;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.monitor")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.monitor")
public class MonitorWebChangeService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MonitorWebChangeMailService monitorResultMailService;
	@Autowired
	private MonitorService monitorService;
//	@Value("${alldeveloper}")   //注入会出现乱码
//	String alldeveloper;
	//////////////////////////////////////////////////////////////////////////////////////
	//执行监控任务(网站变化)
	public void webChangeTasker(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String currentTime = df.format(System.currentTimeMillis());
		//为本次监控任务指定taskid
		String taskid=UUID.randomUUID().toString(); 
		//由于读取的内容是乱码，暂时如下方式，直接将姓名写入
		String alldeveloper="王培阳,赵辉,韩译兴,张振,曾满意,孙利楠,唐振,赵春香,李世雄,杨磊,齐忠斌,刘芷豪";
		String developers[]=alldeveloper.trim().split(",");
		//监控网站变化
		tracer.addTag("网站改版情况监测任务---手动调用---已经按时启动，本次启动时间为：",currentTime);
		monitorWebByDeveloper(developers,taskid);
	}
	//异步监控所有人的网站
	@Async
	public void monitorWebByDeveloper(String[] developers,String taskid){
		Map<String, Future<String>> listfuture = new HashMap<String, Future<String>>();   //判断异步爬取是否完成
		//根据开发者将所有网站进行分组，执行
		for (String eachDeveloper : developers) {
			try {
				Future<String> future = monitorService.monitorWeb(eachDeveloper,taskid);
				listfuture.put("webChange"+eachDeveloper, future);
			} catch (Exception e) {
				tracer.addTag("监控   "+eachDeveloper+" 负责的网站改版情况任务时报异常，异常内容为：  ",e.toString());
			}
		}
		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			tracer.addTag("本轮网站改版情况监测任务已经执行完毕","下一步调用邮件发送接口");
			//调用发邮件端口，将本次执行任务的结果进行邮件通知
			monitorResultMailService.sendWebChangeMail(taskid);
		} catch (Exception e) {
			tracer.addTag("本轮网站改版情况监测任务，判断异步是否完成时候出现了异常，异常内容为：",e.toString());
		}
	}
}
