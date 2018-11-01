package app.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingHanzhongParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.hanzhong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.hanzhong")
public class HousingHanzhongFutureService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingHanzhongParse housingHanzhongParse;

	@Autowired
	private HousingHanzhongInfoService housingHanzhongInfoService;
	
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskHousing loginTwo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, Integer count)
			throws Exception {

		tracer.addTag("HousingHanzhongFutureService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		try {
			if (null != taskHousing) {
				WebParam webParam = housingHanzhongParse.login(messageLoginForHousing, taskHousing);

				if (null == webParam) {
					tracer.addTag("HousingHanzhongFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
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
					tracer.addTag("HousingHanzhongFutureService.login--次数:" + count,
							messageLoginForHousing.getTask_id() + html);
					if (!html.contains("个人网厅登录页")) {
						taskHousing.setCookies(webParam.getCookies());
						taskHousing.setLoginMessageJson(jsonObject.toString());
						
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
						taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
						
						save(taskHousing);
						
						return taskHousing;
					}else {
						Document doc = Jsoup.parse(html);
						String username_tip = doc.getElementById("username_tip").text();
						tracer.addTag("HousingHanzhongFutureService.username_tip:" + count,
								messageLoginForHousing.getTask_id() + "---"+username_tip);
						
						String pwd_tip = doc.getElementById("pwd_tip").text();
						tracer.addTag("HousingHanzhongFutureService.pwd_tip:" + count,
								messageLoginForHousing.getTask_id() + "---"+pwd_tip);

						String yzm_tip = doc.getElementById("yzm_tip").text();
						tracer.addTag("HousingHanzhongFutureService.yzm_tip:" + count,
								messageLoginForHousing.getTask_id() + "---"+yzm_tip);
						if (username_tip.contains("用户名无效")||username_tip.contains("用户名格式错误")) {

							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription(username_tip);
							taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
							taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
							taskHousing.setLoginMessageJson(jsonObject.toString());
							save(taskHousing);
							return taskHousing;
						}else if (pwd_tip.contains("个人密码错误")||pwd_tip.contains("密码格式不正确")) {

							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription(pwd_tip);
							taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
							taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
							
							taskHousing.setLoginMessageJson(jsonObject.toString());
							save(taskHousing);
							return taskHousing;
						}else if (yzm_tip.contains("验证码错误")||yzm_tip.contains("验证码格式不正确")) {
							tracer.addTag("HousingHanzhongFutureService.login--失败次数:" + count,
									messageLoginForHousing.getTask_id());
							if (count < 4) {
								loginTwo(messageLoginForHousing, taskHousing, ++count);
							} else {
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
								taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
								taskHousing.setError_message(yzm_tip);
								
								taskHousing.setLoginMessageJson(jsonObject.toString());
								// 登录失败状态存储
								save(taskHousing);
								return taskHousing;
							}
						}else {
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
							taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
							taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
							taskHousing.setError_message("username_tip-"+username_tip+",pwd_tip-"+pwd_tip+",yzm_tip-"+yzm_tip);
							
							taskHousing.setLoginMessageJson(jsonObject.toString());
							// 登录失败状态存储
							save(taskHousing);
							return taskHousing;
						}
					}
				}
			}
		} catch (Exception e) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
			taskHousing.setLoginMessageJson(jsonObject.toString());
			// 登录失败状态存储
			save(taskHousing);
			return taskHousing;
		}

		return null;
	}

	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		housingHanzhongInfoService.getUserInfo(taskHousing);
//		housingHanzhongInfoService.getPay(taskHousing);

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
			loginTwo(messageLoginForHousing, taskHousing,1);
		} catch (Exception e) {
			tracer.addTag("HousingHanzhongFutureService.login:" , messageLoginForHousing.getTask_id()+"---ERROR:"+e.toString());
			e.printStackTrace();
		}
		return taskHousing;
	}
	
	

}