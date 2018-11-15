package app.eureka;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MonitorEurekaChange;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

/**
 * 直接根据rancher注册的微服务作为对比项，来监测eureka上的微服务变动
 * 
 * 该类用于检测eureka上的微服务变动情况
 * 
 * @author sln
 * 
 *         rancher上注册的微服务—响应的页面格式： 0=housingfund-tieling 1=housingfund-huhehaote
 *         2=housingfund-baotou
 *
 */
@Component
public class MonitorEurekaService {
	private static final Logger log = LoggerFactory.getLogger(MonitorEurekaService.class);
	@Autowired
	private EurekaClient eurekaClient;
	@Autowired
	private MonitorEurekaMailService eurekaMailService;
	// 要排除的监控组
	@Value("${exclude.group}")
	public String excludeGroup;
	// 要排除的监控项
	@Value("${exclude.item}")
	public String excludeItem;

	// 产品化的话，应用如下方法
	public void getRancherMicroServicesAndMonitorEureka() {
		List<MonitorEurekaChange> changeList = new ArrayList<MonitorEurekaChange>();
		// 用于存储当前环境下的微服务分组
		List<String> servicesGroupList = new ArrayList<String>();
		// 用于存储响应回来的页面中包含的微服务
		List<String> servicesList = new ArrayList<String>();
		// 获取该环境下所有的微服务分组
		try {
			String baseUrl = "http://rancher-metadata/latest/stacks/";
			WebRequest webRequest = new WebRequest(new URL(baseUrl), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			Page page = webClient.getPage(webRequest);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				// 根据换行获取所有的微服务分组
				String[] split = html.split("\n");
				for (String string : split) {
					// 获取所有微服务分组
					servicesGroupList.add((string.split("=")[1]).trim());
				}
			}
		} catch (Exception e) {
			log.info("获取该环境下所有的微服务分组过程中出现异常" + e.toString());
		}
		for (String eachGroup : servicesGroupList) {
			if(!excludeGroup.contains(eachGroup)){
				// 获取每个分组下的微服务（部分分组不再监控范围内）
				try {
					String url = "http://rancher-metadata/latest/stacks/" + eachGroup + "/services/";
					WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					WebClient webClient = WebCrawler.getInstance().getWebClient();
					webClient.getOptions().setJavaScriptEnabled(false);
					Page page = webClient.getPage(webRequest);
					if (null != page) {
						String html = page.getWebResponse().getContentAsString();
						// 根据换行获取所有的微服务
						String[] split = html.split("\n");
						for (String string : split) {
							// 获取所有注册的微服务
							servicesList.add((string.split("=")[1]).trim());
						}
					}
					MonitorEurekaChange monitorEurekaChange = null;
					Application app = null;
					if (null != servicesList && servicesList.size() > 0) {
						for (String appName : servicesList) {
							// 排除对无关项的监测
							if (!excludeItem.contains(appName)) {
								monitorEurekaChange = new MonitorEurekaChange();
								app = eurekaClient.getApplication(appName); // 从eureka上找寻指定的微服务
								if (app != null) { // eureka上存在该服务，取出实际节点数
									List<InstanceInfo> instancesList = app.getInstances();
									if (null != instancesList) {
										if (instancesList.size() == 0) {
											// 微服务不存在了
											monitorEurekaChange.setServicegroup(eachGroup);
											monitorEurekaChange.setAppname(appName);
											monitorEurekaChange.setChangedetail("微服务节点消失,请尽快处理~");
											changeList.add(monitorEurekaChange);
										}
									}
								} else {
									log.info(appName + "  在Eureka上未找到相关实例,请检查该微服务启动情况");
									monitorEurekaChange.setServicegroup(eachGroup);
									monitorEurekaChange.setAppname(appName);
									monitorEurekaChange.setChangedetail("微服务被外星人偷走了！！！");
									changeList.add(monitorEurekaChange);
								}
							}
						}
					}
				} catch (Exception e) {
					log.info("监控eureka上"+eachGroup+"分组下微服务变化情况时出现异常");
				}
			}
		}
		log.info("本轮Eureka微服务变化监控任务已经执行完毕");
		// 调用邮件通知服务
		if (changeList != null && changeList.size() > 0) {
			eurekaMailService.sendResultMail(changeList);
		}
	}
}
