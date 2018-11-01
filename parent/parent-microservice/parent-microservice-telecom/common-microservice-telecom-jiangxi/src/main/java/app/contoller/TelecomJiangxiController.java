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

import app.service.TelecomJiangxiService;

/**
 * 
 * @Description: 电信爬取
 * @author sln
 * @date 2017年9月16日
 */
@RestController
@Configuration
@RequestMapping("/carrier") 
public class TelecomJiangxiController {
	@Autowired
	private TelecomJiangxiService telecomJiangxiService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	//江西电信经过正常的登录后，还需要经过二次登录，如下为二次登录接口
	//二次登录需要发送短信验证码
	@RequestMapping(value = "/jiangxilogin", method = { RequestMethod.POST })
	public ResultData<TaskMobile> login(@RequestBody MessageLogin messageLogin) {
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = telecomJiangxiService.sendSms(messageLogin);
		result.setData(taskMobile);
		return result;
	}
	
//	第二次登录短信验证码要是验证通过，就是登录成功
	@RequestMapping(value = "/setphonecodelogin", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomsetcodelogin(@RequestBody MessageLogin messageLogin) {
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = telecomJiangxiService.verifySms(messageLogin);
		//加了异步，就成了空指针异常，前端页面调用，报错，说超时 所以要提前查,如下
		taskMobile=taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		result.setData(taskMobile);
		return result;
	}
	//========================爬取数据短信接口    start======================================
	@RequestMapping(value = "/getphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomgetcode(@RequestBody MessageLogin messageLogin) {
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = telecomJiangxiService.sendSmsTwice(messageLogin);
		result.setData(taskMobile);
		return result;
	}

	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecomsetcode(@RequestBody MessageLogin messageLogin) throws Exception {
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = telecomJiangxiService.verifySmsTwice(messageLogin);
		//加了异步，就成了空指针异常，前端页面调用，报错，说超时 所以要提前查,如下
		taskMobile=taskMobileRepository.findByTaskid(messageLogin.getTask_id().trim());
		result.setData(taskMobile);
		return result;
	}
	//========================爬取数据短信接口    end======================================
	/**
	 * 爬取数据的入口
	 * @param messageLogin
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody  MessageLogin messageLogin) throws Exception {
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = telecomJiangxiService.getAllData(messageLogin);
		result.setData(taskMobile);
		return result;
	}
}
