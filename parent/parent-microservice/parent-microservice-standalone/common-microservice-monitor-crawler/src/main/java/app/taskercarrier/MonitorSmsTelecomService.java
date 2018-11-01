package app.taskercarrier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;

import app.daytelecom.eachtelecom.TelecomOnceSmsService;
import app.daytelecom.eachtelecom.TelecomTwiceSmsService;
import app.exceptiondetail.ExUtils;


/**
 * @description: 将所有需要短信验证码的电信省份定时执行，保证每天可为etl的监测提供数据
 * @author: sln 
 * 
 */
@Component
public class MonitorSmsTelecomService extends MonitorTelecomBasicService{
	@Autowired
	private TelecomOnceSmsService telecomOnceSmsService;
	@Autowired
	private TelecomTwiceSmsService telecomTwiceSmsService;
	
	public void telecomTimedTask(MessageLogin messageLogin,String province, String oncesmskey,String twicesmskey){
		String taskid=messageLogin.getTask_id().trim();
		String username=messageLogin.getUsername().trim();
		String idNum=messageLogin.getIdNum().trim();
		String phonenum = messageLogin.getName().trim();
		String password=messageLogin.getPassword().trim();
		Integer user_id=messageLogin.getUser_id();
		//任务调用之前，先处理该手机号对应的、影响本次任务的所有无关短信，但是有时候登陆成功也会有短信提示故决定在登陆成功，发送验证码之前处理无关短信
		String description = "";
		String phaseName;
		try {
			if(province.contains("浙江")){  //浙江电信在发送短信验证码之前有个页面加载的步骤
				long beginTime = System.currentTimeMillis();//开始时间
				try {
					carrierClient.intermediate(taskid, username, idNum, phonenum, password, user_id, province);
				} catch (Exception e) {
					tracer.addTag("调用~"+ province +"~电信网站发送短信前加载页面接口出现异常：",e.toString());
				}
				phaseName="发送短信前期，加载页面";
				try {
					description=executeTelecomHelper.getResultDescription(taskid,beginTime,province,phaseName);
				} catch (Exception e) {
					tracer.addTag("获取~"+province+"~电信网站数据爬取所需短信验证码过程中出现异常：",e.toString());
				}
				if(description.contains("SUCCESS")){ 
					tracer.addTag(province+"~电信前期页面加载成功", "接下来调用短信验证码发送接口");
					telecomOnceSmsService.telecomTimedTask(messageLogin, province, oncesmskey);
				}else{
					tracer.addTag(province+"~电信前期页面加载失败，失败原因：", description);
				}
			}else if(province.contains("海南")){   //需要二次登录，还需要一次短信验证码的发送和验证
				long beginTime = System.currentTimeMillis();//开始时间
				try {
					carrierClient.loginTwo(taskid, username, idNum, phonenum, password, user_id, province);
				} catch (Exception e) {
					tracer.addTag("调用~"+ province +"~电信网站二次登陆接口过程中出现异常：",e.toString());
				}
				phaseName="二次登录";
				try {
					description=executeTelecomHelper.getResultDescription(taskid,beginTime,province,phaseName);
				} catch (Exception e) {
					tracer.addTag("获取~"+province+"~电信网站数据爬取所需短信验证码过程中出现异常：",e.toString());
				}
				if(description.contains("SUCCESS")){ 
					tracer.addTag(province+"~电信二次登陆成功", "接下来调用短信验证码发送接口");
					telecomOnceSmsService.telecomTimedTask(messageLogin, province, oncesmskey);
				}else{
					tracer.addTag(province+"~电信二次登陆失败，失败原因：", description);
				}
			}else if(province.contains("广西") || province.contains("江西")){   //需要二次短信
				telecomTwiceSmsService.telecomTimedTask(messageLogin, province, oncesmskey, twicesmskey);
			}else{   //只需要一次短信
				telecomOnceSmsService.telecomTimedTask(messageLogin, province, oncesmskey);
			}
		} catch (Exception e) {
			tracer.addTag(province+"~电信执行本次爬取任务时出现异常，异常内容是：",ExUtils.getEDetail(e));
		}
	}
}
