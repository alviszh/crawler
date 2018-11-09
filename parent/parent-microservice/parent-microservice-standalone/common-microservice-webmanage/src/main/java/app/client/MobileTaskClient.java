package app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.PageInfo;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

@FeignClient("mobile-task")
//@FeignClient(name = "mobileTask", url = "http://10.167.202.203:8000")
public interface MobileTaskClient {

    /**
     * 根据taskid获取task状态
     * @param taskid
     * @return
     */
    @GetMapping(path = "/carrier/tasks/{taskid}/status")
    public TaskMobile getTaskByTaskid(@PathVariable(name = "taskid") String taskid);

    @PostMapping(value = "/carrier/tasks/getPages")
    public PageInfo<TaskMobile> getTaskMobilePages(@RequestParam(value = "currentPage") int currentPage,
                                                   @RequestParam(value = "pageSize") int pageSize,
                                                   @RequestParam(value = "taskid") String taskid,
                                                   @RequestParam(value = "mobilenum") String mobilenum);

    /**
     * 获取加载图表的数据（线性）
     * @return
     */
    @GetMapping(value = "/carrier/tasks/lineData")
    public @ResponseBody
    List lineData();


    /**
     * 统计每个运营商的调用量（饼图）
     * @return
     */
    @GetMapping(value = "/carrier/tasks/pieData")
    public @ResponseBody
    List pieData();
}
