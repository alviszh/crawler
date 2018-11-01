package app.client;

import com.crawler.qq.json.TaskQQ;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="qq",configuration = QqClientConfig.class)
public interface QqClient {

    @PostMapping(value="/qq/login")
    public TaskQQ login(@RequestBody PbccrcJsonBean pbccrcJsonBean);

    @PostMapping(value="/qq/crawler")
    public TaskQQ crawler(@RequestBody PbccrcJsonBean pbccrcJsonBean);
}
