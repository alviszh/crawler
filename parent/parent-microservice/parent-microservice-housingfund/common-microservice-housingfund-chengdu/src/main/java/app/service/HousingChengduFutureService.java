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
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chengdu.HousingChengduHtml;
import com.microservice.dao.repository.crawler.housing.chengdu.HousingChengduHtmlRepository;
import com.microservice.dao.repository.crawler.housing.chengdu.HousingChengduUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingChengduParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chengdu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chengdu")
public class HousingChengduFutureService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingChengduParse housingChengduParse;

	@Autowired
	private HousingChengduUserInfoRepository housingChengduUserInfoRepository;

	@Autowired
	private HousingChengduHtmlRepository housingChengduHtmlRepository;

	@Autowired
	private HousingChengduPayService housingChengduPayService;

	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskHousing logiTwo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, Integer count)
			throws Exception {

		tracer.addTag("HousingChengduFutureService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		if (null != taskHousing) {
			WebParam webParam = housingChengduParse.login(messageLoginForHousing);

			if (null == webParam) {
				tracer.addTag("HousingChengduFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());

				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setLoginMessageJson(jsonObject.toString());

				// 登录失败状态存储
				save(taskHousing);

				return taskHousing;
			} else {
				String html = webParam.getHtml();
				tracer.addTag("HousingChengduFutureService.login--次数:" + count,
						messageLoginForHousing.getTask_id() + html);
				if (html == null || "".equals(html)) {

					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
					taskHousing.setCookies(webParam.getCookies());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					save(taskHousing);
					return taskHousing;
				} else if (html.contains("个人客户信息不存在") || html.contains("账号或密码错误")
						|| html.contains("联名卡信息不存在，请使用其他方式登录")) {

					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(html);
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					save(taskHousing);
					return taskHousing;
				} else if (html.contains("滑动验证失败")) {
					tracer.addTag("HousingChengduFutureService.login--失败次数:" + count,
							messageLoginForHousing.getTask_id());
					if (count < 4) {
						logiTwo(messageLoginForHousing, taskHousing, ++count);
					} else {
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());

						taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
						taskHousing.setError_message(html);
						taskHousing.setLoginMessageJson(jsonObject.toString());
						// 登录失败状态存储
						save(taskHousing);
						return taskHousing;
					}
				} else {
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
					taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
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
	public void getUserInfo(TaskHousing taskHousing) {
		tracer.addTag("HousingChengduFutureService.getUserInfo", taskHousing.getTaskid());

		try {

			WebParam webParam = housingChengduParse.getUserInfo(taskHousing);

			if (null != webParam) {

				housingChengduUserInfoRepository.save(webParam.getHousingChengduUserInfo());
				tracer.addTag("HousingChengduFutureService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());

				HousingChengduHtml housingChengduHtml = new HousingChengduHtml(taskHousing.getTaskid(),
						"housing_chengdu_userinfo", "1", webParam.getUrl(), webParam.getHtml());
				housingChengduHtmlRepository.save(housingChengduHtml);

				tracer.addTag("HousingChengduFutureService.getUserInfo---用户信息源码",
						"用户信息源码表入库!" + taskHousing.getTaskid());

			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingChengduFutureService.getUserInfo.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingChengduFutureService.getUserInfo---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

	/**
	 * 缴费信息
	 * 
	 * @param taskMobile
	 * @return
	 */
	public void getPay(TaskHousing taskHousing) {
		tracer.addTag("HousingChengduFutureService.getPay", taskHousing.getTaskid());
		List<Future<String>> listfuture = new ArrayList<Future<String>>();
		List<String> statusCodelist = new ArrayList<String>();
		try {
			List<String> paylist = housingChengduParse.getPaylist(taskHousing);
			paylist.forEach(num -> {
				Future<String> future = housingChengduPayService.getPay(taskHousing, num);
				listfuture.add(future);
			});
			try {
				while (true) {
					for (Future<String> future : listfuture) {
						if (future.isDone()) { // 判断是否执行完毕
							tracer.addTag("Result " + future.get(),
									taskHousing.getTaskid() + "-----" + future.isDone());
							statusCodelist.add(future.get());
							listfuture.remove(future);
							break;
						}
					}
					if (listfuture.size() == 0) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("TelecomjiangsuService-listfuture--ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
			}
			int code  = 200;
			for (int i = 0; i < statusCodelist.size(); i++) {
				if ("200".equals(statusCodelist.get(i))) {
					break;
				}else{
					try {
						code = Integer.valueOf(statusCodelist.get(i));
					} catch (Exception e) {
						tracer.addTag("TelecomjiangsuService-code--ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
					}
				}
			}
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), code,
					taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomjiangsuService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());

		}
		updateTaskHousing(taskHousing.getTaskid());
	}

	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		getUserInfo(taskHousing);
		getPay(taskHousing);
		return null;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			taskHousing = logiTwo(messageLoginForHousing, taskHousing,1);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("TelecomjiangsuService.login---ERROR", messageLoginForHousing.getTask_id() + "---ERROR:" + e);
		}
		return taskHousing;
	}

}