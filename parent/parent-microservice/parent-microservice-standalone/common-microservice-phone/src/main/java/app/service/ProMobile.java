package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.crawler.phone.json.PhoneTaskBean;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileCallInfo;
import com.microservice.dao.entity.crawler.mobile.etl.ProMobileReportCallDetailStatistics;
import com.microservice.dao.entity.crawler.telecom.phone.inquire.InquirePhone;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileCallInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportCallDetailStatisticsRepository;
import com.microservice.dao.repository.crawler.telecom.phone.inquire.InquirePhoneRepository;

import app.client.standalone.PhoneClient;
import app.commontracerlog.TracerLog;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.mobile.etl")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.mobile.etl")
public class ProMobile {
	@Autowired 
	private TracerLog tracerLog;
	@Autowired
    private ProMobileCallInfoRepository proMobileCallInfoRepository;
	@Autowired
    private PhoneClient phoneClient;
	@Autowired
    private PhoneInquire phoneInquire; 
	@Autowired
    private InquirePhoneRepository inquirePhoneRepository;
	@Autowired 
	private ProMobileReportCallDetailStatisticsRepository proMobileReportCallDetailStatisticsRepository;
	public String getProMobile(){
		List<InquirePhone> list = inquirePhoneRepository.findAll();
		for(InquirePhone inquirePhone : list){
			String phonenumFlag = "";
			String phonenumType = "";
			String phonenum = inquirePhone.getPhone().trim();
			List<ProMobileReportCallDetailStatistics> list1 = proMobileReportCallDetailStatisticsRepository.findByPhonenumAndPhonenumFlagAndPhonenumType(phonenum, phonenumFlag, phonenumType);
			System.out.println("phone:"+phonenum);
			System.out.println("list1:"+list1.size());
			for(ProMobileReportCallDetailStatistics pro : list1){
				if(inquirePhone.getPhonenumFlag()==null&&inquirePhone.getMarkType()==null){
					continue;
				}
				if(inquirePhone.getPhonenumFlag()==null){
					pro.setPhonenumType(inquirePhone.getMarkType().trim());
					proMobileReportCallDetailStatisticsRepository.save(pro);
				}
                if(inquirePhone.getMarkType()==null){
					pro.setPhonenumFlag(inquirePhone.getPhonenumFlag().trim());
					proMobileReportCallDetailStatisticsRepository.save(pro);
				}
                if(inquirePhone.getPhonenumFlag()!=null&&inquirePhone.getMarkType()!=null){
                	pro.setPhonenumType(inquirePhone.getMarkType().trim());
                	pro.setPhonenumFlag(inquirePhone.getPhonenumFlag().trim());
                	proMobileReportCallDetailStatisticsRepository.save(pro);
				}
				
				
				
			}
			
		}
		return null;
		
	}
	
	
	
	public PhoneTaskBean getPhoneMobile(String taskid){
		List<ProMobileCallInfo> list = proMobileCallInfoRepository.findByTaskId(taskid);//通过taskid获取pro_mobile_call_info表数据
        String phone = null;
        for(int i = 0;i <list.size();i++){
        	System.out.println("phoneList"+list.get(i).getHisNum());
        	if(i==0){
        		phone = list.get(i).getHisNum()+",";
        	}else if(i==(list.size()-1)){
        		phone = phone +list.get(i).getHisNum();
        	}else{
        		phone = phone +list.get(i).getHisNum()+",";
        	}
        	
        }
        System.out.println("phone"+phone);
        tracerLog.addTag("phone",phone);
        getInquire(phone,taskid);//调用电话插入inquire_phone_item_code表接口   
        tracerLog.addTag("Mobile数据采集完成触发存储过程end,电话号码开始存入inquire_phone_item_code()phone",phone);
        tracerLog.addTag("Mobile数据采集完成触发存储过程end,电话号码开始存入inquire_phone_item_code",taskid);
        PhoneTaskBean pt = new PhoneTaskBean();
        pt.setTaskid(taskid);
        pt.setPhone(phone);
        return  pt;
	}
	
	public List<PhoneTaskBean> getInquire(String phone,String taskid){
		System.out.println("开始插入手机号");
//		List<PhoneTaskBean> list= phoneClient.getInquire(phone, taskid);
		tracerLog.addTag("PhoneController","电话开始存入");
		System.out.println("p"+phone);
		System.out.println("taskid"+taskid);
		PhoneTaskBean  bean = phoneInquire.getFindByPhone(phone,taskid);//缓存数据
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
		return null;
		
	}
}
