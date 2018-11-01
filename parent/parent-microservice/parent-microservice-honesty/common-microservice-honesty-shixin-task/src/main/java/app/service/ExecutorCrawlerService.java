package app.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.gargoylesoftware.htmlunit.Page;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;
import com.microservice.dao.entity.crawler.honesty.shixin.ShiXinBean;
import com.microservice.dao.repository.crawler.honesty.shixin.HonestyTaskRepository;
import com.microservice.dao.repository.crawler.honesty.shixin.ShiXinBeanRepository;

import app.client.aws.AwsApiClient;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HtmlParse;

/**
 * 
 * 项目名称：common-microservice-executor 类名称：ExecutorGetHtmlService 类描述： 创建人：hyx
 * 创建时间：2018年7月12日 上午11:08:38
 * 
 * @version
 */
@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.honesty.shixin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.honesty.shixin")
@EnableAsync
public class ExecutorCrawlerService {

	@Autowired
	private ExecutorGetHtmlService executorGetHtmlService;
	@Autowired
	private ShiXinBeanRepository shiXinBeanRepository;

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private AwsApiClient awsApiClient;

	@Value("${webdriver.proxy}")
	Integer proxy;

	@Autowired
	private HonestyTaskRepository honestyTaskRepository;

	@Async("ExecutorForSearch") // 配置类中的方法名
	public Future<String> getShiXinBreakPromise(String captchaId, long shixinid, String code,
			HttpProxyBean httpProxyBean) {

		String url = "http://zxgk.court.gov.cn/shixin/disDetail?" + "id=" + shixinid + "&pCode=" + code + "&captchaId="
				+ captchaId.trim();
		tracerLog.output("getBreakPromise url", url);

		try {
			Page page = executorGetHtmlService.getByHtmlUnit(url, httpProxyBean);

			tracerLog.output("page", page.getWebResponse().getContentAsString());

			if (page.getWebResponse().getContentAsString().indexOf("Status 404") != -1) {
				return new AsyncResult<String>("error");
			}

			if (page == null || page.getWebResponse().getContentAsString().length() < 30) {
				return new AsyncResult<String>("error");
			} else {
				ShiXinBean result = HtmlParse.shixin_parse(page.getWebResponse().getContentAsString());
				result.setShixinid(shixinid);
				System.out.println(result.toString());
				List<ShiXinBean> result_re = shiXinBeanRepository.findByShixinid(shixinid);
				if (result_re.size() > 0) {
					System.out.println("===========数据重复===============");
				} else {
					System.out.println("===========数据未存在 保存===============");
					shiXinBeanRepository.save(result);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new AsyncResult<String>("error");
		}
		return new AsyncResult<String>("sucess");
	}

	public void save(ShiXinBean shiXinBean) {

		List<ShiXinBean> result = shiXinBeanRepository.findByShixinid(shiXinBean.getShixinid());
		if (result.size() > 0) {
			System.out.println("===========数据重复===============");
		} else {
			System.out.println("===========数据未存在 保存===============");
			shiXinBeanRepository.save(shiXinBean);

		}

	}

	public Queue<HonestyTask> getHonestyTask(Queue<HonestyTask> queue) {

		List<HonestyTask> list2 = honestyTaskRepository.findTopNumByTaskidAndTypeAndPhase("0", 40);

		for (HonestyTask HonestyTask : list2) {
			HonestyTask.setPhase("1");
			HonestyTask.setRenum(HonestyTask.getRenum() + 1);
			if (proxy == 0) {
				HttpProxyBean httpProxyBean = awsApiClient.getProxy();
				System.out.println(httpProxyBean.getIp() + "==========" + httpProxyBean.getPort());
				HonestyTask.setIpaddress(httpProxyBean.getIp());
				HonestyTask.setIpport(httpProxyBean.getPort());
				HonestyTask = honestyTaskRepository.save(HonestyTask);
				tracerLog.output("istrueip HonestyTask", HonestyTask.toString());
			}
			try {
				HonestyTask = honestyTaskRepository.save(HonestyTask);
				tracerLog.output("queue 取出数据", HonestyTask.toString());
			} catch (Exception e) {
				tracerLog.output("存储 数据", e.getMessage());

			}

		}

		if (list2.size() > 0) {
			try {
				queue.addAll(list2);

			} catch (Exception e) {
				tracerLog.output("加入数据queue报错", e.getMessage());

				queue = new LinkedList<HonestyTask>();
				queue.addAll(list2);

			}
		} else {
			tracerLog.output("加入数据queue", "无爬取数据");
		}

		return queue;
	}


}
