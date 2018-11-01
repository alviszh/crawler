package app.client.v2;

import app.client.PbccrcClientConfig;
import app.client.PbccrcClientFallback;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name ="PBCCRC-V2",configuration = PbccrcClientConfig.class,fallback=PbccrcClientFallback.class)
public interface V2PbccrcClient {

    @PostMapping("/pbccrc/v1/getCreditAgent")
    String loginAndGetcreditV(@RequestBody PbccrcJsonBean pbccrcJsonBean);

}
