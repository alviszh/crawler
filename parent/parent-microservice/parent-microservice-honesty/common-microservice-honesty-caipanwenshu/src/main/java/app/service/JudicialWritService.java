package app.service;


import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritKeyWord;
import com.microservice.dao.entity.crawler.honesty.judicialwrit.JudicialWritTask;
import com.microservice.dao.repository.crawler.honesty.judicialwrit.JudicialWritKeyWordRepository;
import com.microservice.dao.repository.crawler.honesty.judicialwrit.JudicialWritTaskRepository;

import app.bean.WebParam;
import app.client.aws.AwsApiClient;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.honesty.judicialwrit" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.honesty.judicialwrit" })
public class JudicialWritService {

	@Autowired
	private JudicialWritParameterService judicialWritParameterService;
	@Autowired
	private JudicialWritTaskRepository judicialWritTaskRepository;
	@Autowired
	private JudicialWritGetAllService judicialWritGetAllService;
	@Autowired
	private JudicialWritKeyWordRepository judicialWritKeyWordRepository;
	@Autowired
	private JudicialWritAmountService judicialWritAmountService;
	@Autowired
	private AwsApiClient awsApiClient;
	@Async
	public void crawler(HttpProxyBean httpProxyBean) throws Exception{

		String uuid = UUID.randomUUID().toString();
		JudicialWritTask judicialWritTask = new JudicialWritTask();
		judicialWritTask.setTaskid(uuid);
		judicialWritTask.setDescription("程序启动完成！");
		judicialWritTaskRepository.save(judicialWritTask);
		//--------------------------------------V1-----------------------------------------------------------------
		//关键字
		List<JudicialWritKeyWord> list = judicialWritKeyWordRepository.findAll();
		String keString = "";
		for (JudicialWritKeyWord judicialWritKeyWord : list) {
			String uuid2 = judicialWritKeyWord.getUuid();
			if(uuid2==null||uuid2.equals("")){
				//参数
				WebParam webParam = judicialWritParameterService.getParameter(httpProxyBean,judicialWritTask,1);
				if(webParam!=null){
					judicialWritTask = judicialWritTaskRepository.findByTaskid(judicialWritTask.getTaskid());
					String keyword = judicialWritKeyWord.getKeyword();
					keString = keyword;
					if(keyword==null){
						keyword = "";
					}else if(keyword.equals("案件类型:民事案件")||keyword.equals("案件类型:刑事案件")||keyword.equals("案件类型:行政案件")
							||keyword.equals("案件类型:赔偿案件")||keyword.equals("案件类型:执行案件")){
						keyword=URLEncoder.encode(keyword, "utf-8");
						webParam.getParameter().setNumber("wens");
					}
					else{
						keyword=URLEncoder.encode(keyword, "utf-8"); 
					}
//					int a= judicialWritGetAllService.getAlldata(webParam,httpProxyBean,judicialWritTask,0,keyword);
//					System.out.println("=============================js=================================");
					int c = judicialWritAmountService.getAll(webParam, httpProxyBean, judicialWritTask, keyword, 1);
					if(c!=6){
						httpProxyBean = awsApiClient.getProxy();
						judicialWritAmountService.getAll(webParam, httpProxyBean, judicialWritTask, keyword, c);
					}
					
					int b = judicialWritAmountService.gettimeall(webParam, httpProxyBean, judicialWritTask, keyword, 1);
					if(b!=6){
						httpProxyBean = awsApiClient.getProxy();
						judicialWritAmountService.gettimeall(webParam, httpProxyBean, judicialWritTask, keyword, b);
					}
					
					int d = judicialWritAmountService.getprocedureall(webParam, httpProxyBean, judicialWritTask, keyword, 1);
					if(d!=6){
						httpProxyBean = awsApiClient.getProxy();
						judicialWritAmountService.getprocedureall(webParam, httpProxyBean, judicialWritTask, keyword, d);
					}
				}
			}
		}
		//---------------------------------------------------------------------------------------------------------


		//-----------------------------------------------------------V2--------------------------------------------
		/*for(int i=1;i<50;i++){
			String plusDays = LocalDate.now().plusDays(-i).toString();
			String keyword = "裁判日期:"+plusDays;
			System.out.println("裁判日期:"+plusDays);
			keyword=URLEncoder.encode(keyword, "utf-8"); 
			WebParam webParam = judicialWritParameterService.getParameter(httpProxyBean,judicialWritTask,1);
			int a= judicialWritAmountService.getAlldata(webParam,httpProxyBean,judicialWritTask,0,keyword);
			if(a!=6){
				httpProxyBean = awsApiClient.getProxy();
				judicialWritGetAllService.getAlldata(webParam,httpProxyBean,judicialWritTask,a,keyword);
			}
			
			int c = judicialWritAmountService.getAll(webParam, httpProxyBean, judicialWritTask, keyword, 1);
			if(c!=6){
				httpProxyBean = awsApiClient.getProxy();
				judicialWritAmountService.getAll(webParam, httpProxyBean, judicialWritTask, keyword, c);
			}
			
			int b = judicialWritAmountService.gettimeall(webParam, httpProxyBean, judicialWritTask, keyword, 1);
			if(b!=6){
				httpProxyBean = awsApiClient.getProxy();
				judicialWritAmountService.gettimeall(webParam, httpProxyBean, judicialWritTask, keyword, b);
			}
			
			int d = judicialWritAmountService.getprocedureall(webParam, httpProxyBean, judicialWritTask, keyword, 1);
			if(d!=6){
				httpProxyBean = awsApiClient.getProxy();
				judicialWritAmountService.getprocedureall(webParam, httpProxyBean, judicialWritTask, keyword, d);
			}
		}*/


		//	boolean b = judicialWritTaskRepository.updateBykey(keString);
		//		if(b==true){
		//		System.out.println("jieshu---------------");
		//		System.out.println("jieshu---------------");
		//		System.out.println("jieshu---------------");
		//		}
	}

}
