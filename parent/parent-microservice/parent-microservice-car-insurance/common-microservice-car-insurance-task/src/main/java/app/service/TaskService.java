package app.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.car.insurance.CarInsuranceCompanyCode;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.microservice.dao.repository.crawler.car.insurance.CarInsuranceCompanyCodeRepository;
import com.microservice.dao.repository.crawler.car.insurance.TaskCarInsuranceRepository;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.car.insurance"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.car.insurance"})
public class TaskService {
	
	@Autowired
	private CarInsuranceCompanyCodeRepository carInsuranceCompanyCodeRepository;
	@Autowired
	private TaskCarInsuranceRepository taskCarInsuranceRepository;

	/**
	 * 获取所有公司
	 * @return
	 */
	public List<CarInsuranceCompanyCode> getCompanys() {
		return carInsuranceCompanyCodeRepository.findByIsFlagLessThan(3);
	}

	/**
	 * 创建taskId
	 * @param carInsuranceRequestBean
	 * @return
	 */
	public TaskCarInsurance create(CarInsuranceRequestBean carInsuranceRequestBean) {
		TaskCarInsurance taskCarInsurance = new TaskCarInsurance();
		taskCarInsurance.setTaskid(UUID.randomUUID().toString());
		taskCarInsurance.setCompanyName(carInsuranceRequestBean.getCompanyName());
		taskCarInsurance.setOwner(carInsuranceRequestBean.getOwner());
		taskCarInsurance.setRequestJson(new Gson().toJson(carInsuranceRequestBean));
		taskCarInsurance = taskCarInsuranceRepository.save(taskCarInsurance);
		return taskCarInsurance;
	}
	
	

}
