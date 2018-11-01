package app.webusable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.monitor.json.MonitorWebUsableBean;
import com.microservice.dao.entity.crawler.monitor.MonitorAllWebUsable;
import com.microservice.dao.entity.crawler.monitor.MonitorAllWebLoginUrl;
import com.microservice.dao.repository.crawler.monitor.MonitorAllWebUsableRepository;
import com.microservice.dao.repository.crawler.monitor.MonitorAllWebLoginUrlRepository;

import app.commontracerlog.TracerLog;
import app.utils.WebUsableUtils;

/**
 * @description:监测网站可用性   
 * 			
 * @author: sln 
 * @date: 2018年3月9日 上午10:15:21 
 */
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.monitor")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.monitor")
public class MonitorWebLinkStateService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private  MonitorAllWebLoginUrlRepository allWebLoginUrlRepository;
	@Autowired
	private MonitorAllWebUsableRepository allWebUsableRepository;
	//如果一次性取出所有的城市，判断网站可用性，将会卡死，所以暂时决定根据负责人进行分组，来监测
	@Async
	public Future<String> monitorWebUsable(String developer,String taskid){
		List<MonitorAllWebLoginUrl> list =allWebLoginUrlRepository.findUrlByDeveloper(developer);   
		if(list.size()>0){
			String loginUrl="";
			long urlId;
			String webType="";
			String linkway="";
			boolean usableflag=true;
			MonitorAllWebUsable monitorAllWebUsable=null;
			MonitorWebUsableBean monitorWebUsableBean =null;
			List<MonitorAllWebUsable> webStatusList = new ArrayList<MonitorAllWebUsable>();
			for (MonitorAllWebLoginUrl allWebLoginUrl : list) {
				loginUrl=allWebLoginUrl.getUrl().trim();
				urlId=allWebLoginUrl.getId();
				webType=allWebLoginUrl.getWebtype().trim();
				linkway=allWebLoginUrl.getLinkway().trim();
				if(linkway.equals("default")){
					monitorWebUsableBean = WebUsableUtils.connectingAddress(loginUrl,webType);
				}else if(linkway.equals("htmlunit")){
					monitorWebUsableBean = WebUsableUtils.connectingByHtmlunit(loginUrl,webType);
				}else if(linkway.equals("ssl")){
					monitorWebUsableBean = WebUsableUtils.connectingByIgnoreSSL(loginUrl,webType);
				}else if(linkway.equals("httpclient")){  //工商银行摘取出来的登录页面就需要用这样的方式
					monitorWebUsableBean = WebUsableUtils.connectingByHttpClient(loginUrl,webType);
				}
				int webStatusCode=monitorWebUsableBean.getWebStatusCode();
				if(webStatusCode==200){  
					//将网站可用性字段更新为t
					usableflag=true;
					allWebLoginUrlRepository.updateUsableFlag(usableflag, urlId);
				}else{   
					tracer.addTag(webType+"网站异常------响应的状态码是："+webStatusCode,"登录链接是："+loginUrl);
					//将网站可用性字段更新为f
					usableflag=false;
					allWebLoginUrlRepository.updateUsableFlag(usableflag, urlId);
				}
				//不论网站监测结果如何，都将本次监控的结果进行存储
				monitorAllWebUsable=new MonitorAllWebUsable();
				monitorAllWebUsable.setIsusable(usableflag);
				monitorAllWebUsable.setStatuscode(webStatusCode);
				monitorAllWebUsable.setExceptioninfo(monitorWebUsableBean.getExceptioninfo().trim());
				monitorAllWebUsable.setUrl(loginUrl);
				monitorAllWebUsable.setUrlid(urlId);
				monitorAllWebUsable.setWebtype(webType);
				monitorAllWebUsable.setTaskid(taskid);
				monitorAllWebUsable.setDeveloper(developer);
				webStatusList.add(monitorAllWebUsable);
			}
			allWebUsableRepository.saveAll(webStatusList);
		}
		return new AsyncResult<String>("200");
	}
}
