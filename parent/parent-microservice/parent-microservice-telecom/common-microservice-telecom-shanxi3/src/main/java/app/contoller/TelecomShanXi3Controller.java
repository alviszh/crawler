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

import app.commontracerlog.TracerLog;
import app.service.AsyncShanXi3GetAllDataService;
/**
 * @Description  陕西电信爬取
 * @author sln
 * @date 2017年8月21日 下午5:50:08
 */
@RestController
@Configuration
@RequestMapping("/carrier") 
public class TelecomShanXi3Controller {
	@Autowired
	private AsyncShanXi3GetAllDataService asyncShanXi3GetAllDataService;
	@Autowired
	private TracerLog tracer;
	/**
	 * 爬取数据的入口
	 * @param messageLogin
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody  MessageLogin messageLogin) throws Exception {
		tracer.addTag("action.crawler.nameAndPwd", "爬取的用户的用户名和密码分别是"+messageLogin.getName()+"   "+messageLogin.getPassword());
		ResultData<TaskMobile> result = new ResultData<TaskMobile>();
		TaskMobile taskMobile = asyncShanXi3GetAllDataService.getAllData(messageLogin);
		result.setData(taskMobile);
		return result;
	}
}
