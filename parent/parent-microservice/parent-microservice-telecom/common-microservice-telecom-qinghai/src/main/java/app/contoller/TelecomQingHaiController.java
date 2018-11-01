package app.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import app.commontracerlog.TracerLog;
import app.service.TelecomAsyncQingHaiService;

@RestController
@RequestMapping("/carrier")
public class TelecomQingHaiController {

	public static final Logger log = LoggerFactory.getLogger(TelecomQingHaiController.class);

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private TelecomAsyncQingHaiService telecomAsyncQingHaiService;
	

	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskMobile> telecom(@RequestBody MessageLogin messageLogin) {
		
		tracerLog.addTag("taskid", messageLogin.getTask_id());

		telecomAsyncQingHaiService.getAllData(messageLogin);
		
		return null;
	}

}
