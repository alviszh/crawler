/**
 * 
 */
package app.pbccrc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.TaskStandalone;
import com.microservice.dao.entity.crawler.monitor.MonitorPbccrcTasker;
import com.microservice.dao.repository.crawler.monitor.MonitorPbccrcTaskerRepository;

import app.client.PbccrcClient;
import app.client.PbccrcProxyTaskClient;
import app.client.PbccrcTaskResultClient;
import app.commontracerlog.TracerLog;
import app.exceptiondetail.ExUtils;
import net.sf.json.JSONObject;

/**
 * @author sln
 * @Description:人行征信定时爬取任务
 */
@Component
public class TaskerPbccrcService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private PbccrcClient pbccrcClient;
	@Autowired
	private PbccrcProxyTaskClient taskClient;
	@Autowired
	private MonitorPbccrcTaskerRepository pbccrcTaskerRepository;
	@Autowired
	public PbccrcTaskResultClient pbccrcTaskResultClient;
	@Autowired
	private MonitorPbccrcUtils monitorUtils;
	@Autowired
	private MonitorPbccrcMailService pbccrcMailService;
	public void PbccrcTasker(){
		String exceptionInfo=null;   //可能出现的异常，用于监控程序执行过程中出现的异常，及时邮件通知
		//定时执行人行征信网站zipkin查询入口
		tracer.qryKeyValue("monitor", "daypbccrc");
		//查询所有需要监控的人行征信记录
		String oneSuccessPbccrcRecord = pbccrcTaskResultClient.getOneSuccessPbccrcRecord();
		if(null!=oneSuccessPbccrcRecord && oneSuccessPbccrcRecord.length()>0){  //可以获取用于定时爬取任务的信息json串
			JSONObject jsonObject=JSONObject.fromObject(oneSuccessPbccrcRecord);
			String username=jsonObject.getString("username");
			String password=jsonObject.getString("password");
			String tradecode=jsonObject.getString("tradecode");
			String key;
			try {
				key=jsonObject.getString("key");
			} catch (Exception e) {
				key="未知";
			}
			//根据如上记录，重新拼接新的json串，只取出需要的信息
			String paramsjson="{\"key\":\""+key+"\",\"username\":\""+username+"\","
					+ "\"password\":\""+password+"\",\"tradecode\":\""+tradecode+"\","
					+ "\"html\":false,\"owner\":\"tasker\","
					+ "\"serviceName\":\"pbccrc-v2\"}";
			jsonObject=JSONObject.fromObject(paramsjson);
			PbccrcJsonBean pbccrcJsonBean=(PbccrcJsonBean)JSONObject.toBean(jsonObject, PbccrcJsonBean.class);
			//根据如上信息获取该网站本次任务的task_id
			String taskid = null;
			try {
				TaskStandalone createTask = taskClient.createTask(pbccrcJsonBean);
				taskid = createTask.getTaskid();
				tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务本次执行taskid:",taskid);
			} catch (Exception e) {
				tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务，在获取本地执行任务的taskid时出现异常:",e.toString());
				exceptionInfo=ExUtils.getEDetail(e);
			}
			//将生成的task_id传入参数bean
			pbccrcJsonBean.setMapping_id(taskid);
			try {
				loginAndCrawler(pbccrcJsonBean,username);
			} catch (Exception e) {
				tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务执行过程中出现异常",e.toString());
				exceptionInfo=ExUtils.getEDetail(e);
			}
		}else{
			tracer.addTag("未能从task表中查询最新爬取成功的人行征信记录", "本次定时爬取任务暂无法执行");
		}
		//如果出现异常信息，就发送邮件
		if(exceptionInfo!=null && exceptionInfo.length()>0){
			pbccrcMailService.sendResultMail(exceptionInfo);
		}
	}
	public void loginAndCrawler(PbccrcJsonBean pbccrcJsonBean,String username) throws Exception{
		tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务执行日志start","=========start=========" +pbccrcJsonBean.toString());
		long beginTime =System.currentTimeMillis();//开始时间
		pbccrcClient.loginAndGetcreditV(pbccrcJsonBean);
		String description = "";
		try {
			description=monitorUtils.getResultDescription(pbccrcJsonBean.getMapping_id(),beginTime);
		} catch (Exception e) {
			tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务已经调用，判断实时状态过程中出现异常",e.toString());
			e.printStackTrace();
		}
		if(description.contains("SUCCESS")){
			tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务正确执行", "认证成功且正常获取报告");
		}else{
			tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务执行失败", description);
		}
		tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务执行日志end","=========end=========");
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	
	//手动调用定时任务下执行失败的某个人行征信爬取任务
	public void oneWebByHand(String username){
		String exceptionInfo=null;   //可能出现的异常，用于监控程序执行过程中出现的异常，及时邮件通知
		tracer.qryKeyValue("monitor", "handpbccrc");
		tracer.addTag("手动调用用户名为： "+username+" 的人行征信定时监控任务","======start======");
		MonitorPbccrcTasker pbccrcTasker = pbccrcTaskerRepository.executeOnePbccrc(username);
		if(null!=pbccrcTasker){
			////////////////////////////////////////////////////////////
			String paramsjson = pbccrcTasker.getParamsjson();
			//json串转换为入参bean
			JSONObject jsonObject=JSONObject.fromObject(paramsjson);
			PbccrcJsonBean pbccrcJsonBean=(PbccrcJsonBean)JSONObject.toBean(jsonObject, PbccrcJsonBean.class);
			////////////////////////////////////////////////////////////
			//根据如上信息获取该网站本次任务的task_id
			TaskStandalone createTask = taskClient.createTask(pbccrcJsonBean);
			String taskid = createTask.getTaskid();
			tracer.addTag("用户名为： "+username+" 的人行征信定时监控任务本次执行taskid:",taskid);
			//将生成的task_id传入参数bean
			pbccrcJsonBean.setMapping_id(taskid);
			try {
				loginAndCrawler(pbccrcJsonBean,username);
			} catch (Exception e) {
				tracer.addTag("手动调用---用户名为： "+username+" 的人行征信定时监控任务执行过程中出现异常",e.toString());
				e.printStackTrace();
				exceptionInfo=ExUtils.getEDetail(e);
			}
			//如果出现异常信息，就发送邮件
			if(exceptionInfo!=null && exceptionInfo.length()>0){
				pbccrcMailService.sendResultMail(exceptionInfo);
			}
		}else{
			tracer.addTag("手动调用---用户名为： "+username+" 的人行征信爬取任务时，未能根据用户名查询到相关信息","可能是数据库中并没有对应的监测信息");
		}
		tracer.addTag("手动调用用户名为： "+username+" 的人行征信定时监控任务","======end======");
	}
}
