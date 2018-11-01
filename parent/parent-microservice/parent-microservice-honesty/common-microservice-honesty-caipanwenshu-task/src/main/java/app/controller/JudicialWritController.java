package app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.honesty.bean.HonestyTaskBean;

import app.bean.HonestyJsonBean;
import app.bean.IsDoneBean;
import app.client.aws.AwsApiClient;
import app.commontracerlog.TracerLog;
import app.service.JudicialWritService;
@RestController
@Configuration
@RequestMapping("/honesty") 
@EnableScheduling
public class JudicialWritController {

	@Autowired
	private AwsApiClient awsApiClient;
	@Value("${webdriver.proxy}")
	int proxy;
	@Autowired
	private JudicialWritService judicialWritService;
	@Autowired
	private TracerLog tracer;
	@PostMapping(value = "/caipanwenshu/task")
	public IsDoneBean JudicialWritCrawler(@RequestBody HonestyTaskBean honestyJsonBean) throws Exception{
		tracer.addTag("taskid", honestyJsonBean.getTaskid());
		tracer.output("HonestyJsonBean", honestyJsonBean.toString());
	/*	HttpProxyBean httpProxyBean = null;
		if (proxy == 1) {
			httpProxyBean = awsApiClient.getProxy();
			System.out.println("url:------------->"+httpProxyBean.getIp()+":"+httpProxyBean.getPort());
			
		}
		if (null == honestyJsonBean.getTaskid()) {
			tracer.output("taskid is null !", "");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("taskid is null !");
			return isDoneBean;
		}
		if (honestyJsonBean.getKeys().size() < 0) {
			tracer.output("keys is null !", "");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("keys is null !");
		}

		
		return judicialWritService.crawler(honestyJsonBean,httpProxyBean);*/
		return null;
		
	}
}
