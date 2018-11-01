package app.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.BeiJingCenterBean;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HousingBJParse;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
public class HousingBeiJingCentFutureService extends HousingBasicService implements ICrawlerLogin {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private HousingBeijingCentCrawlerService housingBeijingCentCrawlerService;
	
	@Autowired
	private TaskHousingRepository taskHousingRepository;

	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String url = "https://grwsyw.bjgjj.gov.cn/ish/home";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = LoginAndGetCommon.addcookie(webClient, taskHousing);

		webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/");
		webClient.addRequestHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");

		try {
			Page page = LoginAndGetCommon.getHtml(url, webClient);
			String rgex = "poolSelect = (.*?)\\;";
			Pattern pattern = Pattern.compile(rgex);// 匹配的模式
			Matcher m = pattern.matcher(page.getWebResponse().getContentAsString());
			String value = "";
			while (m.find()) {
				value = m.group(1);
			}

			BeiJingCenterBean beiJingCenterBean = HousingBJParse.beijingcenter_need_parse(value);
			
			tracerLog.addTag("beiJingCenterBean", beiJingCenterBean.toString());
			taskHousing = housingBeijingCentCrawlerService.getBasic(beiJingCenterBean, webClient,taskHousing);
			taskHousing = housingBeijingCentCrawlerService.getBasicPay(beiJingCenterBean, webClient,taskHousing);
			taskHousing = housingBeijingCentCrawlerService.getPay(beiJingCenterBean, webClient,taskHousing);
			taskHousing = taskHousingRepository.save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		housingBeijingCentCrawlerService.login(messageLoginForHousing);

		return taskHousing;
	}

}