package app.client.standalone;

import app.client.MiddleClientConfig;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.TaskStandalone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "standalone-task",configuration = MiddleClientConfig.class)
//@FeignClient(name = "standaloneTask", url = "http://10.167.202.198:3070")
public interface StandaloneTaskClient {


    @GetMapping(path = "/standalone/tasks/{taskid}/status")
    public TaskStandalone getTaskStandalone(@PathVariable(name = "taskid") String taskid);


    /**
     * 创建task
     * @param pbccrcJsonBean
     * @return
     */
    @PostMapping(path = "/standalone/createTask")
    public TaskStandalone createTask(@RequestBody PbccrcJsonBean pbccrcJsonBean);


    /**
     * 获取报告
     * @param taskid
     * @return
     */
    @GetMapping("/standalone/pbccrc/getReport")
    String getReport(@RequestParam("taskid") String taskid,
                     @RequestParam("schema") String schema);
}
