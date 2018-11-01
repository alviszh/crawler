package app.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.taxation.json.TaxationJsonBean;
import com.microservice.dao.entity.crawler.taxation.basic.BasicUserTaxation;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.repository.crawler.taxation.basic.AreaCodeRepository;
import com.microservice.dao.repository.crawler.taxation.basic.BasicUserTaxationRepository;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;

import app.commontracerlog.TracerLog;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.taxation.basic"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.taxation.basic"})
public class TaxationTaskService {

public static final Logger log = LoggerFactory.getLogger(TaxationTaskService.class);
	
	@Autowired
	private BasicUserTaxationRepository basicUserTaxationRepository;
	@Autowired
	private TaskTaxationRepository taskTaxationRepository;
	@Autowired
	private AreaCodeRepository areaCodeRepository;
	@Autowired
	private TracerLog tracer;
	
	public TaskTaxation createTask(TaxationJsonBean taxationJsonBean) {
		TaskTaxation taskTaxation = new TaskTaxation();
		if(null == taxationJsonBean){
			tracer.addTag("TaxationTaskService createTask","taxationJsonBean is null !");
			throw new RuntimeException("taxationJsonBean is null");
		}else if(null == taxationJsonBean.getIdnum()){
			tracer.addTag("TaxationTaskService createTask","taxationJsonBean idNum is null !");
			throw new RuntimeException("taxationJsonBean field(idNum) is null");
		}else if(null == taxationJsonBean.getUsername()){
			tracer.addTag("TaxationTaskService createTask","taxationJsonBean username is null !");
			throw new RuntimeException("taxationJsonBean field(username) is null");
		}else{
//			boolean isSuccess = gongAnValidate(taxationJsonBean);
			if(true){
				BasicUserTaxation basicUserTaxation = basicUserTaxationRepository.findByNameAndIdnum(taxationJsonBean.getUsername(),taxationJsonBean.getIdnum());
				if(null == basicUserTaxation){
					basicUserTaxation = new BasicUserTaxation();
					basicUserTaxation.setIdnum(taxationJsonBean.getIdnum());
					basicUserTaxation.setName(taxationJsonBean.getUsername());
					basicUserTaxationRepository.save(basicUserTaxation);
					tracer.addTag("用户不存在   :",basicUserTaxation.toString());
					
					String uuid = UUID.randomUUID().toString();
					taskTaxation.setTaskid(uuid);
					taskTaxation.setBasicUserTaxation(basicUserTaxation);
					taskTaxation = taskTaxationRepository.save(taskTaxation);
					tracer.addTag("taskTaxation 生成taskid  :",taskTaxation.getTaskid());
				}else{
					tracer.addTag("用户已存在   :",basicUserTaxation.toString());
					String uuid = UUID.randomUUID().toString();
					taskTaxation.setTaskid(uuid);
					taskTaxation.setBasicUserTaxation(basicUserTaxation);
					taskTaxation = taskTaxationRepository.save(taskTaxation);
					tracer.addTag("taskTaxation 生成taskid  :",taskTaxation.getTaskid());
				}				
			}else{
				tracer.addTag("身份证号与姓名不匹配",taxationJsonBean.toString());
				taskTaxation.setPhase("GONGAN");
				taskTaxation.setPhase_status("ERROR");
				taskTaxation.setDescription("身份证号与姓名不匹配！");
				taskTaxation = taskTaxationRepository.save(taskTaxation);
			}
		}
		
		return taskTaxation;
	}

	public TaskTaxation getTaskTaxation(String taskid) {
		tracer.addTag("TaxationTaskService getTaskTaxation",taskid);
		TaskTaxation taskTaxation = taskTaxationRepository.findByTaskid(taskid);
		return taskTaxation;
	}

	public TaxationJsonBean checkUser(TaxationJsonBean taxationJsonBean) {
		if(taxationJsonBean==null){
			tracer.addTag("checkUser RuntimeException", "TaxationJsonBean is null");
			throw new RuntimeException("TaxationJsonBean is null"); 
		}else if(taxationJsonBean.getUsername()==null){
			tracer.addTag("checkUser RuntimeException", "TaxationJsonBean field(username) is null");
			throw new RuntimeException("TaxationJsonBean field(username) is null");
		}else if(taxationJsonBean.getIdnum()==null){
			tracer.addTag("checkUser RuntimeException", "TaxationJsonBean field(idnum) is null");
			throw new RuntimeException("TaxationJsonBean field(idnum) is null");
		}else{
			BasicUserTaxation basicUserTaxation = basicUserTaxationRepository.findByNameAndIdnum(taxationJsonBean.getUsername(), taxationJsonBean.getIdnum()); 
			if(basicUserTaxation==null){ 
				basicUserTaxation = new BasicUserTaxation();
				basicUserTaxation.setIdnum(taxationJsonBean.getIdnum());
				basicUserTaxation.setName(taxationJsonBean.getUsername());
				basicUserTaxation = basicUserTaxationRepository.save(basicUserTaxation);
				tracer.addTag("checkUser 用户不存在", "创建一个新用户"+basicUserTaxation.toString());
				//log.info("用户不存在:"+basicUser.toString());
			}else{
				tracer.addTag("checkUser 用户已存在", "返回该用户"+basicUserTaxation.toString());
				//log.info("用户已存在:"+basicUser.toString());
			}
			taxationJsonBean.setId(basicUserTaxation.getId());  
			return taxationJsonBean;
		}
	}

}
