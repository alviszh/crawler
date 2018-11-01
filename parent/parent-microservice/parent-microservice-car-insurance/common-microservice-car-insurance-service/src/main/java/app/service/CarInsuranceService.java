package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.microservice.dao.repository.crawler.car.insurance.TaskCarInsuranceRepository;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.car.insurance"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.car.insurance",})
public class CarInsuranceService {
	
	@Autowired
	private TaskCarInsuranceRepository taskCarInsuranceRepository;

	public TaskCarInsurance getTaskCarInsurance(String taskid) {
		return taskCarInsuranceRepository.findByTaskid(taskid);
	}
	
	public TaskCarInsurance saveTaskCarInsurance(TaskCarInsurance taskCarInsurance){
		return taskCarInsuranceRepository.save(taskCarInsurance);
	}

}
