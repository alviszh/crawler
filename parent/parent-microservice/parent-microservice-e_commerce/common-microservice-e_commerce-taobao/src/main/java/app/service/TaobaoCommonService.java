package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;

@EnableAsync
@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.e_commerce.basic",
        "com.microservice.dao.entity.crawler.e_commerce.taobao"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.e_commerce.basic",
        "com.microservice.dao.repository.crawler.e_commerce.taobao"})
public class TaobaoCommonService implements ICrawlerLogin{

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private TaobaoService taobaoService;
    @Autowired
    private E_CommerceTaskRepository e_commerceTaskRepository;
    @Autowired
    private E_CommerceTaskStatusService e_commerceTaskStatusService;

	@Override
	@Async
	public E_CommerceTask getAllData(E_CommerceJsonBean e_commerceJsonBean) {
		tracerLog.addTag("crawler.taobao.getAllData.taskid", e_commerceJsonBean.getTaskid());
		
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_commerceJsonBean.getTaskid());
		e_commerceTask = taobaoService.crawler(e_commerceJsonBean, e_commerceTask);
		
		return e_commerceTask;
	}

	@Override
	public E_CommerceTask getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Async
	public E_CommerceTask login(E_CommerceJsonBean e_commerceJsonBean) {
		tracerLog.addTag("crawler.taobao.login.taskid", e_commerceJsonBean.getTaskid());
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_commerceJsonBean.getTaskid());
		try {
            e_commerceTask = taobaoService.login4Alipay(e_commerceTask, e_commerceJsonBean);
        } catch (Exception e) {
            e.printStackTrace();
            tracerLog.output("crawler.taobao.login.exception", e.getMessage());
            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getPhase(),
            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getPhasestatus(),
            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR_GOTO_QRCODE.getDescription(),
                    null, false, e_commerceJsonBean.getTaskid());
            taobaoService.closeWebDirver(e_commerceJsonBean);
        }
		return e_commerceTask;
	}

	@Override
//	@Async
	public E_CommerceTask getQRcode(E_CommerceJsonBean e_commerceJsonBean) {
		tracerLog.addTag("crawler.taobao.getQRcode.taskid", e_commerceJsonBean.getTaskid());
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_commerceJsonBean.getTaskid());
		String logintype = e_commerceJsonBean.getLogintype();
		if(logintype.equals("aliQRcode")){
			try {
				e_commerceTask = taobaoService.getAliQRcode(e_commerceTask, e_commerceJsonBean);
	        } catch (Exception e) {
	            tracerLog.output("crawler.taobao.getAliQRcode.exception", e.getMessage());
	            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
	            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(),
	            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getDescription(),
	                    null, false, e_commerceJsonBean.getTaskid());
	            taobaoService.closeWebDirver(e_commerceJsonBean);
	            e.printStackTrace();
	        }
		}else if(logintype.equals("taobaoQRcode")){
			try {
	            e_commerceTask = taobaoService.getTaobaoQRcode(e_commerceTask, e_commerceJsonBean);
	        } catch (Exception e) {
	            tracerLog.output("crawler.bank.login.exception", e.getMessage());
	            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
	            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(),
	            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getDescription(),
	                    null, false, e_commerceJsonBean.getTaskid());
	            taobaoService.closeWebDirver(e_commerceJsonBean);
	            e.printStackTrace();
	        }
		}
		return e_commerceTask;
	}

	@Override
//	@Async
	public E_CommerceTask checkQRcode(E_CommerceJsonBean e_commerceJsonBean) {
		tracerLog.addTag("crawler.taobao.getQRcode.taskid", e_commerceJsonBean.getTaskid());
		String logintype = e_commerceJsonBean.getLogintype();
		E_CommerceTask e_commerceTask = e_commerceTaskRepository.findByTaskid(e_commerceJsonBean.getTaskid());
		do{
			if(logintype.equals("aliQRcode")){
				try {
		            e_commerceTask = taobaoService.checkAliQRcode(e_commerceTask, e_commerceJsonBean);
		        } catch (Exception e) {
		            e.printStackTrace();
		            tracerLog.output("crawler.taobao.checkAliQRcode.exception", e.getMessage());
		            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
		            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(),
		            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getDescription(),
		                    null, false, e_commerceJsonBean.getTaskid());
		            taobaoService.closeWebDirver(e_commerceJsonBean);
		        }
			}else if(logintype.equals("taobaoQRcode")){
				try {
		            e_commerceTask = taobaoService.checkQRcode(e_commerceTask, e_commerceJsonBean);
		        } catch (Exception e) {
		            e.printStackTrace();
		            tracerLog.output("crawler.bank.login.exception", e.getMessage());
		            e_commerceTask = e_commerceTaskStatusService.changeStatus(E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhase(),
		            		E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getPhasestatus(),
		                    E_ComerceStatusCode.E_COMMERCE_LOGIN_ERROR.getDescription(),
		                    null, false, e_commerceJsonBean.getTaskid());
		            taobaoService.closeWebDirver(e_commerceJsonBean);
		        }
			}
			e_commerceTask = e_commerceTaskRepository.findByTaskid(e_commerceJsonBean.getTaskid());
			try {
				System.out.println("do{}while();循环中++++++++task状态："+e_commerceTask.toString());
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(!e_commerceTask.getPhase_status().contains("ERROR") && !e_commerceTask.getPhase_status().contains("WAIT_TIMEOUT") && !e_commerceTask.getPhase_status().contains("SUCCESS_NEXTSTEP"));
		
		return e_commerceTask;
	}


}