package app.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.TelecomTianjinService;
import app.service.common.LoginAndGetCommonService;

/**
 * 
 * @Description: 天津电信爬取
 * @author sln
 * @date 2017年9月11日
 */
@RestController
@Configuration
@RequestMapping("/carrier") 
public class TelecomTianjinController {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomTianjinService telecomTianjinService;
	@Autowired
	private LoginAndGetCommonService loginAndGetCommonService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	/**
	 * 爬取数据的入口
	 * @param messageLogin
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody  MessageLogin messageLogin) {
		tracer.addTag("action.crawler.nameAndPwd", "爬取的用户的用户名和密码分别是"+messageLogin.getName()+"   "+messageLogin.getPassword());
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		//爬取所有的信息之前，先将可以跳过验证码的cookie更新到taskmobile表中,通过登录的cookie获取这个中间cookie
		try {
			loginAndGetCommonService.getInitMy189homeWebClient(messageLogin, taskMobile);
		} catch (Exception e) {
			tracer.addTag("获取爬取所有信息需要的cookie过程中出现异常：", e.toString());
		}
		taskMobile = telecomTianjinService.getAllData(messageLogin);
		result.setData(taskMobile);
		return result;
	}
}
