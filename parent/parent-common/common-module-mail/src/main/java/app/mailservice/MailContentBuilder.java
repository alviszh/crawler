package app.mailservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.crawler.monitor.json.EurekaInstanceBean;
import com.crawler.monitor.json.MonitorEurekaChange;
import com.crawler.monitor.json.MonitorLoginPageHtmlMailBean;
import com.crawler.monitor.json.MonitorLoginPageJsMailBean;
import com.crawler.monitor.json.MonitorRancherChange;
import com.crawler.monitor.json.MonitorWebUsableMailBean;
import com.crawler.monitor.json.tasker.MonitorBankTaskerBean;
import com.crawler.monitor.json.tasker.MonitorCarrierTempBean;
import com.crawler.monitor.json.tasker.MonitorHousingTaskerBean;
import com.crawler.monitor.json.tasker.MonitorInsuranceTaskerBean;
import com.crawler.monitor.json.tasker.MonitorStandaloneTaskerBean;
import com.crawler.pbccrc.json.PbccrcJsonBean;


/**
 * @author sln
 * @Description: 人行征信异常邮件通知所用类
 */
@Component
@Service
public class MailContentBuilder {
	@Autowired
    private TemplateEngine templateEngine;
    //人行征信出现的异常，进行邮件发送
    public String buildPbccrcException(PbccrcJsonBean pbccrcJsonBean,String exception){
    	 Context context = new Context();
         context.setVariable("taskStandalone", pbccrcJsonBean);
         context.setVariable("exception", exception);
         String mailContent = templateEngine.process("pbccrcExceptionTemplate", context);
         return mailContent;
    }
	/**
	 * @Description   用于监测rancher相关信息，将不达标的内容邮件通知
	 * @author sln
	 * @param rancherswap 
	 * @param rancherdisk 
	 * @date 2018年8月28日 下午4:57:32
	 */
	public String buildRancherEmailContent(List<MonitorRancherChange> changeList, 
			double rancherdisk, double rancherswap,double ranchercpu) {
		 Context context = new Context();
         context.setVariable("rancherlist", changeList);
         context.setVariable("rancherdisk", rancherdisk);
         context.setVariable("rancherswap", rancherswap);         
         context.setVariable("ranchercpu",ranchercpu);         
         String mailContent = templateEngine.process("rancher", context);
         return mailContent;
	}
	/**
	 * @Description 用于构建所有的类型网站的etl执行结果——运营商临时使用
	 * @author sln
	 * @date 2018年9月12日 下午4:13:49
	 */
	public String buildTaskerResult(List<MonitorCarrierTempBean> oneDayCarrierList,
			List<MonitorCarrierTempBean> moreDayCarrierList) {
		 Context context = new Context();
         context.setVariable("oneDayCarrierList", oneDayCarrierList);
         context.setVariable("moreDayCarrierList", moreDayCarrierList);
         String mailContent = templateEngine.process("allTaskerEtlResult", context);
         return mailContent;
	}
	/**
	 * @Description 用于构建所有的类型网站的etl执行结果
	 * @author sln
	 * @date 2018年9月12日 下午4:13:49
	 */
	public String buildTaskerResult(List<MonitorCarrierTempBean> oneDayCarrierList,
			List<MonitorCarrierTempBean> moreDayCarrierList,
			List<MonitorBankTaskerBean> oneDayBankList,
			List<MonitorHousingTaskerBean> oneDayHousingList,
			List<MonitorInsuranceTaskerBean> oneDayInsuranceList,
			List<MonitorStandaloneTaskerBean> oneDayPbccrcList,
			String envirtype) {
		Context context = new Context();
		context.setVariable("oneDayCarrierList", oneDayCarrierList);
		context.setVariable("moreDayCarrierList", moreDayCarrierList);
		context.setVariable("oneDayBankList", oneDayBankList);
		context.setVariable("oneDayHousingList", oneDayHousingList);
		context.setVariable("oneDayInsuranceList", oneDayInsuranceList);
		context.setVariable("oneDayPbccrcList", oneDayPbccrcList);
		context.setVariable("envirtype", envirtype);
		String mailContent = templateEngine.process("allTaskerEtlResult", context);
		return mailContent;
	}
	/**
	 * @Description   指定环境eureka上微服务节点数变化情况通知
	 * @author sln
	 * @date 2018年9月18日 下午2:49:25
	 */
	public String buildEurekaEmailContent(List<MonitorEurekaChange> eurekaChangelist, String envirtype) {
		Context context = new Context();
		context.setVariable("eurekaChangelist", eurekaChangelist);
		context.setVariable("envirtype", envirtype);
//		String mailContent = templateEngine.process("eureka", context);
		String mailContent = templateEngine.process("microServiceChange", context);
		return mailContent;
	}
	/**
	 * @Description
	 * @author sln
	 * @date 2018年9月18日 下午3:53:02
	 */
	public String buildWebUsableEmailContent(List<MonitorWebUsableMailBean> webUsableList, String insurUrl,String housingUrl) {
		Context context = new Context();
		context.setVariable("webUsableList", webUsableList);
		context.setVariable("insurUrl", insurUrl);
		context.setVariable("housingUrl", housingUrl);
		String mailContent = templateEngine.process("webusable", context);
		return mailContent;
	}
	/**
	 * @Description
	 * @author sln
	 * @date 2018年9月18日 下午5:31:04
	 */
	public String buildWebChangeEmailContent(List<MonitorLoginPageHtmlMailBean> htmlMailBeanList,
			List<MonitorLoginPageJsMailBean> jsMailBeanList, String insurUrl, String housingUrl) {
		Context context = new Context();
		context.setVariable("htmlMailBeanList", htmlMailBeanList);
		context.setVariable("jsMailBeanList", jsMailBeanList);
		context.setVariable("insurUrl", insurUrl);
		context.setVariable("housingUrl", housingUrl);
		String mailContent = templateEngine.process("webchange", context);
		return mailContent;
	}
	//微服务注册和下线时间的具体信息通知邮件
	public String buildEurekaInstanceChangeEmailContent(EurekaInstanceBean eurekaInstanceBean, String mailEnvirType, String microEventType, String eventTime) {
		Context context = new Context();
		context.setVariable("eurekaInstanceBean", eurekaInstanceBean);
		context.setVariable("envirtype", mailEnvirType);
		context.setVariable("microEventType", microEventType);
		context.setVariable("eventTime", eventTime);
		String mailContent = templateEngine.process("eurekalistener", context);
		return mailContent;
	}
}
