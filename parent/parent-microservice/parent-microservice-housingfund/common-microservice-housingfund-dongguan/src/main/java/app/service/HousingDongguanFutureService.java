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
import com.microservice.dao.entity.crawler.housing.dongguan.HousingDongguanHtml;
import com.microservice.dao.entity.crawler.housing.dongguan.HousingDongguanPay;
import com.microservice.dao.entity.crawler.housing.dongguan.HousingDongguanUserInfo;
import com.microservice.dao.repository.crawler.housing.dongguan.HousingDongguanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.dongguan.HousingDongguanPayRepository;
import com.microservice.dao.repository.crawler.housing.dongguan.HousingDongguanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingDongguanParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.dongguan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.dongguan")
public class HousingDongguanFutureService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingDongguanParse housingDongguanParse;

	@Autowired
	private HousingDongguanUserInfoRepository housingDongguanUserInfoRepository;
	
	@Autowired
	private HousingDongguanHtmlRepository housingDongguanHtmlRepository;
	
	@Autowired
	private HousingDongguanPayRepository housingDongguanPayRepository;
	
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

		tracer.addTag("HousingDongguanFutureService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		if (null != taskHousing) {
			WebParam webParam = housingDongguanParse.login(messageLoginForHousing, taskHousing);

			if (null == webParam) {
				tracer.addTag("HousingDongguanFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
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
				String html = webParam.getHtml();
				String text = webParam.getText();
				tracer.addTag("HousingDongguanFutureService.login--次数:" + count,
						messageLoginForHousing.getTask_id() + html);
				if (html.contains("公积金账户基本信息")) {
					taskHousing.setCookies(webParam.getCookies());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
					
					save(taskHousing);
					
					userInfoUrl = webParam.getUrl();
					
					return taskHousing;
				}else if (text.contains("不存在账户信息")) {

					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(text);
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					
					taskHousing.setLoginMessageJson(jsonObject.toString());
					save(taskHousing);
					return taskHousing;
				} else if (text.contains("验证码错误")) {
					tracer.addTag("HousingDongguanFutureService.login--失败次数:" + count,
							messageLoginForHousing.getTask_id());
					if (count < 4) {
						loginTwo(messageLoginForHousing, taskHousing, ++count);
					} else {
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
						taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
						taskHousing.setError_message(text);
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

		return null;
	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		getUserInfo(taskHousing,userInfoUrl);
		getPay(messageLoginForHousing,taskHousing);
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
			tracer.addTag("HousingDongguanFutureService.login:" , messageLoginForHousing.getTask_id()+"---ERROR:"+e.toString());
			e.printStackTrace();
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
	@Async
	public void getUserInfo(TaskHousing taskHousing,String html) {
		tracer.addTag("HousingDongguanInfoService.getUserInfo", taskHousing.getTaskid());
		try {
			
			HousingDongguanUserInfo housingDongguanUserInfo = housingDongguanParse.htmlUserInfoParser(taskHousing,html);

			if (null != housingDongguanUserInfo) {

				housingDongguanUserInfoRepository.save(housingDongguanUserInfo);
				tracer.addTag("HousingDongguanInfoService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingDongguanInfoService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
			HousingDongguanHtml housingDongguanHtml = new HousingDongguanHtml(taskHousing.getTaskid(),
					"housing_dongguan_userinfo", "1", "http://wsbs.dggjj.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnInfoQryOnline1.jsp", html);
			housingDongguanHtmlRepository.save(housingDongguanHtml);

			tracer.addTag("HousingDongguanInfoService.getUserInfo---用户信息源码", "用户信息源码表入库!" + taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingDongguanInfoService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

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
	@Async
	public void getPay(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) {
		
		tracer.addTag("HousingDongguanInfoService.getPay", taskHousing.getTaskid());
		try {
			WebParam<HousingDongguanPay> webParam = housingDongguanParse.getPay(messageLoginForHousing,taskHousing);

			if (null != webParam) {

				housingDongguanPayRepository.saveAll(webParam.getList());
				
				tracer.addTag("HousingDongguanInfoService.getPay---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());
				
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				HousingDongguanHtml housingDongguanHtml = new HousingDongguanHtml(taskHousing.getTaskid(),
						"housing_dongguan_pay", "1", webParam.getUrl(), webParam.getHtml());
				housingDongguanHtmlRepository.save(housingDongguanHtml);

				tracer.addTag("HousingDongguanInfoService.getPay---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());

			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingDongguanInfoService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingDongguanInfoService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
	

}