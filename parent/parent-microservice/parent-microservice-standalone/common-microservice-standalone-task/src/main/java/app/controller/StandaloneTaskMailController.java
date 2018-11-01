package app.controller;

import java.util.ArrayList;
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
					taskStandalone.getCreatetime().toString(),
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
}
