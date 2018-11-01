package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.anqing.HousingAnQingPay;
import com.microservice.dao.entity.crawler.housing.anqing.HousingAnQingUserinfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.anqing.HousingAnQingPayRepository;
import com.microservice.dao.repository.crawler.housing.anqing.HousingAnQingUserinfoRepository;

import app.bean.WebParamHousing;
import app.crawler.htmlparse.HousingAnQingParse;
import app.service.common.HousingBasicService;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.anqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.anqing")
public class HousingAsyncService extends HousingBasicService {

	@Autowired
	private LoginAndGetService loginAndGetService;

	@Autowired
	private HousingAnQingPayRepository housingAnQingPayRepository;
	@Autowired
	private HousingAnQingUserinfoRepository housingAnQingUserinfoRepository;
	
	
	

	@Async
	public void getAccountDetails(WebClient webClient, MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) {
		List<Future<WebParamHousing<?>>> listfuture = new ArrayList<>();
		List<WebParamHousing<?>> listWebParamHousing = new ArrayList<>();
		List<HousingAnQingPay> listHousingAnQingPay = new ArrayList<>();

		try {
			Future<WebParamHousing<?>> future1 = loginAndGetService.getAccountDetails(webClient, 1 + "");
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
			Integer pagenum = 1;
			for (WebParamHousing<?> webParamHousing : listWebParamHousing) {
				double statcCode = (double) (200 / (listWebParamHousing.size()));
				if (webParamHousing.getStatusCode() == 200) {
					WebParamHousing<HousingAnQingPay> webParamHousingresult = HousingAnQingParse
							.pay_parse(webParamHousing.getPage().getWebResponse().getContentAsString());
					pagenum = Integer.parseInt(webParamHousingresult.getPagnum().trim());
					listHousingAnQingPay.addAll(webParamHousingresult.getList());
					statuCode = (int) (statuCode + statcCode);
				}
			}

			System.out.println("==getAccountInfor==" + statuCode);

			for (HousingAnQingPay result : listHousingAnQingPay) {
				result.setUserid(messageLoginForHousing.getUser_id());
				result.setTaskid(taskHousing.getTaskid());
				housingAnQingPayRepository.save(result);
			}

			if (pagenum > 1) {
				for (int i = 1; i <= pagenum; i++) {
					Future<WebParamHousing<?>> future = loginAndGetService.getAccountDetails(webClient, 1 + "");
					listfuture.add(future);
				}

				istrue = true;
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

				for (WebParamHousing<?> webParamHousing : listWebParamHousing) {
					double statcCode = (double) (200 / (listWebParamHousing.size()));
					if (webParamHousing.getStatusCode() == 200) {
						WebParamHousing<HousingAnQingPay> webParamHousingresult = HousingAnQingParse
								.pay_parse(webParamHousing.getPage().getWebResponse().getContentAsString());
						pagenum = Integer.parseInt(webParamHousingresult.getPagnum().trim());
						listHousingAnQingPay.addAll(webParamHousingresult.getList());
						statuCode = (int) (statuCode + statcCode);
					}
				}

				System.out.println("==getAccountInfor==" + statuCode);

				for (HousingAnQingPay result : listHousingAnQingPay) {
					result.setUserid(messageLoginForHousing.getUser_id());
					result.setTaskid(taskHousing.getTaskid());
					housingAnQingPayRepository.save(result);
				}
			}

			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), statuCode,
					taskHousing.getTaskid());
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Async
	public void getAccountInfor(WebParamHousing<?> webParamHousing, MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) {
		WebParamHousing<HousingAnQingUserinfo> webParamHousingresult = HousingAnQingParse
				.userinfo_parse(webParamHousing.getPage().getWebResponse().getContentAsString());
		if(webParamHousingresult.getErrormessage()!=null){
			updateUserInfoStatusByTaskid(webParamHousingresult.getErrormessage(), 404,
					taskHousing.getTaskid());
			updateTaskHousing(taskHousing.getTaskid());
		}else{
			List<HousingAnQingUserinfo> listHousingAnQingUserinfo = webParamHousingresult.getList();

			for (HousingAnQingUserinfo result : listHousingAnQingUserinfo) {
				result.setUserid(messageLoginForHousing.getUser_id());
				result.setTaskid(taskHousing.getTaskid());
				housingAnQingUserinfoRepository.save(result);
			}
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
					taskHousing.getTaskid());
			updateTaskHousing(taskHousing.getTaskid());
		}
	
	}

}
