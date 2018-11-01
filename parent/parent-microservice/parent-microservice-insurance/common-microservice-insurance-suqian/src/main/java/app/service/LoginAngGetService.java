package app.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;

import app.bean.WebParamInsurance;


/**   
*    
* 项目名称：common-microservice-housingfund-yvlin   
* 类名称：LoginAngGetService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年1月12日 上午10:30:04   
* @version        
*/

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yvlin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yvlin")
public class LoginAngGetService extends InsuranceSuQianAndLianYunGangCommonService{
	
	@Value("${webdriver.chrome.driver.path}")
	String driverPath;

	@Value("${webdriver.chrome.driver.headless}")
	Boolean headless;

	
	public WebDriver intiChrome() throws Exception {
		System.out.println("launching chrome browser");
		System.setProperty("webdriver.chrome.driver", driverPath);

		// WebDriver driver = new ChromeDriver();
		ChromeOptions chromeOptions = new ChromeOptions();
		// 设置为 headless 模式 （必须）
		System.out.println("headless-------" + headless);
		// if(headless){
		// chromeOptions.addArguments("headless");// headless mode
		// }

		chromeOptions.addArguments("disable-gpu");
		// 设置浏览器窗口打开大小 （非必须）
		// chromeOptions.addArguments("--window-size=1920,1080");
		WebDriver driver = new ChromeDriver(chromeOptions);
		return driver;
	}
	
	
	/**   
	  *    
	  * 项目名称：common-microservice-housingfund-yvlin  
	  * 所属包名：app.service
	  * 类描述：   榆林公积金登录
	  * 创建人：hyx 
	  * 创建时间：2018年1月12日 
	  * @version 1  
	  * 返回值    WebDriver
	 * @return 
	  */
	public  WebParamInsurance<?> loginChrome(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		
		String url = "http://www.sqsbjf2.cn:9888/uaa/personlogin#/personLogin";
		
		return loginChromeCommon(url, insuranceRequestParameters);
		
	}
	
	/**   
	  *    
	  * 项目名称：common-microservice-insurance-suqian  
	  * 所属包名：app.service
	  * 类描述：   获取宿迁公积金
	  * 创建人：hyx 
	  * 创建时间：2018年3月9日 
	  * @version 1  
	  * 返回值    Page
	  */
	public  Page getUser(WebClient webClient) throws Exception {
		String url = "http://www.sqsbjf2.cn:9888/api/security/user";
		return  getHtml(url, webClient); 
	}
	
	public  Page getUserBasic(WebClient webClient,String persionId) throws Exception {
		String url = "http://www.sqsbjf2.cn:9888/ehrss-si-person/api/buzz/person/base/info?personId="
				+ persionId;
		return  getHtml(url, webClient); 
	}
	
	
	

	/**   
	  *    
	  * 项目名称：common-microservice-insurance-suqian  
	  * 所属包名：app.service
	  * 类描述：   
	  * 创建人：hyx 
	  * 创建时间：2018年3月9日 
	  * @version 1  
	  * 返回值    Page
	  */
	public Page getPay(WebClient webClient,String startDate,String endDate,String personId) throws Exception {
		String url = "http://www.sqsbjf2.cn:9888/ehrss-si-person/api/rights/paymenthome/detail/query"
				+ "?endDate="+endDate.trim()
				+ "&personId="+personId.trim()
				+ "&startDate="+startDate.trim();

		return  getHtml(url, webClient); 
	}
}
