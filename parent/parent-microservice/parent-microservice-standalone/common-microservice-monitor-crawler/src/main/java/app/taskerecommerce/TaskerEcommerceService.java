/**
 * 
 */
package app.taskerecommerce;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.crawler.e_commerce.json.E_CommerceTask;
import com.microservice.dao.entity.crawler.monitor.MonitorEcommerceTasker;
import com.microservice.dao.repository.crawler.monitor.MonitorEcommerceTaskerRepository;

import app.client.JdEcommerceClient;
import app.client.SnEcommerceClient;
import app.client.TbEcommerceClient;
import app.client.ecom.EcomTaskClient;
import app.commontracerlog.TracerLog;
import net.sf.json.JSONObject;

/**
 * @author sln
 * @date 2018年8月23日上午11:28:28
 * @Description: 电商定时爬取任务(在不需短信验证码的前提下，京东、淘宝、苏宁的登陆和爬取任务都在登陆接口里边)
 */
@Component
public class TaskerEcommerceService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private JdEcommerceClient jdClient;
	@Autowired
	private TbEcommerceClient tbClient;
	@Autowired
	private SnEcommerceClient snClient;
	@Autowired
	private EcomTaskClient taskClient;
	@Autowired
	private MonitorEcommerceTaskerRepository ecommerceTaskerRepository;
	public void eComTasker(){
		//定时执行电商网站zipkin查询入口
		tracer.qryKeyValue("monitor", "dayecom");
		//查询所有需要监控的网站
		List<MonitorEcommerceTasker> list = ecommerceTaskerRepository.findAllNeedMonitorWeb();
		String paramsjson;
		String taskid;
		String webtype;
		E_CommerceJsonBean eCommerceJsonBean=null;
		for (MonitorEcommerceTasker eachEcommerce : list) {
			paramsjson = eachEcommerce.getParamsjson().trim();
			webtype=eachEcommerce.getWebtype().trim();
			//json串转换为入参bean
			JSONObject jsonObject=JSONObject.fromObject(paramsjson);
			eCommerceJsonBean=(E_CommerceJsonBean)JSONObject.toBean(jsonObject, E_CommerceJsonBean.class);
			//根据如上信息本次任务的taskid
			E_CommerceTask eComTask = taskClient.createTask(eCommerceJsonBean);
			taskid = eComTask.getTaskid();
			tracer.addTag("电商~"+webtype+"~本次执行taskid:",taskid);
			//将生成的taskid传入参数bean
			eCommerceJsonBean.setTaskid(taskid);
			//执行各电商网站
			try {
				loginAndCrawler(eCommerceJsonBean,webtype,taskid);
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("电商~"+webtype+"~定时爬取任务执行过程中出现异常",e.toString());
			}
		}
	}
	public void loginAndCrawler(E_CommerceJsonBean eCommerceJsonBean,String webtype,String taskid) throws Exception{
		tracer.addTag("电商~"+webtype+"~本次执行日志start","=========start=========" +eCommerceJsonBean.toString());
		String idnum = eCommerceJsonBean.getTaskid().trim();
	    String username = eCommerceJsonBean.getUsername().trim();
	    String passwd = eCommerceJsonBean.getPasswd().trim();
	    String logintype = null;
	    try {
	    	logintype=eCommerceJsonBean.getLogintype().trim();
		} catch (Exception e) {
			logintype="未知";
		}
		try {
			if(webtype.contains("jd")){
				jdClient.login(taskid, idnum, username, passwd, logintype);
			}else if(webtype.contains("tb")){
				tbClient.login(taskid, idnum, username, passwd, logintype);
			}else if(webtype.contains("sn")){
				snClient.login(taskid, idnum, username, passwd, logintype);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("调用~"+webtype+"~电商爬取任务对应接口时出现异常，异常信息是：",e.toString());
		}
		tracer.addTag("电商~"+webtype+"~本次执行日志end","=========end=========");
	}
	/////////////////////////////////////////////////////////////////////////////////////
	//手动调用定时任务下执行失败的电商
	public void oneWebByHand(String webtype){
		//手动调用，执行电商网站zipkin查询入口
		tracer.qryKeyValue("monitor", "handecom");
		tracer.addTag("手动调用~"+webtype+"~电商爬取任务", "======start======");
		MonitorEcommerceTasker ecomTasker = ecommerceTaskerRepository.executeOneWeb(webtype);
		if(null!=ecomTasker){
			String paramsjson = ecomTasker.getParamsjson();
			//json串转换为入参bean
			JSONObject jsonObject=JSONObject.fromObject(paramsjson);
			E_CommerceJsonBean eCommerceJsonBean=(E_CommerceJsonBean)JSONObject.toBean(jsonObject, E_CommerceJsonBean.class);
			//根据如上信息本次任务的taskid
			E_CommerceTask eComTask = taskClient.createTask(eCommerceJsonBean);
			String taskid = eComTask.getTaskid();
			tracer.addTag("电商~"+webtype+"~本次执行taskid:",taskid);
			//将生成的taskid传入参数bean
			eCommerceJsonBean.setTaskid(taskid);
			//执行各电商网站
			try {
				loginAndCrawler(eCommerceJsonBean,webtype,taskid);
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("电商~"+webtype+"~定时爬取任务执行过程中出现异常",e.toString());
			}
		}else{
			tracer.addTag("手动调用~"+webtype+"~电商爬取任务时，未能根据电商类型名称查询到相关信息","可能是数据库中并没有该电商的监测信息");
		}
		tracer.addTag("手动调用~"+webtype+"~电商爬取任务", "======end======");
	}
}
