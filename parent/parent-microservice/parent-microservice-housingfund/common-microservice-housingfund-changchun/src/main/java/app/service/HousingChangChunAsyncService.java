package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.changchun.HousingChangChunPay;
import com.microservice.dao.entity.crawler.housing.changchun.HousingChangChunUserInfo;
import com.microservice.dao.repository.crawler.housing.changchun.HousingChangChunPayRepository;
import com.microservice.dao.repository.crawler.housing.changchun.HousingChangChunUserInfoRepository;

import app.bean.WebParamHousing;
import app.crawler.htmlparse.HousingChangChunParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.changchun")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.changchun")
public class HousingChangChunAsyncService extends HousingBasicService {

	@Autowired
	private LoginAndGetService loginAndGetService;
	@Autowired
	private HousingChangChunPayRepository housingChangChunPayRepository;
	
	@Autowired
	private HousingChangChunUserInfoRepository housingChangChunUserInfoRepository;

	@Async
	public void getAccountInfor(WebClient webClient, MessageLoginForHousing messageLoginForHousing, Map<String, String> map,
			TaskHousing taskHousing) {
		List<Future<WebParamHousing<?>>> listfuture = new ArrayList<>();
		List<WebParamHousing<?>> listWebParamHousing = new ArrayList<>();
		List<HousingChangChunUserInfo> listHousingChangChunUserInfo = new ArrayList<>();

		try {
			Future<WebParamHousing<?>> future1 = loginAndGetService.getAccountInfor(webClient, map);
			listfuture.add(future1);

			boolean istrue = true;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				for (Future<WebParamHousing<?>> future : listfuture) {

					if (future.isDone()) { // 判断是否执行完毕
						System.out.println("Result " + future.get().toString() + ":::" + future.isDone());
						listWebParamHousing.add(future.get());
						listfuture.remove(future);
						break;
					}
				}
				if (listfuture.size() == 0) {
					istrue = false;
				}
			}

			Integer statuCode = 0;

			for (WebParamHousing<?> webParamHousing : listWebParamHousing) {
				double statcCode = (double) (200 / (listWebParamHousing.size()));
				if(webParamHousing.getPage().getWebResponse().getContentAsString().indexOf("操作过于频繁")!=-1){
					continue;
				}
				if (webParamHousing.getStatusCode() == 200) {
					listHousingChangChunUserInfo.add(HousingChangChunParse
							.userinfo_parse(webParamHousing.getPage().getWebResponse().getContentAsString()));
					statuCode = (int) (statuCode + statcCode);
				}
			}
			
			System.out.println("==getAccountInfor=="+statuCode);
			
			for(HousingChangChunUserInfo result :listHousingChangChunUserInfo){
				result.setUserid(messageLoginForHousing.getUser_id());
				result.setTaskid(taskHousing.getTaskid());
				housingChangChunUserInfoRepository.save(result);
			}
			if(statuCode == 0){
				statuCode = 404;
			}
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(),
					statuCode , taskHousing.getTaskid());
			taskHousing = findTaskHousing(taskHousing.getTaskid());
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Async
	public void getAccountDetails(WebClient webClient, MessageLoginForHousing messageLoginForHousing,
			Map<String, String> map, TaskHousing taskHousing) {
		List<Future<WebParamHousing<?>>> listfuture = new ArrayList<>();
		List<WebParamHousing<?>> listWebParamHousing = new ArrayList<>();
		List<HousingChangChunPay> listHousingChangChunPay = new ArrayList<>();

		try {
			Future<WebParamHousing<?>> future1 = loginAndGetService.getAccountDetails(webClient, map);
			listfuture.add(future1);

			boolean istrue = true;
			while (istrue) { /// 这里使用了循环判断，等待获取结果信息
				for (Future<WebParamHousing<?>> future : listfuture) {

					if (future.isDone()) { // 判断是否执行完毕
						System.out.println("Result " + future.get().toString() + ":::" + future.isDone());
						listWebParamHousing.add(future.get());
						listfuture.remove(future);
						break;
					}
				}
				if (listfuture.size() == 0) {
					istrue = false;
				}
			}
			Integer statuCode = 0;
			for (WebParamHousing<?> webParamHousing : listWebParamHousing) {
				double code = (double) (200 / (listWebParamHousing.size()));
				if(webParamHousing.getPage().getWebResponse().getContentAsString().indexOf("操作过于频繁")!=-1){
					continue;
				}
				if (webParamHousing.getStatusCode() == 200) {
					listHousingChangChunPay.addAll(HousingChangChunParse
							.pay_parse(webParamHousing.getPage().getWebResponse().getContentAsString()));
					statuCode =  (int) (statuCode + code);
				}
			}
			
			for(HousingChangChunPay result :listHousingChangChunPay){
				result.setUserid(messageLoginForHousing.getUser_id());
				result.setTaskid(taskHousing.getTaskid());
				housingChangChunPayRepository.save(result);
			}
			System.out.println("==getAccountDetails=="+statuCode);
			if(statuCode == 0){
				statuCode = 404;
			}
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), statuCode,
					taskHousing.getTaskid());
			taskHousing = findTaskHousing(taskHousing.getTaskid());
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
