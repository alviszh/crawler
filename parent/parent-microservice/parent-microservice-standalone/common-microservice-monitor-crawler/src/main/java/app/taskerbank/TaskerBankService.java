/**
 * 
 */
package app.taskerbank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankUserBean;
import com.crawler.bank.json.TaskBank;
import com.microservice.dao.entity.crawler.monitor.MonitorBankTasker;
import com.microservice.dao.repository.crawler.monitor.MonitorBankTaskerRepository;

import app.client.BankClient;
import app.client.bank.BankTaskClient;
import app.commontracerlog.TracerLog;
import net.sf.json.JSONObject;

/**
 * @author sln
 * @date 2018年8月23日上午11:28:28
 * @Description: 定时爬取任务
 */
@Component
public class TaskerBankService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private BankClient bankClient;
	@Autowired
	private BankTaskClient taskClient;
	@Autowired
	private MonitorBankTaskerRepository bankTaskerRepository;
	@Autowired
	private MonitorBankUtils monitorUtils;
	public void bankTasker(){
		//定时执行网站zipkin查询入口
		tracer.qryKeyValue("monitor", "daybank");
		//查询所有需要监控的网站
		List<MonitorBankTasker> bankList = bankTaskerRepository.findAllNeedMonitorWeb();
		String username;
		String idnum;
		String paramsjson;
		String taskid;
		String webtype;
		BankUserBean bankUserBean=null;
		for (MonitorBankTasker eachBank : bankList) {
			////////////////////////////////////////////////////////////
			username = eachBank.getUsername().trim();
			idnum = eachBank.getIdnum();
			paramsjson = eachBank.getParamsjson();
			webtype=eachBank.getWebtype().trim();
			////////////////////////////////////////////////////////////
			bankUserBean=new BankUserBean();
			bankUserBean.setUsername(username);
			bankUserBean.setIdNum(idnum);
			//根据如上信息获取该网站本次任务的task_id
			TaskBank taskBank = taskClient.createTask(bankUserBean);
			taskid = taskBank.getTaskid();
			tracer.addTag(webtype+" 本次执行taskid:",taskid);
			//json串转换为入参bean
			JSONObject jsonObject=JSONObject.fromObject(paramsjson);
			BankJsonBean bankJsonBean=(BankJsonBean)JSONObject.toBean(jsonObject, BankJsonBean.class);		
			bankJsonBean.setTaskid(taskid);
			//执行各网站
			try {
				loginAndCrawler(bankJsonBean,webtype,taskid);
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag(webtype+" 执行过程中出现异常",e.toString());
			}
		}
	}
	public void loginAndCrawler(BankJsonBean bankJsonBean,String webtype,String taskid) throws Exception{
		tracer.addTag(webtype+" 本次执行日志start","=========start=========" +bankJsonBean.toString());
		String cardNumber;        //职工账号 
		String description = "";
		try {
			cardNumber=bankJsonBean.getCardNumber().trim();
		} catch (Exception e) {
			cardNumber="unknown";
		}
		long beginTime =System.currentTimeMillis();//开始时间
		try {
			bankClient.login(taskid, bankJsonBean.getBankType().trim(), 
					bankJsonBean.getLoginType().trim(), bankJsonBean.getLoginName().trim(),
					bankJsonBean.getCardType().trim(), bankJsonBean.getPassword().trim(), cardNumber);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("调用 "+webtype+"登陆接口时出现异常：",e.toString());
		}
		try {
			description=monitorUtils.getResultDescription(taskid,beginTime);
		} catch (Exception e) {
			tracer.addTag(webtype+"  登陆时出现异常：",e.toString());
		}
		if(description.contains("SUCCESS")){
			tracer.addTag(webtype+" 登陆认证结果：", "认证成功，接下来调用爬取接口");
			//认证成功之后调用爬取接口
			try {
				bankClient.crawler(taskid, bankJsonBean.getBankType().trim(), 
						bankJsonBean.getLoginType().trim(), bankJsonBean.getLoginName().trim(),
						bankJsonBean.getCardType().trim(), bankJsonBean.getPassword().trim(), cardNumber);
			} catch (Exception e) {
				tracer.addTag("调用 "+webtype+"爬取接口时出现异常：",e.toString());
			}
			
		}else{
			tracer.addTag(webtype+"  登陆认证失败，失败原因：", description);
		}
		tracer.addTag(webtype+" 本次执行日志end","=========end=========");
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	
	//手动调用定时任务下执行失败的
	public void oneWebByHand(String webtype){
		//手动调用，执行网站zipkin查询入口
		tracer.qryKeyValue("monitor", "handbank");
		tracer.addTag("手动调用"+webtype+"爬取任务", "======start======");
		MonitorBankTasker bankTasker = bankTaskerRepository.executeOneWeb(webtype);
		if(null!=bankTasker){
			String username = bankTasker.getUsername().trim();
			String idnum = bankTasker.getIdnum();
			String paramsjson = bankTasker.getParamsjson();
			////////////////////////////////////////////////////////////
			BankUserBean bankUserBean=new BankUserBean();
			bankUserBean.setUsername(username);
			bankUserBean.setIdNum(idnum);
			try {
				//根据如上信息获取该网站本次任务的task_id
				TaskBank taskBank = taskClient.createTask(bankUserBean);
				String taskid = taskBank.getTaskid();
				tracer.addTag(webtype+" 本次执行taskid:",taskid);
				//json串转换为入参bean
				JSONObject jsonObject=JSONObject.fromObject(paramsjson);
				BankJsonBean bankJsonBean=(BankJsonBean)JSONObject.toBean(jsonObject, BankJsonBean.class);
				//将生成的task_id传入参数bean
				bankJsonBean.setTaskid(taskid);
				tracer.addTag("bankJsonBean---toString()---", bankJsonBean.toString());
				//执行各网站
				loginAndCrawler(bankJsonBean,webtype,taskid);
			} catch (Exception e) {
				tracer.addTag("手动调用"+webtype+"爬取任务时出现出现异常，异常信息是：",e.toString());
				e.printStackTrace();
			}
		}else{
			tracer.addTag("手动调用"+webtype+"爬取任务时，未能根据网站类型查询到相关信息","可能是数据库中并没有该类型网站的监测信息");
		}
		tracer.addTag("手动调用"+webtype+"爬取任务", "======end======");
	}
}
