package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.wechat.json.WeChatRequestParameters;
import com.microservice.dao.entity.crawler.wechat.TaskWeChat;
import com.microservice.dao.repository.crawler.wechat.TaskWeChatRepository;

import app.common.WebParam;
import app.parser.WeChatParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.wechat"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.wechat"})
public class WeChatService {

	@Autowired 
	private WeChatParser weChatParser;
	@Autowired
	private TaskWeChatRepository taskWeChatRepository;
	
	public TaskWeChat getCode(WeChatRequestParameters weChatRequestParameters) {
		TaskWeChat taskWeChat = taskWeChatRepository.findByTaskid(weChatRequestParameters.getTaskid());
		try {
			
			WebParam webParam = weChatParser.getCode(weChatRequestParameters,taskWeChat);
			if(null != webParam)
			{
				if(webParam.getBaseCode()!=null)
				{
					taskWeChat.setPhase("200");
					taskWeChat.setPhase_status("200");
					taskWeChat.setDescription("请扫描二维码");
					taskWeChat.setBaseCode(webParam.getBaseCode());
					taskWeChat.setQrUrl(webParam.getQrUrl());
					taskWeChat.setTesthtml(webParam.getUrl());
					taskWeChatRepository.save(taskWeChat);
				}
				else
				{
					taskWeChat.setPhase("201");
					taskWeChat.setPhase_status("201");
					taskWeChat.setDescription("请扫描二维码");
					taskWeChat.setBaseCode(webParam.getBaseCode());
					taskWeChat.setQrUrl(webParam.getQrUrl());
					taskWeChat.setTesthtml(webParam.getUrl());
					taskWeChatRepository.save(taskWeChat);
				}
			}
			else
			{
				taskWeChat.setPhase("404");
				taskWeChat.setPhase_status("404");
				taskWeChat.setDescription("请扫描二维码");
				taskWeChatRepository.save(taskWeChat);
			}
		} catch (Exception e) {
			taskWeChat.setPhase("404");
			taskWeChat.setPhase_status("404");
			taskWeChat.setDescription("请扫描二维码");
			taskWeChatRepository.save(taskWeChat);
			e.printStackTrace();
		}
		return taskWeChat;
	}
	
	
	public TaskWeChat login(WeChatRequestParameters weChatRequestParameters) {
		TaskWeChat taskWeChat = taskWeChatRepository.findByTaskid(weChatRequestParameters.getTaskid());
		try {
			
			WebParam webParam = weChatParser.login(weChatRequestParameters,taskWeChat);
			if(null != webParam)
			{
				if(webParam.getHtml().contains("account.NickName"))
				{
					taskWeChat.setPhase("200");
					taskWeChat.setPhase_status("200");
					taskWeChat.setDescription("登陆成功");
					taskWeChat.setBaseCode(webParam.getBaseCode());
					taskWeChat.setQrUrl(webParam.getQrUrl());
					taskWeChatRepository.save(taskWeChat);
				}
				else if(webParam.getHtml().contains("刷新")|webParam.getHtml().contains("refresh_tips"))
				{
					taskWeChat.setPhase("201");
					taskWeChat.setPhase_status("201");
					taskWeChat.setDescription("请刷新二维码");
					taskWeChat.setBaseCode(webParam.getBaseCode());
					taskWeChat.setQrUrl(webParam.getQrUrl());
					taskWeChatRepository.save(taskWeChat);
				}
				else if(webParam.getHtml().contains("切换账号")|webParam.getHtml().contains("action"))
				{
					taskWeChat.setPhase("201");
					taskWeChat.setPhase_status("201");
					taskWeChat.setDescription("您手机未确认登陆！");
					taskWeChat.setBaseCode(webParam.getBaseCode());
					taskWeChat.setQrUrl(webParam.getQrUrl());
					taskWeChatRepository.save(taskWeChat);
				}
			}
			else
			{
				taskWeChat.setPhase("404");
				taskWeChat.setPhase_status("404");
				taskWeChat.setDescription("超时");
				taskWeChatRepository.save(taskWeChat);
			}
		} catch (Exception e) {
			taskWeChat.setPhase("404");
			taskWeChat.setPhase_status("404");
			taskWeChat.setDescription("超时");
			taskWeChatRepository.save(taskWeChat);
			e.printStackTrace();
		}
		return taskWeChat;
	}


	public TaskWeChat crawlerUserInfo(WeChatRequestParameters weChatRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}


	public TaskWeChat crawlerWechatGroup(WeChatRequestParameters weChatRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}


	public TaskWeChat crawlerOfficialAccount(WeChatRequestParameters weChatRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
