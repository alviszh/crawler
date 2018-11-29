package app.eureka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MailBean;
import com.crawler.monitor.json.MonitorEurekaChange;

import app.client.monitor.MailClient;
import app.mailservice.MailContentBuilder;


/**
 * 该类用于检测eureka上的微服务变动情况以及节点变动情况
 * 
 * 要是检测出数据库中没有出现的节点，就将新出现的添加到数据库中
 * 
 * 根据数据库内容，到eureka上进行对比
 * @author sln
 *
 */
@Component
public class MonitorEurekaMailService {
	@Autowired
	private MailContentBuilder mailContentBuilder;
	@Autowired
	private MailClient mailClient;
	@Value("${MAIL_RECEIVERS}") 
	public String receivers;
	@Value("${mailsender}") 
	public String mailsender;
	@Value("${envirtype}")     //根据环境类型改变邮件展示时候的标题说明
	public String envirtype;
	//发送通知邮件
	public void sendResultMail(List<MonitorEurekaChange> changeList) {
		String mailEnvirType = null;
		//==========================
		if(envirtype.trim().equals("dev")){
			mailEnvirType="开发环境";
		}else if(envirtype.trim().equals("test")){
			mailEnvirType="测试环境";
		}else if(envirtype.trim().equals("prod")){
			mailEnvirType="生产环境";
		}else{
			mailEnvirType="应用商店";
		}
		//==========================
		String content = mailContentBuilder.buildEurekaEmailContent(changeList,mailEnvirType);
		MailBean mailBean=new MailBean();
		mailBean.setMailcontent(content);
		mailBean.setReceiver(receivers);
		mailBean.setSender(mailsender);
		mailBean.setSubject(mailEnvirType+"Eureka上微服务变动情况");
		//调用邮件发送接口
		mailClient.sendMail(mailBean);
		System.out.println(mailEnvirType+"Eureka上微服务变动情况通知邮件接口已经调用");
	}
}
