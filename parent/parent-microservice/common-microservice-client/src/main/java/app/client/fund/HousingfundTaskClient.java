package app.client.fund;

import com.crawler.housingfund.json.AreaCode;
import com.crawler.housingfund.json.TaskHousingfund;
import com.crawler.housingfund.json.HousingFundJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("HousingFund-Task")
public interface HousingfundTaskClient {

    /**
     * 创建task
     * @param housingfundJsonBean
     * @return
     */
    @PostMapping(path = "/housingfund/check")
    public TaskHousingfund createTask(@RequestBody HousingFundJsonBean housingfundJsonBean);

    /**
     * 获取task信息
     * @param taskid
     * @return
     */
    @GetMapping(path="/housingfund/tasks/{taskid}/status")
    public TaskHousingfund taskStatus(@PathVariable(name = "taskid") String taskid);

    /**
     * @Description: 获取所有公积金已开发完成的城市
     * @return
     */
    @GetMapping(path = "/housingfund/citys")
    public List<AreaCode> getCitys();
}
