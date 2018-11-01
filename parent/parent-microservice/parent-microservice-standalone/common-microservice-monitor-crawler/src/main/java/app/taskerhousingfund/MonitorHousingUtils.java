package app.taskerhousingfund;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.TaskHousingfund;

import app.client.fund.HousingfundTaskClient;

@Component
public class MonitorHousingUtils {
	@Autowired
	private HousingfundTaskClient taskClient;
	//设置任一流程中，在Phase_status为DOING的情况下，最多能运行在这个状态下多久，超过这个时间，就退出while(true)循环
	public static long overTime = 2 * 60 * 1000;//任务状态运行时间  设置为2分钟，因为有时候图片验证码识别失败，会重试（用于登陆）
	
	
	//如下方法用于实时判断task的执行状态
	public String getResultDescription(String taskid,long beginTime) throws Exception {
		String description = "";
		long nowTime;
		TaskHousingfund taskHousing=null;
		while(true){
			Thread.sleep(5000);  //五秒执行一次
			taskHousing = taskClient.taskStatus(taskid);
			//如下方式拼接，在进行程序判断的时候可以用当前步骤的结果状态进行判断，也可以用描述判断
			description = taskHousing.getPhase_status().trim()+taskHousing.getDescription().trim();
			//如下内容从枚举类找出（每个步骤最后的状态）
			if(taskHousing.getPhase_status().trim().equals("SUCCESS")
					||taskHousing.getPhase_status().trim().equals("FAIL") 
					|| taskHousing.getPhase_status().trim().equals("ERROR")
					|| taskHousing.getPhase_status().trim().equals("INVALID")
					|| taskHousing.getPhase_status().trim().equals("NEED")){
				break;
			}
			if(taskHousing.getPhase_status().equals("DOING") || taskHousing.getPhase_status().equals("DONING")){
				nowTime = System.currentTimeMillis();
				if((nowTime - beginTime) > overTime){
					break;
				}
			}
		}
		return description;
	}
}
