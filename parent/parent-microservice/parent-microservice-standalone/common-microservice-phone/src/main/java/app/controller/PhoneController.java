package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.phone.json.PhoneTaskBean;

import app.commontracerlog.TracerLog;
import app.service.PhoneInquire;


@RestController
@Configuration
@RequestMapping("/api-service/phone")
public class PhoneController {
	@Autowired
    private PhoneInquire phoneInquire; 
	@Autowired 
	private TracerLog tracerLog;
	@RequestMapping(value = "/inquire",method = {RequestMethod.POST, RequestMethod.GET})
	public List<PhoneTaskBean> getInquire(@RequestParam(name = "phone",required=false) String p,@RequestParam(name = "taskid",required=false) String taskid){
		tracerLog.addTag("PhoneController","电话开始存入");
		System.out.println("p"+p);
		System.out.println("taskid"+taskid);
		PhoneTaskBean  bean = phoneInquire.getFindByPhone(p,taskid);//缓存数据
		List<PhoneTaskBean> aas = null;
		try {
			if(bean.getPhone()!=null){
				aas  = phoneInquire.getPhone(bean.getPhone(),bean.getTaskid());//插入数据并去重
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.addTag("PhoneController.Error",e.getMessage());
		}
//		String del = phoneInquire.delete(phoneTaskBean);
//		System.out.println("delete"+del);
		return aas;
		
	}
	
}
