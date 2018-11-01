package app.taskercarrier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.TaskMobile;
import com.microservice.dao.entity.crawler.sms.SmsRecv;

import app.client.SmsClient;
import app.client.carrier.TaskClient;
import app.commontracerlog.TracerLog;

@Component
public class MonitorTelecomUtils {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskClient taskClient;
	@Autowired
	private SmsClient smsClient;
	
	//设置任一流程中，在Phase_status为DOING的情况下，最多能运行在这个状态下多久，超过这个时间，就退出while(true)循环
	public static long overTime = 3 * 60 * 1000;//任务状态运行时间  设置为3分钟，因为有时候图片验证码识别失败，会重试,湖北电信登录时间很久
	public static long appSmsTime =3 * 60 * 1000;//猫池读取短信并且存储到数据库中，预留时间  设置为3分钟，因为有的短信已经提示发送成功，但是会迟些收到
	//福建电信由于爬取数据慢的原因，将数据在各个步骤中分散爬取，所以，发送短信的时候还有验证短信的时候都会爬取一部分数据
	//故将福建电信状态扫描的时长延长，用如下设置的超时时长
	public static long fuJianOverTime = 10 * 60 * 1000;
	public static long guangxiOverTime = 8 * 60 * 1000;   //广西电信，第二个短信验证码有时会延迟收到
	
	
	//如下方法用于实时判断task的执行状态(phaseName——步骤名称)
	public String getResultDescription(String taskid,long beginTime,String province,String phaseName) throws Exception {
		String description = "";
		long nowTime;
		TaskMobile taskMobile=null;
		while(true){
			Thread.sleep(5000);  //五秒执行一次
			taskMobile = taskClient.post(taskid);
			//如下方式拼接，在进行程序判断的时候可以用当前步骤的结果状态进行判断，也可以用描述判断
			description = taskMobile.getPhase_status().trim()+taskMobile.getDescription().trim();
			//如下内容从枚举类找出（每个步骤最后的状态）
			if(taskMobile.getPhase_status().trim().equals("SUCCESS")
					||taskMobile.getPhase_status().trim().equals("FAIL") 
					|| taskMobile.getPhase_status().trim().equals("ERROR")
					|| taskMobile.getPhase_status().trim().equals("INVALID")
					|| taskMobile.getPhase_status().trim().equals("NEED")){
				nowTime = System.currentTimeMillis();
				long consumeTime=nowTime-beginTime;  //耗时
				tracer.addTag(province+"~电信~"+phaseName+"~消耗时长为： ", consumeTime+"ms");
				break;
			}
			if(taskMobile.getPhase_status().equals("DOING") || taskMobile.getPhase_status().equals("DONING")){
				nowTime = System.currentTimeMillis();
				if(province.contains("福建")){
					if((nowTime - beginTime) > fuJianOverTime){
						break;
					}
				}else{
					if((nowTime - beginTime) > overTime){
						break;
					}
				}
			}
		}
		return description;
	}
	//如下方法用于获取猫池是否在指定时间内将短信进行读取和存储(超过指定时间认为没有读取到)
	public SmsRecv getAppStoreSmsResult(String phonenum,String smskey,long beginTime,String province) throws Exception{
		long nowTime;
		SmsRecv smsrReceiver = null;
		while(true){
			Thread.sleep(5000);  //5秒执行一次
			//通过关键字
			smsrReceiver = smsClient.getsms(phonenum, smskey);
			//期初如下判断条件只是判断null!=smsrReceiver，发现会返回空对象
			if(null!=smsrReceiver && smsrReceiver.toString().contains(smskey)){  //猫池已经成功读取到了数据，并且成功存储到了数据库
				nowTime = System.currentTimeMillis();
				long consumeTime=nowTime-beginTime;  //耗时
				tracer.addTag(province+"~电信从短信验证码提示发送成功到猫池读取入库， 消耗时长为： ", consumeTime+"ms");
				break;
			}			
			nowTime = System.currentTimeMillis();
			if(province.contains("福建")){
				if((nowTime - beginTime) > fuJianOverTime){
					break;
				}
			}else if(province.contains("广西")){
				if((nowTime - beginTime) > guangxiOverTime){
					break;
				}
			}else{
				if((nowTime - beginTime) > appSmsTime){
					break;
				}
			}
		}	
		return smsrReceiver;
	}
	//从原始短信内容中提取6位连续数字的短信验证码
	public String getSixSms(String initSms){
		Pattern p = Pattern.compile("\\d{6}");
	    Matcher m = p.matcher(initSms);
	    return m.find() ? m.group():null;
	}
	//从原始短信内容中提取4位连续数字的短信验证码——例如重庆电信改版后
	public String getFourSms(String initSms){
		Pattern p = Pattern.compile("\\d{4}");
		Matcher m = p.matcher(initSms);
		return m.find() ? m.group():null;
	}
}
