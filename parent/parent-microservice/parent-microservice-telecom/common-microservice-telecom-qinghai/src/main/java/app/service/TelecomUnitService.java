package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.crawler.telecomhtmlunit.LognAndGetQingHaiUnit;
import app.unit.TeleComCommonUnit;

@Component
@Service
public class TelecomUnitService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	public WebClient getWebClientForTeleComQingHai(TaskMobile taskMobile) {
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		WebClient webClientlogin = WebCrawler.getInstance().getNewWebClient();
		webClientlogin = TeleComCommonUnit.addcookie(webClientlogin, taskMobile);

		String url = "http://wwwr.189.cn/dqmh/my189/initMy189home.do?fastcode=00900906";
		LognAndGetQingHaiUnit.getHtml(url, webClientlogin);

		url = "http://qh.189.cn/service/bill/initQueryTicket.parser?rnd=0.3154301050822139";

		LognAndGetQingHaiUnit.getHtml(url, webClientlogin);

		return webClientlogin;

	}

}
