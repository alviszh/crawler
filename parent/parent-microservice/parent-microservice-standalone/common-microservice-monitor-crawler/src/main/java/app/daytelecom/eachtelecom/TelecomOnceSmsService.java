package app.daytelecom.eachtelecom;

import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.sms.SmsRecv;

import app.taskercarrier.MonitorTelecomBasicService;


/**
 * @author sln
 * 
 * 需要一次短信的发送和验证的网站，部分省份数据爬取也需要短信验证码，此处不论是否需要，都将短信内容作为参数进行传递
 * 
 * 需要注意的是，部分网站的短信验证码是四位数的，例如：重庆、黑龙江
 * 
 */
@Component
public class TelecomOnceSmsService extends MonitorTelecomBasicService{
	public void telecomTimedTask(MessageLogin messageLogin,String province, String oncesmskey) throws Exception{
		String taskid=messageLogin.getTask_id().trim();
		String username=messageLogin.getUsername().trim();
		String idNum=messageLogin.getIdNum().trim();
		String phonenum = messageLogin.getName().trim();
		String password=messageLogin.getPassword().trim();
		Integer user_id=messageLogin.getUser_id();
		//任务调用之前，先处理该手机号对应的、影响本次任务的所有无关短信，但是有时候登陆成功也会有短信提示故决定在登陆成功，发送验证码之前处理无关短信
		smsClient.updateSmsIneffective(phonenum);
		SmsRecv smsReceiver = null;
		String description="";
		Integer smsId=0;  //初始化(将使用过的短信验证码进行状态更新)
		String phaseName;
		long beginTime = System.currentTimeMillis();//开始时间
		try {
			carrierClient.sendCode(taskid, username, idNum, phonenum, password, user_id, province);
		} catch (Exception e) {
			tracer.addTag("调用~"+province+"~电信网站获取数据爬取所需短信验证码接口过程中出现异常：",e.toString());
		}
		phaseName="获取短信验证码";
		try {
			description=executeTelecomHelper.getResultDescription(taskid,beginTime,province,phaseName);
		} catch (Exception e) {
			tracer.addTag("获取~"+province+"~电信网站数据爬取所需短信验证码过程中出现异常：",e.toString());
		}
    	if(description.contains("SUCCESS")){  //验证码发送成功
    		beginTime = System.currentTimeMillis();
    		try {
    			smsReceiver=executeTelecomHelper.getAppStoreSmsResult(phonenum,oncesmskey,beginTime,province);
			} catch (Exception e) {
				tracer.addTag("利用猫池读取~"+province+"~电信网站数据爬取所需短信验证码过程中出现异常：", e.toString());
			}
    		if(null!=smsReceiver && smsReceiver.toString().contains(oncesmskey)){  
    			String sms = smsReceiver.getSmscontent().trim();
    			tracer.addTag(province+"~电信网站数据爬取所需短信验证码内容是: ", sms);
    			if(province.contains("重庆") || province.contains("黑龙江")){
    				sms = executeTelecomHelper.getFourSms(sms).trim();
    			}else if(province.contains("海南")){  //返回的短信中带有两个连续数字的串:欢迎您使用中国电信网上营业厅查询话费清单。短信随机码编号：QD[1002611090]，短信随机码是：334221
    				//截取最后6位
    				sms=sms.substring(sms.length()-6, sms.length());
    			}else{
    				sms = executeTelecomHelper.getSixSms(sms).trim();
    			}
    			smsId = smsReceiver.getId();
    			beginTime = System.currentTimeMillis();
    			try {
    				carrierClient.verifiCode(taskid, username, idNum, phonenum, password, user_id, sms, province);
				} catch (Exception e) {
					tracer.addTag("调用~"+province+"~电信网站验证数据爬取所需短信验证码接口过程中出现异常：", e.toString());
				}
    			phaseName="验证短信验证码";
				try {
					description=executeTelecomHelper.getResultDescription(taskid,beginTime,province,phaseName);
				} catch (Exception e) {
					tracer.addTag("验证~"+province+"~电信网站数据爬取所需短信验证码过程中出现异常：", e.toString());
				}
    			if(description.contains("SUCCESS")){ //短信验证成功
    				smsClient.updateSmsEffective(taskid, smsId);
    				tracer.addTag("验证~"+province+"~电信网站数据爬取所需短信验证码成功","接下来调用数据爬取接口");
    				try {
    					if(province.contains("贵州")){
    						Thread.sleep(3000);   
    					}
    					carrierClient.crawlerNeedSmsCode(taskid, username, idNum, phonenum, password, user_id, province,sms);
					} catch (Exception e) {
						tracer.addTag("调用~"+ province +"~电信网站数据爬取接口过程中出现异常，可能不会影响最后的爬取流程",e.toString());
						Thread.sleep(1000); 
					}
    			}else{
    				smsClient.updateSmsEffective(taskid, smsId);
    				tracer.addTag("验证~"+province+"~电信网站数据爬取所需短信验证码失败：", description);
    			}
    		}else{
    			tracer.addTag("猫池并未成功读取并存储~"+province+"~电信网站数据爬取所需短信验证码","该网站本次任务taskid是："+taskid);
			}
    	}else{
    		tracer.addTag("获取~"+province+"~电信网站数据爬取所需短信验证码失败，失败原因：", description);
    	}
	}
}
