package app.rancher;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MonitorRancherChange;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 用授权的方式登陆rancher，访问api
 * 
 * @author sln
 *
 */
@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.monitor")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.monitor")
public class MonitorRancherService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MonitorRancherNodeMailService rancherNodeMailService;
	@Value("${rancherhost}") 
	public String rancherhost;
	//rancher硬盘占用阈值
	@Value("${rancherdisk}") 
	public double rancherdisk;
	//rancher的swap剩余阈值
	@Value("${rancherswap}") 
	public double rancherswap;
	//rancher的cpu阈值
	@Value("${ranchercpu}") 
	public double ranchercpu;
	
	public void rancherTasker(){
		//用于存储满足报警条件的信息
		List<MonitorRancherChange> warnList=new ArrayList<MonitorRancherChange>();
		try {
			//所有需要监控的环境通过基本的url获取到
			String baseUrl="http://676AF07DFFA0C10A51C9:uoGBrQfv4RMCXq5XzMWG9kfXosTmheEWgTHgSUSR@10.167.211.158:8080/v2-beta/projects";
			WebRequest webRequest = new WebRequest(new URL(baseUrl), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient(); 
			webClient.getOptions().setJavaScriptEnabled(false);
			Page page = webClient.getPage(webRequest); 
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				//响应回来的页面会有js代码干扰，故如下方式截取
				String jsonString = html.substring(html.indexOf("\"type")-1, html.lastIndexOf("}")+1);
				//获取应有的环境
				JSONArray jsonArray = JSONObject.fromObject(jsonString).getJSONArray("data");
				int size = jsonArray.size();
				//================================
				MonitorRancherChange rancherChange= null;
				String baseaddr;
				String envirtype;
				String nodeUrl;
				//============================
				String hostname;
				String agentIpAddress;
				String state;
				String selflink;
				double swapfree;
				double swaptotal;
				double memfreeProp = 0;  
				double diskInfoProp = 0;
				String rancherCpuProp = "未超标";
				//================================
				//遍历获取所有需要监控的环境
				for(int n=0;n<size;n++){
					baseaddr = jsonArray.getJSONObject(n).getString("id");   //环境baseaddr
					envirtype = jsonArray.getJSONObject(n).getString("name");  //环境名
					//=========================================
					//获取该环境id下的所有主机信息
					try {
						nodeUrl="http://676AF07DFFA0C10A51C9:uoGBrQfv4RMCXq5XzMWG9kfXosTmheEWgTHgSUSR@"+rancherhost+"/v2-beta/projects/"+baseaddr+"/hosts";
						rancherChange = new MonitorRancherChange();
						webRequest = new WebRequest(new URL(nodeUrl), HttpMethod.GET);
						page = webClient.getPage(webRequest); 
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							if(html.contains("state")){ //可以获取网络连接状态
								//响应回来的页面会有js代码干扰，故如下方式截取
								jsonString = html.substring(html.indexOf("\"type")-1, html.lastIndexOf("}")+1);
								jsonArray = JSONObject.fromObject(jsonString).getJSONArray("data");
								for(int i=0;i<jsonArray.size();i++){
									hostname = jsonArray.getJSONObject(i).getString("hostname");
									agentIpAddress = jsonArray.getJSONObject(i).getString("agentIpAddress");
									state = jsonArray.getJSONObject(i).getString("state");
									selflink = jsonArray.getJSONObject(i).getJSONObject("links").getString("self");
									swapfree = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("memoryInfo").getDouble("swapfree");
									swaptotal = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("memoryInfo").getDouble("swaptotal");
									//================================================================================
									//某个进程cpu占比超标——导致整体cpu占比超标的监测
									JSONArray jsonArrayCpu = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("cpuInfo").getJSONArray("loadAvg");
									size = jsonArrayCpu.size();
									for(int j=0;j<size;j++){
										//遍历的时候，只要有一个超标，就跳出循环，认为这个主机cpu使用占比超标
										if(Double.parseDouble((String) jsonArrayCpu.get(j))>ranchercpu){ //超标
//											System.out.println("超标的数据是："+jsonArrayCpu.get(j));
											rancherCpuProp=(String) jsonArrayCpu.get(j);
											break;   //跳出这个for循环
										}
									}
									//================================================================================
//									swap的free和total如果都是0，或者total是0，说明系统不允许在内存不足的情况下，将硬盘容量转化为内存使用（此功能未开启）
									if(swaptotal!=0){ 
										memfreeProp=swapfree/swaptotal*100;
										memfreeProp=Math.round(memfreeProp*100)/100;
									}
									//================================================================================
									//硬盘使用率---迭代的方式(不同的主机key值不一样)
									for(int m=0;m<jsonArray.size();m++){
										JSONObject jsonObject = jsonArray.getJSONObject(m).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints");
										@SuppressWarnings("unchecked")
										Iterator<String> it = jsonObject.keys();
										while(it.hasNext()){
											// 获得key
											String key = it.next(); 
											String diskInfoPropJson = jsonObject.getString(key);   
											JSONObject fromObject = JSONObject.fromObject(diskInfoPropJson);
											diskInfoProp=fromObject.getDouble("percentage");
											}
										System.out.println("==============================");
									}
									diskInfoProp=Math.round(diskInfoProp*100)/100;
									//================================================================================
									//主机网络连接不上，或者硬盘使用率大于等于90%，或者swap小于10%，就纳入报警邮件信息
									if(!state.equals("active") || (memfreeProp>0 && memfreeProp<=rancherswap) || diskInfoProp>=rancherdisk){
										rancherChange=new MonitorRancherChange();
										rancherChange.setDiskprop(diskInfoProp+"");
										rancherChange.setIp(agentIpAddress);
										rancherChange.setNetstate(state);
										rancherChange.setNodename(hostname);
										if(memfreeProp==0){  //说明系统未启用硬盘转内存功能
											rancherChange.setSwapfree("--");
										}else{
											rancherChange.setSwapfree(memfreeProp+"");
										}
										rancherChange.setSelflink(selflink);
										rancherChange.setEnvirtype(envirtype);
										rancherChange.setLoadavg(rancherCpuProp);
										warnList.add(rancherChange);
									}
								}
							}else{
								tracer.addTag(baseaddr," 对应的rancher网络环境未能正常响应api信息");
							}
						}
					
					} catch (Exception e) {
						tracer.addTag("监控rancher信息时出现异常：",e.toString());
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("监控rancher信息时出现异常：",e.toString());
		}
		//调用邮件通知服务
		if(warnList!=null && warnList.size()>0){
			rancherNodeMailService.sendResultMail(warnList,rancherdisk,rancherswap,ranchercpu);
		}
		tracer.addTag("本日执行记录", "rancher各主机指标监测任务~执行完毕");
	}
}
