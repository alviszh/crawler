package app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;

import app.bean.MonitorStandaloneTaskerBean;
/**
 * @author sln
 * @date 2018年9月20日下午3:26:20
 * @Description:  根据服务名，查询近24小时所有执行过的任务，发送统计邮件
 */
@RestController
@RequestMapping("/standalone")
public class StandaloneTaskMailController {
	@Autowired
	private TaskStandaloneRepository taskStandaloneRepository;   //查询人行征信的爬取任务执行记录
	//获取24小时之内的运行结果(用list方式返回)
	@RequestMapping(path = "/onedaypbccrc",method = {RequestMethod.GET,RequestMethod.POST})
	public List<MonitorStandaloneTaskerBean> oneDayPbccrc(){
		List<MonitorStandaloneTaskerBean> list=new ArrayList<MonitorStandaloneTaskerBean>();
		MonitorStandaloneTaskerBean pbccrcBean=null;
		List<TaskStandalone> oneDayPbccrcList = taskStandaloneRepository.findAllPbccrcTaskForOneDay();
		String key;
		for (TaskStandalone taskStandalone : oneDayPbccrcList) {
			try {
				key=taskStandalone.getKey().trim();
			} catch (Exception e) {
				key="未提供";
			}
			pbccrcBean=new MonitorStandaloneTaskerBean(taskStandalone.getServiceName().trim(),
					getTimeAddEightHours(taskStandalone.getCreatetime().toString()),
					taskStandalone.getOwner().trim(), taskStandalone.getFinished(),
					taskStandalone.getDescription().trim(),
					key, taskStandalone.getTaskid().trim());
			list.add(pbccrcBean);
		}
		return list;
	}
	//获取最新执行成功的人行征信的json串
	@RequestMapping(path = "/getonepbccrc",method = {RequestMethod.GET,RequestMethod.POST})
	public String getOnePbccrcRecord(){
		String topSuccessPbccrcRecord = taskStandaloneRepository.findTopSuccessPbccrcRecord();
		return topSuccessPbccrcRecord;
	}
	//时间转换，增加8小时
	public String getTimeAddEightHours(String time){
		Date d = null;
		String newtime = null;
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d = sd.parse(time);
			long rightTime = (long) (d.getTime() + 8 * 60 * 60 * 1000);
			newtime = sd.format(rightTime);
		} catch (ParseException e) {
			System.out.println("时间转换是报异常："+e.toString());
			newtime=time;  //如果转换出现异常，就把传入的参数作为结果
		}
		return newtime;
	}
}
