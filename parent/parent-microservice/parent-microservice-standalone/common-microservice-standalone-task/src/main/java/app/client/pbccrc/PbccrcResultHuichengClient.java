package app.client.pbccrc;

import app.client.StandaloneClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 回调接口（汇诚）
 */
@FeignClient(name = "pbccrc", url = "${api.pbccrc.result.url}",configuration = StandaloneClientConfig.class)
//@FeignClient(name = "pbccrc", url = "http://localhost:3070/standalone",configuration = StandaloneClientConfig.class)
public interface PbccrcResultHuichengClient {

    /**
     * 发送征信报告
     * @param creditReport
     * @param key
     * @return
     */
    @PostMapping("/as-adapter-ext/keji/pbccredit/report")
     String sendCreditReport(@RequestBody String creditReport,
                             @RequestParam("key") String key);
}
