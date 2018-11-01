package app.client.carrier;

import com.crawler.mobile.json.DirMobileSegment;
import com.crawler.mobile.json.MobileJsonBean;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("MobileCrawlerTask")
//@FeignClient(name = "mobileTask", url = "http://10.167.202.203:8000")
public interface TaskClient {

    @GetMapping(path = "/carrier/tasks/{taskid}/status")
    public TaskMobile post(@PathVariable(name = "taskid") String taskid);

    @PostMapping(path = "/carrier/tasks")
    public TaskMobile createTask(@RequestBody MobileJsonBean mobileJsonBean);

    @PostMapping("/carrier/check")
    public ResultData<MobileJsonBean> check(@RequestBody MobileJsonBean mobileJsonBean);

    @GetMapping(path = "/carrier/mobilesegment/{phonenum}")
    public ResultData<DirMobileSegment> findMobileSegment(@PathVariable(name = "phonenum") String phonenum);

}
