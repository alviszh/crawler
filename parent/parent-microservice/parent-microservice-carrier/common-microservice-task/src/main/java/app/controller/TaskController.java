package app.controller;

import com.crawler.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.crawler.mobile.json.MobileJsonBean;
import com.crawler.mobile.json.ResultData;
import com.microservice.dao.entity.crawler.mobile.DirMobileSegment;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.bean.MessageCode;
import app.commontracerlog.TracerLog;
import app.service.TaskService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carrier")  
public class TaskController {

	public static final Logger log = LoggerFactory.getLogger(TaskController.class);
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TracerLog tracer;
 
	
	@GetMapping(path = "/tasks/{taskid}/status")
	public TaskMobile taskStatus(@PathVariable String taskid) { 
		TaskMobile taskMobile = taskService.getTaskMobile(taskid);
		//tracer.addTag("Request task status", "taskid:"+taskid); 
		return taskMobile;
	}
	
	@PostMapping(path = "/tasks")
	public TaskMobile createTask(@RequestBody MobileJsonBean mobileJsonBean) { 
		tracer.addTag("Request createTask", "mobileJsonBean-----"+mobileJsonBean.toString()); 
		log.info("mobileJsonBean-----"+mobileJsonBean.toString());
		TaskMobile tm = taskService.createTask(mobileJsonBean);
		return tm;
	}
	
	@GetMapping(path = "/mobilesegment/{phonenum}")
	public ResultData<DirMobileSegment> findMobileSegment(@PathVariable String phonenum) { 
		ResultData<DirMobileSegment> data = new ResultData<DirMobileSegment>(); 
		tracer.addTag("Request findMobileSegment", "phonenum:"+phonenum); 
		DirMobileSegment dirMobileSegment = taskService.findMobileSegment(phonenum);
		if(dirMobileSegment!=null){
			data.setData(dirMobileSegment);
			tracer.addTag("findMobileSegment Return", dirMobileSegment.toString());
			data.setMesCode(MessageCode.SUCCESS);
			data.setMessage("查询完成");  
		}else{
			data.setMesCode(MessageCode.NO_DATA_FOUND);
			data.setMessage("该手机号不属于三大运营商");
		}
		return data;
	}

	@RequestMapping(value = "/tasks/getPages", method= RequestMethod.POST)
	public @ResponseBody
	PageInfo<TaskMobile> getTaskMobilePages(@RequestParam(value = "currentPage") int currentPage,
											@RequestParam(value = "pageSize") int pageSize,
											@RequestParam(value = "taskid", required = false) String taskid,
											@RequestParam(value = "mobilenum", required = false) String mobilenum) {

		/*Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable pageable = new PageRequest(currentPage, pageSize, sort);
		Page<TaskMobile> allTaskPage = taskService.getAllTaskPage(pageable);*/

		//根据条件查询
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("taskid",taskid);
		paramMap.put("mobilenum",mobilenum);

		Page<TaskMobile> tasksPage = taskService.getMobileTaskByParams(paramMap, currentPage, pageSize);
		System.out.println("******getTaskPages:"+tasksPage);

		PageInfo<TaskMobile> pageInfo = new PageInfo<TaskMobile>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}

	/**
	 * 根据创建时间统计运营商的调用量（线性图表）
	 * @return
	 */
	@RequestMapping(value = "/tasks/lineData" , method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	List lineData(){
		List result = taskService.getMobileTaskStatistics();
		return result;
	}

	/**
	 * 统计每个运营商的调用量
	 * @return
	 */
	@RequestMapping(value = "/tasks/pieData" , method = {RequestMethod.POST, RequestMethod.GET})
	public @ResponseBody
	List pieData(){
		List result = taskService.getGroupByCarrier();
		return result;
	}
}
