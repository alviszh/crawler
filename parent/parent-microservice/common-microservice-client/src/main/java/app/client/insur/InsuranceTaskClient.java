package app.client.insur;

import com.crawler.insurance.json.AreaCode;
import com.crawler.insurance.json.TaskInsurance;
import com.crawler.insurance.json.InsuranceJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name ="InsuranceTask",configuration = InsurClientConfig.class)
public interface InsuranceTaskClient {

    /**
     * 创建task
     * @param insuranceJsonBean
     * @return
     */
    @PostMapping(path = "/insurance/check")
    public TaskInsurance createTask(@RequestBody InsuranceJsonBean insuranceJsonBean);

    /**
     * 获取task信息
     * @param taskid
     * @return
     */
    @GetMapping(path="/insurance/tasks/{taskid}/status")
    public TaskInsurance taskStatus(@PathVariable(name = "taskid") String taskid);

    /**
     * @Description: 获取所有社保已开发完成的城市
     * @return
     */
    @GetMapping(path = "/insurance/citys")
    public List<AreaCode> getCitys();
}
