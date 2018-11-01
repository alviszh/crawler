package app.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.phone.json.PhoneBean;
import com.crawler.phone.json.PhoneDiscernBean;
import com.microservice.dao.entity.crawler.telecom.phone.inquire.InquirePhone;
import com.microservice.dao.repository.crawler.telecom.phone.inquire.InquirePhoneRepository;

import app.commontracerlog.TracerLog;

@RequestMapping("/api-service/phone") 
@RestController
@Configuration
public class PhoneInquireStatusController {
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
    private InquirePhoneRepository inquirePhoneRepository;
	@RequestMapping(value = "/discern",method = {RequestMethod.POST, RequestMethod.GET})
	public PhoneDiscernBean getInquire(@RequestParam(name = "phone",required=false) String p,@RequestParam(name = "taskid",required=false) String taskid){
		System.out.println("p"+p);
		System.out.println("taskid"+taskid);
		tracerLog.addTag("电话",p);
		tracerLog.addTag("taskid",taskid);
		tracerLog.addTag("PhoneDiscernBean","电话开始查询");
		List<PhoneBean> list = new ArrayList<PhoneBean>();
		PhoneDiscernBean pdb = new PhoneDiscernBean();
		if(p!=null&&(taskid==null||taskid.equals(""))){
			Set<String> set = new HashSet<>(Arrays.asList(p.split(",")));
			tracerLog.addTag("电话数量",set.size()+"个");
			try {
				for (String str : set) {
					System.out.println("aaaaaaa"+str);
					List<InquirePhone> list1 = new ArrayList<InquirePhone>();
					list1 = inquirePhoneRepository.findByPhone(str);
					System.out.println("ph"+list1);
					if(list1!=null||!list1.equals("")){
						for(InquirePhone ph:list1){
							PhoneBean pb = new PhoneBean();
							pb.setTaskId(ph.getTaskId());
							pb.setPhone(ph.getPhone());
							pb.setInquireType(ph.getInquireType());
							pb.setMarkTimes(ph.getMarkTimes());
							pb.setMarkType(ph.getMarkType());
							pb.setPhonenumFlag(ph.getPhonenumFlag());
							pb.setPhoneType(ph.getPhoneType());
							System.out.println("pd"+pb);
							list.add(pb);			
						}
					}else{
						PhoneBean pb = new PhoneBean();
						pb.setPhone(str);
						list.add(pb);	
					}	
				}			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tracerLog.addTag("PhoneInquireStatusController.Error",e.getMessage());
			}
			pdb.setTotal(list.size());
			pdb.setPhone(list);
		}else if((taskid!=null||!taskid.equals(""))&&p==null){
			try {
				System.out.println("taskid"+taskid);
				List<InquirePhone> list1 = new ArrayList<InquirePhone>();
				list1 = inquirePhoneRepository.findByTaskId(taskid);
				System.out.println("ph"+list1);
				if(list1!=null||!list1.equals("")){
					for(InquirePhone ph:list1){
						PhoneBean pb = new PhoneBean();
						pb.setTaskId(ph.getTaskId());
						pb.setPhone(ph.getPhone());
						pb.setInquireType(ph.getInquireType());
						pb.setMarkTimes(ph.getMarkTimes());
						pb.setMarkType(ph.getMarkType());
						pb.setPhonenumFlag(ph.getPhonenumFlag());
						pb.setPhoneType(ph.getPhoneType());
						System.out.println("pd"+pb);
						list.add(pb);			
					}
				}else{
					PhoneBean pb = new PhoneBean();
					pb.setTaskId(taskid);
					list.add(pb);	
				}	
							
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tracerLog.addTag("PhoneInquireStatusController.Error",e.getMessage());
			}
			pdb.setTotal(list.size());
			pdb.setPhone(list);
		}else if((taskid!=null||!taskid.equals(""))&&p.equals("")){
			try {
				System.out.println("taskid"+taskid);
				List<InquirePhone> list1 = new ArrayList<InquirePhone>();
				list1 = inquirePhoneRepository.findByTaskId(taskid);
				System.out.println("ph"+list1);
				if(list1!=null||!list1.equals("")){
					for(InquirePhone ph:list1){
						PhoneBean pb = new PhoneBean();
						pb.setTaskId(ph.getTaskId());
						pb.setPhone(ph.getPhone());
						pb.setInquireType(ph.getInquireType());
						pb.setMarkTimes(ph.getMarkTimes());
						pb.setMarkType(ph.getMarkType());
						pb.setPhonenumFlag(ph.getPhonenumFlag());
						pb.setPhoneType(ph.getPhoneType());
						System.out.println("pd"+pb);
						list.add(pb);			
					}
				}else{
					PhoneBean pb = new PhoneBean();
					pb.setTaskId(taskid);
					list.add(pb);	
				}	
							
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tracerLog.addTag("PhoneInquireStatusController.Error",e.getMessage());
			}
			pdb.setTotal(list.size());
			pdb.setPhone(list);
		}else {
			Set<String> set = new HashSet<>(Arrays.asList(p.split(",")));
			tracerLog.addTag("电话数量",set.size()+"个");
			try {
				for (String str : set) {
					System.out.println("aaaaaaa"+str);
					List<InquirePhone> list1 = new ArrayList<InquirePhone>();
					list1 = inquirePhoneRepository.findByTaskIdAndPhone(taskid,str);
					System.out.println("ph"+list1);
					if(list1!=null||!list1.equals("")){
						for(InquirePhone ph:list1){
							PhoneBean pb = new PhoneBean();
							pb.setTaskId(ph.getTaskId());
							pb.setPhone(ph.getPhone());
							pb.setInquireType(ph.getInquireType());
							pb.setMarkTimes(ph.getMarkTimes());
							pb.setMarkType(ph.getMarkType());
							pb.setPhonenumFlag(ph.getPhonenumFlag());
							pb.setPhoneType(ph.getPhoneType());
							System.out.println("pd"+pb);
							list.add(pb);			
						}
					}else{
						PhoneBean pb = new PhoneBean();
						pb.setPhone(str);
						list.add(pb);	
					}	
				}			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tracerLog.addTag("PhoneInquireStatusController.Error",e.getMessage());
			}
			pdb.setTotal(list.size());
			pdb.setPhone(list);
		}
		return pdb;
	}
}
