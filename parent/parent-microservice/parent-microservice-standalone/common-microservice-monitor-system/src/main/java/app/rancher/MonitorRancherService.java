package app.rancher;

import java.net.URL;
import java.util.ArrayList;
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
import com.microservice.dao.entity.crawler.monitor.MonitorRancherInfo;
import com.microservice.dao.repository.crawler.monitor.MonitorRancherInfoRepository;
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
	private MonitorRancherInfoRepository rancherInfoRepository;
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
		//取出所有需要监控的rancher环境的信息
		List<MonitorRancherInfo> list = rancherInfoRepository.findAllNeedMonitor();
		if(null!=list && list.size()>0){  //有需要监控的内容
			try {
				WebClient webClient = WebCrawler.getInstance().getWebClient(); 
				webClient.getOptions().setJavaScriptEnabled(false);
				MonitorRancherChange rancherChange= null;
				String baseaddr;
				String envirtype;
				//============================
				String jsonString;
				String hostname;
				String agentIpAddress;
				String state;
				String selflink;
				double swapfree;
				double swaptotal;
				double memfreeProp = 0;  
				double diskInfoProp;
				String rancherCpuProp = "未超标";
				//============================
				//取出几种环境的基础信息，进行处理
				for (MonitorRancherInfo rancherInfo : list) {
					rancherChange = new MonitorRancherChange();
					baseaddr = rancherInfo.getBaseaddr().trim();
					envirtype = rancherInfo.getEnvirtype().trim();
					String url="http://676AF07DFFA0C10A51C9:uoGBrQfv4RMCXq5XzMWG9kfXosTmheEWgTHgSUSR@"+rancherhost+"/v2-beta/projects/"+baseaddr+"/hosts";
					WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page = webClient.getPage(webRequest); 
					if(null!=page){
						String html=page.getWebResponse().getContentAsString();
						if(html.contains("state")){ //可以获取网络连接状态
							//响应回来的页面会有js代码干扰，故如下方式截取
							jsonString = html.substring(html.indexOf("\"type")-1, html.lastIndexOf("}")+1);
							JSONArray jsonArray = JSONObject.fromObject(jsonString).getJSONArray("data");
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
								int size = jsonArrayCpu.size();
								for(int j=0;j<size;j++){
									//遍历的时候，只要有一个超标，就跳出循环，认为这个主机cpu使用占比超标
									if(Double.parseDouble((String) jsonArrayCpu.get(j))>ranchercpu){ //超标
//										System.out.println("超标的数据是："+jsonArrayCpu.get(j));
										rancherCpuProp=(String) jsonArrayCpu.get(j);
										break;   //跳出这个for循环
									}
//									System.out.println(jsonArrayCpu.get(j));
								}
								//================================================================================
//								swap的free和total如果都是0，或者total是0，说明系统不允许在内存不足的情况下，将硬盘容量转化为内存使用（此功能未开启）
								if(swaptotal!=0){ 
									memfreeProp=swapfree/swaptotal*100;
									memfreeProp=Math.round(memfreeProp*100)/100;
								}
								//================================================================================
								//硬盘使用率
								if(envirtype.contains("爬虫UAT环境")){  
									diskInfoProp = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints").getJSONObject("/dev/mapper/centos-root").getDouble("percentage");
								}else if(envirtype.contains("爬虫开发环境") || envirtype.contains("舆情开发环境")){   //其他环境，不光涉及到一种定位   （/dev/sda3或者/dev/vda1）
									try {   //如果报了空指针异常，说明是另外一种定位方式
										diskInfoProp = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints").getJSONObject("/dev/vda1").getDouble("percentage");
									} catch (Exception e) {
										diskInfoProp = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints").getJSONObject("/dev/sda3").getDouble("percentage");
									}
								}else{   //mysql兼容性
									try {   //如果报了空指针异常，说明是另外一种定位方式
										diskInfoProp = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints").getJSONObject("/dev/mapper/RootVg-root").getDouble("percentage");
									} catch (Exception e) {
										diskInfoProp = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints").getJSONObject("/dev/mapper/centos-root").getDouble("percentage");
									}
								}
								diskInfoProp=Math.round(diskInfoProp*100)/100;
								//================================================================================
								//主机网络连接不上，或者硬盘使用率大于等于90%，或者swap小于10%，就纳入报警邮件信息
								if(!state.equals("active") || (memfreeProp>0 && memfreeProp<=rancherswap) || diskInfoProp>=rancherdisk){
									rancherChange=new MonitorRancherChange();
									rancherChange.setDiskprop(diskInfoProp+"%");
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
									rancherChange.setLoadavg(rancherCpuProp);
									warnList.add(rancherChange);
								}
							}
						}else{
							tracer.addTag(baseaddr," 对应的rancher网络环境未能正常响应api信息");
						}
					}
				}
			} catch (Exception e) {
				tracer.addTag("监控rancher信息时出现异常：",e.toString());
			}
		}else{
			tracer.addTag("数据库记录中暂时未提供需要监控的rancher环境的信息","请及时添加~");
		}
		//调用邮件通知服务
		if(warnList!=null && warnList.size()>0){
			rancherNodeMailService.sendResultMail(warnList,rancherdisk,rancherswap,ranchercpu);
		}
		tracer.addTag("本日执行记录", "rancher各主机指标监测任务~执行完毕");
	}
}
