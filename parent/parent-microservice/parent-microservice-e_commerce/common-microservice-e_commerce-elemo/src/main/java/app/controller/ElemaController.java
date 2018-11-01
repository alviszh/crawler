package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.service.ElemaService;

@RestController
@RequestMapping("/elema")
public class ElemaController {

	@Autowired
	private ElemaService elemaService;
	@Autowired
	private E_CommerceTaskRepository e_commerceTaskRepository;
	//登录
	@PostMapping(path = "/login")
	public void login(@RequestBody E_CommerceTask pbccrcJsonBean) {
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(pbccrcJsonBean.getTaskid());
		e_commerceTask.setLoginName(pbccrcJsonBean.getLoginName());
		//客户端存入的
		e_commerceTask.setWebsiteType("elemo");
		e_commerceTaskRepository.save(e_commerceTask);
		elemaService.login(pbccrcJsonBean);
	}
	//发送短信验证码
	@PostMapping(path = "/sendSms")
	public void sendSms(@RequestBody E_CommerceTask pbccrcJsonBean) {
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		elemaService.sendSms(pbccrcJsonBean);
	}
	//验证短信验证码
	@PostMapping(path = "/verifySms")
	public void verifySms(@RequestBody E_CommerceTask pbccrcJsonBean) {
		//前端的入参-----短信验证码
		String verification = pbccrcJsonBean.getVerificationPhone();
		System.out.println("前端的入参----短信验证码"+verification);
		elemaService.verifySms(pbccrcJsonBean);
	}
	
	//爬取
	@PostMapping(path = "/crawler")
	public void crawler(@RequestBody E_CommerceTask pbccrcJsonBean) {
		
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(pbccrcJsonBean.getTaskid());
		e_commerceTask.setBankCardInfoStatus(201);
		e_commerceTask.setRenzhengInfoStatus(201);
		e_commerceTask.setBtPrivilegeInfoStatus(201);
		e_commerceTaskRepository.save(e_commerceTask);
		
		//爬取业务
		elemaService.crawler(pbccrcJsonBean);
		

		        if (null != e_commerceTask.getUserinfoStatus()
		                && null != e_commerceTask.getAddressInfoStatus()
		                && null != e_commerceTask.getBankCardInfoStatus()
		                && null != e_commerceTask.getOrderInfoStatus()
		                && null != e_commerceTask.getRenzhengInfoStatus()
		                && null != e_commerceTask.getBtPrivilegeInfoStatus()) {
		            e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhase());
		            e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getPhasestatus());
		            e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_CRAWLER_SUCCESS.getDescription());
		            e_commerceTask.setFinished(true);
			e_commerceTaskRepository.save(e_commerceTask);
		        }

		    }
		
}
