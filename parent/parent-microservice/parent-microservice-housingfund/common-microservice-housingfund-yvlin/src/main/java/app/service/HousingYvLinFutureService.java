package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yvlin.HousingBasicPayResult;
import com.microservice.dao.entity.crawler.housing.yvlin.HousingBasicUserData;
import com.microservice.dao.repository.crawler.housing.yvlin.HousingBasicPayResultRepository;
import com.microservice.dao.repository.crawler.housing.yvlin.HousingBasicUserDataRepository;

import app.crawler.htmlparse.HousingYvLinParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yvlin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yvlin")
public class HousingYvLinFutureService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingYvLinFutureService.class);
	
	@Autowired
	private LoginAngGetService loginAngGetService;
	@Autowired
	private HousingBasicUserDataRepository housingBasicUserDataRepository;
	
	@Autowired
	private HousingBasicPayResultRepository housingBasicPayResultRepository;
	
	public  void getUserBasic(WebDriver driver,String taskid) {
		try {
			String html = loginAngGetService.getUserBasic(driver);
			HousingBasicUserData housingBasicUserData = HousingYvLinParse.basicusereParse(html, taskid);
			housingBasicUserDataRepository.save(housingBasicUserData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			TaskHousing taskHousing = findTaskHousing(taskid.trim());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());

			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
			save(taskHousing);
		}
		//
	}
	
	public  void getPayResult(WebDriver driver,String taskid) {
		String url = "http://www.ylzfgjj.cn/ylwsyyt/init.summer?_PROCID=70000002";
		driver.get(url);
				
		Set<org.openqa.selenium.Cookie> cookiesDriver = driver.manage().getCookies();

		Map<String, String> cookiesmap = new HashMap<>();
		for (org.openqa.selenium.Cookie cookie : cookiesDriver) {
			cookiesmap.put(cookie.getName(), cookie.getValue());
		}
		LocalDate nowdate = LocalDate.now();
		List<Future<String>> listfuture = new ArrayList<>();
		List<String> listhtml = new ArrayList<>();


		for(int i=0;;i++){
			int yearint = nowdate.plusYears(-i).getYear();
			if(yearint<2015){
				break;
			}else{
				String year = yearint+"";
				String begdate = yearint+"-01-01";
				String enddate = yearint+"-12-31";
				try{
					Future<String> future = loginAngGetService.getPayResult(driver, begdate, enddate, year , cookiesmap);
					listfuture.add(future);
				}catch(Exception e){
					e.printStackTrace();
				}
				

			}
		}
		
		boolean istrue = true;
		while (istrue) { /// 这里使用了循环判断，等待获取结果信息
			for (Future<String> future : listfuture) {
				if (future.isDone()) { // 判断是否执行完毕
					try {
						System.out.println("Result " + future.get().toString() + ":::" + future.isDone());
						listhtml.add(future.get());
						listfuture.remove(future);
						break;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (listfuture.size() == 0) {
				istrue = false;
			}
		}
		List<HousingBasicPayResult> result = new ArrayList<>();
		for(String html : listhtml){
			try {
				result.addAll(HousingYvLinParse.basicpayParse(html, taskid));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if(result.size()<0){
			TaskHousing taskHousing = findTaskHousing(taskid.trim());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());

			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getError_code());
			save(taskHousing);
		}else{
			housingBasicPayResultRepository.saveAll(result);
			TaskHousing taskHousing = findTaskHousing(taskid.trim());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());

			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getError_code());
			save(taskHousing);
		}

	}
	
	
}