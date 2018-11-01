package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDBtPrivilege;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDCoffers;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDIndent;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDQueryBindCard;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDReceiverAddress;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDRenZhengInfo;
import com.microservice.dao.entity.crawler.e_commerce.jingdong.JDUserInfo;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.e_commerce.jingdong.JDBtPrivilegeRepository;
import com.microservice.dao.repository.crawler.e_commerce.jingdong.JDCoffersRepository;
import com.microservice.dao.repository.crawler.e_commerce.jingdong.JDQueryBindCardRepository;
import com.microservice.dao.repository.crawler.e_commerce.jingdong.JDReceiverAddressRepository;
import com.microservice.dao.repository.crawler.e_commerce.jingdong.JDRenZhengInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.jingdong.JDUserInfoRepository;

import app.bean.WebParamE;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HtmlParse;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：JDParseService 类描述： 创建人：hyx
 * 创建时间：2017年12月20日 下午5:29:25
 * 
 * @version
 */

@Component
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.e_commerce.jingdong" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.e_commerce.jingdong" })
public class JDParseService {

	@Autowired
	private JDUserInfoRepository jdUserInfoRepository;

	@Autowired
	private JDReceiverAddressRepository jdReceiverAddressRepository;

	@Autowired
	private JDBtPrivilegeRepository jdBtPrivilegeRepository;

	@Autowired
	private JDRenZhengInfoRepository jdRenZhengInfoRepository;

	@Autowired
	private JDQueryBindCardRepository jdQueryBindCardRepository;

	@Autowired
	private JDCoffersRepository jdCoffersRepository;

	@Autowired
	private JDLoginAndGetService jdLoginAndGetService;

	@Autowired
	private TracerLog tracerLog;


	@Autowired
	private E_CommerceTaskRepository e_commerceTaskRepository;

	 
	public void userInfoParse(String html, String taskid) {
		tracerLog.output("userInfoParse", "解析中");
		try {
			JDUserInfo jdUserInfo = HtmlParse.userInfoParse(html, taskid);
			if(jdUserInfo!=null){
				jdUserInfoRepository.save(jdUserInfo);

				tracerLog.output("userInfoParse save ", "保存成功");
				E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
				e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getPhase());
				e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getPhasestatus());
				e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getDescription());
				e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getError_code());
				e_commerceTask.setUserinfoStatus(200);
				e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			}else{
				tracerLog.output("userInfoParse error ", html);
				E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
				e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhase());
				e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhasestatus());
				e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getDescription());
				e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getError_code());
				e_commerceTask.setUserinfoStatus(201);
				e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("userInfoParse error ", html);
			E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getError_code());
			e_commerceTask.setUserinfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

	}

	 
	public void receiverAddressParse(String html, String taskid) {
		tracerLog.output("receiverAddressParse", "解析中");

		try {
			List<JDReceiverAddress> result = HtmlParse.receiverAddressParse(html, taskid);
			
			if(result!=null && result.size()>0){
				jdReceiverAddressRepository.saveAll(result);

				tracerLog.output("receiverAddressParse save ", "保存成功");
				E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
				e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getPhase());
				e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getPhasestatus());
				e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getDescription());
				e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getError_code());
				e_commerceTask.setAddressInfoStatus(200);
				e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			}else{
				tracerLog.output("receiverAddressParse error ", html);
				E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
				e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhase());
				e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhasestatus());
				e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getDescription());
				e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getError_code());
				e_commerceTask.setAddressInfoStatus(201);
				e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("receiverAddressParse error ", html);
			E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getError_code());
			e_commerceTask.setAddressInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

	}

	 
	public void btprivilegeParse(String html, String taskid) {
		tracerLog.output("btprivilegeParse", "解析中");

		try {
			JDBtPrivilege result = HtmlParse.btprivilegeParse(html, taskid);

			html = jdLoginAndGetService.getScoreInfo();
			result.setCreditScore(html);
			
			jdBtPrivilegeRepository.save(result);

			tracerLog.output("btprivilegeParse save ", "保存成功");
			E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_SUCCESS.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_SUCCESS.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_SUCCESS.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_SUCCESS.getError_code());
			e_commerceTask.setBtPrivilegeInfoStatus(200);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("btprivilegeParse error ", html);
			E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getError_code());
			e_commerceTask.setBtPrivilegeInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

	}

	 
	public void renzhengParse(String html, String taskid) {
		tracerLog.output("renzhengParse", "解析中");

		try {
			JDRenZhengInfo result = HtmlParse.renzhengParse(html, taskid);
			
			if(result!=null){
				jdRenZhengInfoRepository.save(result);

				tracerLog.output("renzhengParse save ", "保存成功");
				E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
				e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getPhase());
				e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getPhasestatus());
				e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getDescription());
				e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getError_code());
				e_commerceTask.setRenzhengInfoStatus(200);
				e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			}else{
				tracerLog.output("renzhengParse error ", html);
				E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
				e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhase());
				e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhasestatus());
				e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getDescription());
				e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getError_code());
				e_commerceTask.setRenzhengInfoStatus(201);
				e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("renzhengParse error ", html);
			E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getError_code());
			e_commerceTask.setRenzhengInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

	}

	 
	public void queryBindCardParse(String html, String taskid) {
		tracerLog.output("queryBindCardParse", "解析中");

		try {
			List<JDQueryBindCard> result = HtmlParse.queryBindCardParse(html, taskid);
			
			if(result!=null && result.size()>0){
				jdQueryBindCardRepository.saveAll(result);

				tracerLog.output("queryBindCardParse save ", "保存成功");
				E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
				e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getPhase());
				e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getPhasestatus());
				e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getDescription());
				e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getError_code());
				e_commerceTask.setBankCardInfoStatus(200);
				e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			}else{
				tracerLog.output("queryBindCardParse error ", html);
				E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
				e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhase());
				e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhasestatus());
				e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getDescription());
				e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getError_code());
				e_commerceTask.setBankCardInfoStatus(201);
				e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("queryBindCardParse error ", html);
			E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getError_code());
			e_commerceTask.setBankCardInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}
	}

	 
	public void coffersParse(String html, String taskid) {
		tracerLog.output("coffersParse", "解析中");

		try {
			JDCoffers result = HtmlParse.coffersParse(html, taskid);

			if(result!=null){
				jdCoffersRepository.save(result);

				tracerLog.output("coffersParse save ", "保存成功");
			}else{
				tracerLog.output("coffersParse error ", html);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("coffersParse error ", html);
		}


	}

	public WebParamE<JDIndent> indentParse(String html, String taskid,String url) {
		tracerLog.output("indentParse", "解析中");

		try {
			WebParamE<JDIndent> result = HtmlParse.indentParse(html, taskid);
			
			return result;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.output("indentParse error "+url, html);
		}
		
		WebParamE<JDIndent> result = new WebParamE<>();
		result.setPagnum("0");
		return result;

	}

}
