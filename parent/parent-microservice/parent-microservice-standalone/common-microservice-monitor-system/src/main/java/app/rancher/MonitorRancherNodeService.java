package app.rancher;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MonitorRancherChange;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.monitor.MonitorRancherInfo;
import com.microservice.dao.repository.crawler.monitor.MonitorRancherInfoRepository;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 该类用于rancher上的主机网络的可连接性
 * 需要模拟登录rancher,注意：涉及到了反爬虫中的CSRF,请求头中需要加上cookie
 * 
 * 该类用的是模拟登陆的方式（弃用）
 * 
 * @author sln
 *
 */
@Component
public class MonitorRancherNodeService {
	private static final Logger log = LoggerFactory.getLogger(MonitorRancherNodeService.class);
	@Autowired
	private MonitorRancherInfoRepository rancherInfoRepository;
//	@Autowired
//	private MonitorRancherNodeMailService rancherNodeMailService;
	@Value("${rancherhost}") 
	public String rancherhost;
	//rancher硬盘占用阈值
	@Value("${rancherdisk}") 
	public double rancherdisk;
	//rancher的swap剩余阈值
	@Value("${rancherswap}") 
	public double rancherswap;
	public void rancherTasker(){
		//用于存储满足报警条件的信息
		List<MonitorRancherChange> warnList=new ArrayList<MonitorRancherChange>();
		//取出所有需要监控的rancher环境的信息
		List<MonitorRancherInfo> list = rancherInfoRepository.findAllNeedMonitor();
		if(null!=list && list.size()>0){  //有需要监控的内容
			try {
				String url="http://"+rancherhost+"/v2-beta/token";
				WebClient webClient = WebCrawler.getInstance().getWebClient(); 
				webClient.getOptions().setJavaScriptEnabled(false);
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody="{\"code\":\"rancher:12qwaszx\",\"authProvider\":\"localauthconfig\"}";
				//如下请求头必须加
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setAdditionalHeader("accept", "application/json");
				webRequest.setAdditionalHeader("content-type", "application/json");
				webRequest.setAdditionalHeader("Host", ""+rancherhost+"");
				webRequest.setRequestBody(requestBody);
				Page page = webClient.getPage(webRequest); 
				if(null!=page){
					String html = page.getWebResponse().getContentAsString();
					if(html.contains("jwt")){
						String jwt = JSONObject.fromObject(html).getString("jwt");
						if(jwt.length()>0){  //字符串长度大于0，能够成功获取请求需要的token
							MonitorRancherChange rancherChange= null;
							String baseaddr;
							String envirtype;
							//============================
							String jsonString;
							String hostname;
							String agentIpAddress;
							String state;
							String selflink;
							String swapfree;
							String swaptotal;
							double swapfreeDouble;
							double swaptotalDouble;
							double memfreeProp = 0;  
							String diskInfoProp;
							double diskInfoPropDouble;
							//============================
							//取出几种环境的基础信息，进行处理
							for (MonitorRancherInfo rancherInfo : list) {
								rancherChange = new MonitorRancherChange();
								baseaddr = rancherInfo.getBaseaddr().trim();
								envirtype = rancherInfo.getEnvirtype().trim();
								url="http://"+rancherhost+"/v2-beta/projects/"+baseaddr+"/hosts";
								webRequest = new WebRequest(new URL(url), HttpMethod.GET);
								webRequest.setAdditionalHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
								webRequest.setAdditionalHeader("Cookie", "PL=rancher; CSRF=45FAC5086F; token="+jwt+"");
								webRequest.setAdditionalHeader("Host", "10.167.211.158:8080");
								page = webClient.getPage(webRequest); 
								if(null!=page){
									html=page.getWebResponse().getContentAsString();
									if(html.contains("state")){ //可以获取网络连接状态
										//响应回来的页面会有js代码干扰，故如下方式截取
										jsonString = html.substring(html.indexOf("\"type")-1, html.lastIndexOf("}")+1);
										JSONArray jsonArray = JSONObject.fromObject(jsonString).getJSONArray("data");
										for(int i=0;i<jsonArray.size();i++){
											hostname = jsonArray.getJSONObject(i).getString("hostname");
											agentIpAddress = jsonArray.getJSONObject(i).getString("agentIpAddress");
											state = jsonArray.getJSONObject(i).getString("state");
											selflink = jsonArray.getJSONObject(i).getJSONObject("links").getString("self");
											swapfree = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("memoryInfo").getString("swapfree");
											swaptotal = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("memoryInfo").getString("swaptotal");
											swapfreeDouble = Double.parseDouble(swapfree);
											swaptotalDouble = Double.parseDouble(swaptotal);
//											swap的free和total如果都是0，或者total是0，说明系统不允许在内存不足的情况下，将硬盘容量转化为内存使用（此功能未开启）
											if(swaptotalDouble!=0){ 
												memfreeProp=swapfreeDouble/swaptotalDouble*100;
												memfreeProp=Math.round(memfreeProp*100)/100;
											}
											//硬盘使用率
											if(envirtype.contains("UAT")){  
												diskInfoProp = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints").getJSONObject("/dev/mapper/centos-root").getString("percentage");
											}else{   //其他环境，不光涉及到一种定位   （/dev/sda3或者/dev/vda1）
												try {   //如果报了空指针异常，说明是另外一种定位方式
													diskInfoProp = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints").getJSONObject("/dev/vda1").getString("percentage");
												} catch (Exception e) {
													diskInfoProp = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints").getJSONObject("/dev/sda3").getString("percentage");
												}
											}
											diskInfoPropDouble = Double.parseDouble(diskInfoProp);
											diskInfoPropDouble=Math.round(diskInfoPropDouble*100)/100;
											//主机网络连接不上，或者硬盘使用率大于等于90%，或者swap小于10%，就纳入报警邮件信息
											if(!state.equals("active") || (memfreeProp>0 && memfreeProp<=rancherswap) || diskInfoPropDouble>=rancherdisk){
												rancherChange=new MonitorRancherChange();
												rancherChange.setDiskprop(diskInfoPropDouble+"%");
												rancherChange.setIp(agentIpAddress);
												rancherChange.setNetstate(state);
												rancherChange.setNodename(hostname);
												if(memfreeProp==0){  //说明系统未启用硬盘转内存功能
													rancherChange.setSwapfree("--");
												}else{
													rancherChange.setSwapfree(memfreeProp+"%");
												}
												rancherChange.setSelflink(selflink);
												rancherChange.setEnvirtype(envirtype);
												warnList.add(rancherChange);
											}
										}
									}
								}
							}
						}else{
							log.info("rancher未能成功获取token，故无法进行接下来的监控操作");
						}
					}
				}
			} catch (Exception e) {
				log.info("监控rancher信息时出现异常："+e.toString());
			}
		}else{
			log.info("数据库记录中暂时未提供需要监控的rancher环境的信息");
		}
		//调用邮件通知服务
//		if(warnList!=null && warnList.size()>0){
//			rancherNodeMailService.sendResultMail(warnList,rancherdisk,rancherswap);
//		}
	}
}
