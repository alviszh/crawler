package app.pbccrc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MailBean;

import app.client.monitor.MailClient;


/**
 *  用于及时展示人行征信的定时爬取任务，每天任务执行的过程中看异常是否发生
 *  * @author sln
 *
 */
@Component
public class MonitorPbccrcMailService {
	@Autowired
	private MailClient mailClient;
	@Value("${pbccrcreceivers}") 
	public String receivers;
	@Value("${mailsender}") 
	public String mailsender;
	@Value("${envirtype}")     //根据环境类型改变邮件展示时候的标题说明
	public String envirtype;
	//发送通知邮件
	public void sendResultMail(String exDetail) {
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
		MailBean mailBean=new MailBean();
		mailBean.setMailcontent(exDetail);
		mailBean.setReceiver(receivers);
		mailBean.setSender(mailsender);
		mailBean.setSubject(mailEnvirType+"人行征信相关微服务调用过程中异常情况通知");
		//调用邮件发送接口
		mailClient.sendMail(mailBean);
		System.out.println(mailEnvirType+"人行征信相关微服务调用过程中出现的异常情况通知邮件发送成功");
	}
}
