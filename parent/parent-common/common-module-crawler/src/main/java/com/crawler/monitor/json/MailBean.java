package com.crawler.monitor.json;
/**
 * @description:
 * @author: sln 
 */
public class MailBean {
//	邮件主题
	private String subject;
//	邮件正文
	private String mailcontent;
//	发件人
	private String sender;
//	收件人
	private String receiver;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMailcontent() {
		return mailcontent;
	}
	public void setMailcontent(String mailcontent) {
		this.mailcontent = mailcontent;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
}
