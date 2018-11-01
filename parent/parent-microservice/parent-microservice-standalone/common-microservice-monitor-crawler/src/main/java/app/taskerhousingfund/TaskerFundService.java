/**
 * 
 */
package app.taskerhousingfund;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingFundJsonBean;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.housingfund.json.TaskHousingfund;
import com.microservice.dao.entity.crawler.monitor.MonitorHousingFundTasker;
import com.microservice.dao.repository.crawler.monitor.MonitorHousingFundTaskerRepository;

import app.client.HousingFundClient;
import app.client.fund.HousingfundTaskClient;
import app.commontracerlog.TracerLog;
import net.sf.json.JSONObject;

/**
 * @author sln
 * @date 2018年8月23日上午11:28:28
 * @Description: 公积金定时爬取任务
 */
@Component
public class TaskerFundService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundClient fundClient;
	@Autowired
	private HousingfundTaskClient taskClient;
	@Autowired
	private MonitorHousingFundTaskerRepository housingTaskerRepository;
	@Autowired
	private MonitorHousingUtils monitorHousingUtils;
	public void housingTasker(){
		//定时执行公积金网站zipkin查询入口
		tracer.qryKeyValue("monitor", "dayhousingfund");
		//查询所有需要监控的网站
		List<MonitorHousingFundTasker> housingList = housingTaskerRepository.findAllNeedMonitorWeb();
		String name;
		String idnum;
		String city;
//		String developer;
		String paramsjson;
		String task_id;
		HousingFundJsonBean housingFundJsonBean=null;
		MessageLoginForHousing messageLoginForHousing=null;
		for (MonitorHousingFundTasker eachHousingFund : housingList) {
			////////////////////////////////////////////////////////////
			name = eachHousingFund.getName();
			idnum = eachHousingFund.getIdnum();
			city = eachHousingFund.getCity();
//			developer = eachHousingFund.getDeveloper();
			paramsjson = eachHousingFund.getParamsjson();
			////////////////////////////////////////////////////////////
			housingFundJsonBean=new HousingFundJsonBean();
			housingFundJsonBean.setUsername(name);
			housingFundJsonBean.setIdnum(idnum);
			housingFundJsonBean.setCity(city);		
			//根据如上信息获取该网站本次任务的task_id
			TaskHousingfund taskHousing = taskClient.createTask(housingFundJsonBean);
			task_id = taskHousing.getTaskid();
			tracer.addTag(city+" 公积金本次执行taskid:",task_id);
			//json串转换为入参bean
			JSONObject jsonObject=JSONObject.fromObject(paramsjson);
			messageLoginForHousing=(MessageLoginForHousing)JSONObject.toBean(jsonObject, MessageLoginForHousing.class);
			//将生成的task_id传入参数bean
			messageLoginForHousing.setTask_id(task_id);
			messageLoginForHousing.setUsername(name);
			//执行各公积金网站
			try {
				loginAndCrawler(messageLoginForHousing,city,task_id);
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag(city+" 公积金执行过程中出现异常",e.toString());
			}
		}
	}
	public void loginAndCrawler(MessageLoginForHousing messageLoginForHousing,String city,String task_id) throws Exception{
		tracer.addTag(city+" 公积金本次执行日志start","=========start=========" +messageLoginForHousing.toString());
		String countNumber;        //职工账号 
		String telephone;         //手机号码
		String hosingFundNumber;    //个人公积金账号
		String logintype;         //登录类型
		String description = "";
		try {
			countNumber=messageLoginForHousing.getCountNumber().trim();
		} catch (Exception e) {
			countNumber="unknown";
		}
		try {
			telephone=messageLoginForHousing.getTelephone().trim();
		} catch (Exception e) {
			telephone="unknown";
		}
		try {
			hosingFundNumber=messageLoginForHousing.getHosingFundNumber().trim();
		} catch (Exception e) {
			hosingFundNumber="unknown";
		}
		try {
			logintype=messageLoginForHousing.getLogintype().trim();
		} catch (Exception e) {
			logintype="unknown";
		}
		long beginTime =System.currentTimeMillis();//开始时间
		try {
			fundClient.login(messageLoginForHousing.getTask_id(),
					messageLoginForHousing.getNum(),
					messageLoginForHousing.getPassword(),
					messageLoginForHousing.getCity(),
					messageLoginForHousing.getUsername().trim(),
					telephone,
					hosingFundNumber,
					countNumber,
					logintype);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("调用 "+city+" 公积金登陆接口时出现异常：",e.toString());
		}
		try {
			description=monitorHousingUtils.getResultDescription(task_id,beginTime);
		} catch (Exception e) {
			tracer.addTag(city+"  公积金登陆时出现异常：",e.toString());
		}
		if(description.contains("SUCCESS")){
			tracer.addTag(city+" 公积金登陆认证结果：", "认证成功，接下来调用爬取接口");
			//认证成功之后调用爬取接口
			try {
				fundClient.crawler(messageLoginForHousing.getTask_id(),
						messageLoginForHousing.getNum(),
						messageLoginForHousing.getPassword(),
						messageLoginForHousing.getCity(),
						messageLoginForHousing.getUsername().trim(),
						telephone,
						hosingFundNumber,
						countNumber,
						logintype);
			} catch (Exception e) {
				tracer.addTag("调用 "+city+" 公积金爬取接口时出现异常：",e.toString());
			}
		}else{
			tracer.addTag(city+"  公积金登陆认证失败，失败原因：", description);
		}
		tracer.addTag(city+" 公积金本次执行日志end","=========end=========");
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	
	//手动调用定时任务下执行失败的公积金
	public void oneWebByHand(String city){
		//手动调用，执行公积金网站zipkin查询入口
		tracer.qryKeyValue("monitor", "handhousingfund");
		tracer.addTag("手动调用"+city+"公积金爬取任务", "======start======");
		MonitorHousingFundTasker housingFundTasker = housingTaskerRepository.executeOneWeb(city);
		if(null!=housingFundTasker){
			String name=housingFundTasker.getName().trim();
			String idnum=housingFundTasker.getIdnum().trim();
			String paramsjson = housingFundTasker.getParamsjson();
			////////////////////////////////////////////////////////////
			HousingFundJsonBean housingFundJsonBean=new HousingFundJsonBean();
			housingFundJsonBean.setUsername(name);
			housingFundJsonBean.setIdnum(idnum);
			housingFundJsonBean.setCity(city);	
			try {
				//根据如上信息获取该网站本次任务的task_id
				TaskHousingfund taskHousing = taskClient.createTask(housingFundJsonBean);
				String task_id = taskHousing.getTaskid();
				tracer.addTag(city+" 公积金本次执行taskid:",task_id);
				//json串转换为入参bean
				JSONObject jsonObject=JSONObject.fromObject(paramsjson);
				MessageLoginForHousing messageLoginForHousing=(MessageLoginForHousing)JSONObject.toBean(jsonObject, MessageLoginForHousing.class);
				//将生成的task_id传入参数bean
				messageLoginForHousing.setTask_id(task_id);
				messageLoginForHousing.setUsername(name);
				tracer.addTag("messageLoginForHousing---toString()---", messageLoginForHousing.toString());
				//执行各公积金网站
				loginAndCrawler(messageLoginForHousing,city,task_id);
			} catch (Exception e) {
				tracer.addTag("手动调用"+city+"公积金爬取任务时出现出现异常，异常信息是：",e.toString());
				e.printStackTrace();
			}
		}else{
			tracer.addTag("手动调用"+city+"公积金爬取任务时，未能根据城市名查询到相关信息","可能是数据库中并没有该公积金的监测信息");
		}
		tracer.addTag("手动调用"+city+"公积金爬取任务", "======end======");
	}
}
