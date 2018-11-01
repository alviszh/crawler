package app.client.standalone;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.crawler.phone.json.PhoneTaskBean;


@FeignClient(name = "phonenum-detect",url="https://apitxboss.txtechnologies.com")
public interface PhoneClient {
	//电话插入inquire_phone_item_code表接口
//	@RequestMapping(value = "/api-service/phone/inquire",method = {RequestMethod.POST, RequestMethod.GET})
	@GetMapping(value = "/api-service/phone/inquire")
	public List<PhoneTaskBean> getInquire(@RequestParam(name = "phone",required=false) String p,@RequestParam(name = "taskid",required=false) String taskid);
	
	
	//根据taskid查询pro_mobile_call_info表电话号码
//	@RequestMapping(value = "/phone/proMobile",method = {RequestMethod.POST, RequestMethod.GET})
//	@GetMapping(value = "/api-service/phone/proMobile")
	@RequestMapping(value = "/api-service/phone/proMobile",method = RequestMethod.POST)
	public PhoneTaskBean getInquireProMobile(@RequestParam(name = "taskid",required=false) String taskid);
}
