package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shanghai.HousingShanghaiHtml;
import com.microservice.dao.entity.crawler.housing.shanghai.HousingShanghaiPay;
import com.microservice.dao.entity.crawler.housing.shanghai.HousingShanghaiUserInfo;
import com.microservice.dao.repository.crawler.housing.shanghai.HousingShanghaiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.shanghai.HousingShanghaiPayRepository;
import com.microservice.dao.repository.crawler.housing.shanghai.HousingShanghaiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingShanghaiParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.shanghai")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.shanghai")
public class HousingShanghaiFutureService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingShanghaiParse housingShanghaiParse;
	
	@Autowired
	private HousingShanghaiHtmlRepository housingShanghaiHtmlRepository;
	
	@Autowired
	private HousingShanghaiPayRepository housingShanghaiPayRepository;
	
	@Autowired
	private HousingShanghaiUserInfoRepository housingShanghaiUserInfoRepository;
	
	@Autowired
	private TracerLog tracer;
	
	private String userInfoUrl = "";

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskHousing loginTwo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, Integer count)
			throws Exception {

		tracer.addTag("HousingShanghaiFutureService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		if (null != taskHousing) {
			WebParam webParam = housingShanghaiParse.login(messageLoginForHousing, taskHousing);

			if (null == webParam) {
				tracer.addTag("HousingShanghaiFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				// 登录失败状态存储
				save(taskHousing);

				return taskHousing;
			} else {
				String html = webParam.getText();
				tracer.addTag("HousingShanghaiFutureService.login--次数:" + count,
						messageLoginForHousing.getTask_id() + html);
				if (html == null || "".equals(html)) {
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
					
					taskHousing.setCookies(webParam.getCookies());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					save(taskHousing);
					
					userInfoUrl = webParam.getUrl();
					
					return taskHousing;
				} else if (html.contains("请先注册再登录") || html.contains("用户名密码不匹配")) {

					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(html);
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					save(taskHousing);
					return taskHousing;
				} else if (html.contains("验证码不对")) {
					tracer.addTag("HousingShanghaiFutureService.login--失败次数:" + count,
							messageLoginForHousing.getTask_id());
					if (count < 4) {
						loginTwo(messageLoginForHousing, taskHousing, ++count);
					} else {
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
						taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
						taskHousing.setError_message(html);
						taskHousing.setLoginMessageJson(jsonObject.toString());
						// 登录失败状态存储
						save(taskHousing);
						return taskHousing;
					}
				} else {
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setError_message(html);
					taskHousing.setLoginMessageJson(jsonObject.toString());
					// 登录失败状态存储
					save(taskHousing);
					return taskHousing;
				}
			}
		}
		return taskHousing;
	}
	
	
	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public void getUserInfo(TaskHousing taskHousing,String html) {
		tracer.addTag("HousingShanghaiInfoService.getUserInfo", taskHousing.getTaskid());

		try {

			HousingShanghaiUserInfo housingShanghaiUserInfo = housingShanghaiParse.htmlUserInfoParser(taskHousing,html);

			if (null != housingShanghaiUserInfo) {

				housingShanghaiUserInfoRepository.save(housingShanghaiUserInfo);
				tracer.addTag("HousingShanghaiInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingChengduFutureService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingShanghaiHtml housingShanghaiHtml = new HousingShanghaiHtml(taskHousing.getTaskid(),
					"housing_shanghai_userinfo", "1", "https://persons.shgjj.com/MainServlet", html);
			housingShanghaiHtmlRepository.save(housingShanghaiHtml);

			tracer.addTag("HousingShanghaiInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingShanghaiInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
	
	/**
	 * 缴费信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public TaskHousing getPay(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		
		tracer.addTag("HousingChengduPayService.getPay", taskHousing.getTaskid());
		try {
			WebParam<HousingShanghaiPay> webParam = housingShanghaiParse.getPay(taskHousing);

			if (null != webParam) {

				housingShanghaiPayRepository.saveAll(webParam.getList());
				
				tracer.addTag("HousingChengduPayService.getPay---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());
				
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());

				HousingShanghaiHtml housingShanghaiHtml = new HousingShanghaiHtml(taskHousing.getTaskid(),
						"housing_shanghai_pay", "1", webParam.getUrl(), webParam.getHtml());
				housingShanghaiHtmlRepository.save(housingShanghaiHtml);

				tracer.addTag("HousingChengduPayService.getPay---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingChengduPayService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingChengduPayService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
		}
		updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}
	
	
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		getUserInfo(taskHousing, userInfoUrl);
		getPay(messageLoginForHousing);
		return null;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			taskHousing = loginTwo(messageLoginForHousing, taskHousing,1);
		} catch (Exception e) {
			tracer.addTag("HousingShanghaiFutureService.login:" , messageLoginForHousing.getTask_id()+"---ERROR:"+e.toString());
			e.printStackTrace();
		}
		return taskHousing;
	}
	

	

}