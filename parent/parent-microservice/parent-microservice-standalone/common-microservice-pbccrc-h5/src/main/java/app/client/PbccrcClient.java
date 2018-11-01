package app.client;

import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.TaskStandalone;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name ="PBCCRC",configuration = PbccrcClientConfig.class,fallback=PbccrcClientFallback.class)
public interface PbccrcClient {

    /**
     * 获取报告
     * @param pbccrcJsonBean
     * @return
     */
    @PostMapping("/pbccrc/v1/getCreditAgent")
    String loginAndGetcreditV(@RequestBody PbccrcJsonBean pbccrcJsonBean);

}
