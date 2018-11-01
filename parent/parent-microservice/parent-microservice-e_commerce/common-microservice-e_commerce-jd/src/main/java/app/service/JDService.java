package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;

/**
 * 
 * 项目名称：common-microservice-e_commerce-jd 类名称：JDCrawlerService 类描述： 创建人：hyx
 * 创建时间：2017年12月20日 上午9:28:28
 * 
 * @version
 */
@Component
@EnableAsync
public class JDService {

	@Autowired
	private JDCrawlerService jDCrawlerService;

	@Autowired
	private E_CommerceTaskStatusService e_CommerceTaskStatusService;

	@Async
	public void loginCrawler(E_CommerceJsonBean e_CommerceJsonBean) {
		E_CommerceTask e_CommerceTask = jDCrawlerService.login(e_CommerceJsonBean);

		if (e_CommerceTask != null) {
			if (e_CommerceTask.getPhase()
					.indexOf(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase()) != -1
					&& e_CommerceTask.getPhase_status()
							.indexOf(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus()) != -1) {
				jDCrawlerService.getAllData(e_CommerceJsonBean);
			}
		}

	}

	public E_CommerceTask checkJDQRcode(E_CommerceJsonBean e_CommerceJsonBean) {
		E_CommerceTask e_CommerceTask = jDCrawlerService.checkQRcode(e_CommerceJsonBean);
		if (e_CommerceTask != null) {
			if (e_CommerceTask.getPhase()
					.indexOf(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhase()) != -1
					&& e_CommerceTask.getPhase_status()
							.indexOf(E_ComerceStatusCode.E_COMMERCE_LOGIN_SUCCESS_NEXTSTEP.getPhasestatus()) != -1) {

				/*e_CommerceTask = e_CommerceTaskStatusService.changeStatus(
						E_ComerceStatusCode.E_COMMERCE_CRAWLER_DOING.getPhase(),
						E_ComerceStatusCode.E_COMMERCE_CRAWLER_DOING.getPhasestatus(),
						E_ComerceStatusCode.E_COMMERCE_CRAWLER_DOING.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_CRAWLER_DOING.getError_code(), false,
						e_CommerceTask.getTaskid());*/
				jDCrawlerService.getAllData(e_CommerceJsonBean);
			}
		}

		return e_CommerceTask;

	}

	public E_CommerceTask getQRcode(E_CommerceJsonBean e_CommerceJsonBean) {
		E_CommerceTask e_CommerceTask = jDCrawlerService.getQRcode(e_CommerceJsonBean);

		return e_CommerceTask;

	}

	public E_CommerceTask verifySmsCrawler(E_CommerceJsonBean e_CommerceJsonBean) {
		E_CommerceTask e_CommerceTask = jDCrawlerService.verifySms(e_CommerceJsonBean);

		if (e_CommerceTask != null) {
			if (e_CommerceTask.getPhase().indexOf(E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_SUCCESS.getPhase()) != -1
					&& e_CommerceTask.getPhase_status()
							.indexOf(E_ComerceStatusCode.E_COMMERCE_VALIDATE_CODE_SUCCESS.getPhasestatus()) != -1) {
				jDCrawlerService.getAllData(e_CommerceJsonBean);
			}
		}

		return e_CommerceTask;
	}

}
