/**
 * 
 */
package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.crawler.pbccrc.json.PbccrcJsonBean;
/**
 * @author sln 
 * @date 2018年9月19日 上午11:10:59
 */
//@FeignClient("PBCCRC-H5-V2")    //微服务的名字    直接用h5无法用自己的taskid，因为h5方法中包含创建taskid是全套下来的
@FeignClient("PBCCRC-V2")    //微服务的名字    直接用这个微服务，因为可以用自己创建的taksid
//@FeignClient(name = "h5-proxy", url = "${task-pbccrc-proxy}")
public interface PbccrcClient {
	@PostMapping("/pbccrc/v1/getCreditAgent")
    String loginAndGetcreditV(@RequestBody PbccrcJsonBean pbccrcJsonBean);
}
