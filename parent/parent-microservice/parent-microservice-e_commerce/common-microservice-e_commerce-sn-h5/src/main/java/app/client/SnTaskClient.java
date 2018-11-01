package app.client;

import app.bean.E_CommerceTask;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("ECOMMERCE-TASK")
public interface SnTaskClient {

    /**
     * 创建task
     * @param e_commerceJsonBean
     * @return
     */
    @PostMapping(path = "/ecommerce/check")
    public E_CommerceTask createTask(@RequestBody E_CommerceJsonBean e_commerceJsonBean);

    /**
     * 获取task信息
     * @param taskid
     * @return
     */
    @GetMapping(path="/ecommerce/tasks/{taskid}/status")
    public E_CommerceTask taskStatus(@PathVariable(name = "taskid") String taskid);

    /**
     * @Description: 获取所有社保已开发完成的城市
     * @return
     */
}
