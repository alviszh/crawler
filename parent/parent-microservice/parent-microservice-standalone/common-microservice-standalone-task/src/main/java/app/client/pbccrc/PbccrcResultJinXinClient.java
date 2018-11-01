package app.client.pbccrc;


import app.client.StandaloneClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 回调接口（金信）
 */
//@FeignClient(name = "pbccrc", url = "${jinxin.api.pbccrc.result.url}")
@FeignClient(name = "pbccrc", url = "http://localhost:1221",configuration = StandaloneClientConfig.class)
public interface PbccrcResultJinXinClient {

    /**
     * 发送征信报告
     * @param creditReport
     * @param key
     * @return
     */
    @PostMapping("/jiehost/jie/noticeCreditReport")
     String sendCreditReport(@RequestBody String creditReport,
                             @RequestParam("key") String key);

    //贷乎
    @PostMapping("/h5/pbccrc/sendResultTest")
    String sendPrecedingRuleDaihu(@RequestBody String daihuPrecedingRule);
}
