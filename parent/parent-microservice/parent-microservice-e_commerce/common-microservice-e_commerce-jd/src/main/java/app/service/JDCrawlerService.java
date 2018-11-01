package app.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.bean.WebParamE;
import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.aop.ISms;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：JDCrawlerService 类描述： 创建人：hyx
 * 创建时间：2017年12月20日 上午9:28:28
 * 
 * @version
 */
@Component
@EnableAsync
public class JDCrawlerService implements ISms, ICrawlerLogin {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private JDLoginAndGetService jdLoginAndGetService;

	@Autowired
	private E_CommerceTaskStatusService e_CommerceTaskStatusService;

	@Autowired
	private E_CommerceTaskRepository e_commerceTaskRepository;

	@Autowired
	private JDFutureService jdFutureService;

	@Autowired
	private AgentService agentService;

	private WebParamE<?> webParamE = null;

	@Override
	public E_CommerceTask login(E_CommerceJsonBean e_CommerceJsonBean) {
		long startTime = System.currentTimeMillis(); // 获取开始时间

		E_CommerceTask e_commerceTask = e_CommerceTaskStatusService.changeStatusLoginDoing(e_CommerceJsonBean);

		webParamE = null;
		try {
			webParamE = jdLoginAndGetService.loginChrome(e_CommerceJsonBean, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(), e.getMessage(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getError_code(), false, e_CommerceJsonBean.getTaskid());

		}

		if (webParamE.getErrormessage() != null) {

			if (webParamE.getErrormessage().indexOf("接收短信手机号为") != -1) {
				e_CommerceTaskStatusService.changeStatus(
						E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getPhase(),
						E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
						webParamE.getErrormessage(),
						E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getError_code(), false,
						e_CommerceJsonBean.getTaskid());
			} else {
				e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
						E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(), webParamE.getErrormessage(),
						E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getError_code(), false,
						e_CommerceJsonBean.getTaskid());
				WebDriver driver = webParamE.getDriver();
				agentService.releaseInstance(e_commerceTask.getCrawlerHost(), driver);

				e_commerceTask = e_CommerceTaskStatusService.changecommerceTaskJDFinish(e_CommerceJsonBean.getTaskid(),
						driver);
			}

		} else {
			// crawler(e_commerceTask, webParamE);
			e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), webParamE.getErrormessage(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false,
					e_CommerceJsonBean.getTaskid());
		}
		long endTime = System.currentTimeMillis(); // 获取结束时间
		System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
		System.out.println("程序运行时间： " + (endTime - startTime) / 1000 + "s");
		return e_commerceTask;
	}

	@Override
	public E_CommerceTask verifySms(E_CommerceJsonBean e_CommerceJsonBean) {
		E_CommerceTask e_commerceTask = e_CommerceTaskStatusService.changeStatusLoginDoing(e_CommerceJsonBean);

		WebParamE<?> webParamE = jdLoginAndGetService.setSMS(e_CommerceJsonBean);

		if (webParamE.getErrormessage() != null) {

			e_commerceTask = e_CommerceTaskStatusService.changeStatus(
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_ERROR.getPhasestatus(), webParamE.getErrormessage(),
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_ERROR.getError_code(), false,
					e_CommerceJsonBean.getTaskid());
			WebDriver driver = webParamE.getDriver();
			agentService.releaseInstance(e_commerceTask.getCrawlerHost(), driver);

			e_commerceTask = e_CommerceTaskStatusService.changecommerceTaskJDFinish(e_CommerceJsonBean.getTaskid(),
					driver);

		} else {
			e_commerceTask = e_CommerceTaskStatusService.changeStatus(
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_SUCCESS.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_SUCCESS.getPhasestatus(), webParamE.getErrormessage(),
					E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_SUCCESS.getError_code(), false,
					e_CommerceJsonBean.getTaskid());
		}
		return e_commerceTask;
	}

	@Override
	public E_CommerceTask getQRcode(E_CommerceJsonBean e_CommerceJsonBean) {
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		webParamE = null;
		try {
			webParamE = jdLoginAndGetService.loginChromeByQRcode(e_CommerceJsonBean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_TIMEOUT_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_TIMEOUT_ERROR.getPhasestatus(),null,
					E_ComerceStatusCode.E_COMMERCE_LOGIN_TIMEOUT_ERROR.getError_code(), false,
					e_CommerceJsonBean.getTaskid());

		}

		e_commerceTask.setBaseCode(webParamE.getBase64img());
		e_commerceTask.setQrUrl(webParamE.getUrl());
		e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhase());
		e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getPhasestatus());
		e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getDescription());
		e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_WATING_SCAN_QRCODE.getError_code());
		e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		return e_commerceTask;
	}

	@Override
	public E_CommerceTask checkQRcode(E_CommerceJsonBean e_CommerceJsonBean) {
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		webParamE = null;
		try {
			webParamE = jdLoginAndGetService.checkJDQRcode(e_commerceTask);

		} catch (Exception e) {
			e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(), e.getMessage(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getError_code(), false, e_CommerceJsonBean.getTaskid());
		}

		if (webParamE.getErrormessage() != null) {

			if (webParamE.getErrormessage().indexOf("接收短信手机号为") != -1) {
				e_commerceTask = e_CommerceTaskStatusService.changeStatus(
						E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getPhase(),
						E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getPhasestatus(),
						webParamE.getErrormessage(),
						E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEEDSMS.getError_code(), false,
						e_CommerceJsonBean.getTaskid());
			} else if (webParamE.getErrormessage().indexOf("二维码已失效") != -1) {
				e_CommerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
						E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(), webParamE.getErrormessage(),
						E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getError_code(), false,
						e_CommerceJsonBean.getTaskid());
				WebDriver driver = webParamE.getDriver();
				agentService.releaseInstance(e_commerceTask.getCrawlerHost(), driver);

				e_commerceTask = e_CommerceTaskStatusService.changecommerceTaskJDFinish(e_CommerceJsonBean.getTaskid(),
						driver);
			}

		} else {
			// crawler(e_commerceTask, webParamE);
			e_commerceTask = e_CommerceTaskStatusService.changeStatus(
					E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus(), webParamE.getErrormessage(),
					E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getError_code(), false,
					e_CommerceJsonBean.getTaskid());

		}

		return e_commerceTask;
	}

	@Override
	@Async
	public E_CommerceTask getAllData(E_CommerceJsonBean e_CommerceJsonBean) {

		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_CommerceJsonBean.getTaskid());

		

		 WebDriver driver = webParamE.getDriver();
		String taskid = e_commerceTask.getTaskid();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			// driver =
			jdFutureService.getRenZheng(taskid);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("renzhengParse error ", e.getMessage());
			e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getError_code());
			e_commerceTask.setRenzhengInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

		try {
			// driver =
			jdFutureService.getUserInfo(taskid);

		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("userInfoParse error ", e.getMessage());
			e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getError_code());
			e_commerceTask.setUserinfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

		try {
			// driver =
			jdFutureService.getReceiverAddress(taskid);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("receiverAddressParse error ", e.getMessage());
			e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getError_code());
			e_commerceTask.setAddressInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

		try {
			// driver =
			jdFutureService.getQueryBindCard(taskid);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("queryBindCardParse error ", e.getMessage());
			e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getError_code());
			e_commerceTask.setBankCardInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

		try {
			// driver =
			jdFutureService.getCoffers(taskid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// driver =
			jdFutureService.getIndent(taskid);
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("queryBindCardParse error ", e.getMessage());
			e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getError_code());
			e_commerceTask.setOrderInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

		try {
			jdFutureService.getBtPrivilege(taskid);
		} catch (Exception e) {
			e.printStackTrace();

			tracerLog.output("btprivilegeParse error ", e.getMessage());
			e_commerceTask = e_commerceTaskRepository.findByTaskid(taskid);
			e_commerceTask.setPhase(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getPhase());
			e_commerceTask.setPhase_status(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getPhasestatus());
			e_commerceTask.setDescription(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getDescription());
			e_commerceTask.setError_code(E_ComerceStatusCode.E_COMMERCE_BTPRIVILEGE_ERROR.getError_code());
			e_commerceTask.setBtPrivilegeInfoStatus(404);
			e_commerceTask = e_commerceTaskRepository.save(e_commerceTask);
		}

		e_commerceTask = e_CommerceTaskStatusService.changecommerceTaskJDFinish(e_commerceTask.getTaskid(), driver);
		return e_commerceTask;

	}

	@Override
	public E_CommerceTask getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E_CommerceTask sendSms(E_CommerceJsonBean e_CommerceJsonBean) {
		// TODO Auto-generated method stub
		return null;
	}

}
