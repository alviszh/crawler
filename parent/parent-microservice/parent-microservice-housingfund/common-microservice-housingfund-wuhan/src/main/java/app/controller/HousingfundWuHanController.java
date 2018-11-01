package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.IdleInstance;
import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.client.MiddleClient;
import app.commontracerlog.TracerLog;
import app.service.HousingfundWuHanService;


@RestController
@Configuration
@RequestMapping("/housing/wuhan") 
public class HousingfundWuHanController extends HousingBasicController{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingfundWuHanService housingfundWuHanService;
	
	@Autowired  
	private RestTemplate restTemplate;
	@Autowired
	private MiddleClient middleClient;
	
	@Value("${spring.application.name}") 
	String appName;
	
	@PostMapping(path = "/loginAgent")
	public String loginAgent(@RequestBody MessageLoginForHousing messageLoginForHousing) throws Exception{
		String requestPath="/housing/guangzhou/login";
		IdleInstance idleInstance = middleClient.getIdleInstance(appName.toUpperCase(),requestPath,messageLoginForHousing.getTask_id());
		if(idleInstance==null||idleInstance.getIdleInstanceInfo()==null){
			tracer.addTag("RuntimeException", "NoIdleInstance 没有闲置可用的实例 ");
			throw new RuntimeException("NoIdleInstance"); 
		}else{
			String ip = idleInstance.getIdleInstanceInfo().getIpAddr();
			Integer port = idleInstance.getIdleInstanceInfo().getPort(); 
			String uri = "http://" + ip + ":" + port + requestPath;
			tracer.addTag("uri", uri);  
			TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id().trim());
			taskHousing.setCrawlerHost(ip);
			taskHousing.setCrawlerPort(port+"");
			taskHousingRepository.save(taskHousing);
			ResponseEntity<String> str = this.restTemplate.postForEntity(uri, messageLoginForHousing, String.class);  
			String body = str.getBody();
			return body;
		} 
	}
	@RequestMapping(value = "login",method = RequestMethod.POST)
	public ResultData<TaskHousing> login(@RequestBody MessageLoginForHousing messageLoginForHousing){
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getError_code());
		housingfundWuHanService.login(messageLoginForHousing);
		
		save(taskHousing);
		result.setData(taskHousing);
		return result;
	}
	
	@PostMapping(path = "/sendSmsCodeAgent")
	public String sendSmsCodeAgent(@RequestBody MessageLoginForHousing messageLogin) throws Exception {
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		String ip = taskHousing.getCrawlerHost();
		String port = taskHousing.getCrawlerPort();
		String uri = "http://" + ip + ":" + port + "/housing/wuhan/setphonecode";
		ResponseEntity<String> str = this.restTemplate.postForEntity(uri, messageLogin, String.class);
		String body = str.getBody();
		return body;
	}
	@RequestMapping(value = "/setphonecode", method = RequestMethod.POST )
	public ResultData<TaskHousing> setcode(@RequestBody MessageLoginForHousing messageLogin){
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		tracer.addTag("parser.crawler.taskid 验证手机验证码", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", messageLogin.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLogin.getNum());
		try {
			housingfundWuHanService.verifySms(messageLogin);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.setphonecode.auth", e.toString());
		}
		
		result.setData(taskHousing);
		return result;
	}
	
	@PostMapping(path = "/crawlerAgent")
	public String crawlerAgent(@RequestBody MessageLoginForHousing messageLogin) throws Exception {
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		String ip = taskHousing.getCrawlerHost();
		String port = taskHousing.getCrawlerPort();
		String uri = "http://" + ip + ":" + port + "/housing/wuhan/crawler";
		ResponseEntity<String> str = this.restTemplate.postForEntity(uri, messageLogin, String.class);
		String body = str.getBody();
		return body;
	}
	@RequestMapping(value = "/crawler", method = RequestMethod.POST)
	public ResultData<TaskHousing> crawler(@RequestBody MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		ResultData<TaskHousing> result = new ResultData<TaskHousing>();
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getError_code());
		save(taskHousing);
		
		try {
			housingfundWuHanService.getAllData(messageLoginForHousing);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result.setData(taskHousing);
		return result;
		
	}
	
}
