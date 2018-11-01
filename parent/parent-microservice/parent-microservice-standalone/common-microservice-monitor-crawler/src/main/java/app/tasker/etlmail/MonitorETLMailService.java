package app.tasker.etlmail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MailBean;
import com.crawler.monitor.json.tasker.MonitorBankTaskerBean;
import com.crawler.monitor.json.tasker.MonitorCarrierTempBean;
import com.crawler.monitor.json.tasker.MonitorHousingTaskerBean;
import com.crawler.monitor.json.tasker.MonitorInsuranceTaskerBean;
import com.crawler.monitor.json.tasker.MonitorStandaloneTaskerBean;

import app.client.BankEtlClient;
import app.client.CarrierEtlClient;
import app.client.HousingEtlClient;
import app.client.InsuranceEtlClient;
import app.client.PbccrcTaskResultClient;
import app.client.monitor.MailClient;
import app.commontracerlog.TracerLog;
import app.mailservice.MailContentBuilder;


/**
 * @description:etl执行邮件，将各类网站执行结果放在同一个邮件中进行展示
 * @author: sln 
 */
@Component
public class MonitorETLMailService {
	@Autowired
	private MailContentBuilder mailContentBuilder;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MailClient mailClient; 
	@Autowired
	public CarrierEtlClient carrierEtlClient;  //该接口也可用于调用运营商每日执行结果
	@Autowired
	public BankEtlClient bankEtlClient; 
	@Autowired
	public HousingEtlClient housingEtlClient;  
	@Autowired
	public InsuranceEtlClient insurEtlClient;  
	@Autowired
	public PbccrcTaskResultClient pbccrcTaskResultClient;
	@Value("${etlreceivers}")   //定时任务爬取结果通知邮件
	public String receivers;
	@Value("${mailsender}") 
	public String mailsender;
	@Value("${envirtype}")     //根据环境类型改变邮件展示时候的标题说明
	public String envirtype;
	
	public void getAllWebTaskResultAndSendMail(){
		List<MonitorCarrierTempBean> oneDayCarrierList = null;
		List<MonitorCarrierTempBean> moreDayCarrierList = null;
		List<MonitorBankTaskerBean> oneDayBankList = null;
		List<MonitorHousingTaskerBean> oneDayHousingList = null;
		List<MonitorInsuranceTaskerBean> oneDayInsuranceList = null;
		List<MonitorStandaloneTaskerBean> oneDayPbccrcList = null;
		String mailEnvirType = null;
		try {
			oneDayCarrierList = carrierEtlClient.oneDayCarrier();
		} catch (Exception e) {
			tracer.addTag("获取近24小时运营商定时爬取任务出现异常：oneDayCarrierList.e", e.toString());
		}
		try {
			moreDayCarrierList = carrierEtlClient.moreDayCarrier();
		} catch (Exception e) {
			tracer.addTag("获取近10天运营商定时爬取任务出现异常：moreDayCarrierList.e", e.toString());
		}
		try {
			oneDayBankList = bankEtlClient.oneDayBank();
		} catch (Exception e) {
			tracer.addTag("获取近24小时网银定时爬取任务出现异常：oneDayBankList.e", e.toString());
		}
		try {
			oneDayHousingList = housingEtlClient.oneDayHousing();
		} catch (Exception e) {
			tracer.addTag("获取近24小时公积金定时爬取任务出现异常：oneDayHousingList.e", e.toString());
		}
		try {
			oneDayInsuranceList = insurEtlClient.oneDayInsurance();
		} catch (Exception e) {
			tracer.addTag("获取近24小时社保定时爬取任务出现异常：oneDayInsuranceList.e", e.toString());
		}
		try {
			oneDayPbccrcList = pbccrcTaskResultClient.oneDayPbccrc();
		} catch (Exception e) {
			tracer.addTag("获取近24小时人行征信定时爬取任务出现异常：oneDayPbccrc.e", e.toString());
		}
		//==========================
		if(envirtype.trim().equals("dev")){
			mailEnvirType="开发环境";
		}else if(envirtype.trim().equals("test")){
			mailEnvirType="测试环境";
		}else{
			mailEnvirType="生产环境";
		}
		//==========================
		try {
			MailBean mailBean=new MailBean();
			//获取构建之后的模板文件
			String mailContent = mailContentBuilder.buildTaskerResult(oneDayCarrierList, moreDayCarrierList,
					oneDayBankList, oneDayHousingList, oneDayInsuranceList,oneDayPbccrcList,mailEnvirType);			
			String subject=mailEnvirType+"定时爬取任务执行情况一览表";
			mailBean.setMailcontent(mailContent);
			mailBean.setReceiver(receivers);
			mailBean.setSender(mailsender);
			mailBean.setSubject(subject);
			//调用邮件发送接口
			mailClient.sendMail(mailBean);
			tracer.addTag("邮件发送", "定时爬取任务执行情况通知邮件接口调用完毕，收取情况以实际为主");
		} catch (Exception e) {
			tracer.addTag("定时爬取任务执行情况通知邮件发送过程中出现异常",e.toString());
		}
	}
}
