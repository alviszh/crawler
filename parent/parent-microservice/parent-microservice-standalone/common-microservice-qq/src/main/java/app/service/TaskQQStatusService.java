package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.qq.json.QQStatusCode;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.qq.TaskQQ;
import com.microservice.dao.repository.crawler.qq.TaskQQRepository;

import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.qq"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.qq"})
public class TaskQQStatusService {

	@Autowired 
	private TracerLog tracerLog;
	@Autowired
	private TaskQQRepository taskQQRepository;
	
	public TaskQQ changeStatusLogin(PbccrcJsonBean pbccrcJsonBean){
		TaskQQ taskQQ = taskQQRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		if(null == taskQQ){
			throw new RuntimeException("Entity bean TaskBank is null ! taskid>>"+pbccrcJsonBean.getMapping_id()+"<<");
		}
		Gson gson = new Gson();
		taskQQ.setCrawlerHost(pbccrcJsonBean.getIp());
		taskQQ.setCrawlerPort(pbccrcJsonBean.getPort());
		taskQQ = taskQQRepository.save(taskQQ);
		return taskQQ;
	}
	
	public TaskQQ changeStatusLoginDoing(PbccrcJsonBean pbccrcJsonBean){
		TaskQQ taskQQ = new TaskQQ();
		taskQQ.setTaskid(pbccrcJsonBean.getMapping_id());
		if(null == taskQQ){
			throw new RuntimeException("Entity bean TaskBank is null ! taskid>>"+pbccrcJsonBean.getMapping_id()+"<<");
		}
		taskQQ.setServicename("QQ");
		taskQQ.setDescription(QQStatusCode.QQ_LOGIN_LOADING.getDescription());
		taskQQ.setPhase(QQStatusCode.QQ_LOGIN_LOADING.getPhase());
		taskQQ.setPhasestatus(QQStatusCode.QQ_LOGIN_LOADING.getPhasestatus());
		taskQQ = taskQQRepository.save(taskQQ);
		return taskQQ;
	}
}
