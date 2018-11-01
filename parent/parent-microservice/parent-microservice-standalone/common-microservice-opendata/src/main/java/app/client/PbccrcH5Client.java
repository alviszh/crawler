package app.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 *
 */
// @FeignClient("PBCCRC-H5-V2")
//@FeignClient(name = "PBCCRC-H5-V2",url = "https://uatapitxboss.txtechnologies.com:9018")
@FeignClient(name = "PBCCRC-H5-V2", url = "${pbccrc.prod.url.domain}")
public interface PbccrcH5Client {

	@RequestMapping(value = "/h5/pbccrc/v2/report/{taskid}", method = { RequestMethod.GET})
	public String report(@PathVariable(name = "taskid") String taskid);

}