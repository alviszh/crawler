/**
 * 
 */
package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.crawler.monitor.json.tasker.MonitorStandaloneTaskerBean;
/**
 * @author sln 
 * @Description: 人行征信不涉及到etl,故用此接口来获取近24小时的爬取任务
 */
@FeignClient("STANDALONE-TASK")
//@FeignClient(name = "h5-proxy", url = "${task-pbccrc-proxy}")
public interface PbccrcTaskResultClient {
	//获取近24小时所有人行征信爬取任务
	@PostMapping("/standalone/onedaypbccrc")
	List<MonitorStandaloneTaskerBean> oneDayPbccrc();
	//从数据库中查询出最新一条执行成功的数据，作为定时任务监控素材——避免了登陆信息过期问题
	@GetMapping("/standalone/getonepbccrc")
	String getOneSuccessPbccrcRecord();
}
