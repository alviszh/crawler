package app.rancher;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MailBean;
import com.crawler.monitor.json.MonitorRancherChange;

import app.client.monitor.MailClient;
import app.mailservice.MailContentBuilder;


/**
 * @author sln
 */
@Component
public class MonitorRancherNodeMailService {
	@Autowired
	private MailClient mailClient;
	@Autowired
	private MailContentBuilder mailContentBuilder;
	@Value("${MAIL_RECEIVERS}") 
	public String receivers;
	@Value("${mailsender}") 
	public String mailsender;
	//发送通知邮件
	public void sendResultMail(List<MonitorRancherChange> changeList, double rancherdisk, double rancherswap,double ranchercpu) {
		String content = mailContentBuilder.buildRancherEmailContent(changeList,rancherdisk,rancherswap,ranchercpu);
		MailBean mailBean=new MailBean();
		mailBean.setMailcontent(content);
		mailBean.setReceiver(receivers);
		mailBean.setSender(mailsender);
		mailBean.setSubject("rancher各主机指标监测结果不达标情况通知");
		//调用邮件发送接口
		mailClient.sendMail(mailBean);
		System.out.println("rancher各主机指标监测结果不达标情况通知邮件接口已经调用");
	}
}
