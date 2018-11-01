package app.webchange;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.monitor.json.MailBean;
import com.crawler.monitor.json.MonitorLoginPageHtmlMailBean;
import com.crawler.monitor.json.MonitorLoginPageJsMailBean;
import com.microservice.dao.entity.crawler.monitor.MonitorLoginPageHtml;
import com.microservice.dao.entity.crawler.monitor.MonitorLoginPageJs;
import com.microservice.dao.repository.crawler.monitor.MonitorLoginPageHtmlRepository;
import com.microservice.dao.repository.crawler.monitor.MonitorLoginPageJsRepository;

import app.client.monitor.MailClient;
import app.mailservice.MailContentBuilder;
/**
 * js数量发生变化、js内容变化、登录页源代码发生变化——邮件通知
 * @author sln
 *
 */
@Component
@Service
public class MonitorWebChangeMailService {
	@Autowired
	private MonitorLoginPageHtmlRepository loginPageHtmlRepository;
	@Autowired
	private MonitorLoginPageJsRepository loginPageJsRepository;
	@Autowired
	private MailContentBuilder mailContentBuilder;
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
	public void sendWebChangeMail(String taskid) {
		//由于dao和json这两个分组中实现实体的转换存在困难，所以，用如下方式将要展示的邮件内容list集合传到mail模板
		MonitorLoginPageHtmlMailBean htmlMailBean=null;
		MonitorLoginPageJsMailBean jsMailBean=null;
		List<MonitorLoginPageHtmlMailBean> htmlMailBeanList=new ArrayList<MonitorLoginPageHtmlMailBean>();
		List<MonitorLoginPageJsMailBean> jsMailBeanList=new ArrayList<MonitorLoginPageJsMailBean>();
		
		// ===========根据刚刚执行完，且存储了变化内容的任务组taskid，筛选===============
		//网页源码的变化，和js数量的变化均可以用如下htmlChangeList集合
		List<MonitorLoginPageHtml> htmlChangeList = loginPageHtmlRepository.findByChangeflag(taskid);
		if(htmlChangeList.size()>0){
			for (MonitorLoginPageHtml monitorLoginPageHtml : htmlChangeList) {
				htmlMailBean=new MonitorLoginPageHtmlMailBean(taskid, monitorLoginPageHtml.getUrl().trim(), 
						monitorLoginPageHtml.getWebtype().trim(), monitorLoginPageHtml.getDeveloper().trim(),
						monitorLoginPageHtml.getComparetaskid().trim(), monitorLoginPageHtml.getJscountchangedetail().trim());
				htmlMailBeanList.add(htmlMailBean);
			}
		}
		List<MonitorLoginPageJs> jsChangeList = loginPageJsRepository.findByChangeflag(taskid);
		if(jsChangeList.size()>0){
			for (MonitorLoginPageJs monitorLoginPageJs : jsChangeList) {
				jsMailBean=new MonitorLoginPageJsMailBean(taskid, monitorLoginPageJs.getUrl().trim(),
						monitorLoginPageJs.getJspath().trim(), monitorLoginPageJs.getWebtype().trim(), 
						monitorLoginPageJs.getDeveloper().trim(), monitorLoginPageJs.getComparetaskid().trim());
				jsMailBeanList.add(jsMailBean);
			}
		}
		String content = mailContentBuilder.buildWebChangeEmailContent(htmlMailBeanList,jsMailBeanList,insurUrl,housingUrl);
		MailBean mailBean=new MailBean();
		mailBean.setMailcontent(content);
		mailBean.setReceiver(receivers);
		mailBean.setSender(mailsender);
		mailBean.setSubject("网站登录页及相关js变化监控情况");
		//调用邮件发送接口
		mailClient.sendMail(mailBean);
		System.out.println("网站登录页及相关js变化监控情况通知邮件发送成功");
	}
}
