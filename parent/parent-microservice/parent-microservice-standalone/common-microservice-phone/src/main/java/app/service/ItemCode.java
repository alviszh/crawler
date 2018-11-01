package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.phone.json.PhoneTaskBean;
import com.microservice.dao.entity.crawler.telecom.phone.CommonPhoneNumber;
import com.microservice.dao.entity.crawler.telecom.phone.PhoneDictionary;
import com.microservice.dao.entity.crawler.telecom.phone.inquire.InquirePhone;
import com.microservice.dao.entity.crawler.telecom.phone.wdty.WdtyInformation;
import com.microservice.dao.entity.crawler.telecom.phone.wdzj.WdzjInformation;
import com.microservice.dao.repository.crawler.telecom.phone.CommonPhoneNumberRepository;
import com.microservice.dao.repository.crawler.telecom.phone.PhoneDictionaryRepository;
import com.microservice.dao.repository.crawler.telecom.phone.inquire.InquirePhoneRepository;
import com.microservice.dao.repository.crawler.telecom.phone.wdty.WdtyInformationRepository;
import com.microservice.dao.repository.crawler.telecom.phone.wdzj.WdzjInformationRepository;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.phone")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.phone")
public class ItemCode {
	@Autowired
    private WdzjInformationRepository wdzjInformationRepository;//网贷之家
	@Autowired
    private WdtyInformationRepository wdtyInformationRepository;//网贷天眼
	@Autowired
    private CommonPhoneNumberRepository commonPhoneNumberRepository;//全国常用电话号码
	@Autowired
    private PhoneDictionaryRepository phoneDictionaryRepository;//百度号码认证平台常用号码
	@Autowired
    private InquirePhoneRepository inquirePhoneRepository;
	public String addTo(){
		int i = 1;
		List<WdzjInformation> wdzj = wdzjInformationRepository.findAll();
		List<WdtyInformation> wdty = wdtyInformationRepository.findAll();
		List<CommonPhoneNumber> commonPhone = commonPhoneNumberRepository.findAll();
		List<PhoneDictionary> phoneDictionary = phoneDictionaryRepository.findAll();
		//网贷天眼
		for(WdtyInformation dictionary : wdty){
			if(dictionary.getConsumerHotline()==null||dictionary.getConsumerHotline().equals("")){
	        	continue;
	        }
			Pattern patt = Pattern.compile("[^0-9]");        //得到字符串中的数字
	        Matcher m = patt.matcher(dictionary.getConsumerHotline());
	        String repickStr = m.replaceAll("");
	        System.out.println("i:"+i);
	        i++;
	        List<InquirePhone> list1 = new ArrayList<InquirePhone>();
			list1 = inquirePhoneRepository.findByPhone(repickStr);//查询数据库是否有相同手机号
			if(list1.size()>0){
				System.out.println("有重复数据");
//				tracerLog.addTag("电话数量",set.size()+"个");
			}else{
				System.out.println("无重复数据");
				Date currentTime = new Date();
			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    String updateTime = formatter.format(currentTime);
				InquirePhone ph = new InquirePhone();
				ph.setPhone(repickStr);
				ph.setTaskId("wdty_information_item_code");
				ph.setInquireType("1");
				ph.setUpdateTime(updateTime);
				ph.setPhonenumFlag(dictionary.getCompany().trim());
				inquirePhoneRepository.save(ph);
			}
		}		
		//网贷之家
//		for(WdzjInformation dictionary : wdzj){
//			if(dictionary.getConsumerHotline()==null||dictionary.getConsumerHotline().equals("")){
//	        	continue;
//	        }
//			Pattern patt = Pattern.compile("[^0-9]");        //得到字符串中的数字
//	        Matcher m = patt.matcher(dictionary.getConsumerHotline());
//	        String repickStr = m.replaceAll("");
//	        System.out.println("i:"+i);
//	        i++;
//	        List<InquirePhone> list1 = new ArrayList<InquirePhone>();
//			list1 = inquirePhoneRepository.findByPhone(repickStr);//查询数据库是否有相同手机号
//			if(list1.size()>0){
//				System.out.println("有重复数据");
////				tracerLog.addTag("电话数量",set.size()+"个");
//			}else{
//				System.out.println("无重复数据");
//				Date currentTime = new Date();
//			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    String updateTime = formatter.format(currentTime);
//				InquirePhone ph = new InquirePhone();
//				ph.setPhone(repickStr);
//				ph.setTaskId("wdzj_information_item_code");
//				ph.setInquireType("1");
//				ph.setUpdateTime(updateTime);
//				ph.setPhonenumFlag(dictionary.getCompany().trim());
//				inquirePhoneRepository.save(ph);
//			}
//		}		
		//全国常用电话号码
//		for(CommonPhoneNumber dictionary : commonPhone){
//			if(dictionary.getPhone()==null||dictionary.getPhone().equals("")){
//	        	continue;
//	        }
//			Pattern patt = Pattern.compile("[^0-9]");        //得到字符串中的数字
//	        Matcher m = patt.matcher(dictionary.getPhone());
//	        String repickStr = m.replaceAll("");
//	        System.out.println("i:"+i);
//	        i++;
//	        List<InquirePhone> list1 = new ArrayList<InquirePhone>();
//			list1 = inquirePhoneRepository.findByPhone(repickStr);//查询数据库是否有相同手机号
//			if(list1.size()>0){
//				System.out.println("有重复数据");
////				tracerLog.addTag("电话数量",set.size()+"个");
//			}else{
//				System.out.println("无重复数据");
//				Date currentTime = new Date();
//			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    String updateTime = formatter.format(currentTime);
//				InquirePhone ph = new InquirePhone();
//				ph.setPhone(repickStr);
//				ph.setTaskId("common_phone_number_item_code");
//				ph.setInquireType("1");
//				ph.setUpdateTime(updateTime);
//				ph.setPhonenumFlag(dictionary.getTwoClass().trim());
//				inquirePhoneRepository.save(ph);
//			}
//		}
		//百度号码认证平台常用号码
		
//		for(PhoneDictionary dictionary : phoneDictionary){
//			
//			if(dictionary.getPhone()==null||dictionary.getPhone().equals("")){
//	        	continue;
//	        }
//			Pattern patt = Pattern.compile("[^0-9]");        //得到字符串中的数字
//	        Matcher m = patt.matcher(dictionary.getPhone());
//	        String repickStr = m.replaceAll("");
//	        System.out.println("i:"+i);
//	        i++;
//	        List<InquirePhone> list1 = new ArrayList<InquirePhone>();
//			list1 = inquirePhoneRepository.findByPhone(repickStr);//查询数据库是否有相同手机号
//			if(list1.size()>0){
//				System.out.println("有重复数据");
////				tracerLog.addTag("电话数量",set.size()+"个");
//			}else{
//				String type = null;
//				System.out.println("无重复数据");
//				if(dictionary.getThreeClass()!=null){
//					type = dictionary.getTwoClass().trim()+dictionary.getThreeClass().trim();
//		        	System.out.println("phone:"+repickStr);
//					System.out.println("flag:"+dictionary.getTwoClass().trim()+dictionary.getThreeClass().trim());
//		        }else{
//		        	type = dictionary.getTwoClass().trim();
//		        	System.out.println("phone:"+repickStr);
//					System.out.println("flag:"+dictionary.getTwoClass().trim());
//		        }
//				Date currentTime = new Date();
//			    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			    String updateTime = formatter.format(currentTime);
//				InquirePhone ph = new InquirePhone();
//				ph.setPhone(repickStr);
//				ph.setTaskId("phone_dictionary_item_code");
//				ph.setInquireType("1");
//				ph.setUpdateTime(updateTime);
//				ph.setPhonenumFlag(type);
//				inquirePhoneRepository.save(ph);
//			}
	        
//		}
		
		return null;
		
	}
}
