package app.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;

@RestController
public class TelecomBasicController {


	@Autowired
	protected TracerLog tracerLog;

	@Autowired
	protected TaskMobileRepository taskMobileRepository;
	
	protected void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	protected TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

}
