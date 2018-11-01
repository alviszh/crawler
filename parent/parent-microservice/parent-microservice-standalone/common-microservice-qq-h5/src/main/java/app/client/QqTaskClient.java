package app.client;

import com.crawler.qq.json.TaskQQ;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="QqTask",configuration = QqClientConfig.class)
public interface QqTaskClient {

    /**
     * 创建task
     * @param pbccrcJsonBean
     * @return
     */
    @PostMapping(path = "/qq/check")
    public TaskQQ createTask(@RequestBody PbccrcJsonBean pbccrcJsonBean);

    /**
     * 获取task信息
     * @param taskid
     * @return
     */
    @GetMapping(path="/qq/tasks/{taskid}/status")
    public TaskQQ taskStatus(@PathVariable(name = "taskid") String taskid);
}
