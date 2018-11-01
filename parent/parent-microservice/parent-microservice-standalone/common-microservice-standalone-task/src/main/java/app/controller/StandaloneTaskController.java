package app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.PageInfo;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;

import app.commontracerlog.TracerLog;
import app.service.StandaloneTaskService;

@RestController
@RequestMapping("/standalone")
public class StandaloneTaskController {

    @Autowired
    private StandaloneTaskService taskService;
    @Autowired
    private TracerLog tracer;

    @GetMapping(path = "/tasks/{taskid}/status")
    public TaskStandalone taskStatus(@PathVariable String taskid) {
        TaskStandalone taskStandalone = taskService.getTaskStandalone(taskid);
        return taskStandalone;
    }


    @PostMapping(path = "/createTask")
    public TaskStandalone createTask(@RequestBody PbccrcJsonBean pbccrcJsonBean){

        tracer.addTag("standalone task create",pbccrcJsonBean.toString());
        return taskService.createTask(pbccrcJsonBean);

    }

    /**
     * 测试发送报告结果
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/as-adapter-ext/keji/pbccredit/report", method = RequestMethod.POST)
    public @ResponseBody
    String notifications(@RequestBody String creditReport,@RequestParam("key") String key) throws IOException {
        System.out.println("#############################" );
        System.out.println("^^^测试发送报告结果 汇诚^^");
        return "发送成功";
    }
    
    
    @RequestMapping(value = "/tasks/debitcard/findAll", method = RequestMethod.GET)  
	public @ResponseBody PageInfo<TaskStandalone> findAll(
			@org.springframework.web.bind.annotation.RequestParam(value = "currentPage") int currentPage,
			@org.springframework.web.bind.annotation.RequestParam(value = "pageSize") int pageSize,
			@org.springframework.web.bind.annotation.RequestParam(value = "owner") String owner,
			@org.springframework.web.bind.annotation.RequestParam(value = "environmentId") String environmentId,
			@org.springframework.web.bind.annotation.RequestParam(value = "productId") String productId,
			@org.springframework.web.bind.annotation.RequestParam(value = "beginTime") String beginTime,
			@org.springframework.web.bind.annotation.RequestParam(value = "endTime") String endTime,
			@org.springframework.web.bind.annotation.RequestParam(value = "taskid") String taskid,
			@org.springframework.web.bind.annotation.RequestParam(value = "loginName") String loginName,
			@org.springframework.web.bind.annotation.RequestParam(value = "userId") String userId
			) {

		System.out.println("currentPage-----"+currentPage);
		System.out.println("pageSize-----"+pageSize);
		System.out.println("应用-----"+owner);
		System.out.println("环境-----"+environmentId);
		System.out.println("产品-----"+productId);
		System.out.println("开始时间-----"+beginTime);
		System.out.println("结束时间-----"+endTime);
		System.out.println("任务id-----"+taskid);
		System.out.println("登录账号-----"+loginName);
		System.out.println("用户id-----"+userId);
		
		// 根据条件查询
		Map<String, Object> paramMap = new HashMap();
		paramMap.put("owner", owner);
		paramMap.put("environmentId", environmentId);
		paramMap.put("productId", productId);
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("taskid", taskid);
		paramMap.put("loginName", loginName);
		paramMap.put("userId", userId);

		Page<TaskStandalone> tasksPage = taskService.getTaskByParams(paramMap,currentPage, pageSize);
		
		System.out.println("人行征信获取到的数据-----"+tasksPage.getContent().size());
		
		PageInfo<TaskStandalone> pageInfo = new PageInfo<TaskStandalone>();
		pageInfo.setContent(tasksPage.getContent());
		pageInfo.setSize(tasksPage.getSize());
		pageInfo.setTotalElements(tasksPage.getTotalElements());
		pageInfo.setNumber(tasksPage.getNumber());
		return pageInfo;
	}
    
    
    
}
