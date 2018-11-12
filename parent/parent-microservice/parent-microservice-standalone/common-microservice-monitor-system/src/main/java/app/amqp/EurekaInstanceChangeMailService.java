package app.amqp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.EurekaInstanceBean;
import com.crawler.monitor.json.MailBean;

import app.client.monitor.MailClient;
import app.mailservice.MailContentBuilder;


/**
 * 用于监微服务的上线和下线时间
 * @author sln
 *
 */
@Component
public class EurekaInstanceChangeMailService {
	@Autowired
	private MailContentBuilder mailContentBuilder;
	@Autowired
	private MailClient mailClient;
	@Value("${eurekareceivers}") 
	public String receivers;
	@Value("${mailsender}") 
	public String mailsender;
	@Value("${envirtype}")     //根据环境类型改变邮件展示时候的标题说明
	public String envirtype;
	//发送通知邮件
	public void sendResultMail(EurekaInstanceBean eurekaInstanceBean,String microEventType, String eventTime) {
		String mailEnvirType = null;
		//==========================
		if(envirtype.trim().equals("dev")){
			mailEnvirType="开发环境";
		}else if(envirtype.trim().equals("test")){
			mailEnvirType="测试环境";
		}else{
			mailEnvirType="生产环境";
		}
		//==========================
		String content = mailContentBuilder.buildEurekaInstanceChangeEmailContent(eurekaInstanceBean,mailEnvirType,microEventType,eventTime);
		MailBean mailBean=new MailBean();
		mailBean.setMailcontent(content);
		mailBean.setReceiver(receivers);
		mailBean.setSender(mailsender);
		mailBean.setSubject("Eureka"+mailEnvirType+"-"+microEventType+"-"+eurekaInstanceBean.getAppName().trim());
		//调用邮件发送接口
		mailClient.sendMail(mailBean);
		System.out.println(mailEnvirType+"Eureka上微服务-变动监听情况-通知邮件发送接口已经调用");
	}
}
