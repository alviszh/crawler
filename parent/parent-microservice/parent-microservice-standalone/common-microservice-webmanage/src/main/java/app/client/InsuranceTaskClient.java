package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;



//@FeignClient("InsuranceTask")
@FeignClient(name = "insurancetask", url = "http://10.167.35.59:8011")
public interface InsuranceTaskClient {

    @GetMapping(path = "/insurance/tasks/{taskid}/status")
    public TaskInsurance getTaskByTaskid(@PathVariable(name = "taskid") String taskid);
    
    @PostMapping(value = "/insurance/tasks/getInsurancePages")
    public PageInfo<TaskInsurance> getTaskInsurancePages(@RequestParam(value = "currentPage") int currentPage,
                                                   @RequestParam(value = "pageSize") int pageSize,
                                                   @RequestParam(value = "taskid") String taskid);
    
    /**
     * 获取加载图表的数据（线性）
     * @return
     */
    @GetMapping(value = "/insurance/tasks/lineData")
    public @ResponseBody
    List lineData();


    /**
     * 统计的调用量（饼图）
     * @return
     */
    @GetMapping(value = "/insurance/tasks/pieData")
    public @ResponseBody
    List pieData();
}
