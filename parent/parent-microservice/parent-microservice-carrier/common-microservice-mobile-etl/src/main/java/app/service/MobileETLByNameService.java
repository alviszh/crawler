package app.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.repository.crawler.mobile.BasicUserRepository;

import app.bean.MobileEtlEnum;
import app.bean.WebDataByName;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.mobile"})



public class MobileETLByNameService {
	
	@Autowired 
	private TracerLog tracer;
	@Autowired 
	private BasicUserRepository basicUserRepository;
	
	@Value("${spring.profiles.active}")
	String profile;

	public WebDataByName getAllData(String name){
		
		WebDataByName webDataByName = new WebDataByName();
		
		if(StringUtils.isBlank(name) && name == null){
			
			webDataByName.setName(name);
			webDataByName.setMessage(MobileEtlEnum.MOBILE_ETL_NANE_NULL.getMessage());
			webDataByName.setErrorCode(MobileEtlEnum.MOBILE_ETL_NANE_NULL.getErrorCode());
			webDataByName.setProfile(profile);
			return webDataByName;
		}else{
			
			List<String> idnums = basicUserRepository.findIdnumbyName(name.trim());
			
			if(null != idnums && idnums.size() != 0){
				webDataByName.setName(name);
				webDataByName.setIdnums(idnums);
				webDataByName.setMessage(MobileEtlEnum.MOBILE_ETL_SUCCESS.getMessage());
				webDataByName.setErrorCode(MobileEtlEnum.MOBILE_ETL_SUCCESS.getErrorCode());
				webDataByName.setProfile(profile);
				return webDataByName;
			}else{
				webDataByName.setName(name);
				webDataByName.setMessage(MobileEtlEnum.MOBILE_ETL_IDNUM_RESULT_NULL_BYNAME.getMessage());
				webDataByName.setErrorCode(MobileEtlEnum.MOBILE_ETL_IDNUM_RESULT_NULL_BYNAME.getErrorCode());
				webDataByName.setProfile(profile);
				return webDataByName;				
			}			
		}				
	}
}
