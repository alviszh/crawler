package app.webusable;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.monitor.MonitorAllWebUsable;
import com.microservice.dao.repository.crawler.monitor.MonitorAllWebUsableRepository;

import app.commontracerlog.TracerLog;

@Component
public class MonitorWebUsableService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MonitorWebLinkStateService webLinkService;
	@Autowired
	private MonitorWebUsableMailService monitorResultMailService;
	@Autowired
	private MonitorAllWebUsableRepository allWebUsableRepository;
	@Value("${alldeveloper}")
	public String alldeveloper;    //注入汉字会出现乱码
	
	//执行监控任务(网站可用性)
	public void webUsableTasker(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String currentTime = df.format(System.currentTimeMillis());
		System.out.println("网站可用性监测任务---手动调用---已经按时启动，本次启动时间为："+currentTime);
		String taskid=UUID.randomUUID().toString();  
		String alldeveloper="wangpeiyang,zhaohui,hanyixing,zhangzhen,zengmanyi,sunlinan,tangzhen,zhaochunxiang,lishixiong,yanglei,qizhongbin,liuzhihao";
		String developers[]=alldeveloper.trim().split(",");
		//先监测出所有可用的网站
		monitorWebUsableByDeveloper(taskid,developers);   
	}
//	@Async
	public void monitorWebUsableByDeveloper(String taskid,String[] developers){
		Map<String, Future<String>> listfuture = new HashMap<String, Future<String>>();   //判断异步爬取是否完成
		//根据开发者将所有网站进行分组，执行
		for (String eachDeveloper : developers) {
			try {
				Future<String> future = webLinkService.monitorWebUsable(eachDeveloper,taskid);;
				listfuture.put("webUsable"+eachDeveloper, future);
			} catch (Exception e) {
				tracer.addTag("监控   "+eachDeveloper+" 负责的网站可用性时报异常，异常内容为：  ",e.toString());
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
			tracer.addTag("本轮网站可用性监测任务已经执行完毕","下一步调用邮件发送接口");
			//此处发送邮件，通知网站可用性监测情况，将状态码不是200的进行说明
			List<MonitorAllWebUsable> webNotUsableList = allWebUsableRepository.findWebNotUsable(taskid);
			if(webNotUsableList!=null && webNotUsableList.size()>0){
				//发送邮件
				monitorResultMailService.sendWebNotUsableMail(webNotUsableList);
			}
		} catch (Exception e) {
			tracer.addTag("本轮网站可用性监测任务，判断异步是否完成时候出现了异常，异常内容为：",e.toString());
		}
	}
	//====================如下内容为监控平台的展示功能做准备=================================
	//根据最近一次执行的task任务，响应所有网站的可用性信息
	public List<MonitorAllWebUsable> showAllWebUsable(){
		MonitorAllWebUsable topBean = allWebUsableRepository.findTopOrderByCreatetimeDesc();
		List<MonitorAllWebUsable> allWebList = null;
		if(null!=topBean){
			String recentlyTaskid = topBean.getTaskid().trim();
			allWebList = allWebUsableRepository.findAllWebByRecentlyTaskid(recentlyTaskid);
		}
		return allWebList;
	}
	//根据网站名称查询该网站指定天数内，每一天的最新记录，用于邮件展示，波动展示
	public List<MonitorAllWebUsable> showDaysWebUsableByWebType(String webtype){
		return allWebUsableRepository.findDaysWebUsableTaskerResult(webtype);
	}
	//====================如上内容为监控平台的展示功能做准备=================================
}
