package app.service;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MailBean;

import app.commontracerlog.TracerLog;

/**
 * @description:将邮件服务提取出来
 * @author: sln 
 * @date: 2018年3月19日 下午2:26:14 
 */
@Component
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TracerLog tracer;
	@Async
	public void sendMail(MailBean mailBean) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			String mailReceivers[]=mailBean.getReceiver().trim().split(",");
			messageHelper.setText(mailBean.getMailcontent().trim(), true);   //邮件内容
			messageHelper.setFrom(mailBean.getSender().trim());// 发送者
			messageHelper.setTo(mailReceivers); // 群发
			messageHelper.setSubject(mailBean.getSubject().trim());// 邮件主题
			javaMailSender.send(mimeMessage);// 发送邮件
			tracer.addTag(mailBean.getSender().trim()+"邮件发送"+mailBean.getSubject().trim()+"主题邮件成功", null);
		} catch (Exception e) {
			tracer.addTag(mailBean.getSender().trim()+"邮件发送"+mailBean.getSubject().trim()+"主题邮件过程中出现异常：", e.toString());
		}
	}
}
