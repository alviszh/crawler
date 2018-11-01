package app.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.aws.json.HttpProxyBean;
import com.microservice.dao.repository.crawler.honesty.judicialwrit.JudicialWritTaskRepository;

import app.client.aws.AwsApiClient;
import app.service.JudicialWritService;
@RestController
@Configuration
@RequestMapping("/honesty") 
@EnableScheduling
public class JudicialWritController {

	@Autowired
	private AwsApiClient awsApiClient;
	@Autowired
	private JudicialWritService judicialWritService;
	@Autowired
	private JudicialWritTaskRepository judicialWritTaskRepository;
	@Value("${webdriver.proxy}")
	int proxy;
	
//	@Scheduled(cron = "0/2 * * * * ?")
	@PostMapping(value = "/crawler/cpws")
	public void JudicialWritCrawler(){
		
		HttpProxyBean httpProxyBean = null;
		if (proxy == 1) {
			httpProxyBean = awsApiClient.getProxy();
			System.out.println("url:------------->"+httpProxyBean.getIp()+":"+httpProxyBean.getPort());
			
		}
		try {
			judicialWritService.crawler(httpProxyBean);
		} catch (Exception e) {
			if(e.getMessage().indexOf("Read timed out")!=-1){
				JudicialWritCrawler();
			}else if(e.getMessage().indexOf("connect timed out")!=-1){
				JudicialWritCrawler();
			}
			System.out.println(e);
		}
		
	}
}
