/**
 * 
 */
package app.taskersocialinsurance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceJsonBean;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.TaskInsurance;
import com.microservice.dao.entity.crawler.monitor.MonitorInsuranceTasker;
import com.microservice.dao.repository.crawler.monitor.MonitorInsuranceTaskerRepository;

import app.client.InsuranceClient;
import app.client.InsuranceProxyTaskClient;
import app.commontracerlog.TracerLog;
import net.sf.json.JSONObject;

/**
 * @author sln
 * @Description:社保定时爬取任务
 */
@Component
public class TaskerSocialInsurService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceClient insurClient;
	@Autowired
	private InsuranceProxyTaskClient taskClient;
	@Autowired
	private MonitorInsuranceTaskerRepository insuranceTaskerRepository;
	@Autowired
	private MonitorSocialInsurUtils monitorUtils;
	public void insuranceTasker(){
		//定时执行社保网站zipkin查询入口
		tracer.qryKeyValue("monitor", "dayinsurance");
		//查询所有需要监控的网站
		List<MonitorInsuranceTasker> insuranceList = insuranceTaskerRepository.findAllNeedMonitorWeb();
		String name;
		String idnum;
		String city;
		String paramsjson;
		String taskId;
		InsuranceJsonBean insuranceJsonBean=null;
		InsuranceRequestParameters insuranceRequestParameters=null;
		for (MonitorInsuranceTasker eachInsurance : insuranceList) {
			////////////////////////////////////////////////////////////
			name = eachInsurance.getName();   
			idnum = eachInsurance.getIdnum();
			city = eachInsurance.getCity();
			paramsjson = eachInsurance.getParamsjson();
			////////////////////////////////////////////////////////////
			insuranceJsonBean=new InsuranceJsonBean();
			insuranceJsonBean.setUsername(name);
			insuranceJsonBean.setIdnum(idnum);
			//根据如上信息获取该网站本次任务的task_id
			TaskInsurance taskInsurance = taskClient.createTask(insuranceJsonBean);
			taskId = taskInsurance.getTaskid();
			tracer.addTag(city+" 社保本次执行taskid:",taskId);
			//json串转换为入参bean
			JSONObject jsonObject=JSONObject.fromObject(paramsjson);
			insuranceRequestParameters=(InsuranceRequestParameters)JSONObject.toBean(jsonObject, InsuranceRequestParameters.class);
			//将生成的task_id传入参数bean
			insuranceRequestParameters.setTaskId(taskId);
			insuranceRequestParameters.setName(name);
			insuranceRequestParameters.setIdnum(idnum);
			//执行各社保网站
			try {
				loginAndCrawler(insuranceRequestParameters,city,taskId);
			} catch (Exception e) {
				tracer.addTag(city+" 社保执行过程中出现异常",e.toString());
			}
		}
	}
	public void loginAndCrawler(InsuranceRequestParameters insuranceRequestParameters,String city,String taskId) throws Exception{
		tracer.addTag(city+" 社保本次执行日志start","=========start=========" +insuranceRequestParameters.toString());
		//获取各个参数
		String loginType;
		String area;          //赣州社保需要区号
		//如下操作是为了防止出现空指针
		try {
			loginType=insuranceRequestParameters.getLoginType().trim();
		} catch (Exception e) {
			loginType="unknown";
		}
		try {
			area=insuranceRequestParameters.getArea().trim();
		} catch (Exception e) {
			area="unknown";
		}
		long beginTime =System.currentTimeMillis();//开始时间
		try {
			insurClient.login(insuranceRequestParameters.getTaskId(),
					insuranceRequestParameters.getName().trim(),
					insuranceRequestParameters.getIdnum().trim(),
					insuranceRequestParameters.getUsername(),
					insuranceRequestParameters.getPassword(),
					insuranceRequestParameters.getCity(),
					loginType,area);    //个别社保需要区号
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("调用 "+city+" 社保登陆接口时出现异常：",e.toString());
		}
		String description = "";
		try {
			description=monitorUtils.getResultDescription(taskId,beginTime);
		} catch (Exception e) {
			tracer.addTag(city+"  社保登陆时出现异常：",e.toString());
		}
		if(description.contains("SUCCESS")){
			tracer.addTag(city+" 社保登陆认证结果：", "认证成功，接下来调用爬取接口");
			//认证成功之后调用爬取接口
			try {
				insurClient.crawler(insuranceRequestParameters.getTaskId(),
						insuranceRequestParameters.getName().trim(),
						insuranceRequestParameters.getIdnum().trim(),
						insuranceRequestParameters.getUsername(),
						insuranceRequestParameters.getPassword(),
						insuranceRequestParameters.getCity(),
						loginType,area);    //个别社保需要区号
			} catch (Exception e) {
				tracer.addTag("调用 "+city+" 社保爬取接口时出现异常：",e.toString());
			}
			
		}else{
			tracer.addTag(city+"  社保登陆认证失败，失败原因：", description);
		}
		tracer.addTag(city+" 社保本次执行日志end","=========end=========");
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	
	//手动调用定时任务下执行失败的社保
	public void oneWebByHand(String city){
		//手动调用，执行社保网站zipkin查询入口
		tracer.qryKeyValue("monitor", "handinsurance");
		tracer.addTag("手动调用"+city+"社保爬取任务", "======start======");
		MonitorInsuranceTasker insuranceTasker = insuranceTaskerRepository.executeOneWeb(city);
		if(null!=insuranceTasker){
			String name=insuranceTasker.getName().trim();
			String idnum=insuranceTasker.getIdnum().trim();
			String paramsjson = insuranceTasker.getParamsjson();
			////////////////////////////////////////////////////////////
			InsuranceJsonBean insuranceJsonBean=new InsuranceJsonBean();
			InsuranceRequestParameters insuranceRequestParameters=new InsuranceRequestParameters();
			insuranceJsonBean.setUsername(name);
			insuranceJsonBean.setIdnum(idnum);
			try {
				//根据如上信息获取该网站本次任务的task_id
				TaskInsurance taskInsurance = taskClient.createTask(insuranceJsonBean);
				String taskId = taskInsurance.getTaskid();
				tracer.addTag(city+" 社保本次执行taskid:",taskId);
				//json串转换为入参bean
				JSONObject jsonObject=JSONObject.fromObject(paramsjson);
				insuranceRequestParameters=(InsuranceRequestParameters)JSONObject.toBean(jsonObject, InsuranceRequestParameters.class);
				//将生成的task_id传入参数bean
				insuranceRequestParameters.setTaskId(taskId);
				insuranceRequestParameters.setName(name);
				insuranceRequestParameters.setIdnum(idnum);
				//执行各社保网站
				loginAndCrawler(insuranceRequestParameters,city,taskId);
			} catch (Exception e) {
				tracer.addTag("手动调用"+city+"社保爬取任务时出现异常，异常信息是：",e.toString());
				e.printStackTrace();
			}
		}else{
			tracer.addTag("手动调用"+city+"社保爬取任务时，未能根据城市名查询到相关信息","可能是数据库中并没有该社保的监测信息");
		}
		tracer.addTag("手动调用"+city+"社保爬取任务", "======end======");
	}
}
