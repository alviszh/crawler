package app.client.bank;

import com.crawler.bank.json.BankCode;
import com.crawler.bank.json.TaskBank;
import com.crawler.bank.json.BankUserBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//@FeignClient(name ="Bank-Task",configuration = BankClientConfig.class)
@FeignClient(name ="Bank-Task",url = "http://10.167.35.20:81")
public interface BankTaskClient {

    /**
     * 创建task
     * @param bankUserBean
     * @return
     */
    @PostMapping(path = "/bank/check")
    public TaskBank createTask(@RequestBody BankUserBean bankUserBean);

    /**
     * 获取task信息
     * @param taskid
     * @return
     */
    @GetMapping(path="/bank/tasks/{taskid}/status")
    public TaskBank taskStatus(@PathVariable(name = "taskid") String taskid);

    /**
     * @Description: 获取所有银行已开发完成的城市
     * @return
     */
    @GetMapping(path = "/bank/getBank")
    public List<BankCode> getBank();

}
