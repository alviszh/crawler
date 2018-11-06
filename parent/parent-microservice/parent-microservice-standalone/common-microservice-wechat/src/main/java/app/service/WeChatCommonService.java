package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.wechat.json.WeChatRequestParameters;
import com.microservice.dao.entity.crawler.wechat.TaskWeChat;
import com.microservice.dao.repository.crawler.wechat.TaskWeChatRepository;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.wechat"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.wechat"})
public class WeChatCommonService {

	@Autowired
	private WeChatService weChatServcie;
	@Autowired
	private TaskWeChatRepository taskWeChatRepository;
	
	
	public TaskWeChat getCode(WeChatRequestParameters weChatRequestParameters) {
		TaskWeChat taskWeChat = taskWeChatRepository.findByTaskid(weChatRequestParameters.getTaskid());
		taskWeChat = weChatServcie.getCode(weChatRequestParameters);
		return taskWeChat;
	}
	
	public TaskWeChat login(WeChatRequestParameters weChatRequestParameters) {
		TaskWeChat taskWeChat = taskWeChatRepository.findByTaskid(weChatRequestParameters.getTaskid());
		taskWeChat = weChatServcie.login(weChatRequestParameters);
		return taskWeChat;
	}

	public TaskWeChat getAllData(WeChatRequestParameters weChatRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
