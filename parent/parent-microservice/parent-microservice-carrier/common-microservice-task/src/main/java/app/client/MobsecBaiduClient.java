package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 百度Api查询手机号归属地
 */
@FeignClient(name = "mobsec", url = "http://mobsec-dianhua.baidu.com")
public interface MobsecBaiduClient {

    @GetMapping("/dianhua_api/open/location")
    String mobsec(@RequestParam("tel") String tel);
}
