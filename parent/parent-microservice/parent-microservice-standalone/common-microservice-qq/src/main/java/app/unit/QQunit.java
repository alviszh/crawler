package app.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.qq.json.QQStatusCode;
import com.microservice.dao.entity.crawler.qq.TaskQQ;
import com.microservice.dao.repository.crawler.qq.TaskQQRepository;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.qq"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.qq"})
public class QQunit {

	@Autowired
	private TaskQQRepository taskQQRepository;
	public TaskQQ updateTaskQQ(String taskid){
		TaskQQ taskQQ = taskQQRepository.findByTaskid(taskid);
		
		if(taskQQ.getQqMessageStatus()!=null
				&&taskQQ.getQqFriendStatus()!=null&&taskQQ.getQqQunStatus()!=null){
			taskQQ.setDescription(QQStatusCode.QQ_CRAWLER_SUCCESS.getDescription());
			taskQQ.setPhase(QQStatusCode.QQ_CRAWLER_SUCCESS.getPhase());
			taskQQ.setPhasestatus(QQStatusCode.QQ_CRAWLER_SUCCESS.getPhasestatus());
			taskQQRepository.save(taskQQ);
		}else{ 
			System.out.println("此时还有数据没有爬取完成，故暂未更新最终爬取状态"); 
		}
		return taskQQ;
	}
	
	public void updateMessageStatus(String taskid,Integer state,String Description){
		taskQQRepository.updateMessageStatus(taskid, state,Description);
	}
	public void updateFriendStatus(String taskid,Integer state,String Description){
		taskQQRepository.updateFriendStatus(taskid, state,Description);
	}
	public void updateQunMsgStatus(String taskid,Integer state,String Description){
		taskQQRepository.updateQunMsgStatus(taskid, state,Description);
	}
}
