package app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.tongyi.fuyang.HousingAnHuiTongyiFuYangPay;
import com.microservice.dao.entity.crawler.housing.tongyi.fuyang.HousingAnHuiTongyiFuYangUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.tongyi.fuyang.HousingAnHuiTongyiFuYangPayRepository;
import com.microservice.dao.repository.crawler.housing.tongyi.fuyang.HousingAnHuiTongyiFuYangUserInfoRepository;

import app.bean.PayRootBean;
import app.bean.UserInfoRootBean;
import app.commontracerlog.TracerLog;
import app.htmlparse.HousingFuYangParse;
import app.service.common.HousingBasicService;

/**
 * 
 * 项目名称：common-microservice-housingfund-sz-anhui-tongyi
 * 类名称：HousingAnhuiTongyiService 类描述： 创建人：hyx 创建时间：2018年4月25日 上午11:31:02
 * 
 * @version
 */

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tongyi.fuyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tongyi.fuyang")
public class HousingFuYangAsyncService extends HousingBasicService {

	@Autowired
	protected TaskHousingRepository taskHousingRepository;

	@Autowired
	public TracerLog tracerLog;

	@Autowired
	public HousingFuYangGetService housingFuYangGetService;

	@Autowired
	public HousingAnHuiTongyiFuYangUserInfoRepository housingAnHuiTongyiFuYangUserInfoRepository;

	@Autowired
	public HousingAnHuiTongyiFuYangPayRepository housingAnHuiTongyiFuYangPayRepository;

	public String getPersionId(WebClient webClient) throws Exception {

		return housingFuYangGetService.getPersionId(webClient);
	}

	public void getPersionInfo(WebClient webClient, String persionid, String taskid) {

		try {
			Page page = housingFuYangGetService.getPesionInfo(webClient, persionid);

			UserInfoRootBean rootbean = HousingFuYangParse.basicusereParse(page.getWebResponse().getContentAsString(),
					taskid);

			List<HousingAnHuiTongyiFuYangUserInfo> rows = rootbean.getDataset().getRows();
			List<HousingAnHuiTongyiFuYangUserInfo> rows2 = new ArrayList<>();
			for (HousingAnHuiTongyiFuYangUserInfo userinfo : rows) {
				userinfo.setTaskid(taskid);

				rows2.add(userinfo);
			}

			housingAnHuiTongyiFuYangUserInfoRepository.saveAll(rows2);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getError_code(), taskid);

	}

	public void getPayInfo(WebClient webClient, String persionid, String taskid) {

		try {
			Page page = housingFuYangGetService.getPay(webClient, persionid);

			PayRootBean rootbean = HousingFuYangParse.payParse(page.getWebResponse().getContentAsString(), taskid);

			List<HousingAnHuiTongyiFuYangPay> rows = rootbean.getDataset().getRows();
			List<HousingAnHuiTongyiFuYangPay> rows2 = new ArrayList<>();
			for (HousingAnHuiTongyiFuYangPay payInfo : rows) {
				payInfo.setTaskid(taskid);

				rows2.add(payInfo);
			}

			housingAnHuiTongyiFuYangPayRepository.saveAll(rows2);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getError_code(), taskid);

	}

}
