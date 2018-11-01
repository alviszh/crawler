package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;

@RestController
public class HousingBasicController {


	@Autowired
	protected TracerLog tracer;
	
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	
	
	public void save(TaskHousing taskHousing){
		taskHousingRepository.save(taskHousing);
	}
	
	public TaskHousing findTaskHousing(String  taskId){
		return taskHousingRepository.findByTaskid(taskId);
	}
	
	
}
