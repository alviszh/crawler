package app.webusable;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.monitor.json.MailBean;
import com.crawler.monitor.json.MonitorWebUsableMailBean;
import com.microservice.dao.entity.crawler.monitor.MonitorAllWebUsable;
import com.microservice.dao.repository.crawler.monitor.MonitorAllWebUsableRepository;

import app.client.monitor.MailClient;
import app.mailservice.MailContentBuilder;
/**
 * 将本次定时任务执行的网站可用性监测结果进行展示，只展示暂时不可用的
 * 邮件中附带wiki维护链接
 * 
 * @author sln
 */
@Component
@Service
public class MonitorWebUsableMailService {
	@Autowired
	private MailContentBuilder mailContentBuilder;
	@Autowired
	private MonitorAllWebUsableRepository allWebUsableRepository;
	@Autowired
	private MailClient mailClient;
	
	@Value("${webreceivers}") 
	public String receivers;
	@Value("${mailsender}") 
	public String mailsender;	
	@Value("${insurUrl}") 
	public String insurUrl;
	@Value("${housingUrl}") 
	public String housingUrl;
	
	public void sendWebNotUsableMail(List<MonitorAllWebUsable> webNotUsableList) {
		MonitorWebUsableMailBean tempBean=null;
		List<MonitorWebUsableMailBean> webUsableList=new ArrayList<MonitorWebUsableMailBean>();
		String url;      //登录url地址
		String webtype;   //监控网站类型(运营商/社保......)
		Integer statuscode;   //网站状态码
		String exceptioninfo;  //网站异常信息
		String developer;
		String dayslinkcode;  //指定天数范围内，某个网站连接状态码
		for (MonitorAllWebUsable monitorAllWebUsable : webNotUsableList) {
			url=monitorAllWebUsable.getUrl().trim();
			webtype=monitorAllWebUsable.getWebtype().trim();
			statuscode=monitorAllWebUsable.getStatuscode();
			exceptioninfo=monitorAllWebUsable.getExceptioninfo().trim();
			developer=monitorAllWebUsable.getDeveloper().trim();
			dayslinkcode=getDaysWebUsableResult(webtype);
			tempBean=new MonitorWebUsableMailBean(url, webtype, statuscode, exceptioninfo, developer, dayslinkcode);
			webUsableList.add(tempBean);
		}
		String content = mailContentBuilder.buildWebUsableEmailContent(webUsableList,insurUrl,housingUrl);
		MailBean mailBean=new MailBean();
		mailBean.setMailcontent(content);
		mailBean.setReceiver(receivers);
		mailBean.setSender(mailsender);
		mailBean.setSubject("本次监测任务网站访问结果展示");
		//调用邮件发送接口
		mailClient.sendMail(mailBean);
		System.out.println("本次监测任务网站访问情况通知邮件发送成功");
	}
	
	//查询指定天数内，该网站的可连接性结果，并将结果记性拼接，作为邮件展示的一部分
	public String getDaysWebUsableResult(String webtype){
		List<MonitorAllWebUsable> list = allWebUsableRepository.findDaysWebUsableTaskerResult(webtype);
		StringBuffer statusCodeFiveBuffer =new StringBuffer();
		String detailStatusCode="";
		if(list!=null && list.size()>0){
			for (MonitorAllWebUsable monitorAllWebUsable : list) {
				statusCodeFiveBuffer.append(monitorAllWebUsable.getStatuscode()+"，");
			}
			detailStatusCode = statusCodeFiveBuffer.toString();
		}else{
			detailStatusCode="";
		}
		detailStatusCode=detailStatusCode.substring(0, detailStatusCode.lastIndexOf("，"));
		return detailStatusCode;
	}
}
