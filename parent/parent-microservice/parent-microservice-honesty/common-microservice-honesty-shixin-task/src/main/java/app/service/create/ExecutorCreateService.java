package app.service.create;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.aws.json.HttpProxyBean;
import com.microservice.dao.entity.crawler.honesty.shixin.HonestyTask;
import com.microservice.dao.repository.crawler.honesty.shixin.HonestyTaskRepository;

import app.bean.HonestyJsonBean;
import app.bean.Honestybean;
import app.bean.IsDoneBean;
import app.client.aws.AwsApiClient;
import app.commontracerlog.TracerLog;

/**   
*    
* 项目名称：common-microservice-honesty-shixin-task   
* 类名称：ExecutorCreateService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年8月2日 下午2:46:53   
* @version        
*/
@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.honesty.shixin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.honesty.shixin")
public class ExecutorCreateService {

	
	@Autowired
	private TracerLog tracerLog;
	
	@Autowired
	private HonestyTaskRepository honestyTaskRepository;
	
	@Value("${webdriver.proxy}")
	Integer proxy;
	
	@Autowired
	private AwsApiClient awsApiClient;
	
	/**   
	  *    
	  * 项目名称：common-microservice-honesty-shixin-task  
	  * 所属包名：app.service.create
	  * 类描述：   
	  * 创建人：hyx 
	  * 创建时间：2018年8月2日 
	  * @version 1  
	  * 返回值    IsDoneBean
	  */
	public IsDoneBean createTaskList(HonestyJsonBean honestyJsonBean) {
		String taskid = honestyJsonBean.getTaskid();
		List<Honestybean> keywords = honestyJsonBean.getKeys();

		Integer priority = honestyJsonBean.getPriority(); // 优先级

		if (priority == 0 || priority == null) {
			priority = 20;
		}
		if (null == taskid) {
			tracerLog.output("error", "taskid is null !");
			IsDoneBean isDoneBean = new IsDoneBean();
			isDoneBean.setErrormessage("taskid is null !");
			return isDoneBean;
		} else {
			List<HonestyTask> listTask = honestyTaskRepository.findByTaskid(taskid);

			if (null == listTask || listTask.size() <= 0) {
				int i = 0;
				for (Honestybean keyword : keywords) {
					if(keyword == null){
						tracerLog.output("第" + i + "个", "传递keyword为空值");
						continue;
					}
					
					if(keyword.getpName().length()<=0 &&(keyword.getpCardNum()==null||keyword.getpCardNum().length()<=0)){
						tracerLog.output("第" + i + "个", "传递keyword为空值");
						continue;
					}
					tracerLog.output("第" + i + "个", keyword.toString());
					HonestyTask honestyTask = new HonestyTask();
					honestyTask.setTaskid(taskid);
					honestyTask.setpName(keyword.getpName());
					honestyTask.setpCardNum(keyword.getpCardNum());
					honestyTask.setPhase("0");
					listTask.add(honestyTask);
					i++;
				}

				honestyTaskRepository.saveAll(listTask);

				int totalnum = listTask.size();
				int unfinishednum = totalnum;
				int finishednum = 0;
				int crawleringnum = 0;
				int errornum = 0;

				IsDoneBean isDoneBean = new IsDoneBean();
				isDoneBean.setTaskid(taskid);
				isDoneBean.setTotalnum(totalnum);
				isDoneBean.setUnfinishednum(unfinishednum);
				isDoneBean.setFinishednum(finishednum);
				isDoneBean.setCrawleringnum(crawleringnum);
				isDoneBean.setErrornum(errornum);
				tracerLog.output("用户不存在   :", taskid);
				tracerLog.output("HonestyTask 生成  :", taskid);
				return isDoneBean;
			} else {
				tracerLog.output("用户存在   :", taskid);
				IsDoneBean isDoneBean = new IsDoneBean();
				isDoneBean.setErrormessage("taskid存在   :" + taskid + ",请创建新的taskid");
				return isDoneBean;
			}
		}
	}
	
	public Queue<HonestyTask> getHonestyTask(Queue<HonestyTask> queue) {

		List<HonestyTask> list= honestyTaskRepository.findTopNumByTaskidAndTypeAndPhase("0", 40);

		for (HonestyTask honestyTask : list) {
			honestyTask.setPhase("1");
			honestyTask.setRenum(honestyTask.getRenum() + 1);
			if (proxy==0) {
				HttpProxyBean httpProxyBean = awsApiClient.getProxy();
				System.out.println(httpProxyBean.getIp()+"=========="+httpProxyBean.getPort());
				honestyTask.setIpaddress(httpProxyBean.getIp());
				honestyTask.setIpport(httpProxyBean.getPort());
				honestyTask = honestyTaskRepository.save(honestyTask);
				tracerLog.output("istrueip honestyTask", honestyTask.toString());
			}
			try {
				honestyTask = honestyTaskRepository.save(honestyTask);
				tracerLog.output("queue 取出数据", honestyTask.toString());
			} catch (Exception e) {
				tracerLog.output("存储 数据", e.getMessage());

			}

		}
		
		if(list.size()>0){
			try{
				queue.addAll(list);

			}catch(Exception e){
				tracerLog.output("加入数据queue报错", e.getMessage());

				queue = new LinkedList<HonestyTask>();
				queue.addAll(list);

			}
		}else{
//			tracerLog.output("加入数据queue", "无爬取数据");
		}
		

		return queue;
	}
	
	
}
