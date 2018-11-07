package app.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mobile-task", url = "${callback.url}",configuration = MobileClientConfig.class)
public interface MobileResultClient {

    /**
     * @des 运营商回调接口
     * @param taskId
     * @param key
     * @return
     */ 
    @PostMapping("/as-adapter-ext/keji/mobileinfo/report")
    String sendMobileResult(@RequestParam("taskId") String taskId,
                            @RequestParam("key") String key);
}
